package com.fingerchar.admin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fingerchar.core.base.service.IBaseService;
import com.fingerchar.db.base.BaseEntity;
import com.fingerchar.db.domain.FcAdminLog;


@Service
public class FcLogService {
    /*@Resource
    private FcAdminLogMapper logMapper;*/

    @Autowired
    private IBaseService baseService;

    public void deleteById(Long id) {
        /*
        logMapper.logicalDeleteByPrimaryKey(id);*/
        baseService.deleteById(FcAdminLog.class,id);
    }

    public void add(FcAdminLog log) {
        log.setCreateTime(System.currentTimeMillis()/1000);
        log.setUpdateTime(System.currentTimeMillis()/1000);
        /*logMapper.insertSelective(log);*/
        baseService.save(log);

    }

    public IPage<FcAdminLog> querySelective(String name, IPage<FcAdminLog> page, boolean isASC, String sortType) {
        /*FcAdminLogExample example = new FcAdminLogExample();
        FcAdminLogExample.Criteria criteria = example.createCriteria();

        if (!StringUtils.isEmpty(name)) {
            criteria.andAdminLike("%" + name + "%");
        }
        criteria.andDeletedEqualTo(false);

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, size);
        return logMapper.selectByExample(example);*/
        QueryWrapper<FcAdminLog> wrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(name)) {
            wrapper.like(FcAdminLog.ADMIN,name );
        }
        wrapper.eq(BaseEntity.DELETED,false);
        if (!StringUtils.isEmpty(isASC) && !StringUtils.isEmpty(sortType)) {
            wrapper.orderBy(true,isASC , sortType);
        }
        return baseService.findByPage(FcAdminLog.class,wrapper,page);

    }
}
