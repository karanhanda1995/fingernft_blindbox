package com.fingerchar.api.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fingerchar.core.base.service.IBaseService;
import com.fingerchar.core.manager.BlindBlindBoxManager;
import com.fingerchar.core.manager.BlindBoxHistoryManager;
import com.fingerchar.core.manager.FcUserManager;
import com.fingerchar.core.util.ResponseUtil;
import com.fingerchar.db.base.BaseEntity;
import com.fingerchar.db.domain.BlindBlindBox;
import com.fingerchar.db.domain.BlindBlindBoxToNft;
import com.fingerchar.db.domain.BlindBoxHistory;
import com.fingerchar.db.domain.FcUser;
import com.fingerchar.db.vo.BlindBlindBoxVo;
import com.fingerchar.db.vo.BlindBoxHistoryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
public class BlindBlindBoxService {

	@Autowired
    private IBaseService baseService;
	
    @Autowired
    private BlindBlindBoxManager boxManager;

    @Autowired
    private BlindBoxHistoryManager historyManager;

    @Autowired
    private FcUserManager userManager;

    public IPage<BlindBlindBoxVo> page(String address, String name, IPage<BlindBlindBox> page) {
        QueryWrapper<BlindBlindBox> wrapper = new QueryWrapper<>();
        if(!StringUtils.isEmpty(address)){
            wrapper.eq(BlindBlindBox.ADDRESS, address);
        }
        if(!StringUtils.isEmpty(name)){
            wrapper.like(BlindBlindBox.NAME, name);
        }
        wrapper.eq(BlindBlindBox.IS_SYNC, true);
        wrapper.eq(BaseEntity.DELETED,false);
        wrapper.orderByDesc(BaseEntity.ID);

        IPage<BlindBlindBox> iPage = baseService.findByPage(BlindBlindBox.class, wrapper, page);
        List<BlindBlindBox> list = iPage.getRecords();
        List<BlindBlindBoxVo> boxVoList = this.boxManager.getBoxInfoList(list);

        IPage<BlindBlindBoxVo> result = new Page<BlindBlindBoxVo>(iPage.getCurrent(), iPage.getSize());
        result.setPages(iPage.getPages());
        result.setRecords(boxVoList);
        result.setTotal(iPage.getTotal());
        return result;
    }

    public boolean add(BlindBlindBox blindBox){
        blindBox.setCreateTime(System.currentTimeMillis()/1000);
        blindBox.setUpdateTime(System.currentTimeMillis()/1000);
        return baseService.save(blindBox) > 0;
    }

    public Object detail(Long id){
        BlindBlindBox box = this.boxManager.get(id);
        if(null == box){
            return ResponseUtil.fail(-1, "blind box is not existed");
        }
        BlindBlindBoxVo boxInfo = this.boxManager.getBoxInfo(box);
        return ResponseUtil.ok(boxInfo);
    }

    public List<BlindBlindBoxToNft> nfts(Long id){
        return this.boxManager.nfts(id);
    }

    public Object historys(Long boxId){
        BlindBlindBox box = this.boxManager.get(boxId);
        if(null == box){
            return ResponseUtil.fail(-1, "blind box is not existed");
        }
        List<BlindBoxHistory> historyList = this.historyManager.list(box);
        if(historyList.isEmpty()){
            return ResponseUtil.okList(new ArrayList<>());
        }
        List<String> addressList = historyList.stream().map(BlindBoxHistory::getBuyer).collect(Collectors.toList());
        List<FcUser> userList = this.userManager.listByMulti(addressList);
        Map<String, FcUser> map = userList.stream().collect(Collectors.toMap(FcUser::getAddress, Function.identity()));
        List<BlindBoxHistoryVo> historyVos = historyList.stream().map(vo -> new BlindBoxHistoryVo(vo, map.get(vo.getBuyer()))).collect(Collectors.toList());
        return ResponseUtil.okList(historyVos);
    }

    public List<BlindBlindBox> listByMulti(List<Long> ids){
        return this.boxManager.listByMulti(ids);
    }


}
