package com.fingerchar.core.constant;

/**
 * @Author： Zjm
 * @Date：2022/3/25 15:03
 */
public enum NftSellType {
    SALE("Sale", 1), AUCTION("Auction", 2);

    private Integer type;

    private String name;

    private NftSellType(String name, Integer type) {
        this.name = name;
        this.type = type;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public static NftSellType get(String name) {
        for(NftSellType t : NftSellType.values()) {
            if(t.getName().equals(name)) {
                return t;
            }
        }
        return null;
    }

    public static Integer getSellType(String name) {
        for(NftSellType t : NftSellType.values()) {
            if(t.getName().equals(name)) {
                return t.getType();
            }
        }
        return null;
    }


}
