package com.fingerchar.db.domain;

import com.fingerchar.db.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;

/**
 * @Description FcAuctionBids
 * @Author 
 * @Date 2022-04-14
 * @Version 2.1
 */
@TableName("fc_auction_bids")
public class FcAuctionBids extends BaseEntity {


    /**
     * 拍卖表ID
     */
    @TableField("`order_id`")
    private Long orderId;

    /**
     * 竞拍者
     */
    @TableField("`owner`")
    private String owner;

    /**
     * 购买token价格
     */
    @TableField("`buying`")
    private String buying;

    /**
     * 盐
     */
    @TableField("`salt`")
    private String salt;

    /**
     * 签名
     */
    @TableField("`signature`")
    private String signature;

    /**
     * 状态0 无操作， 1取消， 2上链
     */
    @TableField("`status`")
    private Integer status;

    /**
     * usdt价格
     */
    @TableField("`usdt_price`")
    private String usdtPrice;

    /**
     * 分红
     */
    @TableField("`bonus`")
    private String bonus;


    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getBuying() {
        return buying;
    }

    public void setBuying(String buying) {
        this.buying = buying;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getUsdtPrice() {
        return usdtPrice;
    }

    public void setUsdtPrice(String usdtPrice) {
        this.usdtPrice = usdtPrice;
    }

    public String getBonus() {
        return bonus;
    }

    public void setBonus(String bonus) {
        this.bonus = bonus;
    }

    public static final String ORDER_ID = "`order_id`";

    public static final String OWNER = "`owner`";

    public static final String BUYING = "`buying`";

    public static final String SALT = "`salt`";

    public static final String SIGNATURE = "`signature`";

    public static final String STATUS = "`status`";

    public static final String USDT_PRICE = "`usdt_price`";

    public static final String BONUS = "`bonus`";

    @Override
    public String toString() {
        return "FcAuctionBids{" +
        "orderId=" + orderId +
        ", owner=" + owner +
        ", buying=" + buying +
        ", salt=" + salt +
        ", signature=" + signature +
        ", status=" + status +
        ", usdtPrice=" + usdtPrice +
        ", bonus=" + bonus +
        "}";
    }
}
