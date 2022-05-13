package com.fingerchar.core.constant;


/**
 * @Author： Zjm
 * @Date：2021/9/28 10:36
 */
public enum AuctionStatus {

    UNTREATED(0, "Untreated"), CANCEL(1, "Cancel"), FINISHED(2, "Finished"), ABORTED(3, "Aborted");

    private Integer code;

    private String name;

    private AuctionStatus(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public static Integer getCode(String name) {
        for(AuctionStatus t : AuctionStatus.values()) {
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
