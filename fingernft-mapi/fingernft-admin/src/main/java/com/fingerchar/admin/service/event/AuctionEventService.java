package com.fingerchar.admin.service.event;

import com.fingerchar.core.manager.FcAuctionOrderManager;
import com.fingerchar.core.manager.FcSystemConfigManager;
import com.fingerchar.core.util.DappEventUtils;
import com.fingerchar.core.util.DappWeb3jUtil;
import com.fingerchar.db.dto.AuctionBuyLog;
import com.fingerchar.db.dto.AuctionCancelLog;
import com.fingerchar.db.dto.ConfigContract;
import com.fingerchar.db.vo.EventValuesExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.EthLog;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

@Service
public class AuctionEventService extends baseEventService {
	
	private static final Logger logger = LoggerFactory.getLogger(AuctionEventService.class);

    @Autowired
    FcSystemConfigManager systemConfigManager;

    @Autowired
    FcAuctionOrderManager auctionOrderManager;

    @Transactional(rollbackFor = Exception.class)
    public List<EventValuesExt> processEvent(List<EthLog.LogResult> allLogs, Map<BigInteger, EthBlock.Block> blockMap) throws Exception {
        //获取以下事件相关的合约列表
        ConfigContract configContract = this.systemConfigManager.getConfigContract();
        String auctionAddress = configContract.getAuctionExchangeAddress();
        if(StringUtils.isEmpty(auctionAddress)){
            throw new Exception("Unset auction address");
        }
        if(!DappWeb3jUtil.isValidAddress(auctionAddress)){
            throw new Exception("auction address is unvalid");
        }
        //拍卖事件
        List<EventValuesExt> list = this.processAuctionEvent(auctionAddress, allLogs, blockMap);
        //拍卖取消事件
        List<EventValuesExt> list1 = this.processAuctionCancelEvent(auctionAddress, allLogs, blockMap);
        list.addAll(list1);
        return list;
    }


    @Transactional(rollbackFor = Exception.class)
    public List<EventValuesExt> processAuctionEvent(String address, List<EthLog.LogResult> allLogs, Map<BigInteger, EthBlock.Block> blockMap) throws Exception {
        //根据事件的合约地址与topic过滤logs
        List<EventValuesExt> auctionList = this.getEventList(address, DappEventUtils.AUCTION_TOPIC, DappEventUtils.AUCTION_EVENT, allLogs,blockMap);

        if (auctionList.isEmpty()) {
            return auctionList;
        }
        int len = auctionList.size();
        for (int i = 0; i < len; i++) {
            this.processAuctionEvent(auctionList.get(i));
        }
        return auctionList;
    }


    /**
     * @param eventValues 日志信息
     * @throws Exception
     */
    private void processAuctionEvent(EventValuesExt eventValues) throws Exception {
        String txHash = eventValues.getTxHash();
        String sellToken = (String) eventValues.getIndexedValues().get(0).getValue();
        BigInteger sellTokenId = (BigInteger) eventValues.getIndexedValues().get(1).getValue();
        BigInteger sellValue = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
        String owner = (String) eventValues.getNonIndexedValues().get(1).getValue();
        String buyToken = (String) eventValues.getNonIndexedValues().get(2).getValue();
        BigInteger buyTokenId = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
        BigInteger buyValue = (BigInteger) eventValues.getNonIndexedValues().get(4).getValue();
        String buyer = (String) eventValues.getNonIndexedValues().get(5).getValue();
        //BigInteger endTime = (BigInteger) eventValues.getNonIndexedValues().get(6).getValue();
        BigInteger buying = (BigInteger) eventValues.getNonIndexedValues().get(7).getValue();
        BigInteger salt = (BigInteger) eventValues.getNonIndexedValues().get(8).getValue();

        AuctionBuyLog log = new AuctionBuyLog(sellToken, sellTokenId, sellValue, owner, buyToken, buyTokenId, buyValue, buyer, buying, salt, eventValues.getTxHash(), eventValues.getBlockTimestamp());
        this.auctionOrderManager.buy(log);
    }
    
    /**
     * 处理取消拍卖
     *
     * @param address 拍卖合约地址
     * @param allLogs 日志列表
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public List<EventValuesExt> processAuctionCancelEvent(String address, List<EthLog.LogResult> allLogs, Map<BigInteger, EthBlock.Block> blockMap) throws Exception {
        //根据事件的合约地址与topic过滤logs
        List<EventValuesExt> auctionList = this.getEventList(address, DappEventUtils.AUCTION_CANCEL_TOPIC, DappEventUtils.AUCTION_CANCEL_EVENT, allLogs, blockMap);

        if (auctionList.isEmpty()) {
            return auctionList;
        }
        int len = auctionList.size();
        for (int i = 0; i < len; i++) {
            this.processAuctionCancelEvent(auctionList.get(i));
        }
        return auctionList;
    }

    /**
     * 具体处理取消拍卖
     *
     * @param eventValues
     * @throws Exception
     */
    private void processAuctionCancelEvent(EventValuesExt eventValues) throws Exception {
        String txHash = eventValues.getTxHash();
        String sellToken = (String) eventValues.getIndexedValues().get(0).getValue();
        BigInteger sellTokenId = (BigInteger) eventValues.getIndexedValues().get(1).getValue();
        String owner = (String) eventValues.getNonIndexedValues().get(0).getValue();
        String buyToken = (String) eventValues.getNonIndexedValues().get(1).getValue();
        BigInteger buyTokenId = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
        BigInteger salt = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();

        AuctionCancelLog log = new AuctionCancelLog(sellToken, sellTokenId, owner, buyToken, buyTokenId, salt, eventValues.getTxHash(), eventValues.getBlockTimestamp());
        this.auctionOrderManager.cancel(log);
    }

}
