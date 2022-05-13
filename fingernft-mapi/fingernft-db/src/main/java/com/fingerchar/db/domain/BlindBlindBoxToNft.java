package com.fingerchar.db.domain;

import com.fingerchar.db.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;

/**
 * @Description BlindBlindBoxToNft
 * @Author 
 * @Date 2022-04-14
 * @Version 2.1
 */
@TableName("blind_blind_box_to_nft")
public class BlindBlindBoxToNft extends BaseEntity {


    @TableField("`address`")
    private String address;

    /**
     * 盲盒id
     */
    @TableField("`blind_box_id`")
    private Long blindBoxId;

    /**
     * 奖品id
     */
    @TableField("`blind_nft_id`")
    private Long blindNftId;

    @TableField("`name`")
    private String name;

    @TableField("`propertys`")
    private String propertys;

    @TableField("`img_url`")
    private String imgUrl;

    @TableField("`storage_id`")
    private Long storageId;

    @TableField("`anim_url`")
    private String animUrl;

    @TableField("`anim_storage_id`")
    private Long animStorageId;

    @TableField("`token_id`")
    private String tokenId;

    @TableField("`type`")
    private Integer type;

    @TableField("`description`")
    private String description;

    @TableField("`metadata_url`")
    private String metadataUrl;

    @TableField("`metadata_content`")
    private String metadataContent;

    /**
     * 卡片数量
     */
    @TableField("`amount`")
    private Integer amount;


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getBlindBoxId() {
        return blindBoxId;
    }

    public void setBlindBoxId(Long blindBoxId) {
        this.blindBoxId = blindBoxId;
    }

    public Long getBlindNftId() {
        return blindNftId;
    }

    public void setBlindNftId(Long blindNftId) {
        this.blindNftId = blindNftId;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public static final String ADDRESS = "`address`";

    public static final String BLIND_BOX_ID = "`blind_box_id`";

    public static final String BLIND_NFT_ID = "`blind_nft_id`";

    public static final String NAME = "`name`";

    public static final String PROPERTYS = "`propertys`";

    public static final String IMG_URL = "`img_url`";

    public static final String STORAGE_ID = "`storage_id`";

    public static final String ANIM_URL = "`anim_url`";

    public static final String ANIM_STORAGE_ID = "`anim_storage_id`";

    public static final String TOKEN_ID = "`token_id`";

    public static final String TYPE = "`type`";

    public static final String DESCRIPTION = "`description`";

    public static final String METADATA_URL = "`metadata_url`";

    public static final String METADATA_CONTENT = "`metadata_content`";

    public static final String AMOUNT = "`amount`";

    @Override
    public String toString() {
        return "BlindBlindBoxToNft{" +
        "address=" + address +
        ", blindBoxId=" + blindBoxId +
        ", blindNftId=" + blindNftId +
        ", name=" + name +
        ", propertys=" + propertys +
        ", imgUrl=" + imgUrl +
        ", storageId=" + storageId +
        ", animUrl=" + animUrl +
        ", animStorageId=" + animStorageId +
        ", tokenId=" + tokenId +
        ", type=" + type +
        ", description=" + description +
        ", metadataUrl=" + metadataUrl +
        ", metadataContent=" + metadataContent +
        ", amount=" + amount +
        "}";
    }
}
