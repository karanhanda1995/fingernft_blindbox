package com.fingerchar.db.dto;

import com.fingerchar.db.domain.BlindBlindBoxToNft;

/**
 * @Author： Zjm
 * @Date：2022/3/27 20:12
 */
public class BlindSellAssets {
    private String token;
    private String tokenId;
    private Integer assetType;

    public BlindSellAssets(BlindBlindBoxToNft boxToNft){
        this.token = boxToNft.getAddress();
        this.tokenId = boxToNft.getTokenId();
        this.assetType = boxToNft.getType();
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public Integer getAssetType() {
        return assetType;
    }

    public void setAssetType(Integer assetType) {
        this.assetType = assetType;
    }


}
