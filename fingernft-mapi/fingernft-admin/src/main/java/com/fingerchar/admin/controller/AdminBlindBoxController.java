package com.fingerchar.admin.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fingerchar.admin.annotation.RequiresPermissionsDesc;
import com.fingerchar.admin.service.BlindBlindBoxService;
import com.fingerchar.core.base.controller.BaseController;
import com.fingerchar.core.util.ResponseUtil;
import com.fingerchar.db.domain.BlindBlindBox;
import com.fingerchar.db.domain.BlindBlindBoxOrder;
import com.fingerchar.db.domain.BlindBlindBoxToNft;
import com.fingerchar.db.vo.BlindBlindBoxVo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/*
 * 盲盒设置
 * @author zjm
 * */

@RestController
@RequestMapping("/admin/blind/box")
public class AdminBlindBoxController extends BaseController {

    @Autowired
    private BlindBlindBoxService blindBoxService;

    @RequiresPermissions("admin:blind:box:list")
    @RequiresPermissionsDesc(menu = {"盲盒管理", "盒子管理"}, button = "查询")
    @PostMapping("/list")
    public Object list(String address, String name) {
        IPage<BlindBlindBoxVo> iPage = blindBoxService.page(address, name, this.getPageInfo());
        return ResponseUtil.okList(iPage);
    }

    @RequiresPermissions("admin:blind:box:add")
    @RequiresPermissionsDesc(menu = {"盲盒管理", "盒子管理"}, button = "添加")
    @PostMapping("/add")
    public Object add(BlindBlindBox blindBox) {
        Object error = validate(blindBox);
        if (error != null) {
            return error;
        }
        return blindBoxService.add(blindBox);
    }

    @RequiresPermissions("admin:blind:box:edit")
    @RequiresPermissionsDesc(menu = {"盲盒管理", "盒子管理"}, button = "修改盲盒")
    @PostMapping("/edit")
    public Object edit(BlindBlindBox blindBox) {
        Object error = validate(blindBox);
        if (error != null) {
            return error;
        }
        return blindBoxService.edit(blindBox);
    }

    @RequiresPermissions("admin:blind:box:deleted")
    @RequiresPermissionsDesc(menu = {"盲盒管理", "盒子管理"}, button = "删除盲盒")
    @PostMapping("/deleted")
    public Object deleted(@NotNull Long id) {
        BlindBlindBox blindBox = blindBoxService.get(id);
        if(null == blindBox) {
        	return ResponseUtil.fail(-1, "Unkown blind box with id=>" + id);
        }
        blindBoxService.delete(blindBox);
        return ResponseUtil.ok();
    }

    @RequiresPermissions("admin:blind:box:addNfts")
    @RequiresPermissionsDesc(menu = {"盲盒管理", "盒子管理"}, button = "配置")
    @PostMapping("/addNfts")
    public Object addNfts(Long blindBoxId, String nfts) {
        if (StringUtils.isEmpty(nfts)) {
            return ResponseUtil.fail(-1, "nft list is empty");
        }
        BlindBlindBox box = this.blindBoxService.get(blindBoxId);
        if(null == box){
            return ResponseUtil.fail(-1, "blind box is not existed");
        }
        List<BlindBlindBoxToNft> blindBoxToNfts = JSON.parseArray(nfts, BlindBlindBoxToNft.class);
        if(null == blindBoxToNfts || blindBoxToNfts.isEmpty()) {
        	return ResponseUtil.fail(-1, "nft list is empty");
        }
        Object err = this.verifyBox(box, blindBoxToNfts);
        if (null != err) {
            return err;
        }
        return this.blindBoxService.addNfts(box, blindBoxToNfts);
    }


    @RequiresPermissions("admin:blind:box:nfts")
    @RequiresPermissionsDesc(menu = {"盲盒管理", "盒子管理"}, button = "nft列表")
    @PostMapping("/nfts")
    public Object nfts(Long blindBoxId) {
        return ResponseUtil.okList(this.blindBoxService.nfts(blindBoxId));
    }

    // 检测数据是否合规
    private Object verifyBox(BlindBlindBox blindBox, List<BlindBlindBoxToNft> nftList) {
        // 该盒子卡片总量
        Integer totalBox = blindBox.getAmount() * blindBox.getNftAmount();
        Integer totalAmount = nftList.stream().mapToInt(BlindBlindBoxToNft::getAmount).sum();
        if(!totalBox.equals(totalAmount)){
            return ResponseUtil.fail(-1, "nft total amount is incorrect");
        }
        if(!blindBox.getIsRepetition()){
            Integer size = nftList.size();
            if(blindBox.getNftAmount().compareTo(size) > 0){
                return ResponseUtil.fail(-1, "The number of nft cannot be less than the open amount of boxes");
            }
            Integer max = nftList.stream().mapToInt(BlindBlindBoxToNft::getAmount).max().getAsInt();
            if(blindBox.getAmount().compareTo(max) < 0){
                return ResponseUtil.fail(-1, "The maximum amount of nft cannot exceed the amount of boxes");
            }
        }
        return null;
    }

    @RequiresPermissions("admin:blind:box:nftsbymulti")
    @RequiresPermissionsDesc(menu = {"盲盒管理", "盒子管理"}, button = "获取多个关联")
    @PostMapping("/nftsbymulti")
    public Object nftsbymulti(String boxList ) {
        if(StringUtils.isEmpty(boxList)){
            return ResponseUtil.okList(new ArrayList<>());
        }
        String[] _boxList = boxList.split(",");
        List<Long> idList = Arrays.stream(_boxList).map(v -> Long.valueOf(v)).collect(Collectors.toList());

        if(idList.isEmpty()){
            return ResponseUtil.okList(new ArrayList<>());
        }

        List<BlindBlindBoxToNft> list = this.blindBoxService.nftsbymulti(idList);
        return ResponseUtil.okList(list);
    }


    @RequiresPermissions("admin:blind:box:prepare")
    @RequiresPermissionsDesc(menu = {"盲盒管理", "盒子管理"}, button = "获取签名数据")
    @PostMapping("/prepare")
    public Object prepare(@NotNull Long id, String owner) {
        BlindBlindBox blindBox = this.blindBoxService.get(id);
        if(null == blindBox){
            return ResponseUtil.fail(-1, "blind box is not existed");
        }
        List<BlindBlindBoxToNft> boxToNftList = this.blindBoxService.nfts(id);
        Object error = this.verifyBox(blindBox, boxToNftList);
        if(null != error){
            return error;
        }

        return this.blindBoxService.prepare(blindBox, boxToNftList, owner);
    }

    @RequiresPermissions("admin:blind:box:sign")
    @RequiresPermissionsDesc(menu = {"盲盒管理", "盒子管理"}, button = "设置签名")
    @PostMapping("/sign")
    public Object sign(BlindBlindBoxOrder order) {
        return this.blindBoxService.sign(order);
    }

    private Object validate(BlindBlindBox blindBox) {
        if (StringUtils.isEmpty(blindBox.getAddress())) {
            return ResponseUtil.fail(-1, "盲盒种类信息不存在");
        }
        return null;
    }
}
