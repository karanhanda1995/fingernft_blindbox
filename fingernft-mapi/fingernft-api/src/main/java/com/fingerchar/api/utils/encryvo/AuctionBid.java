package com.fingerchar.api.utils.encryvo;

import org.web3j.abi.datatypes.StaticStruct;
import org.web3j.abi.datatypes.Uint;

import java.math.BigInteger;

public class AuctionBid extends StaticStruct {

	public Auction auction;
	
	private BigInteger buyerValue;
	
	public AuctionBid(Auction auction, BigInteger buyerValue) {
		super(auction, new Uint(buyerValue));
		this.auction = auction;
		this.buyerValue = buyerValue;
	}
	
	public AuctionBid(Auction auction, Uint buyerValue) {
		super(auction, buyerValue);
		this.auction = auction;
		this.buyerValue = buyerValue.getValue();
	}

	public Auction getAuction() {
		return auction;
	}

	public void setAuction(Auction auction) {
		this.auction = auction;
	}

	public BigInteger getBuyerValue() {
		return buyerValue;
	}

	public void setBuyerValue(BigInteger buyerValue) {
		this.buyerValue = buyerValue;
	}
}
