package com.fingerchar.db.vo.notice;

import com.fingerchar.db.domain.FcContract;
import com.fingerchar.db.dto.ContractCreateLog;

/**
 * @Author： Zjm
 * @Date：2022/4/4 17:21
 */
public class ContractVo {
    private String cover;
    private String name;
    private String symbol;
    private Integer type;

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    private String address;

    public ContractVo(){
    }

    public ContractVo(FcContract contract){
        this.cover = contract.getCover();
        this.name = contract.getName();
        this.symbol = contract.getSymbol();
        this.type = contract.getType();
        this.address = contract.getAddress();
    }

    public ContractVo(ContractCreateLog log){
        this.type = log.getType();
        this.name = log.getName();
        this.symbol = log.getSymbol();
        this.address = log.getAddress();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
