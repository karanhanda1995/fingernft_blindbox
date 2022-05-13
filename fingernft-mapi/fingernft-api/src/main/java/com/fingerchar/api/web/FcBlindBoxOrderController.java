package com.fingerchar.api.web;

import com.fingerchar.api.service.BlindBoxOrderService;
import com.fingerchar.api.service.FcUserService;
import com.fingerchar.core.base.controller.BaseController;
import com.fingerchar.core.constant.SysConfConstant;
import com.fingerchar.core.util.ResponseUtil;
import com.fingerchar.db.domain.FcUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(SysConfConstant.URL_PREFIX + "/blind/order")
public class FcBlindBoxOrderController extends BaseController {
	
	@Autowired
	FcUserService userService;
	
	@Autowired
	BlindBoxOrderService orderService;


	/**
	 * 获取购买盲盒的上链数据
	 * @param salt 所购买盲盒的盐
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/buyfee")
	public Object buyfee(String salt) throws Exception {
		if(StringUtils.isEmpty(salt)) {
			return ResponseUtil.fail(-1, "Unkown blind box");
		}
		String userAddress = (String) request.getAttribute("userAddress");
		if(StringUtils.isEmpty(userAddress)) {
			return ResponseUtil.unlogin();
		}
		FcUser user = this.userService.getUserByAddress(userAddress);
		if(null == user) {
			return ResponseUtil.unlogin();
		}
		return this.orderService.buyfee(salt);
	}

	/**
	 * 获取盲盒的订单信息
	 * @param blindBoxId 盲盒ID
	 * @return
	 */
	@PostMapping(value = "/get")
	public Object get(Long blindBoxId) {
		if(null == blindBoxId) {
			return ResponseUtil.ok();
		}
		return ResponseUtil.ok(this.orderService.get(blindBoxId));
	}

}
