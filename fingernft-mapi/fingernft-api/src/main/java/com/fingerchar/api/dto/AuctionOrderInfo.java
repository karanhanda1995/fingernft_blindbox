package com.fingerchar.api.dto;

import com.fingerchar.db.domain.FcAuctionOrder;
import com.fingerchar.db.domain.FcPayToken;

import java.util.List;

public class AuctionOrderInfo {

	private Long id;

    private String owner;

    private String sellToken;

    private String sellTokenId;

    private Integer sellType;

    private String sellValue;

    private String buyerToken;

    private String buyerTokenId;

    private Integer buyerType;

    private String buying;

    private String startValue;

    private String rangeValue;
    
    private Long startTime;

    private Long endTime;

    private Long expiredTime;

    private String salt;

    private Integer encourage;
    
    private List<String> recipients;
    
    private String sellerFee;

    private String signature;

    private Integer statusCode;

    private Boolean status;
    
    private String r;
	
	private String s;
	
	private String v;
	
	private String buyerFee;
	
	private String buyerValue;
	
	private String buyer;
	
	private String message;

	private String metadataContent;

	private Boolean deleted;

	private Long createTime;

	private Long updateTime;

	private String highestPrice;

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	private Integer type;

	private String paytokenAddress;
	private String paytokenName;


	private Integer paytokenDecimals;
	private String paytokenSymbol;
	
	public AuctionOrderInfo() {
		
	}
	
	public AuctionOrderInfo(FcAuctionOrder order) {
		if (null != order.getId()) {
			this.id = order.getId();
		}
		this.buyerToken = order.getBuyerToken();
		this.buyerTokenId = order.getBuyerTokenId();
		this.buyerType = order.getBuyerType();
		this.buying = order.getBuying();
		this.encourage = order.getEncourage();
		this.startTime = order.getStartTime();
		this.endTime = order.getEndTime();
		this.status = order.getStatus();
		this.expiredTime = order.getExpiredTime();
		this.owner = order.getOwner();
		this.salt = order.getSalt();
		this.sellToken = order.getSellToken();
		this.sellTokenId = order.getSellTokenId();
		this.sellType = order.getSellType();
		this.sellValue = order.getSellValue();
		this.type = order.getType();
		this.statusCode = order.getStatusCode();
		this.startValue = order.getStartValue();
		this.rangeValue = order.getRangeValue();
		this.signature = order.getSignature();
		this.sellerFee = order.getSellerFee().toString();
		this.buyerFee = order.getBuyerFee().toString();
		this.deleted = order.getDeleted();
		this.createTime = order.getCreateTime();
		this.updateTime = order.getUpdateTime();
	}

	public AuctionOrderInfo(FcAuctionOrder order, FcPayToken fcPayToken) {
		if (null != order.getId()) {
			this.id = order.getId();
		}
		this.buyerToken = order.getBuyerToken();
		this.buyerTokenId = order.getBuyerTokenId();
		this.buyerType = order.getBuyerType();
		this.buying = order.getBuying();
		this.encourage = order.getEncourage();
		this.startTime = order.getStartTime();
		this.endTime = order.getEndTime();
		this.status = order.getStatus();
		this.expiredTime = order.getExpiredTime();
		this.owner = order.getOwner();
		this.salt = order.getSalt();
		this.sellToken = order.getSellToken();
		this.sellTokenId = order.getSellTokenId();
		this.sellType = order.getSellType();
		this.sellValue = order.getSellValue();
		this.statusCode = order.getStatusCode();
		this.startValue = order.getStartValue();
		this.rangeValue = order.getRangeValue();
		this.signature = order.getSignature();
		this.deleted = order.getDeleted();
		this.createTime = order.getCreateTime();
		this.updateTime = order.getUpdateTime();
		this.paytokenAddress = fcPayToken.getAddress();
		this.paytokenName = fcPayToken.getName();
		this.paytokenDecimals = fcPayToken.getDecimals();
		this.paytokenSymbol = fcPayToken.getSymbol();
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getSellToken() {
		return sellToken;
	}

	public void setSellToken(String sellToken) {
		this.sellToken = sellToken;
	}

	public String getSellTokenId() {
		return sellTokenId;
	}

	public void setSellTokenId(String sellTokenId) {
		this.sellTokenId = sellTokenId;
	}

	public Integer getSellType() {
		return sellType;
	}

	public void setSellType(Integer sellType) {
		this.sellType = sellType;
	}

	public String getSellValue() {
		return sellValue;
	}

	public void setSellValue(String sellValue) {
		this.sellValue = sellValue;
	}

	public String getBuyerToken() {
		return buyerToken;
	}

	public void setBuyerToken(String buyerToken) {
		this.buyerToken = buyerToken;
	}

	public String getBuyerTokenId() {
		return buyerTokenId;
	}

	public void setBuyerTokenId(String buyerTokenId) {
		this.buyerTokenId = buyerTokenId;
	}

	public Integer getBuyerType() {
		return buyerType;
	}

	public void setBuyerType(Integer buyerType) {
		this.buyerType = buyerType;
	}

	public String getBuying() {
		return buying;
	}

	public void setBuying(String buying) {
		this.buying = buying;
	}

	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	public Long getEndTime() {
		return endTime;
	}

	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}

