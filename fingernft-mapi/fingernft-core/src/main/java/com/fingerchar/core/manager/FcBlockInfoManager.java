package com.fingerchar.core.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fingerchar.core.base.service.IBaseService;
import com.fingerchar.db.domain.FcBlockInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.protocol.core.methods.response.EthBlock;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author： Zjm
 * @Date：2022/3/23 16:18
 */
@Service
public class FcBlockInfoManager {
    @Autowired
    IBaseService baseService;

    public Integer saveBatch(List<EthBlock.Block> blockList){
        List<FcBlockInfo> infoList = new ArrayList<>();
        FcBlockInfo info = null;
        for(EthBlock.Block block: blockList){
            info = new FcBlockInfo();
            info.setBlockHash(block.getHash());
            info.setBlockNumber(Long.valueOf(block.getNumber().toString()));
            info.setTimestamp(Long.valueOf(block.getTimestamp().toString()));
            infoList.add(info);
        }

        return this.baseService.saveBatch(infoList);
    }

    public FcBlockInfo getLastBlock(){
        QueryWrapper<FcBlockInfo> wrapper = new QueryWrapper<>();
        wrapper.last("limit 1")
                .orderByDesc(FcBlockInfo.BLOCK_NUMBER);
        return this.baseService.getByCondition(FcBlockInfo.class, wrapper);
    }

}
