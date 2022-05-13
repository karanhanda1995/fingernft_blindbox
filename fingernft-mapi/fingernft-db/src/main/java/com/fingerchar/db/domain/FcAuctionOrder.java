package com.fingerchar.db.domain;

import com.fingerchar.db.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;

/**
 * @Description FcAuctionOrder
 * @Author 
 * @Date 2022-04-14
 * @Version 2.1
 */
@TableName("fc_auction_order")
public class FcAuctionOrder extends BaseEntity {


    /**
     * order发起者
     */
    @TableField("`owner`")
    private String owner;

    /**
     * 售卖token地址
     */
    @TableField("`sell_token`")
    private String sellToken;

    /**
     * 售卖token id
     */
    @TableField("`sell_token_id`")
    private String sellTokenId;

    /**
     * 售卖token类型
     */
    @TableField("`sell_type`")
    private Integer sellType;

    /**
     * 售卖token价格
     */
    @TableField("`sell_value`")
    private String sellValue;

    /**
     * 购买token地址（货币地址）
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
     * 期望的售卖价格
     */
    @TableField("`buying`")
    private String buying;

    @TableField("`type`")
    private Integer type;

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
     * 过期时间
     */
    @TableField("`expired_time`")
    private Long expiredTime;

    /**
     * 起始价格
     */
    @TableField("`start_value`")
    private String startValue;

    /**
     * 最小加价
     */
    @TableField("`range_value`")
    private String rangeValue;

    @TableField("`highestPrice`")
    private String highestprice;

    /**
     * 盐
     */
    @TableField("`salt`")
    private String salt;

    /**
     * 分红
     */
    @TableField("`encourage`")
    private Integer encourage;

    /**
     * 分红地址
     */
    @TableField("`recipients`")
    private String recipients;

    /**
     * 签名
     */
    @TableField("`signature`")
    private String signature;

    /**
     * 状态0 无效， 1取消拍卖，2拍卖完成，3未结算（流拍）
     */
    @TableField("`status_code`")
    private Integer statusCode;

    /**
     * 是否处理（0未处理， 1已处理）
     */
    @TableField("`status`")
    private Boolean status;

    /**
     * usdt价格
     */
    @TableField("`usdt_price`")
    private String usdtPrice;

    /**
     * 卖家手续费率
     */
    @TableField("`seller_fee`")
    private Integer sellerFee;

    /**
     * 买家手续费率
     */
    @TableField("`buyer_fee`")
    private Integer buyerFee;

    @TableField("`paytoken_address`")
    private String paytokenAddress;

    @TableField("`paytoken_name`")
    private String paytokenName;

    @TableField("`paytoken_decimals`")
    private Integer paytokenDecimals;

    @TableField("`paytoken_symbol`")
    private String paytokenSymbol;


    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getSellToken() {
        return sellToken;
    }

    public void setSellToken(String sellToken) {
        this.sellToken = sellToken;
    }

    public String getSellTokenId() {
        return sellTokenId;
    }

    public void setSellTokenId(String sellTokenId) {
        this.sellTokenId = sellTokenId;
    }

    public Integer getSellType() {
        return sellType;
    }

    public void setSellType(Integer sellType) {
        this.sellType = sellType;
    }

    public String getSellValue() {
        return sellValue;
    }

