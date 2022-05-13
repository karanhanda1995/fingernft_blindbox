package com.fingerchar.db.dto;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fingerchar.db.domain.BlindBlindBoxOrder;
import com.fingerchar.db.vo.BlindBoxAssets;

import java.util.ArrayList;
import java.util.List;

public class BlindBoxOrder {

    private String owner;

    private String salt;

    private List<BlindBoxAssets> sellAssets;

    private String buyerToken;

    private String buyerTokenId;

    private Integer buyerType;

    private Long opening;

    private Boolean repeat;

    private Long startTime;

    private Long endTime;

    private String buying;

    private List<Long> sellings;

    private String sellerFee;

    private String signature;

    private String r;

    private String s;

    private String v;

    private String buyerFee;

    private List<String> uris;

    public BlindBoxOrder(BlindBlindBoxOrder order) {
        this.owner = order.getOwner();
        this.salt = order.getSalt();
        JSONArray arr = JSON.parseArray(order.getSellAssets());
        Integer len = arr.size();
        this.sellAssets = new ArrayList<>();
        BlindBoxAssets assets = null;
        JSONObject obj = null;
        for(int i=0; i<len; i++) {
            obj = arr.getJSONObject(i);
            assets = new BlindBoxAssets(obj.getString("token"), obj.getBigInteger("tokenId"), obj.getBigInteger("assetType"));
            this.sellAssets.add(assets);
        }
        this.buyerToken = order.getBuyerToken();
        this.buyerTokenId = order.getBuyerTokenId();
        this.buyerType = order.getBuyerType();
        this.opening = order.getOpening();
        this.repeat = order.getRepeat();
        this.startTime = order.getStartTime();
        this.endTime = order.getEndTime();
        this.buying = order.getBuying();
        this.sellings = JSON.parseArray(order.getSellings(), Long.class);
        this.uris = JSON.parseArray(order.getUris(), String.class);
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public List<BlindBoxAssets> getSellAssets() {
        return sellAssets;
    }

    public void setSellAssets(List<BlindBoxAssets> sellAssets) {
        this.sellAssets = sellAssets;
    }

    public String getBuyerToken() {
        return buyerToken;
    }

    public void setBuyerToken(String buyerToken) {
        this.buyerToken = buyerToken;
    }

    public String getBuyerTokenId() {
        return buyerTokenId;
    }

    public void setBuyerTokenId(String buyerTokenId) {
        this.buyerTokenId = buyerTokenId;
    }

    public Integer getBuyerType() {
        return buyerType;
    }

    public void setBuyerType(Integer buyerType) {
        this.buyerType = buyerType;
    }

    public Long getOpening() {
        return opening;
    }

    public void setOpening(Long opening) {
        this.opening = opening;
    }

    public Boolean getRepeat() {
        return repeat;
    }

    public void setRepeat(Boolean repeat) {
        this.repeat = repeat;
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

    public String getBuying() {
        return buying;
    }

    public void setBuying(String buying) {
        this.buying = buying;
    }

    public List<Long> getSellings() {
        return sellings;
    }

    public void setSellings(List<Long> sellings) {
        this.sellings = sellings;
    }

    public String getSellerFee() {
        return sellerFee;
    }

    public void setSellerFee(String sellerFee) {
        this.sellerFee = sellerFee;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getR() {
        return r;
    }

    public void setR(String r) {
        this.r = r;
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    public String getV() {
        return v;
    }

    public void setV(String v) {
        this.v = v;
    }

    public String getBuyerFee() {
        return buyerFee;
    }

    public void setBuyerFee(String buyerFee) {
        this.buyerFee = buyerFee;
    }

    public List<String> getUris() {
        return uris;
    }

    public void setUris(List<String> uris) {
        this.uris = uris;
    }
}
