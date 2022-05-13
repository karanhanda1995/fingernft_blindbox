package com.fingerchar.db.vo;

import com.fingerchar.db.domain.BlindBoxHistory;
import com.fingerchar.db.domain.FcUser;

/**
 * @Author： Zjm
 * @Date：2022/3/28 20:26
 */
public class BlindBoxHistoryVo {

    private String owner;

    private String buyToken;

    private String buyTokenId;

    private String buyValue;

    private String buyer;

    private String amount;

    private String salt;

    private String nftInfos;

    private String txHash;

    private Long createTime;

    private String paytokenName;
    private String paytokenSymbol;
    private Integer paytokenDecimals;
    private String paytokenMetadataContent;

    private UserBaseInfoVo user;

    public BlindBoxHistoryVo() {

    }

    public BlindBoxHistoryVo(BlindBoxHistory his) {
        this.owner = his.getOwner();
        this.buyToken = his.getBuyToken();
        this.buyTokenId = his.getBuyTokenId();
        this.buyValue = his.getBuyValue();
        this.buyer = his.getBuyer();
        this.amount = his.getAmount();
        this.salt = his.getSalt();
        this.nftInfos = his.getNftInfos();
        this.txHash = his.getTxHash();
        this.createTime = his.getCreateTime();

        this.paytokenName = his.getPaytokenName();
        this.paytokenSymbol = his.getPaytokenSymbol();
        this.paytokenDecimals = his.getPaytokenDecimals();
        this.paytokenMetadataContent = his.getPaytokenMetadataContent();
    }

    public BlindBoxHistoryVo(BlindBoxHistory his, FcUser user) {
        this.owner = his.getOwner();
        this.buyToken = his.getBuyToken();
        this.buyTokenId = his.getBuyTokenId();
        this.buyValue = his.getBuyValue();
        this.buyer = his.getBuyer();
        this.amount = his.getAmount();
        this.salt = his.getSalt();
        this.nftInfos = his.getNftInfos();
        this.txHash = his.getTxHash();
        this.createTime = his.getCreateTime();

        this.paytokenName = his.getPaytokenName();
        this.paytokenSymbol = his.getPaytokenSymbol();
        this.paytokenDecimals = his.getPaytokenDecimals();
        this.paytokenMetadataContent = his.getPaytokenMetadataContent();

        this.user = new UserBaseInfoVo(user);
    }

    public BlindBoxHistoryVo setUser(FcUser user) {
        this.user = new UserBaseInfoVo(user);
        return this;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getBuyToken() {
        return buyToken;
    }

    public void setBuyToken(String buyToken) {
        this.buyToken = buyToken;
    }

    public String getBuyTokenId() {
        return buyTokenId;
    }

    public void setBuyTokenId(String buyTokenId) {
        this.buyTokenId = buyTokenId;
    }

    public String getBuyValue() {
        return buyValue;
    }

    public void setBuyValue(String buyValue) {
        this.buyValue = buyValue;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getNftInfos() {
        return nftInfos;
    }

    public void setNftInfos(String nftInfos) {
        this.nftInfos = nftInfos;
    }

    public String getTxHash() {
        return txHash;
    }

    public void setTxHash(String txHash) {
        this.txHash = txHash;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getPaytokenName() {
        return paytokenName;
    }

    public void setPaytokenName(String paytokenName) {
        this.paytokenName = paytokenName;
    }

    public String getPaytokenSymbol() {
        return paytokenSymbol;
    }

    public void setPaytokenSymbol(String paytokenSymbol) {
        this.paytokenSymbol = paytokenSymbol;
    }

    public Integer getPaytokenDecimals() {
        return paytokenDecimals;
    }

    public void setPaytokenDecimals(Integer paytokenDecimals) {
        this.paytokenDecimals = paytokenDecimals;
    }

    public String getPaytokenMetadataContent() {
        return paytokenMetadataContent;
    }

    public void setPaytokenMetadataContent(String paytokenMetadataContent) {
        this.paytokenMetadataContent = paytokenMetadataContent;
    }


    public UserBaseInfoVo getUser() {
        return user;
    }

    public void setUser(UserBaseInfoVo user) {
        this.user = user;
    }

}
