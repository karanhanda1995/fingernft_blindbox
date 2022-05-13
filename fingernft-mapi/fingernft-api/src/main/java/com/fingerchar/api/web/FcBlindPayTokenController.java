package com.fingerchar.api.web;

import com.fingerchar.core.base.controller.BaseController;
import com.fingerchar.core.constant.SysConfConstant;
import com.fingerchar.core.manager.BlindPayTokenManager;
import com.fingerchar.core.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(SysConfConstant.URL_PREFIX + "/blind/paytoken")
public class FcBlindPayTokenController extends BaseController {
	
    @Autowired
    BlindPayTokenManager payTokenManager;

    /**
     * 获取盲盒的payToken列表
     * @return
     */
    @PostMapping("/list")
    public Object list() {
        return ResponseUtil.okList(this.payTokenManager.all());
    }
}
