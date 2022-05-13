package com.fingerchar.core.constant;


/**
 * @Author： Zjm
 * @Date：2021/10/8 11:25
 */
public enum AuctionBidsStatus {
    UNTREATED(0, "Untreated"), CANCEL(1, "Cancel"), SYNC(2, "Sync");

    private Integer code;

    private String name;

    private AuctionBidsStatus(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public static Integer getCode(String name) {
        for(AuctionBidsStatus t : AuctionBidsStatus.values()) {
            if(t.getName().equals(name)) {
                return t.getCode();
            }
        }
        return null;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
