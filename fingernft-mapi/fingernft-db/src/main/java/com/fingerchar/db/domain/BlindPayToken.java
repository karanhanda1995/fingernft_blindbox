package com.fingerchar.db.domain;

import com.fingerchar.db.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;

/**
 * @Description BlindPayToken
 * @Author 
 * @Date 2022-04-14
 * @Version 2.1
 */
@TableName("blind_pay_token")
public class BlindPayToken extends BaseEntity {


    /**
     * 名称
     */
    @TableField("`name`")
    private String name;

    /**
     * 简称
     */
    @TableField("`symbol`")
    private String symbol;

    /**
     * asset type  类型
     */
    @TableField("`type`")
    private Integer type;

    /**
     * 合约地址
     */
    @TableField("`token`")
    private String token;

    /**
     * token id
     */
    @TableField("`token_id`")
    private String tokenId;

    /**
     * 精度
     */
    @TableField("`decimals`")
    private Integer decimals;

    /**
     * 与usdt换算比例
     */
    @TableField("`rate`")
    private String rate;

    /**
     * 媒体信息
     */
    @TableField("`metadata_content`")
    private String metadataContent;


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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public Integer getDecimals() {
        return decimals;
    }

    public void setDecimals(Integer decimals) {
        this.decimals = decimals;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getMetadataContent() {
        return metadataContent;
    }

    public void setMetadataContent(String metadataContent) {
        this.metadataContent = metadataContent;
    }

    public static final String NAME = "`name`";

    public static final String SYMBOL = "`symbol`";

    public static final String TYPE = "`type`";

    public static final String TOKEN = "`token`";

    public static final String TOKEN_ID = "`token_id`";

    public static final String DECIMALS = "`decimals`";

    public static final String RATE = "`rate`";

    public static final String METADATA_CONTENT = "`metadata_content`";

    @Override
    public String toString() {
        return "BlindPayToken{" +
        "name=" + name +
        ", symbol=" + symbol +
        ", type=" + type +
        ", token=" + token +
        ", tokenId=" + tokenId +
        ", decimals=" + decimals +
        ", rate=" + rate +
        ", metadataContent=" + metadataContent +
        "}";
    }
}
