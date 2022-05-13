package com.fingerchar.core.manager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.fingerchar.core.base.service.IBaseService;
import com.fingerchar.core.constant.CommonStatus;
import com.fingerchar.core.constant.SysConfConstant;
import com.fingerchar.db.domain.*;
import com.fingerchar.db.dto.*;
import com.fingerchar.db.vo.UserBaseInfoVo;
import com.fingerchar.db.vo.notice.ContractVo;
import com.fingerchar.db.vo.notice.NftInfoVo;
import com.fingerchar.db.vo.notice.NoticeContentVo;
import com.fingerchar.db.vo.notice.NoticeInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author： Zjm
 * @Date：2022/3/21 18:52
 */
@Service
public class FcNoticeManager {
    @Autowired
    IBaseService baseService;

    @Autowired
    FcUserManager userManager;

    @Autowired
    FcContractNftManager nftManager;

    @Autowired
    FcContractManager contractManager;

    public class AuctionContent{
        public FcAuctionOrder getOrder() {
            return order;
        }
        public void setOrder(FcAuctionOrder order) {
            this.order = order;
        }
        public FcAuctionBids getBid() {
            return bid;
        }
        public void setBid(FcAuctionBids bid) {
            this.bid = bid;
        }
        private FcAuctionOrder order;
        private FcAuctionBids bid;
    }

    public List<NoticeInfoVo> getNoticeInfoList(List<FcNotice> noticeList){
        List<String> operatorList = noticeList.stream().map(FcNotice::getOperator).collect(Collectors.toList());
        List<FcUser> userList = this.userManager.listByMulti(operatorList);
        Map<String, FcUser> userMap = userList.stream().collect(Collectors.toMap(FcUser::getAddress, Function.identity()));
        List<NoticeInfoVo> infoVoList = new LinkedList<>();
        NoticeInfoVo infoVo = null;
        NoticeContentVo contentVo = null;
        FcUser operator = null;
        for (FcNotice notice: noticeList){
            contentVo = this.getContentVo(notice);
            operator = userMap.get(notice.getOperator());
            if(null == operator){
                infoVo = new NoticeInfoVo(notice, new UserBaseInfoVo(notice.getOperator()), contentVo);
            }else{
                infoVo = new NoticeInfoVo(notice, new UserBaseInfoVo(operator), contentVo);
            }
            infoVoList.add(infoVo);
        }
        return infoVoList;
    }

    public NoticeContentVo getContentVo(FcNotice notice){
        CommonStatus status = CommonStatus.getStatusByType(notice.getSubType());
        NoticeContentVo contentVo = null;
        String content = notice.getContent();
        switch (status){
            case LIKED:
                return this.likeNoticeInfo(content);
            case FOLLOWED:
                return this.followNoticeInfo(content);
            case CANCEL_SALE:
            case CANCEL_BID:
                return this.cancelNoticeInfo(content, notice.getSubType());
            case BID:
                return this.bidNoticeInfo(content);
            case BUY:
            case ACCEPT_BID:
                return this.buyNoticeInfo(content, notice.getSubType());
            case TRANSFER:
            case RECEIVE:
                return this.transferNoticeInfo(content);
            case AUCTION_BID:
                return this.auctionNoticeInfo(content);
            case CANCEL_AUCTION:
                return this.cancelAuctionNoticeInfo(content);
            case SUCCESSFUL_TRADE:
            case AUCTION_BUY:
                return this.buyAuctionNoticeInfo(content);
            case CREATE_CONTRACT:
                return this.createContractNoticeInfo(content);
        }
        return null;

    }


    public NoticeContentVo likeNoticeInfo(String content){
        NoticeContentVo contentVo = new NoticeContentVo();
        NftInfoVo infoVo = JSON.parseObject(content, NftInfoVo.class);
        FcContractNft nft = this.nftManager.get(infoVo.getAddress(), infoVo.getTokenId());
        if(null != nft){
            infoVo = new NftInfoVo(nft);
        }
        contentVo.setNft(infoVo);
        return contentVo;
    }

    public NoticeContentVo followNoticeInfo(String content){
        NoticeContentVo contentVo = new NoticeContentVo();
        UserBaseInfoVo infoVo = JSON.parseObject(content, UserBaseInfoVo.class);
        FcUser user = this.userManager.get(infoVo.getAddress());
        if(null != user){
            infoVo = new UserBaseInfoVo(user);
        }
        contentVo.setUser(infoVo);
        return contentVo;
    }

