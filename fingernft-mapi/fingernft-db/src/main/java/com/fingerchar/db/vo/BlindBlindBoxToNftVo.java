package com.fingerchar.db.vo;

import com.fingerchar.db.domain.BlindBlindBoxToNft;
import com.fingerchar.db.domain.BlindNft;

/**
 * @Author： Zjm
 * @Date：2022/3/26 21:20
 */
public class BlindBlindBoxToNftVo {

    private Long id;
    /**
     * 盲盒id
     */
    private Long blindBoxId;

    /**
     * 奖品id
     */
    private Long blindNftId;


    /**
     * 卡片数量
     */
    private Integer amount;

    private BlindNft blindNft;

    public BlindBlindBoxToNftVo() {
    }

    public BlindBlindBoxToNftVo(BlindBlindBoxToNft blindBoxToNft, BlindNft blindNft) {
        this.id = blindBoxToNft.getId();
        this.blindBoxId = blindBoxToNft.getBlindBoxId();
        this.blindNftId = blindBoxToNft.getBlindNftId();
        this.blindNft = blindNft;
        this.amount = blindBoxToNft.getAmount();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public BlindNft getBlindNft() {
        return blindNft;
    }

    public void setBlindNft(BlindNft blindNft) {
        this.blindNft = blindNft;
    }

}
