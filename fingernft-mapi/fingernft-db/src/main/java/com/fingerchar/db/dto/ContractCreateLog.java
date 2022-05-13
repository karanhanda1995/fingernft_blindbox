package com.fingerchar.db.dto;

import java.math.BigInteger;

/**
 * @Author： Zjm
 * @Date：2022/3/23 20:28
 */
public class ContractCreateLog {

    private String address;
    private String creator;
    private String signer;
    private String name;
    private String symbol;
    private String txHash;
    private BigInteger blockTimestamp;

    private Integer type;

    public ContractCreateLog(String address, String creator, String signer, String name, String symbol, Integer type, String txHash, BigInteger blockTimestamp){
        this.address = address;
        this.creator = creator;
        this.signer = signer;
        this.name = name;
        this.symbol = symbol;
        this.type = type;
        this.txHash = txHash;
        this.blockTimestamp = blockTimestamp;
    }
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getSigner() {
        return signer;
    }

    public void setSigner(String signer) {
        this.signer = signer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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
