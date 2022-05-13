package com.fingerchar.admin.controller;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fingerchar.admin.annotation.RequiresPermissionsDesc;
import com.fingerchar.core.base.controller.BaseController;
import com.fingerchar.core.util.ResponseUtil;
import com.fingerchar.db.domain.BlindPayToken;
import com.fingerchar.admin.service.BlindPayTokenService;

@RestController
@RequestMapping("/admin/blind/paytoken")
public class AdminBlindPayTokenController extends BaseController {
	
	@Autowired
	BlindPayTokenService payTokenService;

	@RequiresPermissions("admin:blind:paytoken:list")
    @RequiresPermissionsDesc(menu = {"盲盒管理", "盲盒币种管理"}, button = "查询")
    @PostMapping("/list")
    public Object list() {
        return ResponseUtil.okList(this.payTokenService.all());
    }

	@RequiresPermissions("admin:blind:paytoken:add")
    @RequiresPermissionsDesc(menu = {"盲盒管理", "盲盒币种管理"}, button = "保存")
    @PostMapping("/add")
    public Object add(BlindPayToken payToken) {
		if(null == payToken) {
			return ResponseUtil.fail(-1, "盲盒币种信息不能为空！");
		}
		return this.payTokenService.add(payToken);
    }

	@RequiresPermissions("admin:blind:paytoken:edit")
	@RequiresPermissionsDesc(menu = {"盲盒管理", "盲盒币种管理"}, button = "编辑")
	@PostMapping("/edit")
	public Object edit(BlindPayToken payToken) {
		if(null == payToken) {
			return ResponseUtil.fail(-1, "盲盒币种信息不能为空！");
		}
		return this.payTokenService.edit(payToken);
	}

	@RequiresPermissions("admin:blind:paytoken:delete")
    @RequiresPermissionsDesc(menu = {"盲盒管理", "盲盒币种管理"}, button = "删除")
    @PostMapping("/delete")
    public Object delete(String token, String tokenId) {
		if(StringUtils.isEmpty(token) || StringUtils.isEmpty(tokenId)) {
			return ResponseUtil.fail(-1, "Unkown pay token");
		}
		return this.payTokenService.delete(token, tokenId);
    }
}
