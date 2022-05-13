package com.fingerchar.db.domain;

import com.fingerchar.db.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;

/**
 * @Description BlindNft
 * @Author 
 * @Date 2022-04-14
 * @Version 2.1
 */
@TableName("blind_nft")
public class BlindNft extends BaseEntity {


    /**
     * 合约地址
     */
    @TableField("`address`")
    private String address;

    /**
     * 种类id
     */
    @TableField("`blind_type_id`")
    private Long blindTypeId;

    /**
     * 名称
     */
    @TableField("`name`")
    private String name;

    /**
     * 属性
     */
    @TableField("`propertys`")
    private String propertys;

    /**
     * 图片路径
     */
    @TableField("`img_url`")
    private String imgUrl;

    /**
     * 图片在storage的id
     */
    @TableField("`storage_id`")
    private Long storageId;

    /**
     * 视频路径
     */
    @TableField("`anim_url`")
    private String animUrl;

    /**
     * 图片在storage的id
     */
    @TableField("`anim_storage_id`")
    private Long animStorageId;

    /**
     * 所属合约的id
     */
    @TableField("`token_id`")
    private String tokenId;

    /**
     * ETH("ETH", 0), ERC20("ERC20", 1), ERC1155("ERC1155", 2), ERC721("ERC721", 3), ERC721Deprecated("ERC721Deprecated", 4);
     */
    @TableField("`type`")
    private Integer type;

    /**
     * 描述
     */
    @TableField("`description`")
    private String description;

    /**
     * 是否上链
     */
    @TableField("`is_sync`")
    private Boolean isSync;

    /**
     * 资源地址
     */
    @TableField("`metadata_url`")
    private String metadataUrl;

    /**
     * 资源内容
     */
    @TableField("`metadata_content`")
    private String metadataContent;


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public static final String ADDRESS = "`address`";

    public static final String BLIND_TYPE_ID = "`blind_type_id`";

    public static final String NAME = "`name`";

    public static final String PROPERTYS = "`propertys`";

    public static final String IMG_URL = "`img_url`";

    public static final String STORAGE_ID = "`storage_id`";

    public static final String ANIM_URL = "`anim_url`";

    public static final String ANIM_STORAGE_ID = "`anim_storage_id`";

    public static final String TOKEN_ID = "`token_id`";

    public static final String TYPE = "`type`";

    public static final String DESCRIPTION = "`description`";

    public static final String IS_SYNC = "`is_sync`";

    public static final String METADATA_URL = "`metadata_url`";

    public static final String METADATA_CONTENT = "`metadata_content`";

    @Override
    public String toString() {
        return "BlindNft{" +
        "address=" + address +
        ", blindTypeId=" + blindTypeId +
        ", name=" + name +
        ", propertys=" + propertys +
        ", imgUrl=" + imgUrl +
        ", storageId=" + storageId +
        ", animUrl=" + animUrl +
        ", animStorageId=" + animStorageId +
        ", tokenId=" + tokenId +
        ", type=" + type +
        ", description=" + description +
        ", isSync=" + isSync +
        ", metadataUrl=" + metadataUrl +
        ", metadataContent=" + metadataContent +
        "}";
    }
}
