package com.fingerchar.core.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fingerchar.core.base.service.IBaseService;
import com.fingerchar.db.base.BaseEntity;
import com.fingerchar.db.domain.BlindNft;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author： Zjm
 * @Date：2022/3/26 17:12
 */
@Service
public class BlindNftManager {
    @Autowired
    IBaseService baseService;

    public List<BlindNft> allByAddress(String address){
        QueryWrapper<BlindNft> wrapper = new QueryWrapper<>();
        wrapper.eq(BlindNft.ADDRESS, address)
                .eq(BaseEntity.DELETED, false)
                .orderByDesc(BaseEntity.ID);
        return this.baseService.findByCondition(BlindNft.class, wrapper);
    }

    public List<BlindNft> listByMulti(List<Long> idList){
        QueryWrapper<BlindNft> wrapper = new QueryWrapper<>();
        wrapper.in(BlindNft.ID, idList)
                .eq(BlindNft.DELETED, false);
        return this.baseService.findByCondition(BlindNft.class, wrapper);
    }

    public Integer update(BlindNft blindNft){
        return this.baseService.update(blindNft);
    }

    public BlindNft get(Long id){
        return this.baseService.getById(BlindNft.class, id);
    }

    public Integer save(BlindNft blindNft){
        return this.baseService.save(blindNft);
    }

}
