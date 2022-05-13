package com.fingerchar.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fingerchar.core.base.service.IBaseService;
import com.fingerchar.db.domain.FcOrderLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/*
 *
 * @author zjm
 * */

@Service
public class FcOrderLogService {

	@Autowired
    private IBaseService baseService;

    public List<FcOrderLog> findByToken(String address){
        QueryWrapper<FcOrderLog> wrapper = new QueryWrapper<>();
        wrapper.eq(FcOrderLog.TOKEN,address);
        return baseService.findByCondition(FcOrderLog.class,wrapper);
    }
}
