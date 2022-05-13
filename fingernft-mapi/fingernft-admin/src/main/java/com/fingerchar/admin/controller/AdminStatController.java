package com.fingerchar.admin.controller;

import com.fingerchar.admin.service.StatService;
import com.fingerchar.core.base.controller.BaseController;
import com.fingerchar.core.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminStatController extends BaseController {

    @Autowired
    private StatService statService ;
    @PostMapping("/stat")
    public Object stat(){
        return ResponseUtil.ok(this.statService.stat());
    }

    @PostMapping("/statOneDay")
    public Object statOneDay(){
        return ResponseUtil.ok(this.statService.statOneDay());
    }
}
