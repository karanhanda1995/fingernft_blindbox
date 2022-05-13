package com.fingerchar.db.vo;

import com.fingerchar.db.domain.FcUser;

import java.math.BigDecimal;

public class NftOwnerVo {

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 钱包账号地址
     */
    private String address;

    public NftOwnerVo(FcUser user){
        this.nickname = user.getNickname();
        this.avatar = user.getAvatar();
        this.address = user.getAddress();
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
