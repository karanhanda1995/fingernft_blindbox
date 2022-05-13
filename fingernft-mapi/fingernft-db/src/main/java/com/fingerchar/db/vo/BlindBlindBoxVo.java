package com.fingerchar.db.vo;

import com.fingerchar.db.domain.BlindBlindBox;
import com.fingerchar.db.domain.BlindBlindBoxOrder;

/**
 * @Author： Zjm
 * @Date：2022/3/27 18:36
 */
public class BlindBlindBoxVo {

    protected Long id;

    protected Boolean deleted;

    protected Long createTime;

    protected Long updateTime;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    private String address;

    /**
     * 活动名称
     */
    private String name;

    /**
     * 属性
     */
    private String property;

    /**
     * 数量
     */
    private Integer amount;

    /**
     * NFT数量（比如：十连抽）
     */
    private Integer nftAmount;

    /**
     * 开始时间
     */
    private Long startTime;

    /**
     * 结束时间
     */
    private Long endTime;

    /**
     * 售价
     */
    private String price;

    /**
     * 是否可重复（0不可重复，1可重复）
     */
    private Boolean isRepetition;

    /**
     * 图片路径
     */
    private String imgUrl;

    /**
     * 图片id
     */
    private Long imgId;

    private Long paytokenId;
    private String paytokenAddress;
    private Integer paytokenType;
    private String paytokenName;

    private String paytokenSymbol;
    private String paytokenTokenId;
    private Integer paytokenDecimals;
    private String paytokenMetadataContent;

    /**
     * 是否上链
     */
    private Boolean isSync;

    /**
     * 描述
     */
    private String describe;

    private String anim1;

    private String anim2;

    private Integer nftNums;


    public BlindBlindBoxOrder getOrder() {
        return order;
    }

    public void setOrder(BlindBlindBoxOrder order) {
        this.order = order;
    }

    private BlindBlindBoxOrder order;

    public BlindBlindBoxVo(BlindBlindBox blindBox) {
        this.id = blindBox.getId();
        this.deleted = blindBox.getDeleted();
        this.createTime = blindBox.getCreateTime();
        this.updateTime = blindBox.getUpdateTime();
        this.address = blindBox.getAddress();
        this.name = blindBox.getName();
        this.property = blindBox.getProperty();
        this.amount = blindBox.getAmount();
        this.nftAmount = blindBox.getNftAmount();
        this.startTime = blindBox.getStartTime();
        this.endTime = blindBox.getEndTime();
        this.price = blindBox.getPrice();
        this.isRepetition = blindBox.getIsRepetition();
        this.imgUrl = blindBox.getImgUrl();
        this.imgId = blindBox.getImgId();
        this.isSync = blindBox.getIsSync();
        this.describe = blindBox.getDescribe();
        this.anim1 = blindBox.getAnim1();
        this.anim2 = blindBox.getAnim2();

        this.paytokenId = blindBox.getPaytokenId();
        this.paytokenAddress = blindBox.getPaytokenAddress();
        this.paytokenName = blindBox.getPaytokenName();
        this.paytokenSymbol = blindBox.getPaytokenSymbol();
        this.paytokenDecimals = blindBox.getPaytokenDecimals();
        this.paytokenTokenId = blindBox.getPaytokenTokenId();
        this.paytokenType = blindBox.getPaytokenType();
        this.paytokenMetadataContent = blindBox.getPaytokenMetadataContent();
    }

    public BlindBlindBoxVo(BlindBlindBox blindBox, BlindBlindBoxOrder blindBoxOrder) {
        this.id = blindBox.getId();
        this.deleted = blindBox.getDeleted();
        this.createTime = blindBox.getCreateTime();
        this.updateTime = blindBox.getUpdateTime();
        this.address = blindBox.getAddress();
        this.name = blindBox.getName();
        this.property = blindBox.getProperty();
        this.amount = blindBox.getAmount();
        this.nftAmount = blindBox.getNftAmount();
        this.startTime = blindBox.getStartTime();
        this.endTime = blindBox.getEndTime();
        this.price = blindBox.getPrice();
        this.isRepetition = blindBox.getIsRepetition();
        this.imgUrl = blindBox.getImgUrl();
        this.imgId = blindBox.getImgId();

        this.paytokenId = blindBox.getPaytokenId();
        this.paytokenAddress = blindBox.getPaytokenAddress();
        this.paytokenName = blindBox.getPaytokenName();
        this.paytokenSymbol = blindBox.getPaytokenSymbol();
        this.paytokenDecimals = blindBox.getPaytokenDecimals();
        this.paytokenTokenId = blindBox.getPaytokenTokenId();
        this.paytokenType = blindBox.getPaytokenType();
        this.paytokenMetadataContent = blindBox.getPaytokenMetadataContent();

        this.isSync = blindBox.getIsSync();
        this.describe = blindBox.getDescribe();

        this.anim1 = blindBox.getAnim1();
        this.anim2 = blindBox.getAnim2();

        if(null != blindBoxOrder) {
            this.order = blindBoxOrder;
        }

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
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

    public Boolean getIsSync() {
        return isSync;
    }

    public void setIsSync(Boolean isSync) {
        this.isSync = isSync;
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

    public Boolean getSync() {
        return isSync;
    }

    public void setSync(Boolean sync) {
        isSync = sync;
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

    public Integer getNftNums() {
        return nftNums;
    }

    public void setNftNums(Integer nftNums) {
        if(null == nftNums) {
            this.nftNums = 0;
        }
        this.nftNums = nftNums;
    }

    public Boolean getRepetition() {
        return isRepetition;
    }

    public void setRepetition(Boolean repetition) {
        isRepetition = repetition;
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

}
