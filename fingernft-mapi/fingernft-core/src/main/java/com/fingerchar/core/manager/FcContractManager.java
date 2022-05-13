package com.fingerchar.core.manager;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fingerchar.core.base.service.IBaseService;
import com.fingerchar.core.constant.CommonStatus;
import com.fingerchar.core.constant.ContractType;
import com.fingerchar.core.constant.NoticeType;
import com.fingerchar.db.domain.FcContract;
import com.fingerchar.db.dto.ContractCreateLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

/**
 * @Author： Zjm
 * @Date：2022/3/21 20:25
 */
@Service
public class FcContractManager {
    @Autowired
    IBaseService baseService;

    @Autowired
    FcNoticeManager noticeManager;

    public FcContract setAdminContract(String name, String symbol, String address, Integer type){
        if(!type.equals(ContractType.ERC721.getType()) &&
                !type.equals(ContractType.ERC1155.getType())
        ){
            return null;
        }
        FcContract contract = this.getAdminContract(type);
        if(null != contract){
            return contract;
        }
        contract = new FcContract();
        contract.setName(name);
        contract.setSymbol(symbol);
        contract.setAddress(address.toLowerCase(Locale.ROOT));
        contract.setIsSync(true);
        contract.setType(type);
        contract.setIsAdmin(true);
        contract.setVerify(true);
        this.save(contract);
        return contract;
    }

    public FcContract getAdminContract(Integer type){
        QueryWrapper<FcContract> wrapper = new QueryWrapper<>();
        wrapper.eq(FcContract.IS_SYNC, true)
                .eq(FcContract.TYPE, type)
                .eq(FcContract.IS_ADMIN, true)
                .eq(FcContract.DELETED, false);
        return this.baseService.getByCondition(FcContract.class, wrapper);
    }

    public FcContract get(String address){
        QueryWrapper<FcContract> wrapper = new QueryWrapper<>();
        wrapper.eq(FcContract.ADDRESS, address)
                .eq(FcContract.DELETED, false);
        return this.baseService.getByCondition(FcContract.class, wrapper);
    }
    public List<FcContract> getUnSyncContract(
            String name,
            String symbol,
            Integer type,
            String owner,
            String signer
            ){
        QueryWrapper<FcContract> wrapper = new QueryWrapper<>();
        wrapper.eq(FcContract.TYPE, type)
                .eq(FcContract.SYMBOL, symbol)
                .eq(FcContract.NAME, name)
                .eq(FcContract.OWNER, owner)
                .eq(FcContract.IS_SYNC, false)
                .eq(FcContract.ADDRESS, "")
                .eq(FcContract.SIGNER, signer)
                .eq(FcContract.DELETED, false);
        return this.baseService.findByCondition(FcContract.class, wrapper);
    }

    public Integer addSyncContract(
            ContractCreateLog log
    ){
        String address = log.getAddress();
        String name = log.getName();
        String symbol = log.getSymbol();
        Integer type = log.getType();
        String owner = log.getCreator();
        String signer = log.getSigner();

        FcContract contract = this.get(address);
        if(null != contract && contract.getIsSync()){
            // contract is existed
            return 0;
        }
        String content = JSON.toJSONString(log);
        Integer _type = CommonStatus.CREATE_CONTRACT.getType();
        Integer noticeType = NoticeType.TRADE.getType();
        this.noticeManager.add(content, owner, _type, noticeType, owner);

        List<FcContract> contracts = this.getUnSyncContract(name, symbol, type, owner, signer);
        if(contracts.isEmpty()){
            // contract is unexisted
            contract = new FcContract();
            contract.setAddress(address);
            contract.setName(name);
            contract.setSymbol(symbol);
            contract.setType(type);
            contract.setOwner(owner);
            contract.setSigner(signer);
            contract.setIsSync(true);
            return this.save(contract);
        }
        // contract is existed,but not sync
        contract = contracts.get(0);
        contract.setAddress(address);
        contract.setIsSync(true);
        return this.update(contract);
    }



    public Integer add(String address, Integer type){
        FcContract contract = new FcContract();
        contract.setAddress(address);
        contract.setType(type);
        contract.setIsSync(true);
        return this.save(contract);
    }

    public Integer update(FcContract contract){
        return this.baseService.update(contract);
    }

    public Integer save(FcContract contract){
        return this.baseService.save(contract);
    }
}
