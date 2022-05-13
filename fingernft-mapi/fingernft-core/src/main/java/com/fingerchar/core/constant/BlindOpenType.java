package com.fingerchar.core.constant;

/**
 * @Author： Zjm
 * @Date：2022/3/29 10:27
 */
public enum BlindOpenType {
    OPEN("Open", 1), OPEN_INDEX("OpenIndex", 2);

    private String name;
    private Integer type;

    private BlindOpenType(String name, Integer type) {
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
