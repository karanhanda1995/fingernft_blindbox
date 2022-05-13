package com.fingerchar.api.web;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fingerchar.api.service.BlindBlindBoxService;
import com.fingerchar.core.base.controller.BaseController;
import com.fingerchar.core.constant.SysConfConstant;
import com.fingerchar.core.util.ResponseUtil;
import com.fingerchar.db.domain.BlindBlindBoxToNft;
import com.fingerchar.db.vo.BlindBlindBoxVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
 * 盲盒设置
 * @author zjm
 * */

@RestController
@RequestMapping(SysConfConstant.URL_PREFIX + "/blind/box")
public class FcBlindBoxController extends BaseController {
	
	@Autowired
	private BlindBlindBoxService blindBoxService;
	
	/**
	 * 获取盲盒列表
	 * @param address blind type address
	 * @param name blind name
	 * @return
	 */
	@PostMapping("/list")
	public Object list(String address, String name) {
		IPage<BlindBlindBoxVo> iPage = blindBoxService.page(address, name, this.getPageInfo());
		return ResponseUtil.okList(iPage);
	}

	/**
	 * 获取单个盲盒详情
	 * @param blindBoxId 盲盒ID
	 * @return
	 */
	@PostMapping("/detail")
	public Object detail(Long blindBoxId) {
		if(null == blindBoxId) {
			return ResponseUtil.ok();
		}
		return blindBoxService.detail(blindBoxId);
	}

	@PostMapping("/listbyids")
	public Object listbyids(Long[] ids) {
		List<Long> tempList = Arrays.asList(ids);
		return ResponseUtil.ok(this.blindBoxService.listByMulti(tempList));
	}

	/**
	 * 获取单个盲盒内装的nft列表
	 * @param blindBoxId 盲盒ID
	 * @return
	 */
	@PostMapping("/nfts")
	public Object getBlindBoxToNft(Long blindBoxId) {
		if(null == blindBoxId) {
			return ResponseUtil.okList(new ArrayList<>());
		}
		List<BlindBlindBoxToNft> nftList = this.blindBoxService.nfts(blindBoxId);
		return ResponseUtil.okList(nftList);
	}

	/**
	 * 获取单个盲盒的开启（购买）历史
	 * @param blindBoxId 盲盒ID
	 * @return
	 */
	@PostMapping("/history")
	public Object getHistorys(Long blindBoxId) {
		if(null == blindBoxId) {
			return ResponseUtil.ok(new ArrayList<>());
		}
		return this.blindBoxService.historys(blindBoxId);
	}
}
