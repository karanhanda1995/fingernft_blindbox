package com.fingerchar.api.utils.encryvo;

import java.math.BigInteger;
import java.util.List;

import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.DynamicStruct;
import org.web3j.abi.datatypes.Uint;

public class BlindBoxKey extends DynamicStruct {

	private String owner;
	
	private BigInteger salt;
	
	private List<Assets> sellAssets;
	
	private Assets buyAsset;
	
	public BlindBoxKey(String owner, BigInteger salt, List<Assets> sellAssets, Assets buyAsset) {
		super(new Address(owner), new Uint(salt), new DynamicArray<>(Assets.class, sellAssets), buyAsset);
		this.owner = owner;
		this.salt = salt;
		this.sellAssets = sellAssets;
		this.buyAsset = buyAsset;
	}
	
	public BlindBoxKey(Address owner, Uint salt, DynamicArray<Assets> sellAssets, Assets buyAsset) {
		super(owner, salt, sellAssets, buyAsset);
		this.owner = owner.getValue();
		this.salt = salt.getValue();
		this.sellAssets = sellAssets.getValue();
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

	public List<Assets> getSellAssets() {
		return sellAssets;
	}

	public void setSellAssets(List<Assets> sellAssets) {
		this.sellAssets = sellAssets;
	}

	public Assets getBuyAsset() {
		return buyAsset;
	}

	public void setBuyAsset(Assets buyAsset) {
		this.buyAsset = buyAsset;
	}
}
