package com.fingerchar.db.vo;

import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.DynamicStruct;
import org.web3j.abi.datatypes.Uint;
import java.math.BigInteger;
import java.util.List;

public class BlindBoxKey extends DynamicStruct {

    private String owner;

    private BigInteger salt;

    private List<BlindBoxAssets> sellAssets;

    private BlindBoxAssets buyAsset;

    public BlindBoxKey(String owner, BigInteger salt, List<BlindBoxAssets> sellAssets, BlindBoxAssets buyAsset) {
        super(new Address(owner), new Uint(salt), new DynamicArray<>(BlindBoxAssets.class, sellAssets), buyAsset);
        this.owner = owner;
        this.salt = salt;
        this.sellAssets = sellAssets;
        this.buyAsset = buyAsset;
    }

    public BlindBoxKey(Address owner, Uint salt, DynamicArray<BlindBoxAssets> sellAssets, BlindBoxAssets buyAsset) {
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

    public List<BlindBoxAssets> getSellAssets() {
        return sellAssets;
    }

    public void setSellAssets(List<BlindBoxAssets> sellAssets) {
        this.sellAssets = sellAssets;
    }

    public BlindBoxAssets getBuyAsset() {
        return buyAsset;
    }

    public void setBuyAsset(BlindBoxAssets buyAsset) {
        this.buyAsset = buyAsset;
    }
}
