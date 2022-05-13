package com.fingerchar.admin.service;

import com.fingerchar.core.base.service.IBaseService;
import com.fingerchar.core.constant.AuctionStatus;
import com.fingerchar.core.constant.ContractType;
import com.fingerchar.core.constant.NftSellType;
import com.fingerchar.core.manager.*;
import com.fingerchar.db.dao.ext.FcContractNftExtMapper;
import com.fingerchar.db.dao.ext.FcNftItemsExtMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
public class StatService {
    @Autowired
    IBaseService baseService ;

    @Autowired
    FcUserService userService ;

    @Autowired
    FcUserLogService userLogService;

    @Autowired
    FcContractNftExtMapper contractNftExtMapper ;

    @Autowired
    FcNftItemsExtMapper nftItemsExtMapper;

    @Autowired
    FcOrderManager orderManager;

    @Autowired
    FcAuctionOrderManager auctionOrderManager ;

    @Autowired
    BlindBlindBoxOrderManager blindBlindBoxOrderManager ;

    @Autowired
    BlindBlindBoxService blindBlindBoxService ;

    @Transactional(rollbackFor = Exception.class)
    public Map<String ,Integer> stat() {
        Integer userCount = this.userService.count(null);
        Integer nftCountTotal = this.contractNftExtMapper.countByCondition(null, null);
        Integer nftCount1155 = this.contractNftExtMapper.countByCondition(null, ContractType.getAssetType("ERC1155"));
        Integer nftCount721 = this.contractNftExtMapper.countByCondition(null, ContractType.getAssetType("ERC721"));
        Integer nftOnSellCount = this.nftItemsExtMapper.countByCondition(null, 1, null);
        Integer nftOnSaleCount = this.nftItemsExtMapper.countByCondition(null, 1, NftSellType.getSellType("Sale"));
        Integer nftOnAuctionCount = this.nftItemsExtMapper.countByCondition(null, 1, NftSellType.getSellType("Auction"));
        //bid中的nft种类数量
        Integer bidCount = this.orderManager.bidCountByCondition(null);
        //拍卖中的订单个数
        Integer inAuctionCount = this.auctionOrderManager.orderCountByCondition(null, AuctionStatus.getCode("Untreated"));
        //拍卖完成的订单个数
        Integer auctionFinishedCount = this.auctionOrderManager.orderCountByCondition(null, AuctionStatus.getCode("Finished"));
        //拍卖取消的订单个数
        Integer auctionCancelCount = this.auctionOrderManager.orderCountByCondition(null, AuctionStatus.getCode("Cancel"));
        //拍卖流拍的订单个数
        Integer auctionAbortedCount = this.auctionOrderManager.orderCountByCondition(null, AuctionStatus.getCode("Aborted"));
        //盲盒个数
        Integer blindBoxCount = this.blindBlindBoxService.countByCondition(null);
        //开启的盲盒个数
        Integer blindBoxOpenCount = blindBlindBoxOrderManager.countByCondition(null);
        Map<String, Integer> map = new HashMap<>();
        //用户总数
        map.put("userCount",userCount);
        //nft总数
        map.put("nftCountTotal",nftCountTotal);
        //1155类型nft总数
        map.put("nftCount1155",nftCount1155);
        //721类型nft总数
        map.put("nftCount721",nftCount721);
        //onsell中的nft总数
        map.put("nftOnSellCount",nftOnSellCount);
        //sale中的nft总数
        map.put("nftOnSaleCount",nftOnSaleCount);
        //auction中的nft总数
        map.put("nftOnAuctionCount",nftOnAuctionCount);
        //bid中的nft种类数量
        map.put("bidCount",bidCount);
        //拍卖中的订单个数
        map.put("inAuctionCount",inAuctionCount);
        //拍卖完成的订单个数
        map.put("auctionFinishedCount",auctionFinishedCount);
        //拍卖取消的订单个数
        map.put("auctionCancelCount",auctionCancelCount);
        //拍卖流拍的订单个数
        map.put("auctionAbortedCount",auctionAbortedCount);
        //盲盒个数
        map.put("blindBoxCount",blindBoxCount);
        //开启的盲盒个数
        map.put("blindBoxOpenCount",blindBoxOpenCount);
        return map;

    }

