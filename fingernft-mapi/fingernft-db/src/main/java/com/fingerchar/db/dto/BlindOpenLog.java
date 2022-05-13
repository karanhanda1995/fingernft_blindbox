package com.fingerchar.db.dto;

import java.math.BigInteger;

/**
 * @Author： Zjm
 * @Date：2022/3/23 17:14
 */
public class BlindOpenLog {
    private String owner;

    private String buyToken;

    private BigInteger buyTokenId;

    private BigInteger buyValue;

    private String buyer;

    private BigInteger amount;

    private BigInteger salt;

    private String txHash;

    private BigInteger blockTimestamp;


    public BlindOpenLog(
            String owner,
            String buyToken,
            BigInteger buyTokenId,
            BigInteger buyValue,
            String buyer,
            BigInteger amount,
            BigInteger salt,
            String txHash,
            BigInteger blockTimestamp
    ){
        this.owner = owner;
        this.buyToken = buyToken;
        this.buyTokenId = buyTokenId;
        this.buyValue = buyValue;
        this.buyer = buyer;
        this.amount = amount;
        this.salt = salt;
        this.txHash = txHash;
        this.blockTimestamp = blockTimestamp;
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

    public BigInteger getBuyTokenId() {
        return buyTokenId;
    }

    public void setBuyTokenId(BigInteger buyTokenId) {
        this.buyTokenId = buyTokenId;
    }

    public BigInteger getBuyValue() {
        return buyValue;
    }

    public void setBuyValue(BigInteger buyValue) {
        this.buyValue = buyValue;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public BigInteger getAmount() {
        return amount;
    }

    public void setAmount(BigInteger amount) {
        this.amount = amount;
    }

    public BigInteger getSalt() {
        return salt;
    }

    public void setSalt(BigInteger salt) {
        this.salt = salt;
    }

    public String getTxHash() {
        return txHash;
    }

    public void setTxHash(String txHash) {
        this.txHash = txHash;
    }

    public BigInteger getBlockTimestamp() {
        return blockTimestamp;
    }

    public void setBlockTimestamp(BigInteger blockTimestamp) {
        this.blockTimestamp = blockTimestamp;
    }

}
