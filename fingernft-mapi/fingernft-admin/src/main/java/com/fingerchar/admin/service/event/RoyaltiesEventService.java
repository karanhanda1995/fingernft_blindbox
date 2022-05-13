package com.fingerchar.admin.service.event;

import com.alibaba.fastjson.JSON;
import com.fingerchar.core.base.service.IBaseService;
import com.fingerchar.core.manager.FcContractNftManager;
import com.fingerchar.core.util.DappEventUtils;
import com.fingerchar.db.vo.EventValuesExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.EthLog;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RoyaltiesEventService extends baseEventService{
	
	@Autowired
	IBaseService baseService;

    @Autowired
    FcContractNftManager contractNftManager;

    @Transactional
    public List<EventValuesExt> processRoyaltiesEvent(List<String> addressList, List<EthLog.LogResult> allLogs, Map<BigInteger, EthBlock.Block> blockMap) {

        //根据事件的合约地址与topic过滤logs
        List<EventValuesExt> voList = this.getEventListByTopic(DappEventUtils.SECONDARYSALEFEES_TOPIC, DappEventUtils.SECONDARYSALEFEES_TOPIC, DappEventUtils.SECONDARYSALEFEES_EVENT, allLogs, blockMap);

        if (voList.isEmpty()) {
            return voList;
        }
        int len = voList.size();
        for (int i = 0; i < len; i++) {
            this.processRoyaltiesEvent(voList.get(i));
        }
        return voList;
    }

    private void processRoyaltiesEvent(EventValuesExt eventValues) {
        BigInteger tokenId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
        @SuppressWarnings("unchecked")
        List<Uint256> bps = (List<Uint256>) eventValues.getNonIndexedValues().get(2).getValue();
        List<Long> bpsTemp = bps.stream().map(bi -> bi.getValue().longValueExact()).collect(Collectors.toList());
        String royalties = JSON.toJSONString(bpsTemp);

        contractNftManager.addRoyalties(eventValues.getAddress(), tokenId.toString(), royalties);
    }
}
