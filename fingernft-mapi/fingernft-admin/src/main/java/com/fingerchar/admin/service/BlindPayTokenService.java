package com.fingerchar.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fingerchar.core.base.service.IBaseService;
import com.fingerchar.core.manager.BlindPayTokenManager;
import com.fingerchar.core.util.ResponseUtil;
import com.fingerchar.db.base.BaseEntity;
import com.fingerchar.db.domain.BlindPayToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class BlindPayTokenService {

	@Autowired
	IBaseService baseServcie;

	@Autowired
	BlindPayTokenManager payTokenManager;
	
	public List<BlindPayToken> all() {
		QueryWrapper<BlindPayToken> wrapper = new QueryWrapper<>();
		wrapper.eq(BaseEntity.DELETED, false);
		return this.baseServcie.findByCondition(BlindPayToken.class, wrapper);
	}

	public BlindPayToken get(String token, String tokenId) {
		return this.payTokenManager.get(token, tokenId);
	}

	public Object edit(BlindPayToken payToken){
		if(StringUtils.isEmpty(payToken.getToken())) {
			return ResponseUtil.fail(-1, "token can not be empty");
		}
		if(StringUtils.isEmpty(payToken.getTokenId())) {
			return ResponseUtil.fail(-1, "token id can not be empty");
		}
		if(null == payToken.getType()) {
			return ResponseUtil.fail(-1, "type can not be empty");
		}
		BlindPayToken payToken1 = this.payTokenManager.get(payToken.getToken(), payToken.getTokenId());
		if(null != payToken1 && !payToken1.getId().equals(payToken.getId())){
			return ResponseUtil.fail(-1, "paytoken is existed");
		}
		payToken = this.payTokenManager.getPayTokenInfo(payToken);
		if(null == payToken){
			return ResponseUtil.fail(-1, "get paytoken info fail");
		}

		this.payTokenManager.update(payToken);
		return ResponseUtil.ok();
	}

	public Object add(BlindPayToken payToken){
		if(StringUtils.isEmpty(payToken.getToken())) {
			return ResponseUtil.fail(-1, "token can not be empty");
		}
		if(StringUtils.isEmpty(payToken.getTokenId())) {
			return ResponseUtil.fail(-1, "token id can not be empty");
		}
		if(null == payToken.getType()) {
			return ResponseUtil.fail(-1, "type can not be empty");
		}
		BlindPayToken payToken1 = this.payTokenManager.get(payToken.getToken(), payToken.getTokenId());
		if(null != payToken1){
			return ResponseUtil.fail(-1, "paytoken is existed");
		}
		payToken = this.payTokenManager.getPayTokenInfo(payToken);
		if(null == payToken){
			return ResponseUtil.fail(-1, "get paytoken info fail");
		}

		this.payTokenManager.save(payToken);
		return ResponseUtil.ok();
	}

	public Object delete(String token, String tokenId) {
		if(this.payTokenManager.delete(token, tokenId).equals(0)){
			return ResponseUtil.ok("paytoken is not existed");
		}
		return ResponseUtil.ok();
	}
}
