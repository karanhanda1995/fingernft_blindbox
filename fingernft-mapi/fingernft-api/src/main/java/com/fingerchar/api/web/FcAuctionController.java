package com.fingerchar.api.web;

import com.fingerchar.api.dto.AuctionOrderInfo;
import com.fingerchar.api.service.FcAuctionService;
import com.fingerchar.core.base.controller.BaseController;
import com.fingerchar.core.constant.CommonStatus;
import com.fingerchar.core.constant.SysConfConstant;
import com.fingerchar.core.manager.FcUserManager;
import com.fingerchar.core.util.ResponseUtil;
import com.fingerchar.db.domain.FcAuctionOrder;
import com.fingerchar.db.domain.FcUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping(SysConfConstant.URL_PREFIX + "/auction")
public class FcAuctionController extends BaseController {

    @Autowired
    FcAuctionService auctionService;

    @Autowired
    FcUserManager userManager;

    @PostMapping("list")
    public Object list(String token, String tokenId, String owner, Boolean status) {
        return this.auctionService.list(token, tokenId, owner, status, this.getPageInfo());
    }

    @PostMapping("activeauctions")
    public Object activeauctions(String nfts) {
        if(StringUtils.isEmpty(nfts)){
            return ResponseUtil.fail(-1, "nfts is empty");
        }
        return this.auctionService.listbymulti(nfts);
    }

    
    @PostMapping("newlist")
    public Object newList() {
        return this.auctionService.newList(this.getPageInfo());
    }


    /**
     * @param token   nft地址
     * @param tokenId nftId
     * @param owner   拥有者
     * @param salt    盐
     * @return 拍卖参与记录
     */
    @PostMapping("bids")
    public Object bids(String token, String tokenId, String owner, String salt) {
        return this.auctionService.bids(token, tokenId, owner, salt);
    }



    /**
     * 准备，将信息放入缓存中
     *
     * @param order 拍卖订单信息
     * @return
     */
    @PostMapping("prepare")
    public Object prepare(AuctionOrderInfo order) {
        String userAddress = (String) request.getAttribute("userAddress");
        if (StringUtils.isEmpty(userAddress)) {
            return ResponseUtil.unlogin();
        }
        FcUser user = this.userManager.get(userAddress);
        if (null == user) {
            return ResponseUtil.unlogin();
        }
        if (order.getType().equals(CommonStatus.AUCTION.getType())) {
            if (!this.checkOrder(order, user.getAddress())) {
                return ResponseUtil.fail(-1, "Invalid order");
            }
        } else {
            if (!this.checkBidOrder(order, user.getAddress())) {
                return ResponseUtil.fail(-1, "Invalid bid");
            }
            order.setBuyer(user.getAddress());
        }
        return this.auctionService.prepare(order);
    }

    /**
     * 保存信息，从缓存中拿取
     *
     * @param auctionOrder 订单信息
     * @return
     */
    @PostMapping("add")
    public Object add(FcAuctionOrder auctionOrder) {
        String userAddress = (String) request.getAttribute("userAddress");
        if (StringUtils.isEmpty(userAddress)) {
            return ResponseUtil.unlogin();
        }
        FcUser user = this.userManager.get(userAddress);
        if (null == user) {
            return ResponseUtil.unlogin();
        }

        AuctionOrderInfo info = new AuctionOrderInfo(auctionOrder);
        if (!this.checkOrder(info, user.getAddress())) {
            return ResponseUtil.fail(-1, "invalid order");
        }
        return this.auctionService.add(auctionOrder);
    }


    /**
     * 参与拍卖
     *
     * @param auctionOrder 拍卖信息
     * @return
     */
    @PostMapping("bid")
    public Object bid(AuctionOrderInfo auctionOrder) {
        String userAddress = (String) request.getAttribute("userAddress");
        if (StringUtils.isEmpty(userAddress)) {
            return ResponseUtil.unlogin();
        }
        FcUser user = this.userManager.get(userAddress);
        if (null == user) {
            return ResponseUtil.unlogin();
        }

        if (!this.checkBidOrder(auctionOrder, user.getAddress())) {
            return ResponseUtil.fail(-1, "invalid order");
        }

        return this.auctionService.bid(auctionOrder, userAddress);
    }


    @PostMapping("buyerFee")
    public Object buyerFee(String token, String tokenId, String owner, String salt) {
        if (StringUtils.isEmpty(tokenId) || StringUtils.isEmpty(owner) || StringUtils.isEmpty(salt)) {
            return ResponseUtil.fail(-1, "parameter invalid");
        }
        String userAddress = (String) request.getAttribute("userAddress");
        if (StringUtils.isEmpty(userAddress)) {
            return ResponseUtil.unlogin();
        }
        FcUser user = this.userManager.get(userAddress);
        if (null == user) {
            return ResponseUtil.unlogin();
        }
        return this.auctionService.buyPrepare(token, tokenId, owner, salt, user);
    }

    /**
     * 验证order数据是否完整
     *
     * @param order
     * @param address
     * @return
     */
    private boolean checkOrder(AuctionOrderInfo order, String address) {
        if (null == order
                || StringUtils.isEmpty(order.getOwner())
                || !address.equalsIgnoreCase(order.getOwner())
                || StringUtils.isEmpty(order.getBuyerToken())
                || StringUtils.isEmpty(order.getBuying())
                || StringUtils.isEmpty(order.getBuyerTokenId())
                || StringUtils.isEmpty(order.getBuyerType())
                || StringUtils.isEmpty(order.getSellToken())
                || StringUtils.isEmpty(order.getSellTokenId())
                || StringUtils.isEmpty(order.getSellValue())
                || StringUtils.isEmpty(order.getSellType())
                || StringUtils.isEmpty(order.getRangeValue())
                || StringUtils.isEmpty(order.getStartValue())
                || null == order.getStartTime()
                || null == order.getEndTime()
        ) {
            return false;
        }

        int temp = new BigDecimal(order.getBuying()).compareTo(new BigDecimal("0"));
        if (temp <= 0) {
            return false;
        }
        temp = new BigDecimal(order.getSellValue()).compareTo(new BigDecimal("0"));
        if (temp <= 0) {
            return false;
        }
        return true;
    }

    /**
     * 验证bidOrder数据是否完整
     *
     * @param order
     * @param address
     * @return
     */
    private boolean checkBidOrder(AuctionOrderInfo order, String address) {
        if (null == order
                || StringUtils.isEmpty(order.getOwner())
                || order.getOwner().equalsIgnoreCase(address)
                || StringUtils.isEmpty(order.getBuyerValue())
                || StringUtils.isEmpty(order.getSellToken())
                || StringUtils.isEmpty(order.getSellTokenId())
                || StringUtils.isEmpty(order.getSalt())) {
            return false;
        }

        int temp = new BigDecimal(order.getBuyerValue()).compareTo(new BigDecimal("0"));
        if (temp <= 0) {
            return false;
        }
        return true;
    }


}
