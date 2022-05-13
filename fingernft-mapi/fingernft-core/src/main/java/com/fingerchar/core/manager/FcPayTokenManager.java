package com.fingerchar.core.manager;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.fingerchar.core.base.service.IBaseService;
import com.fingerchar.core.constant.ContractType;
import com.fingerchar.core.constant.SysConfConstant;
import com.fingerchar.core.util.DappWeb3jUtil;
import com.fingerchar.core.util.TokenExchangeCompute;
import com.fingerchar.db.base.BaseEntity;
import com.fingerchar.db.domain.FcPayToken;
import com.fingerchar.db.vo.ERCTokenInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author： Zjm
 * @Date：2022/3/21 22:21
 */
@Service
public class FcPayTokenManager {
    @Autowired
    IBaseService baseService;

    @Autowired
    FcSystemConfigManager systemConfigManager;

    public List<FcPayToken> all() {
        QueryWrapper<FcPayToken> wrapper = new QueryWrapper<>();
        wrapper.eq(BaseEntity.DELETED, false);
        return this.baseService.findByCondition(FcPayToken.class, wrapper);
    }

    public FcPayToken getPayTokenInfo(FcPayToken payToken){
        if(payToken.getType().equals(ContractType.ETH.getType())){
            // paytoken type == mainnet coin
            String ConfigNetwork = this.systemConfigManager.getKeyValue(SysConfConstant.CONFIG_NETWORK);
            payToken.setDecimals(18);
            if(ConfigNetwork.isEmpty()) {
                payToken.setName("ETH");
                payToken.setSymbol("ETH");
                payToken.setAddress(SysConfConstant.ZERO_ADDRESS);
            }else{
                Map<String, Object> networkMap = JSON.parseObject(ConfigNetwork);
                String symbol = (String) networkMap.get("symbol");
                payToken.setName(symbol);
                payToken.setSymbol(symbol);
                payToken.setAddress(SysConfConstant.ZERO_ADDRESS);
            }
            return payToken;
        }
        try {
            if(payToken.getType().equals(ContractType.ERC20.getType())){
                // get erc20 name + symbol + decimals at chain
                ERCTokenInfo info = DappWeb3jUtil.getErc20Info(payToken.getAddress());
                payToken.setName(info.getContractName());
                payToken.setDecimals(info.getContractDecimals());
                payToken.setSymbol(info.getContractSymbol());
                return payToken;
            }
        }catch (Exception e){
            return null;
        }
        return null;
    }

    public String getErcValue(Integer type, String value, Integer decimals) {
        ContractType ctype = ContractType.getContractType(type);
        String result = null;
        switch(ctype) {
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



    public String getErcValue(Integer type, String value, String address) {
        ContractType ctype = ContractType.getContractType(type);
        String result = null;
        switch(ctype) {
            case ETH:
            case ERC20:
                FcPayToken token = this.get(address);
                if(null == token) {
                    return "";
                }
                result = TokenExchangeCompute.computeErc20OrEth(value, token.getDecimals());
                if(result.indexOf(".") > -1) {
                    // 删除result小数点数据
                    result = result.substring(0, result.indexOf("."));
                }
                break;
            case ERC1155:
                result = value;
                break;
            case ERC721:
                result = value;
                break;
            case ERC721Deprecated:
                result = value;
                break;
            default:
                result = null;
        }
        return result;
    }

    public FcPayToken getETH(){
        QueryWrapper<FcPayToken> wrapper = new QueryWrapper<>();
        wrapper.eq(FcPayToken.TYPE, ContractType.ETH.getType());
        wrapper.eq(FcPayToken.DELETED, false);
        return this.baseService.getByCondition(FcPayToken.class, wrapper);
    }

    public FcPayToken getDefault(){
        QueryWrapper<FcPayToken> wrapper = new QueryWrapper<>();
        wrapper.eq(FcPayToken.IS_DEFAULT, true);
        wrapper.eq(FcPayToken.DELETED, false);
        return this.baseService.getByCondition(FcPayToken.class, wrapper);
    }

    public FcPayToken get(String address){
        QueryWrapper<FcPayToken> wrapper = new QueryWrapper<>();
        wrapper.eq(FcPayToken.ADDRESS, address)
                .eq(FcPayToken.DELETED, false);
        return this.baseService.getByCondition(FcPayToken.class, wrapper);
    }

    public Integer delete(String address){
        UpdateWrapper<FcPayToken> wrapper = new UpdateWrapper<>();
        wrapper.eq(FcPayToken.ADDRESS, address);
        return this.baseService.deleteByCondition(FcPayToken.class, wrapper);
    }

    public Integer update(FcPayToken payToken){
        return this.baseService.update(payToken);
    }

    public Integer save(FcPayToken payToken){
        return this.baseService.save(payToken);
    }
}
