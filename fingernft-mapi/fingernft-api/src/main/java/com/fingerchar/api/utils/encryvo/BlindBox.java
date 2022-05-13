package com.fingerchar.api.utils.encryvo;

import java.math.BigInteger;
import java.util.List;

import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.DynamicStruct;
import org.web3j.abi.datatypes.Uint;
import org.web3j.abi.datatypes.Utf8String;

public class BlindBox extends DynamicStruct {

	private BlindBoxKey key;
	
	private BigInteger opening;
	
	private Boolean repeat;
	
	private BigInteger startTime;
	
	private BigInteger endTime;
	
	private BigInteger buying;
	
	private List<Uint> assetAmounts;
	
	private BigInteger sellerFee;
	
	private List<Utf8String> uris;

	public BlindBox(BlindBoxKey key, BigInteger opening, Boolean repeat, BigInteger startTime, BigInteger endTime, BigInteger buying, List<Uint> assetAmounts, BigInteger sellerFee, List<Utf8String> uris) {
		super(key, new Uint(opening), new Bool(repeat), new Uint(startTime), new Uint(endTime), new Uint(buying), new DynamicArray<>(Uint.class, assetAmounts), new Uint(sellerFee), new DynamicArray<>(Utf8String.class, uris));
		this.key = key;
		this.opening = opening;
		this.repeat = repeat;
		this.startTime = startTime;
		this.endTime = endTime;
		this.buying = buying;
		this.assetAmounts = assetAmounts;
		this.sellerFee = sellerFee;
		this.uris = uris;
	}
	
	public BlindBox(BlindBoxKey key, Uint opening, Bool repeat, Uint startTime, Uint endTime, Uint buying, DynamicArray<Uint> assetAmounts, Uint sellerFee, DynamicArray<Utf8String> uris) {
		super(key, opening, repeat, startTime, endTime, buying, assetAmounts, sellerFee, uris);
		this.key = key;
		this.opening = opening.getValue();
		this.repeat = repeat.getValue();
		this.startTime = startTime.getValue();
		this.endTime = endTime.getValue();
		this.buying = buying.getValue();
		this.assetAmounts = assetAmounts.getValue();
		this.sellerFee = sellerFee.getValue();
		this.uris = uris.getValue();
	}

	public BlindBoxKey getKey() {
		return key;
	}

	public void setKey(BlindBoxKey key) {
		this.key = key;
	}

	public BigInteger getOpening() {
		return opening;
	}

	public void setOpening(BigInteger opening) {
		this.opening = opening;
	}

	public Boolean getRepeat() {
		return repeat;
	}

	public void setRepeat(Boolean repeat) {
		this.repeat = repeat;
	}

	public BigInteger getStartTime() {
		return startTime;
	}

	public void setStartTime(BigInteger startTime) {
		this.startTime = startTime;
	}

	public BigInteger getEndTime() {
		return endTime;
	}

	public void setEndTime(BigInteger endTime) {
		this.endTime = endTime;
	}

	public BigInteger getBuying() {
		return buying;
	}

	public void setBuying(BigInteger buying) {
		this.buying = buying;
	}

	public List<Uint> getAssetAmounts() {
		return assetAmounts;
	}

	public void setAssetAmounts(List<Uint> assetAmounts) {
		this.assetAmounts = assetAmounts;
	}

	public BigInteger getSellerFee() {
		return sellerFee;
	}

	public void setSellerFee(BigInteger sellerFee) {
		this.sellerFee = sellerFee;
	}

	public List<Utf8String> getUris() {
		return uris;
	}

	public void setUris(List<Utf8String> uris) {
		this.uris = uris;
	}
}