    public NoticeContentVo cancelNoticeInfo(String content, Integer type){
        ExchangeCancelLog cancelLog = JSON.parseObject(content, ExchangeCancelLog.class);
        NftInfoVo infoVo = new NftInfoVo();
        FcContractNft nft = null;
        if(CommonStatus.CANCEL_SALE.getType().equals(type)){
            nft = this.nftManager.get(cancelLog.getSellToken(), cancelLog.getSellTokenId().toString());
            infoVo.setAddress(cancelLog.getSellToken());
            infoVo.setTokenId(cancelLog.getSellTokenId().toString());
        }else{
            nft = this.nftManager.get(cancelLog.getBuyToken(), cancelLog.getBuyTokenId().toString());
            infoVo.setAddress(cancelLog.getBuyToken());
            infoVo.setTokenId(cancelLog.getBuyTokenId().toString());
        }
        if(null != nft){
            infoVo = new NftInfoVo(nft);
        }
        NoticeContentVo contentVo = new NoticeContentVo();
        contentVo.setNft(infoVo);
        contentVo.setTxHash(cancelLog.getTxHash());
        return contentVo;
    }

    public NoticeContentVo buyNoticeInfo(String content, Integer type){
        ExchangeBuyLog buyLog = JSON.parseObject(content, ExchangeBuyLog.class);
        NftInfoVo infoVo = new NftInfoVo();
        FcContractNft nft = null;
        if(CommonStatus.BUY.getType().equals(type)){
            nft = this.nftManager.get(buyLog.getSellToken(), buyLog.getSellTokenId().toString());
            infoVo.setAddress(buyLog.getSellToken());
            infoVo.setTokenId(buyLog.getSellTokenId().toString());
        }else{
            nft = this.nftManager.get(buyLog.getBuyToken(), buyLog.getBuyTokenId().toString());
            infoVo.setAddress(buyLog.getBuyToken());
            infoVo.setTokenId(buyLog.getBuyTokenId().toString());
        }
        if(null != nft){
            infoVo = new NftInfoVo(nft);
        }
        NoticeContentVo contentVo = new NoticeContentVo();
        contentVo.setNft(infoVo);
        contentVo.setTxHash(buyLog.getTxHash());
        return contentVo;
    }

    public NoticeContentVo bidNoticeInfo(String content){
        PrepareOrderInfo orderInfo = JSON.parseObject(content, PrepareOrderInfo.class);
        NftInfoVo infoVo = new NftInfoVo();
        FcContractNft nft = null;
        nft = this.nftManager.get(orderInfo.getBuyToken(), orderInfo.getBuyTokenId());
        if(null != nft){
            infoVo = new NftInfoVo(nft);
        }else{
            infoVo.setAddress(orderInfo.getBuyToken());
            infoVo.setTokenId(orderInfo.getBuyTokenId());
        }
        NoticeContentVo contentVo = new NoticeContentVo();
        contentVo.setNft(infoVo);
        return contentVo;
    }

    public NoticeContentVo transferNoticeInfo(String content){
        TransferLog log = JSON.parseObject(content, TransferLog.class);
        NftInfoVo infoVo = new NftInfoVo();
        FcContractNft nft = null;
        nft = this.nftManager.get(log.getAddress(), log.getTokenId().toString());
        if(null != nft){
            infoVo = new NftInfoVo(nft);
        }else{
            infoVo.setAddress(log.getAddress());
            infoVo.setTokenId(log.getTokenId().toString());
        }
        NoticeContentVo contentVo = new NoticeContentVo();
        contentVo.setNft(infoVo);
        contentVo.setTxHash(log.getTxHash());
        return contentVo;
    }

    public NoticeContentVo auctionNoticeInfo(String content){
        Map<String, Object> map = JSON.parseObject(content);
        JSONObject order = (JSONObject) map.get("order");
        String sellToken = order.getString("sellToken");
        String sellTokenId = order.getString("sellTokenId");
        NftInfoVo infoVo = new NftInfoVo();
        FcContractNft nft = this.nftManager.get(sellToken, sellTokenId);
        if(null != nft){
            infoVo = new NftInfoVo(nft);
        }else{
            infoVo.setAddress(sellToken);
            infoVo.setTokenId(sellTokenId);
        }
        NoticeContentVo contentVo = new NoticeContentVo();
        contentVo.setNft(infoVo);
        return contentVo;
    }

