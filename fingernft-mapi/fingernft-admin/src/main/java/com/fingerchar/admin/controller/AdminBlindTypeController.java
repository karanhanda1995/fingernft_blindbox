package com.fingerchar.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fingerchar.admin.annotation.RequiresPermissionsDesc;
import com.fingerchar.admin.service.BlindBlindBoxService;
import com.fingerchar.admin.service.BlindNftService;
import com.fingerchar.admin.service.BlindTypeService;
import com.fingerchar.admin.service.LogHelper;
import com.fingerchar.admin.vo.BlindTypeParamVo;
import com.fingerchar.core.base.controller.BaseController;
import com.fingerchar.core.util.DappWeb3jUtil;
import com.fingerchar.core.util.ResponseUtil;
import com.fingerchar.db.domain.BlindType;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Locale;

/*
 * 盲盒种类管理
 * @author zjm
 * */
@RestController
@RequestMapping("/admin/blind/type")
public class AdminBlindTypeController extends BaseController {

    @Autowired
    private BlindTypeService supplierService;

    @Autowired
    private BlindBlindBoxService blindBoxService;
    @Autowired
    private BlindNftService blindNftService;



    @Autowired
    private LogHelper logHelper;

    @RequiresPermissions("admin:blind:type:list")
    @RequiresPermissionsDesc(menu = {"盲盒管理", "种类管理"}, button = "查询")
    @PostMapping("/list")
    public Object list(BlindTypeParamVo typeParamVo) {
        IPage<BlindType> iPage = supplierService.page(typeParamVo, this.getPageInfo());
        return ResponseUtil.okList(iPage);
    }

    @RequiresPermissions("admin:blind:type:add")
    @RequiresPermissionsDesc(menu = {"盲盒管理", "种类管理"}, button = "添加种类")
    @PostMapping("/add")
    public Object add(BlindType blindType){
        Object error = validate(blindType);
        if (error != null){
            return error;
        }
        blindType.setAddress(blindType.getAddress().toLowerCase(Locale.ROOT));
        return supplierService.add(blindType);
    }

    @RequiresPermissions("admin:blind:type:edit")
    @RequiresPermissionsDesc(menu = {"盲盒管理", "种类管理"}, button = "修改种类")
    @PostMapping("/edit")
    public Object edit(BlindType blindType){
        Object error = validate(blindType);
        if (error != null){
            return error;
        }
        Long id = blindType.getId();
        if (id == null){
        	return ResponseUtil.fail(-1, "种类ID不能为空！");
        }
        blindType.setAddress(blindType.getAddress().toLowerCase(Locale.ROOT));
        return supplierService.edit(blindType);
    }

    @RequiresPermissions("admin:blind:type:delete")
    @RequiresPermissionsDesc(menu = {"盲盒管理", "种类管理"}, button = "删除种类")
    @PostMapping("/delete")
    public Object deleteSupplier(@NotNull String address){
        supplierService.delete(address);
        logHelper.logAuthSucceed("删除种类", address);
        return ResponseUtil.ok();
    }

    @RequiresPermissions("admin:blind:type:all")
    @RequiresPermissionsDesc(menu = {"盲盒管理", "种类管理"}, button = "查询所有")
    @PostMapping("/all")
    public Object all(){
        List<BlindType> blindTypeList = supplierService.getAll();
        return ResponseUtil.ok(blindTypeList);
    }

    private Object validate(BlindType supplier) {
        if (StringUtils.isEmpty(supplier.getName())) {
        	return ResponseUtil.fail(-1, "Blind type name is empty");
        }
        String address = supplier.getAddress();
        if(StringUtils.isEmpty(address)){
            return ResponseUtil.fail(-1, "Blind address name is empty");
        }
        if(!DappWeb3jUtil.isValidAddress(address)){
            return ResponseUtil.fail(-1, "Blind address name is invalid");
        }
        return null;
    }


}
