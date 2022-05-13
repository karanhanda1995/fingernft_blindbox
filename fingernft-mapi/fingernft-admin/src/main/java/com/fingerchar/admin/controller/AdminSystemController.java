package com.fingerchar.admin.controller;

import com.fingerchar.admin.annotation.RequiresPermissionsDesc;
import com.fingerchar.core.base.controller.BaseController;
import com.fingerchar.core.util.ResponseUtil;
import com.fingerchar.admin.service.FcSystemConfigService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/admin/config")
public class AdminSystemController extends BaseController {

	@Autowired
    private FcSystemConfigService fcSystemConfigService;

    /**
     * 获取Key
     * @return
     */
    @RequiresPermissions("admin:config:fetch")
    @RequiresPermissionsDesc(menu = {"配置管理", "配置管理"}, button = "查询")
    @PostMapping("/fetch")
    public  Object fetchList() {
        Map<String, String> data = fcSystemConfigService.all();
        return ResponseUtil.ok(data);
    }
    
    @RequiresPermissions("admin:config:update")
    @RequiresPermissionsDesc(menu = {"配置管理", "配置管理"}, button = "更新")
    @PostMapping("/update")
    public Object update(String key, String value) {
    	if(StringUtils.isEmpty(key)) {
    		return ResponseUtil.fail(-1, "配置信息不能为空");
    	}
    	return this.fcSystemConfigService.updateConfig(key, value);
    }
}
