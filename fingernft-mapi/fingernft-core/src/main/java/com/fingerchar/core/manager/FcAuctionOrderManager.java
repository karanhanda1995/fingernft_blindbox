package com.fingerchar.core.manager;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.fingerchar.core.base.service.IBaseService;
import com.fingerchar.core.constant.AuctionStatus;
import com.fingerchar.core.constant.CommonStatus;
import com.fingerchar.core.constant.NoticeType;
import com.fingerchar.core.constant.SysConfConstant;
import com.fingerchar.db.base.BaseEntity;
import com.fingerchar.db.dao.ext.FcAuctionOrderExtMapper;
import com.fingerchar.db.domain.*;
import com.fingerchar.db.dto.AuctionBuyLog;
import com.fingerchar.db.dto.AuctionCancelLog;
import com.fingerchar.db.vo.AuctionParamVO;
import com.fingerchar.db.vo.FcAuctionOrderVo;
import com.fingerchar.db.vo.NftParamVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author： Zjm
 * @Date：2022/3/22 1:07
 */
@Service
public class FcAuctionOrderManager {
    @Autowired
    IBaseService baseService;

    @Autowired
    FcNoticeManager noticeManager;

    @Autowired
    FcAuctionBidsManager auctionBidsManager;

    @Autowired
    FcSystemConfigManager systemConfigManager;

    @Autowired
    FcAuctionOrderExtMapper auctionOrderExtMapper;

    @Autowired
    FcNftItemsManager nftItemsManager ;

    @Autowired
    FcUserManager userManager;

    @Autowired
    FcPayTokenManager payTokenManager;

    public List<FcAuctionOrderVo> activeAuctions(List<AuctionParamVO> params){
        List<FcAuctionOrder> orderList = this.auctionOrderExtMapper.listbymulti(params);

        List<NftParamVO> paramVOList = params.stream().map(vo -> new NftParamVO(vo.getAddress(), vo.getTokenId())).collect(Collectors.toList());
        List<FcNftItems> itemsList = nftItemsManager.listByMulti(paramVOList);
        Set<String> ownerList = itemsList.stream().map(vo->vo.getItemOwner() + ":" + vo.getAddress() + ":" + vo.getTokenId()).collect(Collectors.toSet());

        orderList = orderList.stream().filter(
                vo -> ownerList.contains(vo.getOwner() + ":" + vo.getSellToken() + ":" + vo.getSellTokenId())
        ).collect(Collectors.toList());
        if(orderList.isEmpty()){
            return new ArrayList<>();
        }
        List<String> owners = orderList.stream().map(FcAuctionOrder::getOwner).collect(Collectors.toList());

        List<FcUser> userList = this.userManager.listByMulti(owners);
        Map<String, FcUser> userMap = userList.stream().collect(Collectors.toMap(FcUser::getAddress, Function.identity()));
        List<FcAuctionOrderVo> orderVoList = orderList.stream().map(vo->new FcAuctionOrderVo(vo, userMap.get(vo.getOwner()))).collect(Collectors.toList());
        return orderVoList;
    }

    public FcAuctionOrder getActiveOrder(String sellToken, String sellTokenId, String owner){
        QueryWrapper<FcAuctionOrder> auctionWrapper = new QueryWrapper<>();
        auctionWrapper.eq(FcAuctionOrder.SELL_TOKEN, sellToken)
                .eq(FcAuctionOrder.SELL_TOKEN_ID, sellTokenId)
                .eq(FcAuctionOrder.OWNER, owner)
                .eq(FcAuctionOrder.STATUS, false)
                .eq(BaseEntity.DELETED, false);
        return this.baseService.getByCondition(FcAuctionOrder.class, auctionWrapper);
    }

    public void auction(FcAuctionOrder order){
        FcAuctionOrder auctionOrder = this.get(order.getSellToken(), order.getSellTokenId(), order.getSalt(), order.getOwner());
        if(null != auctionOrder){
            order.setId(auctionOrder.getId());
        }
        FcPayToken payToken = payTokenManager.get(order.getBuyerToken());
        order.setPaytokenAddress(payToken.getAddress());
        order.setPaytokenName(payToken.getName());
        order.setPaytokenSymbol(payToken.getSymbol());
        order.setPaytokenDecimals(payToken.getDecimals());
        order.setSellerFee(Integer.parseInt(this.systemConfigManager.getKeyValue(SysConfConstant.SELLER_FEE)));
        order.setBuyerFee(Integer.parseInt(this.systemConfigManager.getKeyValue(SysConfConstant.BUYER_FEE)));
        order.setHighestprice(order.getStartValue());
        if(null == order.getId()){
            this.save(order);
        }else{
            this.update(order);
        }
    }

    public Integer auctionBid(FcAuctionOrder order, FcAuctionBids bid){
        FcAuctionBids highestBid = this.auctionBidsManager.getHighestBid(order.getId());
        if(null != highestBid){
            // 通知当前最高价者，有更高出价者
            Integer noticeType = NoticeType.TRADE.getType();
            Integer type = CommonStatus.AUCTION_BID.getType();
            Map<String, Object> map = new HashMap<>();
            map.put("order", order);
            map.put("bid", bid);
            String content = JSON.toJSONString(map);
            this.noticeManager.add(content, highestBid.getOwner(), type, noticeType, bid.getOwner());
        }
        this.auctionBidsManager.bid(bid);
        order.setHighestprice(bid.getBuying());
        return this.update(order);
    }

