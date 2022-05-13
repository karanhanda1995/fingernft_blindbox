package com.fingerchar.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fingerchar.core.base.service.IBaseService;
import com.fingerchar.core.constant.ContractType;
import com.fingerchar.core.constant.SysConfConstant;
import com.fingerchar.core.manager.*;
import com.fingerchar.core.util.DappCryptoUtil;
import com.fingerchar.core.util.ResponseUtil;
import com.fingerchar.core.util.TokenExchangeCompute;
import com.fingerchar.db.base.BaseEntity;
import com.fingerchar.db.domain.*;
import com.fingerchar.db.dto.BlindBoxOrder;
import com.fingerchar.db.vo.BlindBlindBoxVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;


@Service
public class BlindBlindBoxService {

    @Autowired
    private IBaseService baseService;

    @Autowired
    BlindPayTokenManager payTokenManager;

    @Autowired
    private BlindBlindBoxManager blindBoxManager;

    @Autowired
    private BlindBlindBoxOrderManager blindBoxOrderManager;

    @Autowired
    private BlindBlindBoxToNftManager boxToNftManager;

    @Autowired
    private FcSystemConfigManager systemConfigManager;


    public IPage<BlindBlindBoxVo> page(String address, String name, IPage<BlindBlindBox> page) {
        QueryWrapper<BlindBlindBox> wrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(address)) {
            wrapper.eq(BlindBlindBox.ADDRESS, address);
        }
        if (!StringUtils.isEmpty(name)) {
            wrapper.like(BlindBlindBox.NAME, name);
        }
        wrapper.eq(BaseEntity.DELETED, false);
        wrapper.orderByDesc(BaseEntity.ID);

        IPage<BlindBlindBox> iPage = baseService.findByPage(BlindBlindBox.class, wrapper, page);
        List<BlindBlindBox> list = iPage.getRecords();
        List<BlindBlindBoxVo> boxVoList = this.blindBoxManager.getBoxInfoList(list);

