package com.fingerchar.core.manager;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fingerchar.core.base.service.IBaseService;
import com.fingerchar.core.constant.SysConfConstant;
import com.fingerchar.core.util.DappWeb3jUtil;
import com.fingerchar.db.domain.BlindPayToken;
import com.fingerchar.db.vo.ERCTokenInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

/**
 * @Author： Zjm
 * @Date：2022/3/26 15:24
 */
@Service
public class BlindPayTokenManager {
    @Autowired
    IBaseService baseService;

    @Autowired
    FcPayTokenManager payTokenManager;

    @Autowired
    FcSystemConfigManager systemConfigManager;

    public BlindPayToken getPayTokenInfo(BlindPayToken payToken){
        if(payToken.getType().equals(0)){
            // paytoken type == mainnet coin
            String ConfigNetwork = this.systemConfigManager.getKeyValue(SysConfConstant.CONFIG_NETWORK);
            payToken.setDecimals(18);
            if(ConfigNetwork.isEmpty()) {
                payToken.setName("ETH");
                payToken.setSymbol("ETH");
                payToken.setToken(SysConfConstant.ZERO_ADDRESS);
            }else{
                Map<String, Object> networkMap = JSON.parseObject(ConfigNetwork);
                String symbol = (String) networkMap.get("symbol");
                payToken.setName(symbol);
                payToken.setSymbol(symbol);
                payToken.setToken(SysConfConstant.ZERO_ADDRESS);
            }
            return payToken;
        }
        try {
            if(payToken.getType().equals(1)){
                // get erc20 name + symbol + decimals at chain
                ERCTokenInfo info = DappWeb3jUtil.getErc20Info(payToken.getToken());
                payToken.setName(info.getContractName());
                payToken.setDecimals(info.getContractDecimals());
                payToken.setSymbol(info.getContractSymbol());
                return payToken;
            }else if(payToken.getType().equals(2)){
                // get erc1155 uri content at chain
                ERCTokenInfo info = DappWeb3jUtil.getErc1155URI(payToken.getToken(), new BigInteger(payToken.getTokenId()));
                payToken.setName(info.getName());
                payToken.setDecimals(0);
                payToken.setSymbol(info.getName());
                payToken.setMetadataContent(info.getContent());
                return payToken;
            }
        }catch (Exception e){
            return null;
        }
        return null;
    }

    public List<BlindPayToken> all(){
        QueryWrapper<BlindPayToken> wrapper = new QueryWrapper<>();
        wrapper.eq(BlindPayToken.DELETED, false);
        return this.baseService.findByCondition(BlindPayToken.class, wrapper);
    }

    public BlindPayToken get(String address, String tokenId){
        QueryWrapper<BlindPayToken> wrapper = new QueryWrapper<>();
        wrapper.eq(BlindPayToken.TOKEN, address)
                .eq(BlindPayToken.TOKEN_ID, tokenId)
                .eq(BlindPayToken.DELETED, false);
        return this.baseService.getByCondition(BlindPayToken.class, wrapper);
    }

    public BlindPayToken get(Long id){
        return this.baseService.getById(BlindPayToken.class, id);
    }

    public Integer delete(String address, String tokenId){
        BlindPayToken payToken = this.get(address, tokenId);
        if(null == payToken){
            return 0;
        }
        return this.delete(payToken);
    }

    public Integer delete(BlindPayToken payToken){
        payToken.setDeleted(true);
        return this.update(payToken);
    }

    public Integer update(BlindPayToken payToken){
        return this.baseService.update(payToken);
    }

    public Integer save(BlindPayToken payToken){
        return this.baseService.save(payToken);
    }
}
