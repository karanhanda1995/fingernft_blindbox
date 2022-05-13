package com.fingerchar.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fingerchar.admin.annotation.RequiresPermissionsDesc;
import com.fingerchar.admin.service.BlindNftService;
import com.fingerchar.core.base.controller.BaseController;
import com.fingerchar.core.util.ResponseUtil;
import com.fingerchar.db.domain.BlindNft;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
 * 奖品设置
 * @author zjm
 * */
@RestController
@RequestMapping("/admin/blind/nft")
public class AdminBlindNftController extends BaseController {

	@Autowired
    private BlindNftService blindNftService;

    @RequiresPermissions("admin:blind:nft:list")
    @RequiresPermissionsDesc(menu = {"盲盒管理", "NFT管理"}, button = "查询")
    @PostMapping("/list")
    public Object list(String address, String name) {
        IPage<BlindNft> iPage = blindNftService.page(address, name, this.getPageInfo());
        return ResponseUtil.okList(iPage);
    }

    @RequiresPermissions("admin:blind:nft:add")
    @RequiresPermissionsDesc(menu = {"盲盒管理", "NFT管理"}, button = "添加")
    @PostMapping("/add")
    public Object add(BlindNft blindNft) {
    	if(null == blindNft) {
    		return ResponseUtil.fail(-1, "blind nft is null");
    	}
    	if(null == blindNft.getType()) {
    		return ResponseUtil.fail(-1, "Unkown nft type");
    	}
        Object error = validate(blindNft);
        if (error != null){
            return error;
        }
        return blindNftService.add(blindNft);
    }

    @RequiresPermissions("admin:blind:nft:deleted")
    @RequiresPermissionsDesc(menu = {"盲盒管理", "NFT管理"}, button = "删除")
    @PostMapping("/deleted")
    public Object deleted(@NotNull Long id) {
        return blindNftService.deleteById(id);
    }

    @RequiresPermissions("admin:blind:nft:all")
    @RequiresPermissionsDesc(menu = {"盲盒管理", "nft管理"}, button = "查询所有")
    @PostMapping("/all")
    public Object all(String address){
        return ResponseUtil.okList(blindNftService.allByAddress(address));
    }

    private Object validate(BlindNft blindNft) {
        if ( StringUtils.isEmpty(blindNft.getImgUrl()) ||
                StringUtils.isEmpty(blindNft.getAddress())) {
            return ResponseUtil.fail(-1, "盲盒NFT所属盲盒类型或者图片信息或者地址信息不能为空");
        }
        return null;
    }
}
