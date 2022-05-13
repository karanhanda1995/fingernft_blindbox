package com.fingerchar.db.domain;

import com.fingerchar.db.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;

/**
 * @Description FcBlockInfo
 * @Author 
 * @Date 2022-04-14
 * @Version 2.1
 */
@TableName("fc_block_info")
public class FcBlockInfo extends BaseEntity {


    /**
     * 区块高度
     */
    @TableField("`block_number`")
    private Long blockNumber;

    /**
     * 区块哈希
     */
    @TableField("`block_hash`")
    private String blockHash;

    /**
     * 区块的时间戳
     */
    @TableField("`timestamp`")
    private Long timestamp;


    public Long getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber(Long blockNumber) {
        this.blockNumber = blockNumber;
    }

    public String getBlockHash() {
        return blockHash;
    }

    public void setBlockHash(String blockHash) {
        this.blockHash = blockHash;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public static final String BLOCK_NUMBER = "`block_number`";

    public static final String BLOCK_HASH = "`block_hash`";

    public static final String TIMESTAMP = "`timestamp`";

    @Override
    public String toString() {
        return "FcBlockInfo{" +
        "blockNumber=" + blockNumber +
        ", blockHash=" + blockHash +
        ", timestamp=" + timestamp +
        "}";
    }
}
