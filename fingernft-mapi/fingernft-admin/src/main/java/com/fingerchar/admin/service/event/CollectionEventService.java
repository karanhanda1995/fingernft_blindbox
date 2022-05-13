package com.fingerchar.admin.service.event;

import com.fingerchar.core.constant.ContractType;
import com.fingerchar.core.manager.FcContractManager;
import com.fingerchar.core.manager.FcSystemConfigManager;
import com.fingerchar.core.util.DappEventUtils;
import com.fingerchar.db.dto.ConfigDeploy;
import com.fingerchar.db.dto.ContractCreateLog;
import com.fingerchar.db.vo.EventValuesExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.EthLog;

import java.math.BigInteger;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class CollectionEventService extends baseEventService{

	@Autowired
	FcSystemConfigManager systemConfigManager;

	@Autowired
	FcContractManager contractManager;

	@Transactional(rollbackFor = Exception.class)
	public List<EventValuesExt> processCollection721Event(List<String> addressList, List<EthLog.LogResult> allLogs, Map<BigInteger, EthBlock.Block> blockMap) throws Exception {
		//根据事件的合约地址与topic过滤logs
		// List<EventValuesExt> collectionList = this.getEventList(addressList, DappEventUtils.CREATE_ERC721_EVENT_TOPIC, DappEventUtils.CREATE_ERC721_EVENT, allLogs, blockMap);
		List<EventValuesExt> collectionList = this.getEventListByTopic(DappEventUtils.CREATE_ERC721_EVENT_TOPIC, DappEventUtils.CREATE_ERC721_EVENT_TOPIC, DappEventUtils.CREATE_ERC721_EVENT, allLogs, blockMap);

		if (collectionList.isEmpty()) {
            return collectionList;
        }
        int len = collectionList.size();
		Integer type = ContractType.ERC721.getType();
        for (int i = 0; i < len; i++) {
            this.processCollectionEvent(collectionList.get(i), type);
        }
		return collectionList;
	}




	@Transactional(rollbackFor = Exception.class)
	public List<EventValuesExt> processCollection1155Event(List<String> addressList, List<EthLog.LogResult> allLogs, Map<BigInteger, EthBlock.Block> blockMap) throws Exception {
		//根据事件的合约地址与topic过滤logs
		// List<EventValuesExt> collectionList = this.getEventList(addressList, DappEventUtils.CREATE_ERC1155_EVENT_TOPIC, DappEventUtils.CREATE_ERC1155_EVENT, allLogs, blockMap);

		List<EventValuesExt> collectionList = this.getEventListByTopic(DappEventUtils.CREATE_ERC1155_EVENT_TOPIC, DappEventUtils.CREATE_ERC1155_EVENT_TOPIC, DappEventUtils.CREATE_ERC1155_EVENT, allLogs, blockMap);

		if (collectionList.isEmpty()) {
            return collectionList;
        }
        int len = collectionList.size();
		Integer type = ContractType.ERC1155.getType();
        for (int i = 0; i < len; i++) {
            this.processCollectionEvent(collectionList.get(i), type);
        }
		return collectionList;
	}

    private void processCollectionEvent(EventValuesExt eventValues, Integer type) throws Exception {
		String owner = (String) eventValues.getIndexedValues().get(0).getValue();
		String signer = (String) eventValues.getNonIndexedValues().get(0).getValue();
		String name = (String) eventValues.getNonIndexedValues().get(1).getValue();
		String symbol = (String) eventValues.getNonIndexedValues().get(2).getValue();
		String address = eventValues.getAddress();
		signer = signer.toLowerCase(Locale.ROOT);

		ConfigDeploy configDeploy = this.systemConfigManager.getConfigDeploy();
		String miner = configDeploy.getMinerAddress();
		if (miner.isEmpty()) {
			throw new Exception("miner is not set");
		}
		if (!signer.equalsIgnoreCase(miner)) {
			return;
		}

		ContractCreateLog log = new ContractCreateLog(address, owner, signer, name, symbol, type, eventValues.getTxHash(), eventValues.getBlockTimestamp());
		this.contractManager.addSyncContract(log);
	}
}
