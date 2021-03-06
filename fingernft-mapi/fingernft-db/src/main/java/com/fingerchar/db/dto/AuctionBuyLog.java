package com.fingerchar.db.dto;

import java.math.BigInteger;

/**
 * @Author： Zjm
 * @Date：2022/3/23 17:19
 */
public class AuctionBuyLog {
    private String sellToken;

    private BigInteger sellTokenId;

    private BigInteger sellValue;

    private String owner;

    private String buyToken;

    private BigInteger buyTokenId;

    private BigInteger buyValue;

    private String buyer;

    private BigInteger salt;

    private BigInteger buying;

    private String txHash;

    private BigInteger blockTimestamp;

    public AuctionBuyLog(
            String sellToken,
            BigInteger sellTokenId,
            BigInteger sellValue,
            String owner,
            String buyToken,
            BigInteger buyTokenId,
            BigInteger buyValue,
            String buyer,
            BigInteger buying,
            BigInteger salt,
            String txHash,
            BigInteger blockTimestamp
    ){
        this.sellToken = sellToken;
        this.sellTokenId = sellTokenId;
        this.sellValue = sellValue;
        this.owner = owner;
        this.buyToken = buyToken;
        this.buyTokenId = buyTokenId;
        this.buyValue = buyValue;
        this.buyer = buyer;
        this.buying = buying;
        this.salt = salt;
        this.txHash = txHash;
        this.blockTimestamp = blockTimestamp;
    }

    public String getSellToken() {
        return sellToken;
    }

    public void setSellToken(String sellToken) {
        this.sellToken = sellToken;
    }

    public BigInteger getSellTokenId() {
        return sellTokenId;
    }

    public void setSellTokenId(BigInteger sellTokenId) {
        this.sellTokenId = sellTokenId;
    }

    public BigInteger getSellValue() {
        return sellValue;
    }

    public void setSellValue(BigInteger sellValue) {
        this.sellValue = sellValue;
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

    public BigInteger getSalt() {
        return salt;
    }

    public void setSalt(BigInteger salt) {
        this.salt = salt;
    }

    public BigInteger getBuying() {
        return buying;
    }

    public void setBuying(BigInteger buying) {
        this.buying = buying;
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
