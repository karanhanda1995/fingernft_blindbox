package com.fingerchar.db.domain;

import com.fingerchar.db.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;

/**
 * @Description BlindBoxHistory
 * @Author 
 * @Date 2022-04-14
 * @Version 2.1
 */
@TableName("blind_box_history")
public class BlindBoxHistory extends BaseEntity {


    /**
     * 拥有者address
     */
    @TableField("`owner`")
    private String owner;

    /**
     * 盲盒id
     */
    @TableField("`blind_box_id`")
    private Long blindBoxId;

    /**
     * 支付代币address
     */
    @TableField("`buy_token`")
    private String buyToken;

    /**
     * 支付代币id
     */
    @TableField("`buy_token_id`")
    private String buyTokenId;

    /**
     * 买的内容
     */
    @TableField("`buy_value`")
    private String buyValue;

    /**
     * 买的address
     */
    @TableField("`buyer`")
    private String buyer;

    /**
     * 数量
     */
    @TableField("`amount`")
    private String amount;

    /**
     * 盐
     */
    @TableField("`salt`")
    private String salt;

    /**
     * 开出的NFT信息
     */
    @TableField("`nft_infos`")
    private String nftInfos;

    @TableField("`paytoken_name`")
    private String paytokenName;

    @TableField("`paytoken_symbol`")
    private String paytokenSymbol;

    @TableField("`paytoken_decimals`")
    private Integer paytokenDecimals;

    @TableField("`paytoken_metadata_content`")
    private String paytokenMetadataContent;

    /**
     * txHash值
     */
    @TableField("`tx_hash`")
    private String txHash;


    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Long getBlindBoxId() {
        return blindBoxId;
    }

    public void setBlindBoxId(Long blindBoxId) {
        this.blindBoxId = blindBoxId;
    }

    public String getBuyToken() {
        return buyToken;
    }

    public void setBuyToken(String buyToken) {
        this.buyToken = buyToken;
    }

    public String getBuyTokenId() {
        return buyTokenId;
    }

    public void setBuyTokenId(String buyTokenId) {
        this.buyTokenId = buyTokenId;
    }

    public String getBuyValue() {
        return buyValue;
    }

    public void setBuyValue(String buyValue) {
        this.buyValue = buyValue;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getNftInfos() {
        return nftInfos;
    }

    public void setNftInfos(String nftInfos) {
        this.nftInfos = nftInfos;
    }

    public String getPaytokenName() {
        return paytokenName;
    }

    public void setPaytokenName(String paytokenName) {
        this.paytokenName = paytokenName;
    }

    public String getPaytokenSymbol() {
        return paytokenSymbol;
    }

    public void setPaytokenSymbol(String paytokenSymbol) {
        this.paytokenSymbol = paytokenSymbol;
    }

    public Integer getPaytokenDecimals() {
        return paytokenDecimals;
    }

    public void setPaytokenDecimals(Integer paytokenDecimals) {
        this.paytokenDecimals = paytokenDecimals;
    }

    public String getPaytokenMetadataContent() {
        return paytokenMetadataContent;
    }

    public void setPaytokenMetadataContent(String paytokenMetadataContent) {
        this.paytokenMetadataContent = paytokenMetadataContent;
    }

    public String getTxHash() {
        return txHash;
    }

    public void setTxHash(String txHash) {
        this.txHash = txHash;
    }

    public static final String OWNER = "`owner`";

    public static final String BLIND_BOX_ID = "`blind_box_id`";

    public static final String BUY_TOKEN = "`buy_token`";

    public static final String BUY_TOKEN_ID = "`buy_token_id`";

    public static final String BUY_VALUE = "`buy_value`";

    public static final String BUYER = "`buyer`";

    public static final String AMOUNT = "`amount`";

    public static final String SALT = "`salt`";

    public static final String NFT_INFOS = "`nft_infos`";

    public static final String PAYTOKEN_NAME = "`paytoken_name`";

    public static final String PAYTOKEN_SYMBOL = "`paytoken_symbol`";

    public static final String PAYTOKEN_DECIMALS = "`paytoken_decimals`";

    public static final String PAYTOKEN_METADATA_CONTENT = "`paytoken_metadata_content`";

    public static final String TX_HASH = "`tx_hash`";

    @Override
    public String toString() {
        return "BlindBoxHistory{" +
        "owner=" + owner +
        ", blindBoxId=" + blindBoxId +
        ", buyToken=" + buyToken +
        ", buyTokenId=" + buyTokenId +
        ", buyValue=" + buyValue +
        ", buyer=" + buyer +
        ", amount=" + amount +
        ", salt=" + salt +
        ", nftInfos=" + nftInfos +
        ", paytokenName=" + paytokenName +
        ", paytokenSymbol=" + paytokenSymbol +
        ", paytokenDecimals=" + paytokenDecimals +
        ", paytokenMetadataContent=" + paytokenMetadataContent +
        ", txHash=" + txHash +
        "}";
    }
}
