package com.fingerchar.db.domain;

import com.fingerchar.db.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;

/**
 * @Description BlindBlindBoxOrder
 * @Author 
 * @Date 2022-04-14
 * @Version 2.1
 */
@TableName("blind_blind_box_order")
public class BlindBlindBoxOrder extends BaseEntity {


    /**
     * 盲盒id
     */
    @TableField("`blind_box_id`")
    private Long blindBoxId;

    /**
     * 盲盒发起者
     */
    @TableField("`owner`")
    private String owner;

    /**
     * 盲盒NFT信息
     */
    @TableField("`sell_assets`")
    private String sellAssets;

    /**
     * 购买token信息
     */
    @TableField("`buyer_token`")
    private String buyerToken;

    /**
     * 购买token id
     */
    @TableField("`buyer_token_id`")
    private String buyerTokenId;

    /**
     * 购买token类型
     */
    @TableField("`buyer_type`")
    private Integer buyerType;

    /**
     * 购买token价格
     */
    @TableField("`buying`")
    private String buying;

    /**
     * 盲盒NFT个数{数字}
     */
    @TableField("`sellings`")
    private String sellings;

    /**
     * 盐
     */
    @TableField("`salt`")
    private String salt;

    @TableField("`message`")
    private String message;

    /**
     * 签名
     */
    @TableField("`signature`")
    private String signature;

    /**
     * 一次可以开多少个
     */
    @TableField("`opening`")
    private Long opening;

    /**
     * 是否可以重复
     */
    @TableField("`repeat`")
    private Boolean repeat;

    /**
     * 已经开启次数
     */
    @TableField("`openeds`")
    private Long openeds;

    /**
     * 开始时间
     */
    @TableField("`start_time`")
    private Long startTime;

    /**
     * 结束时间
     */
    @TableField("`end_time`")
    private Long endTime;

    /**
     * nft uri信息
     */
    @TableField("`uris`")
    private String uris;

    @TableField("`sellerFee`")
    private Integer sellerfee;

    @TableField("`buyerFee`")
    private Integer buyerfee;

    @TableField("`paytoken_name`")
    private String paytokenName;

    @TableField("`paytoken_symbol`")
    private String paytokenSymbol;

    @TableField("`paytoken_decimals`")
    private Integer paytokenDecimals;

    @TableField("`paytoken_metadata_content`")
    private String paytokenMetadataContent;


    public Long getBlindBoxId() {
        return blindBoxId;
    }

    public void setBlindBoxId(Long blindBoxId) {
        this.blindBoxId = blindBoxId;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getSellAssets() {
        return sellAssets;
    }

    public void setSellAssets(String sellAssets) {
        this.sellAssets = sellAssets;
    }

    public String getBuyerToken() {
        return buyerToken;
    }

    public void setBuyerToken(String buyerToken) {
        this.buyerToken = buyerToken;
    }

    public String getBuyerTokenId() {
        return buyerTokenId;
    }

    public void setBuyerTokenId(String buyerTokenId) {
        this.buyerTokenId = buyerTokenId;
    }

    public Integer getBuyerType() {
        return buyerType;
    }

    public void setBuyerType(Integer buyerType) {
        this.buyerType = buyerType;
    }

    public String getBuying() {
        return buying;
    }

    public void setBuying(String buying) {
        this.buying = buying;
    }

    public String getSellings() {
        return sellings;
    }

    public void setSellings(String sellings) {
        this.sellings = sellings;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public Long getOpening() {
        return opening;
    }

    public void setOpening(Long opening) {
        this.opening = opening;
    }

    public Boolean getRepeat() {
        return repeat;
    }

    public void setRepeat(Boolean repeat) {
        this.repeat = repeat;
    }

    public Long getOpeneds() {
        return openeds;
    }

    public void setOpeneds(Long openeds) {
        this.openeds = openeds;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public String getUris() {
        return uris;
    }

    public void setUris(String uris) {
        this.uris = uris;
    }

    public Integer getSellerfee() {
        return sellerfee;
    }

    public void setSellerfee(Integer sellerfee) {
        this.sellerfee = sellerfee;
    }

    public Integer getBuyerfee() {
        return buyerfee;
    }

    public void setBuyerfee(Integer buyerfee) {
        this.buyerfee = buyerfee;
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

    public static final String BLIND_BOX_ID = "`blind_box_id`";

    public static final String OWNER = "`owner`";

    public static final String SELL_ASSETS = "`sell_assets`";

    public static final String BUYER_TOKEN = "`buyer_token`";

    public static final String BUYER_TOKEN_ID = "`buyer_token_id`";

    public static final String BUYER_TYPE = "`buyer_type`";

    public static final String BUYING = "`buying`";

    public static final String SELLINGS = "`sellings`";

    public static final String SALT = "`salt`";

    public static final String MESSAGE = "`message`";

    public static final String SIGNATURE = "`signature`";

    public static final String OPENING = "`opening`";

    public static final String REPEAT = "`repeat`";

    public static final String OPENEDS = "`openeds`";

    public static final String START_TIME = "`start_time`";

    public static final String END_TIME = "`end_time`";

    public static final String URIS = "`uris`";

    public static final String SELLERFEE = "`sellerFee`";

    public static final String BUYERFEE = "`buyerFee`";

    public static final String PAYTOKEN_NAME = "`paytoken_name`";

    public static final String PAYTOKEN_SYMBOL = "`paytoken_symbol`";

    public static final String PAYTOKEN_DECIMALS = "`paytoken_decimals`";

    public static final String PAYTOKEN_METADATA_CONTENT = "`paytoken_metadata_content`";

    @Override
    public String toString() {
        return "BlindBlindBoxOrder{" +
        "blindBoxId=" + blindBoxId +
        ", owner=" + owner +
        ", sellAssets=" + sellAssets +
        ", buyerToken=" + buyerToken +
        ", buyerTokenId=" + buyerTokenId +
        ", buyerType=" + buyerType +
        ", buying=" + buying +
        ", sellings=" + sellings +
        ", salt=" + salt +
        ", message=" + message +
        ", signature=" + signature +
        ", opening=" + opening +
        ", repeat=" + repeat +
        ", openeds=" + openeds +
        ", startTime=" + startTime +
        ", endTime=" + endTime +
        ", uris=" + uris +
        ", sellerfee=" + sellerfee +
        ", buyerfee=" + buyerfee +
        ", paytokenName=" + paytokenName +
        ", paytokenSymbol=" + paytokenSymbol +
        ", paytokenDecimals=" + paytokenDecimals +
        ", paytokenMetadataContent=" + paytokenMetadataContent +
        "}";
    }
}
