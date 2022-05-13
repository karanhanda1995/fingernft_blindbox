package com.fingerchar.api.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fingerchar.core.base.service.IBaseService;
import com.fingerchar.db.base.BaseEntity;
import com.fingerchar.db.domain.BlindType;

/*
 *
 * @author zjm
 * */
@Service
public class BlindTypeService {

	@Autowired
	private IBaseService baseService;

	public IPage<BlindType> querySelective(String name, IPage<BlindType> page, boolean isASC, String sortType) {
		QueryWrapper<BlindType> wrapper = new QueryWrapper<>();
		wrapper.eq(BaseEntity.DELETED, false);
		if (!StringUtils.isEmpty(name)) {
			wrapper.like(BlindType.NAME, name);
		}
		if (!StringUtils.isEmpty(isASC) && !StringUtils.isEmpty(sortType)) {
			wrapper.orderBy(true, isASC, sortType);
		}
		return baseService.findByPage(BlindType.class, wrapper, page);
	}

	public List<Long> findByName(String name) {
		QueryWrapper<BlindType> wrapper = new QueryWrapper<>();
		wrapper.eq(BaseEntity.DELETED, false);
		if (!StringUtils.isEmpty(name)) {
			wrapper.like(BlindType.NAME, name);
		}
		List<BlindType> list = baseService.findByCondition(BlindType.class, wrapper);
		List<Long> result = list.stream().map(BlindType::getId).collect(Collectors.toList());
		return result;
	}

	public BlindType findById(Long id) {
		QueryWrapper<BlindType> wrapper = new QueryWrapper<>();
		wrapper.eq(BaseEntity.DELETED, false);
		wrapper.eq(BaseEntity.ID, id);
		return baseService.getById(BlindType.class, id);
	}

	public Map<Long, String> idForName() {
		Map<Long, String> map = new HashMap<>();
		QueryWrapper<BlindType> wrapper = new QueryWrapper<>();
		wrapper.eq(BaseEntity.DELETED, false);
		List<BlindType> li = baseService.findByCondition(BlindType.class, wrapper);
		for (BlindType supplier : li) {
			map.put(supplier.getId(), supplier.getName());
		}
		return map;
	}

	public List<Map<String, Object>> getIdAndName() {
		List<Map<String, Object>> result = new ArrayList<>();
		QueryWrapper<BlindType> wrapper = new QueryWrapper<>();
		wrapper.eq(BaseEntity.DELETED, false);
		List<BlindType> typeList = baseService.findByCondition(BlindType.class, wrapper);
		Map<String, Object> map = null;
		for (BlindType supplier : typeList) {
			map = new HashMap<>();
			map.put("id", supplier.getId());
			map.put("name", supplier.getName());
			result.add(map);
		}
		return result;
	}
}
