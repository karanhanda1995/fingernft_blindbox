package com.fingerchar.core.constant;

/**
 * @Author： Zjm
 * @Date：2022/4/2 9:46
 */
public enum AuctionOrderType {
    AUCTION("Auction", 1), AUCTION_BID("AuctionBid", 2);

    private String name;
    private Integer type;

    private AuctionOrderType(String name, Integer type) {
        this.name = name;
        this.type = type;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
