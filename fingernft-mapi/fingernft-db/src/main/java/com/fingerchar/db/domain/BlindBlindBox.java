package com.fingerchar.db.domain;

import com.fingerchar.db.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;

/**
 * @Description BlindBlindBox
 * @Author 
 * @Date 2022-04-14
 * @Version 2.1
 */
@TableName("blind_blind_box")
public class BlindBlindBox extends BaseEntity {


    /**
     * 种类id
     */
    @TableField("`blind_type_id`")
    private Long blindTypeId;

    @TableField("`address`")
    private String address;

    /**
     * 拥有者
     */
    @TableField("`owner`")
    private String owner;

    /**
     * 活动名称
     */
    @TableField("`name`")
    private String name;

    /**
     * 属性
     */
    @TableField("`property`")
    private String property;

    /**
     * 数量
     */
    @TableField("`amount`")
    private Integer amount;

    /**
     * NFT数量（比如：十连抽）
     */
    @TableField("`nft_amount`")
    private Integer nftAmount;

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
     * 售价
     */
    @TableField("`price`")
    private String price;

    /**
     * 是否可重复（0不可重复，1可重复）
     */
    @TableField("`is_repetition`")
    private Boolean isRepetition;

    /**
     * 图片路径
     */
    @TableField("`img_url`")
    private String imgUrl;

    /**
     * img_id
     */
    @TableField("`img_id`")
    private Long imgId;

    /**
     * 支付tokenId
     */
    @TableField("`paytoken_id`")
    private Long paytokenId;

    /**
     * 支付合约地址
     */
    @TableField("`paytoken_address`")
    private String paytokenAddress;

    @TableField("`paytoken_type`")
    private Integer paytokenType;

    @TableField("`paytoken_name`")
    private String paytokenName;

    @TableField("`paytoken_symbol`")
    private String paytokenSymbol;

    @TableField("`paytoken_token_id`")
    private String paytokenTokenId;

    @TableField("`paytoken_decimals`")
    private Integer paytokenDecimals;

    @TableField("`paytoken_metadata_content`")
    private String paytokenMetadataContent;

    /**
     * 是否上链
     */
    @TableField("`is_sync`")
    private Boolean isSync;

    /**
     * 盒子描述
     */
    @TableField("`describe`")
    private String describe;

    /**
     * 持续动画
     */
    @TableField("`anim_1`")
    private String anim1;

    /**
     * 开启动画
     */
    @TableField("`anim_2`")
    private String anim2;


    public Long getBlindTypeId() {
        return blindTypeId;
    }

    public void setBlindTypeId(Long blindTypeId) {
        this.blindTypeId = blindTypeId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getNftAmount() {
        return nftAmount;
    }

    public void setNftAmount(Integer nftAmount) {
        this.nftAmount = nftAmount;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Boolean getIsRepetition() {
        return isRepetition;
    }

    public void setIsRepetition(Boolean isRepetition) {
        this.isRepetition = isRepetition;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Long getImgId() {
        return imgId;
    }

    public void setImgId(Long imgId) {
        this.imgId = imgId;
    }

    public Long getPaytokenId() {
        return paytokenId;
    }

    public void setPaytokenId(Long paytokenId) {
        this.paytokenId = paytokenId;
    }

    public String getPaytokenAddress() {
        return paytokenAddress;
    }

    public void setPaytokenAddress(String paytokenAddress) {
        this.paytokenAddress = paytokenAddress;
    }

    public Integer getPaytokenType() {
        return paytokenType;
    }

    public void setPaytokenType(Integer paytokenType) {
        this.paytokenType = paytokenType;
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

    public String getPaytokenTokenId() {
        return paytokenTokenId;
    }

    public void setPaytokenTokenId(String paytokenTokenId) {
        this.paytokenTokenId = paytokenTokenId;
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

    public Boolean getIsSync() {
        return isSync;
    }

    public void setIsSync(Boolean isSync) {
        this.isSync = isSync;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getAnim1() {
        return anim1;
    }

    public void setAnim1(String anim1) {
        this.anim1 = anim1;
    }

    public String getAnim2() {
        return anim2;
    }

    public void setAnim2(String anim2) {
        this.anim2 = anim2;
    }

    public static final String BLIND_TYPE_ID = "`blind_type_id`";

    public static final String ADDRESS = "`address`";

    public static final String OWNER = "`owner`";

    public static final String NAME = "`name`";

    public static final String PROPERTY = "`property`";

    public static final String AMOUNT = "`amount`";

    public static final String NFT_AMOUNT = "`nft_amount`";

    public static final String START_TIME = "`start_time`";

    public static final String END_TIME = "`end_time`";

    public static final String PRICE = "`price`";

    public static final String IS_REPETITION = "`is_repetition`";

    public static final String IMG_URL = "`img_url`";

    public static final String IMG_ID = "`img_id`";

    public static final String PAYTOKEN_ID = "`paytoken_id`";

    public static final String PAYTOKEN_ADDRESS = "`paytoken_address`";

    public static final String PAYTOKEN_TYPE = "`paytoken_type`";

    public static final String PAYTOKEN_NAME = "`paytoken_name`";

    public static final String PAYTOKEN_SYMBOL = "`paytoken_symbol`";

    public static final String PAYTOKEN_TOKEN_ID = "`paytoken_token_id`";

    public static final String PAYTOKEN_DECIMALS = "`paytoken_decimals`";

    public static final String PAYTOKEN_METADATA_CONTENT = "`paytoken_metadata_content`";

    public static final String IS_SYNC = "`is_sync`";

    public static final String DESCRIBE = "`describe`";

    public static final String ANIM_1 = "`anim_1`";

    public static final String ANIM_2 = "`anim_2`";

    @Override
    public String toString() {
        return "BlindBlindBox{" +
        "blindTypeId=" + blindTypeId +
        ", address=" + address +
        ", owner=" + owner +
        ", name=" + name +
        ", property=" + property +
        ", amount=" + amount +
        ", nftAmount=" + nftAmount +
        ", startTime=" + startTime +
        ", endTime=" + endTime +
        ", price=" + price +
        ", isRepetition=" + isRepetition +
        ", imgUrl=" + imgUrl +
        ", imgId=" + imgId +
        ", paytokenId=" + paytokenId +
        ", paytokenAddress=" + paytokenAddress +
        ", paytokenType=" + paytokenType +
        ", paytokenName=" + paytokenName +
        ", paytokenSymbol=" + paytokenSymbol +
        ", paytokenTokenId=" + paytokenTokenId +
        ", paytokenDecimals=" + paytokenDecimals +
        ", paytokenMetadataContent=" + paytokenMetadataContent +
        ", isSync=" + isSync +
        ", describe=" + describe +
        ", anim1=" + anim1 +
        ", anim2=" + anim2 +
        "}";
    }
}
