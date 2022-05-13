package com.fingerchar.api.service;

import com.fingerchar.core.base.service.impl.BaseService;
import com.fingerchar.db.domain.BlindNft;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/*
 *
 * @author zjm
 * */
@Service
public class BlindNftService {

	@Autowired
	private BaseService baseService;
	
	public BlindNft findById(Long id, Integer j) {
		return baseService.getById(BlindNft.class, id);
	}


}
