package com.fingerchar.admin.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fingerchar.core.base.service.IBaseService;
import com.fingerchar.db.base.BaseEntity;
import com.fingerchar.db.dao.ext.FcUserExtMapper;
import com.fingerchar.db.domain.*;
import com.fingerchar.db.vo.FcUserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FcUserService {
    @Autowired
    private FcUserExtMapper userExtMapper;

    @Autowired
    private FcPayTokenService payTokenService;

    @Autowired
    private IBaseService baseService;


    /**
     * @param useraddress
     * @param isverify
     * @param name
     * @param page
     * @param isASC
     * @param sortType
     * @return
     */
    public IPage<FcUserVo> querySelective(String useraddress, Integer isverify, String name, IPage<FcUserVo> page, boolean isASC, String sortType) {
        QueryWrapper<FcUser> wrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(name)) {
            wrapper.like("user.nickname", name);
        }
        if (!StringUtils.isEmpty(isverify)) {
            if (isverify != 0) {
                wrapper.eq("user.user_verify", isverify);
            } else {
                wrapper.and(i -> i.eq("user.user_verify", isverify).or().isNull("user.user_verify"));
            }

        }
        if (!StringUtils.isEmpty(useraddress)) {
            wrapper.eq("user.address", useraddress);
        }
        wrapper.eq("user.is_web", true);

        if (!StringUtils.isEmpty(isASC) && !StringUtils.isEmpty(sortType)) {
            wrapper.orderBy(true, isASC, sortType);
        }
        /*
         * 填装买卖nft数据等
         * */
        IPage<FcUserVo> iPage = userExtMapper.getList2(page, wrapper);
        List<FcUserVo> list = iPage.getRecords();


        IPage<FcUserVo> result = new Page<FcUserVo>(iPage.getCurrent(), iPage.getSize());
        result.setPages(iPage.getPages());
        // packFcUserVo(list)打包数据
        result.setRecords(list);
        result.setTotal(iPage.getTotal());
        return result;
    }


    /**
     * 根据ID更新Verify
     *
     * @param contract Verify记录
     * @return 更新成功返回true，否则返回false
     */
    public boolean VerifyContract(FcUser contract, Integer userVerify) {
        contract.setUserVerify(userVerify);
        contract.setUpdateTime(System.currentTimeMillis() / 1000);
        return baseService.update(contract) > 0;
    }

    /**
     * 根据ID禁用
     *
     * @param contract
     * @return 更新成功返回true，否则返回false
     */
    public boolean disableContract(FcUser contract) {
        contract.setDeleted(true);
        contract.setUpdateTime(System.currentTimeMillis() / 1000);
        return baseService.update(contract) > 0;
    }


    /**
     * 根据ID启用
     *
     * @param contract
     * @return 更新成功返回true，否则返回false
     */
    public boolean enableContract(FcUser contract) {
        contract.setDeleted(false);
        contract.setUpdateTime(System.currentTimeMillis() / 1000);
        return baseService.update(contract) > 0;
    }


    /**
     * findById
     *
     * @param id
     * @return
     */
    public FcUser findById(Long id) {
        return baseService.getById(FcUser.class, id);
    }


    /**
     * 统计新增用户数
     * @param staTime
     * @return
     */
    public Map<String, Object> newCreateList(Long staTime) {
        Map<String, Object> map = new HashMap<>();
        QueryWrapper<FcUser> wrapper = new QueryWrapper<>();
        long oneDay = 24 * 60 * 60;
        wrapper.lt(FcUser.CREATE_TIME, staTime + oneDay).ge(FcUser.CREATE_TIME, staTime).eq(FcUser.IS_WEB,true);
        Integer toDayCounts = baseService.counts(FcUser.class, wrapper);
        QueryWrapper<FcUser> beforeWrapper = new QueryWrapper<>();
        beforeWrapper.lt(FcUser.CREATE_TIME, staTime).ge(FcUser.CREATE_TIME, staTime - oneDay).eq(FcUser.IS_WEB,true);
        Integer beforeCounts = baseService.counts(FcUser.class, beforeWrapper);
        map.put("todayAdd", toDayCounts);
        map.put("beforeAdd", beforeCounts);
        return map;
    }

    /**
     * 统计用户
     */
    public Map<String, Object> statUser() {
        Map<String, Object> map = new HashMap<>();
        QueryWrapper<FcUser> wrapper = new QueryWrapper<>();
        wrapper.ge(BaseEntity.CREATE_TIME, System.currentTimeMillis() / 1000).eq(FcUser.IS_WEB,true);
        Integer todayCounts = baseService.counts(FcUser.class, wrapper);
        QueryWrapper<FcUser> beforeWrapper = new QueryWrapper<>();
        beforeWrapper.ge(BaseEntity.CREATE_TIME, System.currentTimeMillis() / 1000 - 24 * 60 * 60).lt(BaseEntity.CREATE_TIME, System.currentTimeMillis() / 1000).eq(FcUser.IS_WEB,true);
        Integer beforeCounts = baseService.counts(FcUser.class, beforeWrapper);
        QueryWrapper<FcUser> verifyWrapper = new QueryWrapper<>();
        verifyWrapper.eq("user_verify", 1).eq(FcUser.IS_WEB,true);
        Integer verifyCounts = baseService.counts(FcUser.class, verifyWrapper);
        QueryWrapper<FcUser> notVerifyWrapper = new QueryWrapper<>();
        notVerifyWrapper.isNull("user_verify").or().eq("user_verify", 0).eq(FcUser.IS_WEB,true);
        Integer notVerifyCounts = baseService.counts(FcUser.class, notVerifyWrapper);
        map.put("todayAdd", todayCounts);
        map.put("yesterdayAdd", beforeCounts);
        map.put("verifyUsers", verifyCounts);
        map.put("notVerifyUser", notVerifyCounts);
        return map;
    }
    
    public List<FcUser> listUserByAddrs(Collection<String> addrs) {
    	QueryWrapper<FcUser> wrapper = new QueryWrapper<>();
    	wrapper.in(FcUser.ADDRESS, addrs);
    	return this.baseService.findByCondition(FcUser.class, wrapper);
    }

    public Integer count(Long time) {
        QueryWrapper<FcUser> wrapper = new QueryWrapper<>();
        wrapper.eq(FcUser.DELETED, false);
        if (null !=time){
            wrapper.gt(FcUser.CREATE_TIME, time);
        }
        return this.baseService.counts(FcUser.class, wrapper);
    }
}
