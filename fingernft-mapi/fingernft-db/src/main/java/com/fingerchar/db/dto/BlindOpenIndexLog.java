package com.fingerchar.db.dto;

import java.math.BigInteger;

/**
 * @Author： Zjm
 * @Date：2022/3/29 10:42
 */
public class BlindOpenIndexLog {
    private BigInteger index;
    private String txHash;
    private BigInteger blockTimestamp;

    public BlindOpenIndexLog(
            BigInteger index,
            String txHash,
            BigInteger blockTimestamp
    ){
        this.index = index;
        this.txHash = txHash;
        this.blockTimestamp = blockTimestamp;
    }

    public BigInteger getIndex() {
        return index;
    }

    public void setIndex(BigInteger index) {
        this.index = index;
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
