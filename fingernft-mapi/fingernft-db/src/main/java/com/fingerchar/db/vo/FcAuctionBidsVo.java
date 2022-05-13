package com.fingerchar.db.vo;

import com.fingerchar.db.domain.FcAuctionBids;
import com.fingerchar.db.domain.FcUser;

/**
 * @Author： Zjm
 * @Date：2022/4/12 11:56
 */
public class FcAuctionBidsVo {

    private String owner;
    private String buying;
    private String salt;
    private String signature;
    private Integer status;
    private String bonus;
    private UserBaseInfoVo user;

    public FcAuctionBidsVo(FcAuctionBids auctionBids, FcUser user){
        this.owner = auctionBids.getOwner();
        this.buying = auctionBids.getBuying();
        this.salt = auctionBids.getSalt();
        this.signature = auctionBids.getSignature();
        this.status = auctionBids.getStatus();
        this.bonus = auctionBids.getBonus();
        this.user = new UserBaseInfoVo(user);
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getBuying() {
        return buying;
    }

    public void setBuying(String buying) {
        this.buying = buying;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }


    public String getBonus() {
        return bonus;
    }

    public void setBonus(String bonus) {
        this.bonus = bonus;
    }

    public UserBaseInfoVo getUser() {
        return user;
    }

    public void setUser(UserBaseInfoVo user) {
        this.user = user;
    }
}
