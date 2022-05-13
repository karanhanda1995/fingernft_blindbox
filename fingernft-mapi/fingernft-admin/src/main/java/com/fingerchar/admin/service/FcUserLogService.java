package com.fingerchar.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fingerchar.core.base.service.IBaseService;
import com.fingerchar.db.domain.FcUserLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/*
 *
 * @author zjm
 * */
@Service
public class FcUserLogService {

    @Autowired
    IBaseService baseService ;

    public Integer countByCondition(Long time) {
        QueryWrapper<FcUserLog> wrapper = new QueryWrapper<>();
        wrapper.eq(FcUserLog.DELETED, false);
        if (null != time){
            wrapper.gt(FcUserLog.CREATE_TIME, time);
        }
        return this.baseService.counts(FcUserLog.class, wrapper);
    }

    public Integer amountByCondition(Long time) {
        QueryWrapper<FcUserLog> wrapper = new QueryWrapper<>();
        wrapper.select("distinct address")
                .eq(FcUserLog.DELETED, false);
        if (null != time){
            wrapper.gt(FcUserLog.CREATE_TIME, time);
        }
        return this.baseService.counts(FcUserLog.class, wrapper);
    }
}
