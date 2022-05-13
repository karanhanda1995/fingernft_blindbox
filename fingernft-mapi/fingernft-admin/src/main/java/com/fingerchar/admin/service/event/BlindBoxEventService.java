package com.fingerchar.admin.service.event;

import com.fingerchar.core.manager.BlindBlindBoxOrderManager;
import com.fingerchar.core.manager.FcSystemConfigManager;
import com.fingerchar.core.util.DappEventUtils;
import com.fingerchar.core.util.DappWeb3jUtil;
import com.fingerchar.db.dto.BlindOpenLog;
import com.fingerchar.db.dto.ConfigContract;
import com.fingerchar.db.vo.EventValuesExt;
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
public class BlindBoxEventService extends com.fingerchar.admin.service.event.baseEventService {
	
	@Autowired
	FcSystemConfigManager systemConfigManager;

	@Autowired
	BlindBlindBoxOrderManager blindBlindBoxOrderManager;

	@Transactional(rollbackFor = Exception.class)
	public List<EventValuesExt> processEvent(List<EthLog.LogResult> allLogs, Map<BigInteger, EthBlock.Block> blockMap) throws Exception {
		//获取以下事件相关的合约列表
		ConfigContract configContract = this.systemConfigManager.getConfigContract();
		String blindBoxAddress = configContract.getBlindBoxAddress();
		if(StringUtils.isEmpty(blindBoxAddress)){
			throw new Exception("Unset blind address");
		}
		if(!DappWeb3jUtil.isValidAddress(blindBoxAddress)){
			throw new Exception("blind address is invalid");
		}
		//盲盒开启事件
		return this.processOpenEvent(blindBoxAddress, allLogs, blockMap);
	}

	@Transactional(rollbackFor = Exception.class)
	public List<EventValuesExt> processOpenEvent(String address, List<EthLog.LogResult> allLogs, Map<BigInteger, EthBlock.Block> blockMap) {
		//根据事件的合约地址与topic过滤logs
		List<EventValuesExt> eventList = this.getEventList(address, DappEventUtils.BLIND_OPEN_EVENT_TOPIC, DappEventUtils.BLIND_OPEN_EVENT, allLogs, blockMap);
		if(eventList.isEmpty()){
			return eventList;
		}

		for(EventValuesExt valuesExt: eventList){
			this.processOpenEvent(valuesExt);
		}
		return eventList;
	}


	private void processOpenEvent(EventValuesExt eventValues){
		String owner = (String)eventValues.getIndexedValues().get(0).getValue();
		String buyToken = (String) eventValues.getNonIndexedValues().get(0).getValue();
		BigInteger buyTokenId = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
		BigInteger buyValue = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
		String buyer = (String) eventValues.getNonIndexedValues().get(3).getValue();
		BigInteger amount = (BigInteger) eventValues.getNonIndexedValues().get(4).getValue();
		BigInteger salt = (BigInteger) eventValues.getNonIndexedValues().get(5).getValue();

		BlindOpenLog log = new BlindOpenLog(owner, buyToken, buyTokenId, buyValue, buyer, amount, salt, eventValues.getTxHash(), eventValues.getBlockTimestamp());
		blindBlindBoxOrderManager.open(log);
	}



}