    public Integer buy(AuctionBuyLog log){
        String sellToken = log.getSellToken();
        String sellTokenId = log.getSellTokenId().toString();
        String owner = log.getOwner();

        FcAuctionOrder order = this.get(sellToken, sellTokenId, log.getSalt().toString(), owner);
        if(null == order){
            return 0;
        }
        order.setStatusCode(AuctionStatus.getCode("Finished"));
        order.setStatus(true);

        String content = JSON.toJSONString(order);
        Integer type = CommonStatus.AUCTION_BUY.getType();
        Integer noticeType = NoticeType.TRADE.getType();
        noticeManager.add(content, order.getOwner(), type, noticeType, owner);

        noticeManager.add(content, log.getBuyer(), type, noticeType, order.getOwner());
        FcNftItems nftItems = this.nftItemsManager.get(sellToken, sellTokenId, owner);
        nftItems.setOnsell(false);
        this.nftItemsManager.update(nftItems);
        auctionBidsManager.finishAuctionBids(log, order);
        return this.update(order);
    }


    public Integer cancel(
            AuctionCancelLog log
    ){
        String sellToken = log.getSellToken();
        String sellTokenId = log.getSellTokenId().toString();
        String salt = log.getSalt().toString();
        String owner = log.getOwner();
        FcAuctionOrder order = this.get(log.getSellToken(), sellTokenId, salt, owner);
        if(null == order){
            return 0;
        }
        Integer noticeType = NoticeType.TRADE.getType();
        Integer type = CommonStatus.CANCEL_AUCTION.getType();
        String content = JSON.toJSONString(log);
        this.noticeManager.add(content, order.getOwner(), type, noticeType, log.getOwner());

        auctionBidsManager.cancelAuctionBids(log, order);
        order.setStatusCode(AuctionStatus.getCode("Cancel"));
        order.setStatus(true);
        FcNftItems nftItems = this.nftItemsManager.get(sellToken, sellTokenId, owner);
        if(null != nftItems){
          nftItems.setOnsell(false);
          this.nftItemsManager.update(nftItems);
        }
        return this.update(order);
    }

    public FcAuctionOrder get(
            String sellToken,
            String sellTokenId,
            String salt,
            String owner
    ){
        QueryWrapper<FcAuctionOrder> wrapper = new QueryWrapper<>();
        wrapper.eq(FcAuctionOrder.SELL_TOKEN, sellToken)
                .eq(FcAuctionOrder.SELL_TOKEN_ID, sellTokenId)
                .eq(FcAuctionOrder.SALT, salt)
                .eq(FcAuctionOrder.OWNER, owner)
                .eq(FcAuctionOrder.DELETED, false);
        return this.baseService.getByCondition(FcAuctionOrder.class, wrapper);
    }


    public Integer update(FcAuctionOrder auctionOrder){
        return this.baseService.update(auctionOrder);
    }

    public Integer save(FcAuctionOrder auctionOrder){
        return this.baseService.save(auctionOrder);
    }

    public void processTime(long time, BigInteger block) {
        QueryWrapper<FcAuctionOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.le(FcAuctionOrder.EXPIRED_TIME, time)
                .eq(FcAuctionOrder.STATUS, 0)
                .eq(FcAuctionOrder.DELETED, false);
        List<FcAuctionOrder> fcAuctionOrderList = this.baseService.findByCondition(FcAuctionOrder.class, queryWrapper);
        for (FcAuctionOrder fcAuctionOrder : fcAuctionOrderList) {
            this.nftItemsManager.auctionExpire(fcAuctionOrder.getOwner(),fcAuctionOrder.getSellToken(),fcAuctionOrder.getSellTokenId());
        }
        this.auctionExpire(time);
    }

    private void auctionExpire(long time) {
        UpdateWrapper<FcAuctionOrder> wrapper = new UpdateWrapper<>();
        wrapper.set(FcAuctionOrder.STATUS, true)
                .set(FcAuctionOrder.STATUS_CODE, AuctionStatus.getCode("Aborted"));
        wrapper.le(FcAuctionOrder.EXPIRED_TIME, time)
                .eq(FcAuctionOrder.STATUS, 0)
                .eq(FcAuctionOrder.DELETED, false);
        this.baseService.updateByCondition(FcAuctionOrder.class, wrapper);
    }

    public Integer orderCountByCondition(Long time, Integer statusCode) {
        QueryWrapper<FcAuctionOrder> wrapper = new QueryWrapper<>();
        wrapper.eq(FcAuctionOrder.DELETED, false);
        if (null!=time){
            wrapper.gt(FcAuctionOrder.CREATE_TIME, time);
        }
        wrapper.eq(FcAuctionOrder.STATUS_CODE, statusCode);
        return this.baseService.counts(FcAuctionOrder.class, wrapper);
    }

}
