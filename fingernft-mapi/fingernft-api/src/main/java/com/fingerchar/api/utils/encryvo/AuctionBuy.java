package com.fingerchar.api.utils.encryvo;

import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.DynamicStruct;
import org.web3j.abi.datatypes.Uint;

import java.math.BigInteger;
import java.util.List;

public class AuctionBuy extends DynamicStruct {

	private Auction auction;
	
	private BigInteger buyerValue;
	
	private String buyer;
	
	private List<Address> recipinets;
	
	private BigInteger buyerFee;
	
	public AuctionBuy(Auction auction, BigInteger buyerValue, String buyer, List<Address> recipinets, BigInteger buyerFee) {
		super(auction, new DynamicArray<>(Address.class, recipinets), new Uint(buyerFee));
		this.auction = auction;
		this.buyerValue = buyerValue;
		this.buyer = buyer;
		this.recipinets = recipinets;
		this.buyerFee = buyerFee;
	}
	
	public AuctionBuy(Auction auction, Uint buyerValue, Address buyer, DynamicArray<Address> recipinets, Uint buyerFee) {
		super(auction, buyerValue, buyer, recipinets, buyerFee);
		this.auction = auction;
		this.buyerValue = buyerValue.getValue();
		this.buyer = buyer.getValue();
		this.recipinets = recipinets.getValue();
		this.buyerFee = buyerFee.getValue();
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

	public String getBuyer() {
		return buyer;
	}

	public void setBuyer(String buyer) {
		this.buyer = buyer;
	}

	public List<Address> getRecipinets() {
		return recipinets;
	}

	public void setRecipinets(List<Address> recipinets) {
		this.recipinets = recipinets;
	}

	public BigInteger getBuyerFee() {
		return buyerFee;
	}

	public void setBuyerFee(BigInteger buyerFee) {
		this.buyerFee = buyerFee;
	}
}
