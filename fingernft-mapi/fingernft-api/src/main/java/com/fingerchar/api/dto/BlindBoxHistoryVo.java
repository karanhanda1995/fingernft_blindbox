package com.fingerchar.api.dto;

import com.fingerchar.db.domain.BlindBoxHistory;
import com.fingerchar.db.domain.FcUser;

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
    
    private String buyerAvatar;
    
    private String buyerName;
    
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
    }

    public BlindBoxHistoryVo setUser(FcUser user) {
    	this.buyerAvatar = user.getAvatar();
    	this.buyerName = user.getNickname();
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

	public String getBuyerAvatar() {
		return buyerAvatar;
	}

	public void setBuyerAvatar(String buyerAvatar) {
		this.buyerAvatar = buyerAvatar;
	}

	public String getBuyerName() {
		return buyerName;
	}

	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}
}
