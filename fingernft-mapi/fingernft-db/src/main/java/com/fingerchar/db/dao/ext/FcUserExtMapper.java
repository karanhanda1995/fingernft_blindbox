package com.fingerchar.db.dao.ext;

import java.util.Collection;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.fingerchar.db.vo.FcUserVo;
import com.fingerchar.db.vo.IndexUserVO;
import com.fingerchar.db.vo.TopSellerVO;

/*
* 扩展userMapper
* */
public interface FcUserExtMapper {
	
    @SuppressWarnings("rawtypes")
	IPage<FcUserVo> getList(IPage<FcUserVo> page, @Param(Constants.WRAPPER) Wrapper ew);

    @SuppressWarnings("rawtypes")
	IPage<FcUserVo> getList2(IPage<FcUserVo> page, @Param(Constants.WRAPPER) Wrapper ew);
    
    IPage<IndexUserVO> findUserWithDeal(@Param("addrList") Collection<String> addrList, IPage<IndexUserVO> page);
    
    /**
	 * @param name
	 * @param address
	 * @param pageInfo
	 * @return
	 */
	IPage<TopSellerVO> listTopSeller(@Param("name")String name, @Param("address")String address, IPage<TopSellerVO> pageInfo);
}
