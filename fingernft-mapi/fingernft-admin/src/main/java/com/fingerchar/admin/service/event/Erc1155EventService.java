package com.fingerchar.admin.service.event;

import com.fingerchar.core.constant.ContractType;
import com.fingerchar.core.manager.FcContractNftManager;
import com.fingerchar.core.util.DappEventUtils;
import com.fingerchar.db.dto.TransferLog;
import com.fingerchar.db.vo.EventValuesExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.EthLog;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

@Service
public class Erc1155EventService extends baseEventService{
	
	private static final Logger logger = LoggerFactory.getLogger(Erc1155EventService.class);

    @Autowired
    FcContractNftManager contractNftManager;
	
	@Transactional(rollbackFor = Exception.class)
    public List<EventValuesExt> processTransferSingleEvent(
            List<String> addressList,
            List<EthLog.LogResult> allLogs,
            Map<BigInteger, EthBlock.Block> blockMap
    ) throws Exception {
        //根据事件的合约地址与topic过滤logs
        List<EventValuesExt> valueList = this.getEventList(addressList, DappEventUtils.TRANSFERSINGLE_TOPIC, DappEventUtils.TRANSFERSINGLE_EVENT, allLogs, blockMap);
        if (valueList.isEmpty()) {
            return valueList;
        }
        for(EventValuesExt value: valueList){
            this.processTransferSingleEvent(value);
        }
        return valueList;
    }

    /**
     * @param eventValues 日志信息
     * @throws Exception
     */
    private void processTransferSingleEvent(EventValuesExt eventValues) throws Exception {
        logger.info("erc1155单个转移事件");
        String from = (String) eventValues.getIndexedValues().get(1).getValue();
        String to = (String) eventValues.getIndexedValues().get(2).getValue();
        BigInteger tokenId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
        BigInteger quantity = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
        String address = eventValues.getAddress();
        TransferLog log = new TransferLog(eventValues.getAddress(), tokenId, from, to, quantity, eventValues.getTxHash(), eventValues.getBlockTimestamp(), eventValues.getBlockNumber().longValue());

        Integer type = ContractType.ERC1155.getType();

        contractNftManager.transfer(log,  type);
    }

    /**
     * @param addressList 合约地址列表
     * @param allLogs 日志列表
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public List<EventValuesExt> processTransferBatchEvent(
            List<String> addressList,
            List<EthLog.LogResult> allLogs,
            Map<BigInteger, EthBlock.Block> blockMap
    ) throws Exception {
        //根据事件的合约地址与topic过滤logs
        List<EventValuesExt> valueList = this.getEventList(addressList, DappEventUtils.TRANSFERBATCH_TOPIC, DappEventUtils.TRANSFERBATCH_EVENT, allLogs, blockMap);
        if (valueList.isEmpty()) {
            return valueList;
        }

        for(EventValuesExt value: valueList){
            this.processTransferBatchEvent(value);
        }
        return valueList;
    }

    /**
     * @param eventValues 日志信息
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    private void processTransferBatchEvent(EventValuesExt eventValues) throws Exception {

        logger.info("erc1155 transfer batch event");
        String from = (String) eventValues.getIndexedValues().get(1).getValue();
        String to = (String) eventValues.getIndexedValues().get(2).getValue();
        List<Uint256> tokenIdList = (List<Uint256>) eventValues.getNonIndexedValues().get(0).getValue();
        List<Uint256> quantityList = (List<Uint256>) eventValues.getNonIndexedValues().get(1).getValue();
        
        int len = tokenIdList.size();
        for (int i = 0; i < len; i++) {
            this.processTransferBatchEvent(eventValues, from, to, tokenIdList.get(i).getValue(), quantityList.get(i).getValue());
        }
    }

    private void processTransferBatchEvent(EventValuesExt eventValues, String from, String to, BigInteger tokenId, BigInteger quantity) throws Exception {
        logger.info("ERC1155 批量转移事件");
        String address = eventValues.getAddress();

        TransferLog log = new TransferLog(eventValues.getAddress(), tokenId, from, to, quantity, eventValues.getTxHash(), eventValues.getBlockTimestamp(), eventValues.getBlockNumber().longValue());

        Integer type = ContractType.ERC1155.getType();
        contractNftManager.transfer(log, type);
    }
}
