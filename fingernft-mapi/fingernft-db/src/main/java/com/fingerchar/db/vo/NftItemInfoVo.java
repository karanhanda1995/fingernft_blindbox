package com.fingerchar.db.vo;

import com.fingerchar.db.domain.FcNftItems;
import com.fingerchar.db.domain.FcUser;

/**
 * @Author： Zjm
 * @Date：2022/3/25 14:32
 */
public class NftItemInfoVo {

    private String address;

    private String tokenId;


    private String price;

    private String paytokenAddress;

    private String paytokenName;

    private Integer paytokenDecimals;

    private String paytokenSymbol;

    private String sellQuantity;

    private String quantity;

    private String itemOwner;

    private Long categoryId;

    private Boolean onsell;

    private Integer onsellType;

    private Long onsellTime;

    private UserBaseInfoVo user;

    public UserBaseInfoVo getUser() {
        return user;
    }

    public void setUser(UserBaseInfoVo user) {
        this.user = user;
    }

    public NftItemInfoVo(FcNftItems nftItems, FcUser user){
        this.address = nftItems.getAddress();
        this.tokenId = nftItems.getTokenId();
        this.price = nftItems.getPrice();
        this.paytokenAddress = nftItems.getPaytokenAddress();
        this.paytokenName = nftItems.getPaytokenName();
        this.paytokenDecimals = nftItems.getPaytokenDecimals();
        this.paytokenSymbol = nftItems.getPaytokenSymbol();
        this.sellQuantity = nftItems.getSellQuantity();
        this.quantity = nftItems.getQuantity();
        this.itemOwner = nftItems.getItemOwner();
        this.onsell = nftItems.getOnsell();
        this.categoryId = nftItems.getCategoryId();
        this.onsellType = nftItems.getOnsellType();
        this.onsellTime = nftItems.getOnsellTime();
        this.user = new UserBaseInfoVo(user);
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
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

    public String getSellQuantity() {
        return sellQuantity;
    }

    public void setSellQuantity(String sellQuantity) {
        this.sellQuantity = sellQuantity;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getItemOwner() {
        return itemOwner;
    }

    public void setItemOwner(String itemOwner) {
        this.itemOwner = itemOwner;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Boolean getOnsell() {
        return onsell;
    }

    public void setOnsell(Boolean onsell) {
        this.onsell = onsell;
    }

    public Integer getOnsellType() {
        return onsellType;
    }

    public void setOnsellType(Integer onsellType) {
        this.onsellType = onsellType;
    }


    public Long getOnsellTime() {
        return onsellTime;
    }

    public void setOnsellTime(Long onsellTime) {
        this.onsellTime = onsellTime;
    }
}
