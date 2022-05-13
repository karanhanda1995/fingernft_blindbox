package com.fingerchar.db.dao.ext;

import java.util.Map;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.fingerchar.db.domain.BlindBlindBoxOrder;

public interface BlindBlindBoxOrderExtMapper {
	
    @SuppressWarnings("rawtypes")
	@MapKey("blindBoxId")
    Map<Long, BlindBlindBoxOrder> getIdAll(@Param(Constants.WRAPPER) Wrapper ew);
}