    private NoticeContentVo cancelAuctionNoticeInfo(String content){
        AuctionCancelLog log = JSON.parseObject(content, AuctionCancelLog.class);
        NftInfoVo infoVo = new NftInfoVo();
        FcContractNft nft = this.nftManager.get(
                log.getSellToken(),
                log.getSellTokenId().toString()
        );
        if(null != nft){
            infoVo = new NftInfoVo(nft);
        }else{
            infoVo.setAddress(log.getSellToken());
            infoVo.setTokenId(log.getSellTokenId().toString());
        }
        NoticeContentVo contentVo = new NoticeContentVo();
        contentVo.setNft(infoVo);
        contentVo.setTxHash(log.getTxHash());
        return contentVo;
    }

    private NoticeContentVo buyAuctionNoticeInfo(String content){
        AuctionBuyLog log = JSON.parseObject(content, AuctionBuyLog.class);
        NftInfoVo infoVo = new NftInfoVo();
        FcContractNft nft = this.nftManager.get(
                log.getSellToken(),
                log.getSellTokenId().toString()
        );
        if(null != nft){
            infoVo = new NftInfoVo(nft);
        }else{
            infoVo.setAddress(log.getSellToken());
            infoVo.setTokenId(log.getSellTokenId().toString());
        }
        NoticeContentVo contentVo = new NoticeContentVo();
        contentVo.setNft(infoVo);
        contentVo.setTxHash(log.getTxHash());
        return contentVo;
    }

    private NoticeContentVo createContractNoticeInfo(String content){
        ContractCreateLog log = JSON.parseObject(content, ContractCreateLog.class);
        ContractVo contractVo = null;
        FcContract contract = this.contractManager.get(log.getAddress());
        if(null != contract){
            contractVo = new ContractVo(contract);
        }else{
            contractVo = new ContractVo(log);
        }
        NoticeContentVo contentVo = new NoticeContentVo();
        contentVo.setContract(contractVo);
        contentVo.setTxHash(log.getTxHash());
        return contentVo;
    }

    public Integer readAll(String address){
        UpdateWrapper<FcNotice> wrapper = new UpdateWrapper<>();
        wrapper.eq(FcNotice.OWNER, address)
                .eq(FcNotice.IS_READ, false)
                .eq(FcNotice.DELETED, false);
        wrapper.set(FcNotice.IS_READ, true);
        return this.baseService.updateByCondition(FcNotice.class, wrapper);
    }

    public Integer countTotal(String address){
        QueryWrapper<FcNotice> wrapper = new QueryWrapper<>();
        wrapper.eq(FcNotice.OWNER, address)
                .eq(FcNotice.DELETED, false);
        return this.baseService.counts(FcNotice.class, wrapper);
    }

    public Integer countUnread(String address){
        QueryWrapper<FcNotice> wrapper = new QueryWrapper<>();
        wrapper.eq(FcNotice.OWNER, address)
                .eq(FcNotice.IS_READ, false)
                .eq(FcNotice.DELETED, false);
        return this.baseService.counts(FcNotice.class, wrapper);
    }

    public Integer add(Map<String, Object> content, String owner, Integer type, Integer noticeType, String operator){
        String _content = JSON.toJSONString(content);
        return this.add(_content, owner, type, noticeType, operator);
    }

    public Integer add(String content, String owner, Integer type, Integer noticeType, String operator){
        if(owner.equalsIgnoreCase(SysConfConstant.ZERO_ADDRESS)){
            return 0;
        }
        FcNotice notice = new FcNotice();
        notice.setContent(content);
        notice.setOwner(owner);
        notice.setType(noticeType);
        notice.setSubType(type);
        notice.setIsRead(false);
        notice.setOperator(operator);
        return this.save(notice);
    }

    public FcNotice get(Long id){
        return this.baseService.getById(FcNotice.class, id);
    }

    public Integer update(FcNotice notice){
        return this.baseService.update(notice);
    }

    public Integer save(FcNotice notice){
        notice.setOperator(notice.getOperator().toLowerCase(Locale.ROOT));
        notice.setOwner(notice.getOwner().toLowerCase(Locale.ROOT));
        return this.baseService.save(notice);
    }

}
