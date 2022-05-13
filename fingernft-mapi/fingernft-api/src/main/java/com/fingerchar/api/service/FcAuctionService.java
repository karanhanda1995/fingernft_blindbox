package com.fingerchar.api.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fingerchar.api.dto.AuctionOrderInfo;
import com.fingerchar.api.utils.DappCryptoUtil;
import com.fingerchar.core.base.service.IBaseService;
import com.fingerchar.core.constant.CommonStatus;
import com.fingerchar.core.constant.SysConfConstant;
import com.fingerchar.core.manager.*;
import com.fingerchar.core.util.ListUtils;
import com.fingerchar.core.util.ResponseUtil;
import com.fingerchar.db.base.BaseEntity;
import com.fingerchar.db.dao.ext.FcAuctionOrderExtMapper;
import com.fingerchar.db.domain.*;
import com.fingerchar.db.vo.AuctionParamVO;
import com.fingerchar.db.vo.FcAuctionBidsVo;
import com.fingerchar.db.vo.FcAuctionOrderVo;
import com.fingerchar.db.vo.NftParamVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class FcAuctionService {

    private static final Logger logger = LoggerFactory.getLogger(FcAuctionService.class);

    @Autowired
    FcPayTokenManager payTokenManager;

    @Autowired
    FcSystemConfigManager systemConfigManager;

    @Autowired
    IBaseService baseService;

    @Autowired
    FcAuctionOrderExtMapper auctionOrderExtMapper;

    @Autowired
    FcAuctionBidsManager bidsManager;

    @Autowired
    FcNoticeManager noticeManager;

    @Autowired
    FcContractNftService contractNftService;

    @Autowired
    FcNftItemsManager nftItemsManager;

    @Autowired
    FcAuctionOrderManager auctionOrderManager;

    @Autowired
    FcOrderManager orderManager;

    @Autowired
    FcUserManager userManager;

    @Autowired
    FcContractNftManager contractNftManager;

    /**
     * @param status 是否进行中
     * @return
     */
    public Object list(String token, String tokenId, String owner, Boolean status, IPage<FcAuctionOrder> pageInfo) {
        QueryWrapper<FcAuctionOrder> wrapper = new QueryWrapper<>();
        wrapper.eq(BaseEntity.DELETED, false);
        if (!StringUtils.isEmpty(token)) {
            wrapper.eq(FcAuctionOrder.SELL_TOKEN, token);
        }
        if (!StringUtils.isEmpty(tokenId)) {
            wrapper.eq(FcAuctionOrder.SELL_TOKEN_ID, tokenId);
        }
        if (!StringUtils.isEmpty(owner)) {
            wrapper.eq(FcAuctionOrder.OWNER, owner);
        }
        if (null != status) {
            wrapper.eq(FcAuctionOrder.STATUS, status);
        }
        wrapper.eq(FcAuctionOrder.DELETED, false);
        wrapper.orderByDesc(BaseEntity.CREATE_TIME);
        IPage<FcAuctionOrder> iPage = this.baseService.findByPage(FcAuctionOrder.class, wrapper, pageInfo);
        List<FcAuctionOrder> list = iPage.getRecords();

        if (list.size() == 0) {
            return ResponseUtil.okList(iPage);
        }


        List<String> owners = list.stream().map(FcAuctionOrder::getOwner).collect(Collectors.toList());

        List<FcUser> userList = this.userManager.listByMulti(owners);
        Map<String, FcUser> userMap = userList.stream().collect(Collectors.toMap(FcUser::getAddress, Function.identity()));

        List<FcAuctionOrderVo> orderVoList = list.stream().map(vo->new FcAuctionOrderVo(vo, userMap.get(vo.getOwner()))).collect(Collectors.toList());
        IPage<FcAuctionOrderVo> infoIPage = new Page<>(iPage.getCurrent(), iPage.getSize());
        infoIPage.setRecords(orderVoList);
        infoIPage.setPages(iPage.getPages());
        infoIPage.setTotal(iPage.getTotal());
        return ResponseUtil.okList(infoIPage);

    }

    public Object listbymulti(String nfts){

        List<String> tempList = Arrays.asList(nfts.split(","));
        List<AuctionParamVO> auctionList = new ArrayList<>();
        for(String temp : tempList){
            String[] infos = temp.split(":");
            List<String> _infos = Arrays.asList(infos);
            auctionList.add(
                new AuctionParamVO(_infos.get(0), _infos.get(1), Boolean.parseBoolean(_infos.get(2)))
            );
        }
        List<FcAuctionOrderVo> orderVoList = this.auctionOrderManager.activeAuctions(auctionList);
        return ResponseUtil.okList(orderVoList);
    }

    /**
     * @param token
     * @param tokenId
     * @param owner
     * @return
     */
    public Object bids(String token, String tokenId, String owner, String salt) {
        QueryWrapper<FcAuctionOrder> wrapper = new QueryWrapper<>();
        wrapper.eq(FcAuctionOrder.SELL_TOKEN, token)
                .eq(FcAuctionOrder.SELL_TOKEN_ID, tokenId)
                .eq(FcAuctionOrder.OWNER, owner)
                .eq(FcAuctionOrder.SALT, salt)
                .eq(BaseEntity.DELETED, false);
        FcAuctionOrder order = this.baseService.getByCondition(FcAuctionOrder.class, wrapper);
        if (null == order) {
            return ResponseUtil.okList(new ArrayList<>());
        }

        QueryWrapper<FcAuctionBids> bidWrapper = new QueryWrapper<>();
        bidWrapper.eq(FcAuctionBids.ORDER_ID, order.getId());
        bidWrapper.orderByDesc(BaseEntity.CREATE_TIME);
        List<FcAuctionBids> bidList = this.baseService.findByCondition(FcAuctionBids.class, bidWrapper);
        List<String> ownerList = bidList.stream().map(FcAuctionBids::getOwner).collect(Collectors.toList());
        ownerList = ListUtils.unrepeated(ownerList);

        List<FcUser> userList = this.userManager.listByMulti(ownerList);
        Map<String, FcUser> userMap = userList.stream().collect(Collectors.toMap(FcUser::getAddress, Function.identity()));
        List<FcAuctionBidsVo> bidsVos = bidList.stream().map(vo -> new FcAuctionBidsVo(vo, userMap.get(vo.getOwner()))).collect(Collectors.toList());
        return ResponseUtil.ok(bidsVos);

    }

    /**
     * @param token
     * @param tokenId
     * @param owner
     * @return
     */
    public Object get(String token, String tokenId, String owner) {
        FcAuctionOrder order = this.auctionOrderManager.getActiveOrder(token, tokenId, owner);
        return ResponseUtil.ok(order);
    }

    /**
     * 保存，添加拍卖
     *
     * @param order
     * @return
     */
    public Object add(FcAuctionOrder order) {
        if(this.nftItemsManager.isOnSale(order.getSellValue(), order.getSellTokenId(), order.getOwner())){
            return ResponseUtil.fail(-1, "Nft is on sell, can not put on auction");
        }
        if (order.getStartTime() > order.getEndTime()) {
            return ResponseUtil.fail(-1, "Auction end time must be greater then start time");
        }
        if (new BigDecimal(order.getBuying()).compareTo(new BigDecimal(order.getStartValue())) < 0) {
            return ResponseUtil.fail(-1, "Hope price must be higher then start price");
        }

        FcContractNft nft = this.contractNftManager.get(order.getSellToken(), order.getSellTokenId());
        if (null == nft) {
            return ResponseUtil.fail(-1, "Then token is not existed or bured");
        }
        FcNftItems nftItems = this.nftItemsManager.get(order.getSellToken(), order.getSellTokenId(), order.getOwner());
        if (null == nftItems) {
            return ResponseUtil.fail(-1, "Then token is not existed or owner is not you");
        }

        if (new BigInteger(nftItems.getQuantity()).compareTo(new BigInteger(order.getSellValue())) < 0) {
            return ResponseUtil.fail(-1, "no enough quantity");
        }
        FcPayToken payToken = this.payTokenManager.get(order.getBuyerToken());
        if (null == payToken) {
            return ResponseUtil.fail(-1, "Unkown pay type");
        }

        String transaction = this.systemConfigManager.getKeyValue(SysConfConstant.AUCTION_EXPIRE_TIME);
        if (StringUtils.isEmpty(transaction)) {
            logger.error("系统未设置交易时间");
            return ResponseUtil.fail(-1, "System error");
        }
        Long expiredTime = order.getEndTime() + 3600 * Long.valueOf(transaction);
        order.setExpiredTime(expiredTime);

        FcAuctionOrder auctionOrder = this.auctionOrderManager.getActiveOrder(
                order.getSellToken(), order.getSellTokenId(),order.getOwner()
        );
        if(order.getType().equals(CommonStatus.EDIT_AUCTION.getType())){
            if(null == auctionOrder){
                return ResponseUtil.fail(-1, "auction is not existed");
            }
            if(!order.getSalt().equals(auctionOrder.getSalt())){
                return ResponseUtil.fail(-1, "Invalid salt");
            }
            if(auctionOrder.getStartTime() < System.currentTimeMillis() / 1000){
                return ResponseUtil.fail(-1, "Auction is started, can not edit");
            }
        }else{
            if(null != auctionOrder){
                return ResponseUtil.fail(-1, "This nft is on auction already");
            }
        }
        this.contractNftManager.auction(order);
        return ResponseUtil.ok();
    }


    /**
     * @param info
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Object prepare(AuctionOrderInfo info) {
        String sellFee = this.systemConfigManager.getKeyValue(SysConfConstant.SELLER_FEE);
        String buyerFee = this.systemConfigManager.getKeyValue(SysConfConstant.BUYER_FEE);
        if (null == sellFee || null == buyerFee) {
            logger.error("系统未设置sell fee or buyer fee");
            return ResponseUtil.fail(-1, "System error");
        }
        if (CommonStatus.AUCTION.getType().equals(info.getType())) {

            if (info.getStartTime() > info.getEndTime()) {
                return ResponseUtil.fail(-1, "Auction end time must be greater then start time");
            }
            if (new BigDecimal(info.getBuying()).compareTo(new BigDecimal(info.getStartValue())) < 0) {
                return ResponseUtil.fail(-1, "Hope price must be higher then start price");
            }

            if(this.nftItemsManager.isOnSale(info.getSellToken(), info.getSellTokenId(), info.getOwner())){
                return ResponseUtil.fail(-1, "Nft is on sell, can not put on auction");
            }
            FcAuctionOrder auctionOrder = this.auctionOrderManager.getActiveOrder(
                    info.getSellToken(),
                    info.getSellTokenId(),
                    info.getOwner()
            );
            if(null != auctionOrder ){
                if(!info.getSalt().equals(auctionOrder.getSalt())) {
                    return ResponseUtil.fail(-1, "Can't resubmit an order");
                }
                if(auctionOrder.getStartTime() < System.currentTimeMillis() / 1000){
                    return ResponseUtil.fail(-1, "Auction is started, can not edit");
                }
            }

            String transaction = this.systemConfigManager.getKeyValue(SysConfConstant.AUCTION_EXPIRE_TIME);
            if (StringUtils.isEmpty(transaction)) {
                logger.error("系统未设置交易时间");
                return ResponseUtil.fail(-1, "System error");
            }
            Long expiredTime = info.getEndTime() + 3600 * Long.valueOf(transaction);
            info.setExpiredTime(expiredTime);

            info.setSellerFee(sellFee);
            info.setBuyerFee(buyerFee);
            return this.prepareAuction(info);
        } else {
            FcAuctionOrder order = this.auctionOrderManager.get(
                    info.getSellToken(),
                    info.getSellTokenId(),
                    info.getSalt(),
                    info.getOwner()
            );
            if (null == order) {
                return ResponseUtil.fail(-1, "Auction not exist");
            }
            if(order.getStatus()){
                return ResponseUtil.fail(-1, "Auction is end");
            }
            if (order.getStartTime() > System.currentTimeMillis() / 1000) {
                return ResponseUtil.fail(-1, "Auction is not start");
            }
            FcAuctionBids highestBid = this.bidsManager.getHighestBid(order.getId());
            if(null != highestBid){
                if (new BigDecimal(info.getBuyerValue()).subtract(new BigDecimal(highestBid.getBuying())).compareTo(new BigDecimal(order.getRangeValue())) < 0) {
                    return ResponseUtil.fail(-1, "Each price increase shall not be less than " + order.getRangeValue());
                }
            }else{
                if (new BigDecimal(info.getBuyerValue()).compareTo(new BigDecimal(order.getStartValue())) < 0) {
                    return ResponseUtil.fail(-1, "Bid price can not lower then " + order.getStartValue());
                }
            }
            AuctionOrderInfo temp = new AuctionOrderInfo(order);
            temp.setBuyerValue(info.getBuyerValue());
            temp.setBuyer(info.getBuyer());
            temp.setSellerFee(sellFee);
            temp.setBuyerFee(buyerFee);

            return this.prepareBidAuction(temp);
        }
    }


    /**
     * 准备拍卖信息，放入缓存中
     *
     * @param info
     * @return
     */
    private Object prepareAuction(AuctionOrderInfo info) {
        if (null == info.getBuyerTokenId()) {
            info.setBuyerTokenId("0");
        }
        DappCryptoUtil.auctionSign(info);
        return ResponseUtil.ok(info);

    }

    /**
     * 准备竞拍，将信息放入缓存中
     *
     * @param info
     * @return
     */
    private Object prepareBidAuction(AuctionOrderInfo info) {
        DappCryptoUtil.auctionSign(info, new BigInteger(info.getBuyerValue()), info.getBuyer());
        return ResponseUtil.ok(info);
    }

    /**
     * @param token
     * @param tokenId
     * @param owner
     * @param salt
     * @param user
     * @return
     */
    public Object buyPrepare(String token, String tokenId, String owner, String salt, FcUser user) {
        FcAuctionOrder order = this.auctionOrderManager.get(token, tokenId, salt,owner);

        if (null == order) {
            return ResponseUtil.fail(-1, "Auction not exist");
        }
        if (order.getEndTime() > System.currentTimeMillis() / 1000) {
            return ResponseUtil.fail(-1, "The auction is not over");
        }
        if (order.getExpiredTime() < System.currentTimeMillis() / 1000) {
            return ResponseUtil.fail(-1, "Auction is expired");
        }

        List<FcAuctionBids> list = this.bidsManager.getAuctionBids(order);
        if (list.isEmpty()) {
            return ResponseUtil.fail(-1, "Can not find any bid");
        }
        FcAuctionBids highestBid = this.bidsManager.getMaxAuctionBids(list);

        if (!owner.equals(user.getAddress())) {
            if (!highestBid.getOwner().equals(user.getAddress())) {
                return ResponseUtil.fail(-1, "Your bid is not highest bid");
            }
        }

        if (new BigDecimal(highestBid.getBuying()).compareTo(new BigDecimal(order.getBuying())) < 0 ){
            return ResponseUtil.fail(-1, "Your bid is lower than expected price");
        }

        AuctionOrderInfo info = new AuctionOrderInfo(order);
        info.setBuyer(highestBid.getOwner());
        List<FcAuctionBids> recipientBids = this.bidsManager.getRecipients(order, list, highestBid);
        List<String> recipients = recipientBids.stream().map(FcAuctionBids::getOwner).collect(Collectors.toList());
        info.setRecipients(recipients);
        String buyerValue = highestBid.getBuying();
        if (null == buyerValue) {
            return ResponseUtil.fail(-1, "Unkown pay type");
        }
        info.setBuyerValue(buyerValue);

        DappCryptoUtil.auctionSign(info, Integer.valueOf(order.getBuyerFee()));
        return ResponseUtil.ok(info);
    }

    /**
     * @param
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Object bid(AuctionOrderInfo bidOrder, String address){
        FcAuctionOrder order = this.auctionOrderManager.get(
                bidOrder.getSellToken(),
                bidOrder.getSellTokenId(),
                bidOrder.getSalt(),
                bidOrder.getOwner());
        if (null == order) {
            return ResponseUtil.fail(-1, "auction not exist");
        }
        FcAuctionBids bid = new FcAuctionBids();
        bid.setBuying(bidOrder.getBuyerValue());
        bid.setOrderId(order.getId());
        bid.setOwner(address);
        bid.setSignature(bidOrder.getSignature());
        bid.setStatus(0);
        bid.setSalt(bidOrder.getSalt());
        this.contractNftManager.auctionBid(order, bid);

        return ResponseUtil.ok();
    }

	/**
	 * @param pageInfo
	 * @return
	 */
	public Object newList(Page<FcAuctionOrder> pageInfo) {
		QueryWrapper<FcAuctionOrder> wrapper = new QueryWrapper<>();
        wrapper.gt(FcAuctionOrder.END_TIME, System.currentTimeMillis() / 1000)
                .eq(FcAuctionOrder.STATUS, false)
                .eq(BaseEntity.DELETED, false);
        List<FcAuctionOrder> auctionOrderList = this.baseService.findByPage(FcAuctionOrder.class, wrapper, pageInfo).getRecords();
        if(auctionOrderList.isEmpty()) {
            return ResponseUtil.okList(auctionOrderList);
        }
        List<NftParamVO> paramVOList = auctionOrderList.stream().map(
                vo -> new NftParamVO(vo.getSellToken(), vo.getSellTokenId())).collect(Collectors.toList());
        List<FcNftItems> itemsList = this.nftItemsManager.listByMulti(paramVOList);
        Set<String> ownerList = itemsList.stream().map(vo->vo.getItemOwner() + ":" + vo.getAddress() + ":" + vo.getTokenId()).collect(Collectors.toSet());

        auctionOrderList = auctionOrderList.stream().filter(
                vo -> ownerList.contains(vo.getOwner() + ":" + vo.getSellToken() + ":" + vo.getSellTokenId())
        ).collect(Collectors.toList());

        paramVOList = auctionOrderList.stream().map(
                vo -> new NftParamVO(vo.getSellToken(), vo.getSellTokenId())).collect(Collectors.toList());

        Set<NftParamVO> _params = new HashSet<>(paramVOList);
        return this.contractNftService.listByTokenAndTokenId(_params);
	}
}