    public void setSellValue(String sellValue) {
        this.sellValue = sellValue;
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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

    public Long getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(Long expiredTime) {
        this.expiredTime = expiredTime;
    }

    public String getStartValue() {
        return startValue;
    }

    public void setStartValue(String startValue) {
        this.startValue = startValue;
    }

    public String getRangeValue() {
        return rangeValue;
    }

    public void setRangeValue(String rangeValue) {
        this.rangeValue = rangeValue;
    }

    public String getHighestprice() {
        return highestprice;
    }

    public void setHighestprice(String highestprice) {
        this.highestprice = highestprice;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public Integer getEncourage() {
        return encourage;
    }

    public void setEncourage(Integer encourage) {
        this.encourage = encourage;
    }

    public String getRecipients() {
        return recipients;
    }

    public void setRecipients(String recipients) {
        this.recipients = recipients;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getUsdtPrice() {
        return usdtPrice;
    }

    public void setUsdtPrice(String usdtPrice) {
        this.usdtPrice = usdtPrice;
    }

    public Integer getSellerFee() {
        return sellerFee;
    }

    public void setSellerFee(Integer sellerFee) {
        this.sellerFee = sellerFee;
    }

    public Integer getBuyerFee() {
        return buyerFee;
    }

    public void setBuyerFee(Integer buyerFee) {
        this.buyerFee = buyerFee;
    }

    public String getPaytokenAddress() {
        return paytokenAddress;
    }

    public void setPaytokenAddress(String paytokenAddress) {
        this.paytokenAddress = paytokenAddress;
    }

    public String getPaytokenName() {
        return paytokenName;
    }

    public void setPaytokenName(String paytokenName) {
        this.paytokenName = paytokenName;
    }

    public Integer getPaytokenDecimals() {
        return paytokenDecimals;
    }

    public void setPaytokenDecimals(Integer paytokenDecimals) {
        this.paytokenDecimals = paytokenDecimals;
    }

    public String getPaytokenSymbol() {
        return paytokenSymbol;
    }

    public void setPaytokenSymbol(String paytokenSymbol) {
        this.paytokenSymbol = paytokenSymbol;
    }

    public static final String OWNER = "`owner`";

    public static final String SELL_TOKEN = "`sell_token`";

    public static final String SELL_TOKEN_ID = "`sell_token_id`";

    public static final String SELL_TYPE = "`sell_type`";

    public static final String SELL_VALUE = "`sell_value`";

    public static final String BUYER_TOKEN = "`buyer_token`";

    public static final String BUYER_TOKEN_ID = "`buyer_token_id`";

    public static final String BUYER_TYPE = "`buyer_type`";

    public static final String BUYING = "`buying`";

    public static final String TYPE = "`type`";

    public static final String START_TIME = "`start_time`";

    public static final String END_TIME = "`end_time`";

    public static final String EXPIRED_TIME = "`expired_time`";

    public static final String START_VALUE = "`start_value`";

    public static final String RANGE_VALUE = "`range_value`";

    public static final String HIGHESTPRICE = "`highestPrice`";

    public static final String SALT = "`salt`";

    public static final String ENCOURAGE = "`encourage`";

    public static final String RECIPIENTS = "`recipients`";

    public static final String SIGNATURE = "`signature`";

    public static final String STATUS_CODE = "`status_code`";

    public static final String STATUS = "`status`";

    public static final String USDT_PRICE = "`usdt_price`";

    public static final String SELLER_FEE = "`seller_fee`";

    public static final String BUYER_FEE = "`buyer_fee`";

    public static final String PAYTOKEN_ADDRESS = "`paytoken_address`";

    public static final String PAYTOKEN_NAME = "`paytoken_name`";

    public static final String PAYTOKEN_DECIMALS = "`paytoken_decimals`";

    public static final String PAYTOKEN_SYMBOL = "`paytoken_symbol`";

    @Override
    public String toString() {
        return "FcAuctionOrder{" +
        "owner=" + owner +
        ", sellToken=" + sellToken +
        ", sellTokenId=" + sellTokenId +
        ", sellType=" + sellType +
        ", sellValue=" + sellValue +
        ", buyerToken=" + buyerToken +
        ", buyerTokenId=" + buyerTokenId +
        ", buyerType=" + buyerType +
        ", buying=" + buying +
        ", type=" + type +
        ", startTime=" + startTime +
        ", endTime=" + endTime +
        ", expiredTime=" + expiredTime +
        ", startValue=" + startValue +
        ", rangeValue=" + rangeValue +
        ", highestprice=" + highestprice +
        ", salt=" + salt +
        ", encourage=" + encourage +
        ", recipients=" + recipients +
        ", signature=" + signature +
        ", statusCode=" + statusCode +
        ", status=" + status +
        ", usdtPrice=" + usdtPrice +
        ", sellerFee=" + sellerFee +
        ", buyerFee=" + buyerFee +
        ", paytokenAddress=" + paytokenAddress +
        ", paytokenName=" + paytokenName +
        ", paytokenDecimals=" + paytokenDecimals +
        ", paytokenSymbol=" + paytokenSymbol +
        "}";
    }
}
