package com.fingerchar.db.vo;

/**
 * @Author： Zjm
 * @Date：2022/3/5 11:08
 */
public class AuctionParamVO {
    private String address;

    private String tokenId;


    private Boolean status;

    public AuctionParamVO(String address, String tokenId, Boolean status) {
        this.address = address;
        this.tokenId = tokenId;
        this.status = status;
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

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

}
