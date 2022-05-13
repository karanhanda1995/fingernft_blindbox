package com.fingerchar.db.vo.notice;

import com.fingerchar.db.domain.BlindBlindBox;

/**
 * @Author： Zjm
 * @Date：2022/4/1 14:41
 */
public class BlindBoxVo {

    private Long id;
    private String address;
    private String name;
    private String price;
    private String imgUrl;
    private String paytokenAddress;
    private Integer paytokenType;
    private String paytokenName;
    private String paytokenSymbol;
    private String paytokenTokenId;
    private Integer paytokenDecimals;
    private String paytokenMetadataContent;

    public BlindBoxVo(BlindBlindBox blindBox){
        this.id = blindBox.getId();
        this.address = blindBox.getAddress();
        this.name = blindBox.getName();
        this.price = blindBox.getPrice();
        this.imgUrl = blindBox.getImgUrl();
        this.paytokenAddress = blindBox.getPaytokenAddress();
        this.paytokenType = blindBox.getPaytokenType();
        this.paytokenName = blindBox.getPaytokenName();
        this.paytokenSymbol = blindBox.getPaytokenSymbol();
        this.paytokenTokenId = blindBox.getPaytokenTokenId();
        this.paytokenDecimals = blindBox.getPaytokenDecimals();
        this.paytokenMetadataContent = blindBox.getPaytokenMetadataContent();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
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
