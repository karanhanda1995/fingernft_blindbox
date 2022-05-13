package com.fingerchar.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fingerchar.core.base.service.IBaseService;
import com.fingerchar.db.domain.FcBlockInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.protocol.core.methods.response.EthBlock;

import java.math.BigInteger;
import java.util.*;

@Service
public class FcBlockInfoService {
    @Autowired
    IBaseService baseService;

    public void saveBlock(Map<BigInteger, EthBlock.Block> blockMap) {
        Set keys = blockMap.keySet();
        BigInteger key = BigInteger.ZERO;
        Long value = 0l;
        String hash = null;
        FcBlockInfo fcBlockInfo = new FcBlockInfo();
        List<FcBlockInfo> list = new ArrayList<>();
        if (null != keys) {
            Iterator iterator = keys.iterator();
            while (iterator.hasNext()) {
                fcBlockInfo = new FcBlockInfo();
                key = (BigInteger) iterator.next();
                value = blockMap.get(key).getTimestamp().longValue();
                hash = blockMap.get(key).getHash();
                fcBlockInfo.setBlockNumber(key.longValue());
                fcBlockInfo.setTimestamp(value);
                fcBlockInfo.setBlockHash(hash);
                list.add(fcBlockInfo);
            }
        }
        if (list.size() > 0){
            this.baseService.saveBatch(list);
        }
    }

    public Long getLastBlockTimestamp() {
        QueryWrapper<FcBlockInfo> wrapper = new QueryWrapper<>();
        wrapper.eq(FcBlockInfo.DELETED, false)
                .orderByDesc(FcBlockInfo.TIMESTAMP)
                .last("limit 1");
        FcBlockInfo fcBlockInfo = this.baseService.getByCondition(FcBlockInfo.class, wrapper);
        if (null == fcBlockInfo){
            return null;
        }
        return fcBlockInfo.getTimestamp();
    }

    public Long getLastBlockNumber() {
        QueryWrapper<FcBlockInfo> wrapper = new QueryWrapper<>();
        wrapper.eq(FcBlockInfo.DELETED, false)
                .orderByDesc(FcBlockInfo.BLOCK_NUMBER)
                .last("limit 1");
        FcBlockInfo fcBlockInfo = this.baseService.getByCondition(FcBlockInfo.class, wrapper);
        if (null == fcBlockInfo) {
            return null;
        }
        return fcBlockInfo.getBlockNumber();
    }
}