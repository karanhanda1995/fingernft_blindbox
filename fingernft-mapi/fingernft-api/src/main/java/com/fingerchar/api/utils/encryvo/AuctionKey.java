package com.fingerchar.api.utils.encryvo;

import java.math.BigInteger;

import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.StaticStruct;
import org.web3j.abi.datatypes.Uint;

public class AuctionKey extends StaticStruct {

	private String owner;
	
	private BigInteger salt;
	
	private Assets sellAsset;
	
	private Assets buyAsset;
	
	public AuctionKey(String owner, BigInteger salt, Assets sellAsset, Assets buyAsset) {
		super(new Address(owner), new Uint(salt), sellAsset, buyAsset);
		this.owner = owner;
		this.salt = salt;
		this.sellAsset = sellAsset;
		this.buyAsset = buyAsset;
	}
	
	public AuctionKey(Address owner, Uint salt, Assets sellAsset, Assets buyAsset) {
		super(owner, salt, sellAsset, buyAsset);
		this.owner = owner.getValue();
		this.salt = salt.getValue();
		this.sellAsset = sellAsset;
		this.buyAsset = buyAsset;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public BigInteger getSalt() {
		return salt;
	}

	public void setSalt(BigInteger salt) {
		this.salt = salt;
	}

	public Assets getSellAsset() {
		return sellAsset;
	}

	public void setSellAsset(Assets sellAsset) {
		this.sellAsset = sellAsset;
	}

	public Assets getBuyAsset() {
		return buyAsset;
	}

	public void setBuyAsset(Assets buyAsset) {
		this.buyAsset = buyAsset;
	}
}
