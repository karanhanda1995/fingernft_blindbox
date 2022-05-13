package com.fingerchar.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fingerchar.admin.vo.BlindTypeParamVo;
import com.fingerchar.core.base.service.IBaseService;
import com.fingerchar.core.util.ResponseUtil;
import com.fingerchar.db.base.BaseEntity;
import com.fingerchar.db.domain.BlindType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class BlindTypeService {

	@Autowired
    private IBaseService baseService;

    public IPage<BlindType> page(BlindTypeParamVo typeParamVo, IPage<BlindType> page) {
        QueryWrapper<BlindType> wrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(typeParamVo.getName())){
            wrapper.like(BlindType.NAME, typeParamVo.getName());
        }
        wrapper.eq(BaseEntity.DELETED,false);
        wrapper.orderByDesc(BaseEntity.ID);
        return baseService.findByPage(BlindType.class,wrapper, page);
    }

    public Object add(BlindType blindType){
        BlindType _blindType = this.get(blindType.getAddress());
        if(null != _blindType){
            return ResponseUtil.fail(-1, "Blind type address is existed");
        }
        blindType.setCreateTime(System.currentTimeMillis() / 1000);
        blindType.setUpdateTime(System.currentTimeMillis() / 1000);
        if(baseService.save(blindType).equals(0)){
            return ResponseUtil.fail(-1, "add blind type fail");
        }
        return ResponseUtil.ok(blindType);
    }

    public Object edit(BlindType blindType){
        BlindType _blindType = this.get(blindType.getAddress());
        if(null != _blindType && !_blindType.getId().equals(blindType.getId())){
            return ResponseUtil.fail(-1, "Blind type address is existed");
        }
        blindType.setUpdateTime(System.currentTimeMillis()/1000);
        if(this.update(blindType).equals(0)){
            return ResponseUtil.fail(-1, "update blind type fail");
        }
        return ResponseUtil.ok(blindType);
    }

    public BlindType get(String address){
        QueryWrapper<BlindType> wrapper = new QueryWrapper<>();
        wrapper.eq(BlindType.ADDRESS, address)
                .eq(BlindType.DELETED, false);
        return this.baseService.getByCondition(BlindType.class, wrapper);
    }


    public Integer update(BlindType blindType) {
        return baseService.update(blindType);
    }

    public void delete(String address) {
        BlindType blindType = this.get(address);
        blindType.setDeleted(true);
        baseService.update(blindType);
    }

    public BlindType findById(Long id){
        QueryWrapper<BlindType> wrapper = new QueryWrapper<>();
        wrapper.eq(BaseEntity.DELETED,false);
        wrapper.eq(BaseEntity.ID,id);
        return baseService.getById(BlindType.class,id);
    }

    public List<BlindType> getAll() {
        QueryWrapper<BlindType> wrapper = new QueryWrapper<>();
        wrapper.eq(BlindType.DELETED, false);
        return this.baseService.findByCondition(BlindType.class, wrapper);
    }

}
