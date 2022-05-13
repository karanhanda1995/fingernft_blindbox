package com.fingerchar.core.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.fingerchar.core.base.service.IBaseService;
import com.fingerchar.db.domain.BlindBlindBox;
import com.fingerchar.db.domain.BlindBlindBoxToNft;
import com.fingerchar.db.domain.BlindNft;
import com.fingerchar.db.vo.BlindBlindBoxToNftVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Date：2022/3/26 21:18
 */
@Service
public class BlindBlindBoxToNftManager {
    @Autowired
    IBaseService baseService;

    @Autowired
    BlindNftManager blindNftManager;

    public List<BlindBlindBoxToNftVo> listNftInfo(Long blindBoxId){
        List<BlindBlindBoxToNft> blindBoxToNftList = this.list(blindBoxId);
        if(blindBoxToNftList.isEmpty()){
            return new ArrayList<>();
        }
        List<Long> idList = blindBoxToNftList.stream().map(BlindBlindBoxToNft::getBlindNftId).collect(Collectors.toList());
        List<BlindNft> blindNftList = this.blindNftManager.listByMulti(idList);
        Map<Long, BlindBlindBoxToNft> map = blindBoxToNftList.stream().collect(
                Collectors.toMap(BlindBlindBoxToNft::getBlindNftId, Function.identity())
        );
        List<BlindBlindBoxToNftVo> blindBoxToNftVoList = blindNftList.stream()
                .map(vo-> new BlindBlindBoxToNftVo(map.get(vo.getId()), vo))
                .collect(Collectors.toList());

        return blindBoxToNftVoList;
    }

    public Integer activeNftCount(Long nftId){
        QueryWrapper<BlindBlindBoxToNft> wrapper = new QueryWrapper<>();
        wrapper.eq(BlindBlindBoxToNft.BLIND_NFT_ID, nftId)
                .eq(BlindBlindBoxToNft.DELETED, false);
        return this.baseService.counts(BlindBlindBoxToNft.class, wrapper);
    }

    public List<BlindBlindBoxToNft> listbymulti(List<Long> boxList){
        QueryWrapper<BlindBlindBoxToNft> wrapper = new QueryWrapper<>();
        wrapper.in(BlindBlindBoxToNft.BLIND_BOX_ID, boxList)
                .eq(BlindBlindBoxToNft.DELETED, false);
        return this.baseService.findByCondition(BlindBlindBoxToNft.class, wrapper);
    }

    public List<BlindBlindBoxToNft> list(Long blindBoxId){
        QueryWrapper<BlindBlindBoxToNft> wrapper = new QueryWrapper<>();
        wrapper.eq(BlindBlindBoxToNft.BLIND_BOX_ID, blindBoxId)
                .eq(BlindBlindBoxToNft.DELETED, false);
        return this.baseService.findByCondition(BlindBlindBoxToNft.class, wrapper);
    }

    public Integer addBatch(List<BlindBlindBoxToNft> blindBoxToNftList){
        return this.baseService.saveBatch(blindBoxToNftList);
    }

    public Integer delete(BlindBlindBox blindBox){
        UpdateWrapper<BlindBlindBoxToNft> wrapper = new UpdateWrapper<>();
        wrapper.eq(BlindBlindBoxToNft.BLIND_BOX_ID, blindBox.getId())
                .eq(BlindBlindBoxToNft.DELETED, false);
        wrapper.set(BlindBlindBoxToNft.DELETED, true);
        return this.baseService.updateByCondition(BlindBlindBoxToNft.class, wrapper);
    }

    public Integer update(BlindBlindBoxToNft blindBoxToNft){
        return this.baseService.update(blindBoxToNft);
    }

    public Integer save(BlindBlindBoxToNft blindBoxToNft){
        return this.baseService.save(blindBoxToNft);
    }
}