        IPage<BlindBlindBoxVo> result = new Page<BlindBlindBoxVo>(iPage.getCurrent(), iPage.getSize());
        result.setPages(iPage.getPages());
        result.setRecords(boxVoList);
        result.setTotal(iPage.getTotal());
        return result;
    }


    @Transactional
    public Object add(BlindBlindBox blindBox) {
        Long paytokenId = Long.valueOf(blindBox.getPaytokenId().toString());

        BlindPayToken payToken = this.payTokenManager.get(paytokenId);
        if (null == payToken) {
            return ResponseUtil.fail(-1, "paytoken is not existed");
        }
        blindBox.setPaytokenName(payToken.getName());
        blindBox.setPaytokenSymbol(payToken.getSymbol());
        blindBox.setPaytokenType(payToken.getType());
        blindBox.setPaytokenAddress(payToken.getToken());
        blindBox.setPaytokenTokenId(payToken.getTokenId());
        blindBox.setPaytokenDecimals(payToken.getDecimals());
        blindBox.setPaytokenMetadataContent(payToken.getMetadataContent());

        if (this.blindBoxManager.save(blindBox).equals(0)) {
            return ResponseUtil.fail(-1, "add blind box fail");
        }
        return ResponseUtil.ok();
    }

    @Transactional
    public Object edit(BlindBlindBox blindBox) {
        Long paytokenId = Long.valueOf(blindBox.getPaytokenId().toString());
        BlindPayToken payToken = this.payTokenManager.get(paytokenId);
        if (null == payToken) {
            return ResponseUtil.fail(-1, "paytoken is not existed");
        }

        this.blindBoxManager.clear(blindBox);

        blindBox.setPaytokenName(payToken.getName());
        blindBox.setPaytokenSymbol(payToken.getSymbol());
        blindBox.setPaytokenType(payToken.getType());
        blindBox.setPaytokenAddress(payToken.getToken());
        blindBox.setPaytokenTokenId(payToken.getTokenId());
        blindBox.setPaytokenDecimals(payToken.getDecimals());
        blindBox.setPaytokenMetadataContent(payToken.getMetadataContent());
        blindBox.setIsSync(false);
        this.blindBoxManager.update(blindBox);
        return ResponseUtil.ok();
    }


    @Transactional
    public int updateById(BlindBlindBox blindBox) {
        blindBox.setIsSync(false);
        return baseService.update(blindBox);
    }

    @Transactional
    public Object addNfts(BlindBlindBox box, List<BlindBlindBoxToNft> nftList) {
        this.blindBoxManager.clear(box);
        for (BlindBlindBoxToNft boxToNft : nftList) {
            boxToNft.setBlindBoxId(box.getId());
        }
        this.boxToNftManager.addBatch(nftList);
        box.setIsSync(false);
        this.blindBoxManager.update(box);
        return ResponseUtil.ok();
    }

    @Transactional
    public Integer delete(BlindBlindBox blindBox) {
        return this.blindBoxManager.delete(blindBox);
    }

    public BlindBlindBox get(Long id) {
        QueryWrapper<BlindBlindBox> wrapper = new QueryWrapper<>();
        wrapper.eq(BaseEntity.ID, id)
                .eq(BaseEntity.DELETED, false);
        return baseService.getById(BlindBlindBox.class, id);
    }

    public List<BlindBlindBoxToNft> nfts(Long boxId) {
        List<BlindBlindBoxToNft> list = this.blindBoxManager.nfts(boxId);
        return list;
    }

    public List<BlindBlindBoxToNft> nftsbymulti(List<Long> boxList) {
        return this.blindBoxManager.nftsbymulti(boxList);
    }

    public Object prepare(BlindBlindBox box, List<BlindBlindBoxToNft> boxToNftList, String owner) {
        BlindBlindBoxOrder order = this.blindBoxOrderManager.get(box.getId());
        if (null == order) {
            order = this.blindBoxOrderManager.packBoxOrder(box, boxToNftList);
        }

        String sellFee = this.systemConfigManager.getKeyValue(SysConfConstant.SELLER_FEE);
        if (StringUtils.isEmpty(sellFee)) {
            return ResponseUtil.fail(-1, "Not set sellFee");
        }

        String buyerFee = this.systemConfigManager.getKeyValue(SysConfConstant.BUYER_FEE);
        if (StringUtils.isEmpty(buyerFee)) {
            return ResponseUtil.fail(-1, "Not set buyerFee");
        }

        order.setSellerfee(Integer.valueOf(sellFee));
        order.setBuyerfee(Integer.valueOf(buyerFee));
        order.setOwner(owner);
        order = this.encodeOrder(order);
        return ResponseUtil.ok(order);
    }

    public BlindBlindBoxOrder encodeOrder(BlindBlindBoxOrder order) {
        BlindBoxOrder orderInfo = new BlindBoxOrder(order);
        orderInfo.setSellerFee(order.getSellerfee().toString());

        String buyerValue = this.getErcValue(
                order.getBuyerType(),
                order.getBuying(),
                order.getPaytokenDecimals()
        );
        orderInfo.setBuying(buyerValue);
        String message = DappCryptoUtil.blindBoxSignMessage(orderInfo, orderInfo.getSalt());
        order.setMessage(message);
        return order;
    }

    public Object sign(BlindBlindBoxOrder order) {
        BlindBlindBox box = this.blindBoxManager.get(order.getBlindBoxId());
        if (null == box) {
            return ResponseUtil.fail(-1, "box is not existed");
        }
        if (this.blindBoxManager.sign(order).equals(0)) {
            return ResponseUtil.fail(-1, "add order is fail");
        }
        return ResponseUtil.ok();
    }

    private String getErcValue(
            Integer type,
            String value,
            Integer decimals) {
        ContractType ctype = ContractType.getContractType(type);
        String result = null;
        switch (ctype) {
            case ETH:
            case ERC20:
                result = TokenExchangeCompute.computeErc20OrEth(value, decimals);
                break;
            case ERC1155:
            case ERC721:
            case ERC721Deprecated:
                result = value;
                break;
            default:
                result = null;
        }
        return result;
    }

    public Integer countByCondition(Long time) {
        QueryWrapper<BlindBlindBox> wrapper = new QueryWrapper<>();
        wrapper.eq(BlindBlindBox.DELETED, false)
                .eq(BlindBlindBox.IS_SYNC, true);
        if (null != time) {
            wrapper.gt(BlindBlindBox.CREATE_TIME, time);
        }
        List<BlindBlindBox> boxList = this.baseService.findByCondition(BlindBlindBox.class, wrapper);
        Integer amount = 0;
        for (BlindBlindBox blindBlindBox : boxList) {
            amount += blindBlindBox.getAmount();
        }
        return amount;
    }
}
