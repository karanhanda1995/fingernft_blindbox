package com.fingerchar.api.utils.encryvo;

import java.math.BigInteger;

import org.web3j.abi.datatypes.StaticStruct;
import org.web3j.abi.datatypes.Uint;

public class Auction extends StaticStruct {

	private AuctionKey key;
	
	private BigInteger selling;
	
	private BigInteger buying;
	
	private BigInteger endTime;
	
	private BigInteger expiredTime;
	
	private BigInteger encourage;
	
	private BigInteger sellerFee;
	
	public Auction(AuctionKey key, BigInteger selling, BigInteger buying, BigInteger endTime, BigInteger expiredTime, BigInteger encourage, BigInteger sellerFee) {
		super(key, new Uint(selling), new Uint(buying), new Uint(endTime), new Uint(expiredTime), new Uint(encourage), new Uint(sellerFee));
		this.key = key;
		this.selling = selling;
		this.buying = buying;
		this.endTime = endTime;
		this.expiredTime = expiredTime;
		this.encourage = encourage;
		this.sellerFee = sellerFee;
	}
	
	public Auction(AuctionKey key, Uint selling, Uint buying, Uint endTime, Uint expiredTime, Uint encourage, Uint sellerFee) {
		super(key, selling, buying, endTime, expiredTime, encourage, sellerFee);
		this.key = key;
		this.selling = selling.getValue();
		this.buying = buying.getValue();
		this.endTime = endTime.getValue();
		this.expiredTime = expiredTime.getValue();
		this.encourage = encourage.getValue();
		this.sellerFee = sellerFee.getValue();
	}

	public AuctionKey getKey() {
		return key;
	}

	public void setKey(AuctionKey key) {
		this.key = key;
	}

	public BigInteger getSelling() {
		return selling;
	}

	public void setSelling(BigInteger selling) {
		this.selling = selling;
	}

	public BigInteger getBuying() {
		return buying;
	}

	public void setBuying(BigInteger buying) {
		this.buying = buying;
	}

	public BigInteger getEndTime() {
		return endTime;
	}

	public void setEndTime(BigInteger endTime) {
		this.endTime = endTime;
	}

	public BigInteger getExpiredTime() {
		return expiredTime;
	}

	public void setExpiredTime(BigInteger expiredTime) {
		this.expiredTime = expiredTime;
	}

	public BigInteger getEncourage() {
		return encourage;
	}

	public void setEncourage(BigInteger encourage) {
		this.encourage = encourage;
	}

	public BigInteger getSellerFee() {
		return sellerFee;
	}

	public void setSellerFee(BigInteger sellerFee) {
		this.sellerFee = sellerFee;
	}
}
