package com.fingerchar.admin.dto;

import com.fingerchar.db.domain.BlindBlindBoxToNft;
import com.fingerchar.db.domain.BlindNft;

public class BlindNftDto {

	private Long id;
	
    private Long blindTypeId;

    private String name;

    private String propertys;

    private String description;

    private String imgUrl;

    private Long storageId;

    private String animUrl;

    private Long animStorageId;

    private String videoType;

    private String address;

    private String tokenId;

    private Integer type;

    private Boolean isSync;

    private String metadataUrl;

    private String metadataContent;
    
    private Boolean deleted;
    
    private Long createTime;
    
    private Long updateTime;
    
    private Integer amount;
    
    public BlindNftDto() {
    	
    }
	public BlindNftDto(BlindNft nft, BlindBlindBoxToNft boxToNft) {
		this.id = nft.getId();
		this.address = nft.getAddress();
		this.blindTypeId = nft.getBlindTypeId();
		this.createTime = nft.getCreateTime();
		this.deleted = nft.getDeleted();
		this.description = nft.getDescription();
		this.storageId = nft.getStorageId();
		this.imgUrl = nft.getImgUrl();
		this.isSync = nft.getIsSync();
		this.metadataContent = nft.getMetadataContent();
		this.metadataUrl = nft.getMetadataUrl();
		this.name = nft.getName();
		this.propertys = nft.getPropertys();
		this.tokenId = nft.getTokenId();
		this.type = nft.getType();
		this.updateTime = nft.getUpdateTime();
		this.animUrl = nft.getAnimUrl();
		this.animStorageId = nft.getAnimStorageId();
		if(null != boxToNft) {
			this.amount = boxToNft.getAmount();
		}
	}
    public BlindNftDto(BlindNft nft) {
    	this.id = nft.getId();
    	this.address = nft.getAddress();
    	this.blindTypeId = nft.getBlindTypeId();
    	this.createTime = nft.getCreateTime();
    	this.deleted = nft.getDeleted();
    	this.description = nft.getDescription();
    	this.storageId = nft.getStorageId();
    	this.imgUrl = nft.getImgUrl();
    	this.isSync = nft.getIsSync();
    	this.metadataContent = nft.getMetadataContent();
    	this.metadataUrl = nft.getMetadataUrl();
    	this.name = nft.getName();
    	this.propertys = nft.getPropertys();
    	this.tokenId = nft.getTokenId();
    	this.type = nft.getType();
    	this.updateTime = nft.getUpdateTime();
    	this.animStorageId = nft.getAnimStorageId();
    	this.animUrl = nft.getAnimUrl();
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getBlindTypeId() {
		return blindTypeId;
	}

	public void setBlindTypeId(Long blindTypeId) {
		this.blindTypeId = blindTypeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPropertys() {
		return propertys;
	}

	public void setPropertys(String propertys) {
		this.propertys = propertys;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTokenId() {
		return tokenId;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Boolean getIsSync() {
		return isSync;
	}

	public void setIsSync(Boolean isSync) {
		this.isSync = isSync;
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

}