	public Long getExpiredTime() {
		return expiredTime;
	}

	public void setExpiredTime(Long expiredTime) {
		this.expiredTime = expiredTime;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public Integer getEncourage() {
		return encourage;
	}

	public void setEncourage(Integer encourage) {
		this.encourage = encourage;
	}
	
	public List<String> getRecipients() {
		return recipients;
	}

	public void setRecipients(List<String> recipients) {
		this.recipients = recipients;
	}

	public String getSellerFee() {
		return sellerFee;
	}

	public void setSellerFee(String sellerFee) {
		this.sellerFee = sellerFee;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public Integer getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public String getR() {
		return r;
	}

	public void setR(String r) {
		this.r = r;
	}

	public String getS() {
		return s;
	}

	public void setS(String s) {
		this.s = s;
	}

	public String getV() {
		return v;
	}

	public void setV(String v) {
		this.v = v;
	}

	public String getBuyerFee() {
		return buyerFee;
	}

	public void setBuyerFee(String buyerFee) {
		this.buyerFee = buyerFee;
	}

	public String getBuyerValue() {
		return buyerValue;
	}

	public void setBuyerValue(String buyerValue) {
		this.buyerValue = buyerValue;
	}

	public String getBuyer() {
		return buyer;
	}

	public void setBuyer(String buyer) {
		this.buyer = buyer;
	}

	public String getStartValue() {
		return startValue;
	}

	public void setStartValue(String startValue) {
		this.startValue = startValue;
	}

	public String getRangeValue() {
		return rangeValue;
	}

	public void setRangeValue(String rangeValue) {
		this.rangeValue = rangeValue;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMetadataContent() {
		return metadataContent;
	}

	public void setMetadataContent(String metadataContent) {
		this.metadataContent = metadataContent;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public Long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}

	public String getHighestPrice() {
		return highestPrice;
	}

	public void setHighestPrice(String highestPrice) {
		this.highestPrice = highestPrice;
	}

	public String getPaytokenAddress() {
		return paytokenAddress;
	}

	public void setPaytokenAddress(String paytokenAddress) {
		this.paytokenAddress = paytokenAddress;
	}

	public String getPaytokenName() {
		return paytokenName;
	}

	public void setPaytokenName(String paytokenName) {
		this.paytokenName = paytokenName;
	}

	public Integer getPaytokenDecimals() {
		return paytokenDecimals;
	}

	public void setPaytokenDecimals(Integer paytokenDecimals) {
		this.paytokenDecimals = paytokenDecimals;
	}

	public String getPaytokenSymbol() {
		return paytokenSymbol;
	}

	public void setPaytokenSymbol(String paytokenSymbol) {
		this.paytokenSymbol = paytokenSymbol;
	}
}