    @Transactional(rollbackFor = Exception.class)
    public Map<String ,Integer> statOneDay() {
        Long oneDay = 60*60*24l;
        Long time = System.currentTimeMillis()/1000-oneDay;
        //日活跃人次
        Integer userLoginCount = this.userLogService.countByCondition(time);
        //日活跃人数
        Integer userLoginAmount = this.userLogService.amountByCondition(time);
        Integer oneDayUserCount = this.userService.count(time);
        Integer oneDayNftCountTotal = this.contractNftExtMapper.countByCondition(time, null);
        Integer oneDayNftCount1155 = this.contractNftExtMapper.countByCondition(time, ContractType.getAssetType("ERC1155"));
        Integer oneDayNftCount721 = this.contractNftExtMapper.countByCondition(time, ContractType.getAssetType("ERC721"));
        Integer oneDayNftOnSellCount = this.nftItemsExtMapper.countByCondition(time, 1, null);
        Integer oneDayNftOnSaleCount = this.nftItemsExtMapper.countByCondition(time, 1, NftSellType.getSellType("Sale"));
        Integer oneDayNftOnAuctionCount = this.nftItemsExtMapper.countByCondition(time, 1, NftSellType.getSellType("Auction"));
        //bid中的nft种类数量
        Integer oneDayBidCount = this.orderManager.bidCountByCondition(time);
        //拍卖中的订单个数
        Integer oneDayInAuctionCount = this.auctionOrderManager.orderCountByCondition(time, AuctionStatus.getCode("Untreated"));
        //拍卖完成的订单个数
        Integer oneDayAuctionFinishedCount = this.auctionOrderManager.orderCountByCondition(time, AuctionStatus.getCode("Finished"));
        //拍卖取消的订单个数
        Integer oneDayAuctionCancelCount = this.auctionOrderManager.orderCountByCondition(time, AuctionStatus.getCode("Cancel"));
        //拍卖流拍的订单个数
        Integer oneDayAuctionAbortedCount = this.auctionOrderManager.orderCountByCondition(time, AuctionStatus.getCode("Aborted"));
        //盲盒个数
        Integer oneDayBlindBoxCount = this.blindBlindBoxService.countByCondition(time);
        //开启的盲盒个数
        Integer oneDayBlindBoxOpenCount = blindBlindBoxOrderManager.countByCondition(time);
        Map<String, Integer> map = new HashMap<>();
        map.put("userLoginCount", userLoginCount);
        map.put("userLoginAmount", userLoginAmount);
        map.put("oneDayUserCount",oneDayUserCount);
        map.put("oneDayNftCountTotal",oneDayNftCountTotal);
        map.put("oneDayNftCount1155",oneDayNftCount1155);
        map.put("oneDayNftCount721",oneDayNftCount721);
        map.put("oneDayNftOnSellCount",oneDayNftOnSellCount);
        map.put("oneDayNftOnSaleCount",oneDayNftOnSaleCount);
        map.put("oneDayNftOnAuctionCount",oneDayNftOnAuctionCount);
        map.put("oneDayBidCount",oneDayBidCount);
        map.put("oneDayInAuctionCount",oneDayInAuctionCount);
        map.put("oneDayAuctionFinishedCount",oneDayAuctionFinishedCount);
        map.put("oneDayAuctionCancelCount",oneDayAuctionCancelCount);
        map.put("oneDayAuctionAbortedCount",oneDayAuctionAbortedCount);
        map.put("oneDayBlindBoxCount",oneDayBlindBoxCount);
        map.put("oneDayBlindBoxOpenCount",oneDayBlindBoxOpenCount);
        return map;
    }
}
