package com.fingerchar.api.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fingerchar.api.dto.BlindBoxOrder;
import com.fingerchar.api.utils.DappCryptoUtil;
import com.fingerchar.core.base.service.IBaseService;
import com.fingerchar.core.manager.BlindBlindBoxOrderManager;
import com.fingerchar.core.manager.FcPayTokenManager;
import com.fingerchar.core.manager.FcSystemConfigManager;
import com.fingerchar.core.util.ResponseUtil;
import com.fingerchar.db.domain.BlindBlindBoxOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BlindBoxOrderService {

	public static final Logger logger = LoggerFactory.getLogger(BlindBoxOrderService.class);
	
	@Autowired
	FcPayTokenManager payTokenManager;
	
	@Autowired
	FcSystemConfigManager systemConfigManager;

	@Autowired
	BlindBlindBoxOrderManager orderManager;
	
	@Autowired
	IBaseService baseService;

	
	public Object buyfee(String salt) {
		BlindBlindBoxOrder order = this.orderManager.get(salt);
		if(null == order){
			return ResponseUtil.fail(-1, "order is not existed");
		}

		if(order.getStartTime() > System.currentTimeMillis() / 1000) {
			return ResponseUtil.fail(-1, "activity is not starting");
		}
		
		if(order.getEndTime() < System.currentTimeMillis() / 1000) {
			return ResponseUtil.fail(-1, "activity is finished");
		}

		BlindBoxOrder orderInfo = new BlindBoxOrder(order);
		orderInfo.setSellerFee(order.getSellerfee().toString());
		orderInfo.setBuyerFee(order.getBuyerfee().toString());

		String buyerValue = this.payTokenManager.getErcValue(order.getBuyerType(), order.getBuying(), order.getPaytokenDecimals());
		if(null == buyerValue) {
			return ResponseUtil.fail(-1, "Unkown pay type");
		}
		orderInfo.setBuying(buyerValue);

		orderInfo = DappCryptoUtil.blindBoxSign(orderInfo, order.getBuyerfee());

		return ResponseUtil.ok(orderInfo);
	}

	/**
	 * @param id
	 * @return
	 */
	public BlindBlindBoxOrder get(Long id) {
		return this.orderManager.get(id);
	}

}
