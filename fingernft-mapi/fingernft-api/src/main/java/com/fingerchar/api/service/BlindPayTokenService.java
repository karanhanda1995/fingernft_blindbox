package com.fingerchar.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fingerchar.core.base.service.IBaseService;
import com.fingerchar.db.base.BaseEntity;
import com.fingerchar.db.domain.BlindPayToken;

@Service
public class BlindPayTokenService {

	@Autowired
	IBaseService baseServcie;
	
	public List<BlindPayToken> findAll() {
		QueryWrapper<BlindPayToken> wrapper = new QueryWrapper<>();
		wrapper.eq(BaseEntity.DELETED, false);
		return this.baseServcie.findByCondition(BlindPayToken.class, wrapper);
	}
}
