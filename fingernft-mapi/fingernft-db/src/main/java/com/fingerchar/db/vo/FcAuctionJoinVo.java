package com.fingerchar.db.vo;

/**
 * @Author： Zjm
 * @Date：2021/10/15 10:07
 */
public class FcAuctionJoinVo {

    private Long orderId;

    private Integer amount;

    private Long joinId;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Long getJoinId() {
        return joinId;
    }

    public void setJoinId(Long joinId) {
        this.joinId = joinId;
    }
}
