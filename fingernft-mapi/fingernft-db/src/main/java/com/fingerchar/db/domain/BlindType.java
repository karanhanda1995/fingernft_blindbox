package com.fingerchar.db.domain;

import com.fingerchar.db.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;

/**
 * @Description BlindType
 * @Author 
 * @Date 2022-04-14
 * @Version 2.1
 */
@TableName("blind_type")
public class BlindType extends BaseEntity {


    /**
     * 供应商名称
     */
    @TableField("`name`")
    private String name;

    /**
     * 721合约地址
     */
    @TableField("`address`")
    private String address;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public static final String NAME = "`name`";

    public static final String ADDRESS = "`address`";

    @Override
    public String toString() {
        return "BlindType{" +
        "name=" + name +
        ", address=" + address +
        "}";
    }
}
