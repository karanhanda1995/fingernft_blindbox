package com.fingerchar.core.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fingerchar.core.base.service.IBaseService;
import com.fingerchar.db.domain.BlindBlindBox;
import com.fingerchar.db.domain.BlindBlindBoxOrder;
import com.fingerchar.db.domain.BlindBlindBoxToNft;
import com.fingerchar.db.vo.BlindBlindBoxVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author： Zjm
 * @Date：2022/3/26 17:42
 */
@Service
public class BlindBlindBoxManager {
    @Autowired
    IBaseService baseService;

    @Autowired
    BlindBlindBoxOrderManager blindBlindBoxOrderManager;

    @Autowired
    BlindBlindBoxToNftManager blindBoxToNftManager;


    public List<BlindBlindBox> listByMulti(List<Long> idList){
        QueryWrapper<BlindBlindBox> wrapper = new QueryWrapper<>();
        wrapper.in(BlindBlindBox.ID, idList)
                .eq(BlindBlindBox.DELETED, false);
        return this.baseService.findByCondition(BlindBlindBox.class, wrapper);
    }

    public List<BlindBlindBoxVo> getBoxInfoList(List<BlindBlindBox> boxList){
        List<BlindBlindBoxOrder> orderList = this.blindBlindBoxOrderManager.listByMulti(boxList);
        Map<Long, BlindBlindBoxOrder> map = orderList.stream().collect(Collectors.toMap(BlindBlindBoxOrder::getBlindBoxId, Function.identity()));
        List<BlindBlindBoxVo> boxVoList = boxList.stream().map(vo -> new BlindBlindBoxVo(vo, map.get(vo.getId()))).collect(Collectors.toList());
        return boxVoList;
    }

    public BlindBlindBoxVo getBoxInfo(BlindBlindBox box){
        BlindBlindBoxOrder order = this.blindBlindBoxOrderManager.get(box.getId());
        return new BlindBlindBoxVo(box, order);
    }

    public void clear(BlindBlindBox box){
        this.blindBlindBoxOrderManager.delete(box);
        this.blindBoxToNftManager.delete(box);
    }

    public Integer sign(BlindBlindBoxOrder order){
        BlindBlindBox box = this.get(order.getBlindBoxId());
        box.setIsSync(true);
        this.blindBlindBoxOrderManager.sign(order);
        return this.update(box);
    }

    public BlindBlindBox get(Long id){
        QueryWrapper<BlindBlindBox> wrapper = new QueryWrapper<>();
        wrapper.eq(BlindBlindBox.ID, id)
                .eq(BlindBlindBox.DELETED, false);
        return this.baseService.getByCondition(BlindBlindBox.class, wrapper);
    }

    public List<BlindBlindBoxToNft> nfts(Long id){
        return this.blindBoxToNftManager.list(id);
    }

    public List<BlindBlindBoxToNft> nftsbymulti(List<Long> boxList){
        return this.blindBoxToNftManager.listbymulti(boxList);
    }

    public Integer delete(BlindBlindBox box){
        box.setDeleted(true);
        this.clear(box);
        return this.update(box);
    }

    public Integer update(BlindBlindBox blindBox){
        return this.baseService.update(blindBox);
    }

    public Integer save(BlindBlindBox blindBox){
        return this.baseService.save(blindBox);
    }
}
