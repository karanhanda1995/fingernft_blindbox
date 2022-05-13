package com.fingerchar.core.manager;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.fingerchar.core.base.service.IBaseService;
import com.fingerchar.core.util.DappCryptoUtil;
import com.fingerchar.db.domain.BlindBlindBox;
import com.fingerchar.db.domain.BlindBlindBoxOrder;
import com.fingerchar.db.domain.BlindBlindBoxToNft;
import com.fingerchar.db.dto.BlindBoxOrder;
import com.fingerchar.db.dto.BlindOpenLog;
import com.fingerchar.db.dto.BlindSellAssets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author： Zjm
 * @Date：2022/3/22 11:46
 */
@Service
public class BlindBlindBoxOrderManager {
    @Autowired
    IBaseService baseService;

    @Autowired
    BlindBoxHistoryManager blindBoxHistoryManager;

    public Integer sign(BlindBlindBoxOrder order){
        BlindBlindBoxOrder blindBoxOrder = this.get(order.getBlindBoxId());
        if(null == blindBoxOrder){
            return this.save(order);
        }
        return this.update(order);
    }

    public String getSalt() {
        String salt = DappCryptoUtil.getSalt();
        BlindBlindBoxOrder bySalt = this.findBySalt(salt);
        while (null != bySalt ){
            salt =  DappCryptoUtil.getSalt();
            bySalt = this.findBySalt(salt);
        }
        return salt;
    }

    public BlindBlindBoxOrder packBoxOrder(BlindBlindBox blindBox, List<BlindBlindBoxToNft> boxToNftList){
        BlindBlindBoxOrder blindBoxOrder = new BlindBlindBoxOrder();
        blindBoxOrder.setBlindBoxId(blindBox.getId());
        List<BlindSellAssets> sellAssets = new ArrayList<>();
        List<Integer> sellingList = new ArrayList<>();
        List<String> uriList = new ArrayList<>();
        BigDecimal totalAmount = new BigDecimal(blindBox.getAmount()).multiply(new BigDecimal(blindBox.getPrice()));
        for(BlindBlindBoxToNft boxToNft: boxToNftList){
            sellAssets.add(new BlindSellAssets(boxToNft));
            sellingList.add(boxToNft.getAmount());
            uriList.add(boxToNft.getMetadataUrl().replace("ipfs:/", ""));
        }
        blindBoxOrder.setSellAssets(JSON.toJSONString(sellAssets));
        blindBoxOrder.setSellings(JSON.toJSONString(sellingList));
        blindBoxOrder.setUris(JSON.toJSONString(uriList));
        blindBoxOrder.setBuying(totalAmount.toString());
        blindBoxOrder.setStartTime(blindBox.getStartTime());
        blindBoxOrder.setEndTime(blindBox.getEndTime());
        blindBoxOrder.setOpeneds(0L);
        blindBoxOrder.setRepeat(blindBox.getIsRepetition());
        blindBoxOrder.setOpening(blindBox.getNftAmount().longValue());

        blindBoxOrder.setBuyerToken(blindBox.getPaytokenAddress());
        blindBoxOrder.setBuyerTokenId(blindBox.getPaytokenTokenId());
        blindBoxOrder.setBuyerType(blindBox.getPaytokenType());
        blindBoxOrder.setPaytokenDecimals(blindBox.getPaytokenDecimals());
        blindBoxOrder.setPaytokenName(blindBox.getPaytokenName());
        blindBoxOrder.setPaytokenSymbol(blindBox.getPaytokenSymbol());
        blindBoxOrder.setPaytokenMetadataContent(blindBox.getPaytokenMetadataContent());

        String salt = this.getSalt();
        blindBoxOrder.setSalt(salt);

        return blindBoxOrder;
    }

    private BlindBlindBoxOrder findBySalt(String salt){
        QueryWrapper<BlindBlindBoxOrder> wrapper = new QueryWrapper<>();
        wrapper.eq(BlindBlindBoxOrder.SALT, salt)
                .eq(BlindBlindBoxOrder.DELETED, false);
        return this.baseService.getByCondition(BlindBlindBoxOrder.class, wrapper);
    }

    public List<BlindBlindBoxOrder> listByMulti(List<BlindBlindBox> boxList){
        if(boxList.isEmpty()){
            return new ArrayList<>();
        }
        List<Long> idList = boxList.stream().map(BlindBlindBox::getId).collect(Collectors.toList());
        QueryWrapper<BlindBlindBoxOrder> wrapper = new QueryWrapper<>();
        wrapper.in(BlindBlindBoxOrder.BLIND_BOX_ID, idList)
                .eq(BlindBlindBoxOrder.DELETED, false);
        return this.baseService.findByCondition(BlindBlindBoxOrder.class, wrapper);
    }

    public BlindBlindBoxOrder get(String salt ){
        QueryWrapper<BlindBlindBoxOrder> wrapper = new QueryWrapper<>();
        wrapper.eq(BlindBlindBoxOrder.SALT, salt)
               .eq(BlindBlindBoxOrder.DELETED, false);
        return this.baseService.getByCondition(BlindBlindBoxOrder.class, wrapper);
    }

    public BlindBlindBoxOrder get(Long boxId){
        QueryWrapper<BlindBlindBoxOrder> wrapper = new QueryWrapper<>();
        wrapper.eq(BlindBlindBoxOrder.BLIND_BOX_ID, boxId)
                .eq(BlindBlindBoxOrder.DELETED, false);
        return this.baseService.getByCondition(BlindBlindBoxOrder.class, wrapper);
    }

    public Integer open(BlindOpenLog log){
        String salt = log.getSalt().toString();
        BigInteger amount = log.getAmount();

        BlindBlindBoxOrder order = this.get(salt);
        if(null == order){
            return 0;
        }

        blindBoxHistoryManager.add(log, order);

        order.setOpeneds(order.getOpeneds() + Long.parseLong(amount.toString()));
        return this.update(order);
    }

    public Integer delete(BlindBlindBox blindBox){
        UpdateWrapper<BlindBlindBoxOrder> wrapper = new UpdateWrapper<>();
        wrapper.eq(BlindBlindBoxOrder.BLIND_BOX_ID, blindBox.getId())
                .eq(BlindBlindBoxOrder.DELETED, false);
        wrapper.set(BlindBlindBoxOrder.DELETED, true);
        return this.baseService.updateByCondition(BlindBlindBoxOrder.class, wrapper);
    }

    public Integer update(BlindBlindBoxOrder order){
        return this.baseService.update(order);
    }

    public Integer save(BlindBlindBoxOrder order){
        return this.baseService.save(order);
    }

    public Integer countByCondition(Long time) {
        QueryWrapper<BlindBlindBoxOrder> wrapper = new QueryWrapper<>();
        wrapper.eq(BlindBlindBoxOrder.DELETED, false);
        if (null != time) {
            wrapper.gt(BlindBlindBoxOrder.CREATE_TIME, time);
        }
        List<BlindBlindBoxOrder> boxList = this.baseService.findByCondition(BlindBlindBoxOrder.class, wrapper);
        Integer amount = 0;
        for (BlindBlindBoxOrder blindBlindBox : boxList) {
            amount += blindBlindBox.getOpeneds().intValue();
        }
        return amount;
    }
}
