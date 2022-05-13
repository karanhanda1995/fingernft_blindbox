package com.fingerchar.db.dto;

public class NftInfo {

    /**
     * 合约id
     */
    private Long contractId;

    /**
     * 合约地址
     */
    private String address;

    /**
     * 分类id
     */
    private Long categoryId;

    /**
     * 图片地址
     */
    private String imgUrl;

    /**
     * 图片保存Id
     */
    private Long storageId;

    /**
     * 对应tokenId
     */
    private String tokenId;

    /**
     * 数量
     */
    private Long quantity;

    /**
     * 是否锁定
     */
    private Boolean locked;

    /**
     * 锁定描述
     */
    private String lockedContent;

    /**
     * 名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 版权
     */
    private String royalties;

    /**
     * 属性
     */
    private String properties;

    /**
     * 是否已验证
     */
    private Integer nftVerify;

    /**
     * 是否已同步链
     */
    private Boolean isSync;

    /**
     * 类型
     */
    private Integer type;

    /**
     * nft拥有者
     */
    private String creator;

    /**
     * 区块链交易hash值
     */
    private String txHash;

    /**
     * 视频音频地址
     */
    private String animUrl;

    /**
     * 视频音频地址id
     */
    private Long animStorageId;

    /**
     * 资源地址
     */
    private String metadataUrl;

    /**
     * 资源内容
     */
    private String metadataContent;

    /**
     * 获取资源次数
     */
    private Integer getMetaTimes;


    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Long getStorageId() {
        return storageId;
    }

    public void setStorageId(Long storageId) {
        this.storageId = storageId;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    public String getLockedContent() {
        return lockedContent;
    }

    public void setLockedContent(String lockedContent) {
        this.lockedContent = lockedContent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRoyalties() {
        return royalties;
    }

    public void setRoyalties(String royalties) {
        this.royalties = royalties;
    }

    public String getProperties() {
        return properties;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }

    public Integer getNftVerify() {
        return nftVerify;
    }

    public void setNftVerify(Integer nftVerify) {
        this.nftVerify = nftVerify;
    }

    public Boolean getIsSync() {
        return isSync;
    }

    public void setIsSync(Boolean isSync) {
        this.isSync = isSync;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getTxHash() {
        return txHash;
    }

    public void setTxHash(String txHash) {
        this.txHash = txHash;
    }

    public String getAnimUrl() {
        return animUrl;
    }

    public void setAnimUrl(String animUrl) {
        this.animUrl = animUrl;
    }

    public Long getAnimStorageId() {
        return animStorageId;
    }

    public void setAnimStorageId(Long animStorageId) {
        this.animStorageId = animStorageId;
    }

    public String getMetadataUrl() {
        return metadataUrl;
    }

    public void setMetadataUrl(String metadataUrl) {
        this.metadataUrl = metadataUrl;
    }

    public String getMetadataContent() {
        return metadataContent;
    }

    public void setMetadataContent(String metadataContent) {
        this.metadataContent = metadataContent;
    }

    public Integer getGetMetaTimes() {
        return getMetaTimes;
    }

    public void setGetMetaTimes(Integer getMetaTimes) {
        this.getMetaTimes = getMetaTimes;
    }


}
