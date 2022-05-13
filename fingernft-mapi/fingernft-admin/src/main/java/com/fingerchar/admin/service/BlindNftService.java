package com.fingerchar.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fingerchar.core.base.service.impl.BaseService;
import com.fingerchar.core.manager.BlindBlindBoxToNftManager;
import com.fingerchar.core.manager.BlindNftManager;
import com.fingerchar.core.manager.StorageManager;
import com.fingerchar.core.util.ResponseUtil;
import com.fingerchar.db.base.BaseEntity;
import com.fingerchar.db.domain.BlindNft;
import com.fingerchar.db.domain.BlindType;
import com.fingerchar.db.dto.NftMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/*
 *
 * @author zjm
 * */
@Service
public class BlindNftService {

    @Autowired
    private BaseService baseService;

    @Autowired
    private BlindNftManager blindNftManager;

    @Autowired
    BlindTypeService blindTypeService;

    @Autowired
    StorageManager storageManager;

    @Autowired
    private BlindBlindBoxToNftManager blindBoxToNftManager;

    public IPage<BlindNft> page(String address, String name, IPage<BlindNft> page) {
        QueryWrapper<BlindNft> wrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(address)){
            wrapper.eq(BlindNft.ADDRESS, address);
        }
        if(!StringUtils.isEmpty(name)){
            wrapper.like(BlindNft.NAME, name);
        }
        wrapper.eq(BaseEntity.DELETED,false);
        wrapper.orderByDesc(BaseEntity.ID);
        IPage<BlindNft> iPage = baseService.findByPage(BlindNft.class, wrapper, page);
        return iPage;
    }

    @Transactional
    public Object add(BlindNft blindNft){
        BlindType blindType = this.blindTypeService.get(blindNft.getAddress());
        if(null == blindType){
            return ResponseUtil.fail(-1, "blind type is not existed");
        }
        blindNft.setType(5);

        NftMetadata metadata = new NftMetadata(blindNft);
        metadata = this.storageManager.uploadMetadata(metadata);
        blindNft.setMetadataContent(metadata.getMetadataContent());
        blindNft.setMetadataUrl(metadata.getMetadataUrl());

        this.blindNftManager.save(blindNft);
        return ResponseUtil.ok(blindNft);
    }

    @Transactional
    public int updateById(BlindNft blindNft) {
        NftMetadata metadata = new NftMetadata(blindNft);
        metadata = this.storageManager.uploadMetadata(metadata);
        blindNft.setMetadataUrl(metadata.getMetadataUrl());
        blindNft.setMetadataContent(metadata.getMetadataContent());
    	// this.uploadMetadata(blindNft);
    	blindNft.setUpdateTime(System.currentTimeMillis() / 1000);
        return baseService.update(blindNft);
    }

    @Transactional
    public Object deleteById(Long id) {
        Integer count = this.blindBoxToNftManager.activeNftCount(id);
        if(!count.equals(0)){
            return ResponseUtil.fail(-1, "Can not delete the nft as a blind box contained it");
        }
        this.baseService.deleteById(BlindNft.class, id);
    	return ResponseUtil.ok();
    }

    public BlindNft findById(Long id){
        return baseService.getById(BlindNft.class,id);
    }

    public List<BlindNft> allByAddress(String address){
        return this.blindNftManager.allByAddress(address);
    }

}
