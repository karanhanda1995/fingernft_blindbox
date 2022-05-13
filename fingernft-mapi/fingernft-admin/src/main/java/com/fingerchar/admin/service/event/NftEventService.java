package com.fingerchar.admin.service.event;

import com.fingerchar.admin.service.BlindTypeService;
import com.fingerchar.admin.service.FcContractService;
import com.fingerchar.db.domain.BlindType;
import com.fingerchar.db.vo.EventValuesExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.EthLog;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class NftEventService extends baseEventService{

    @Autowired
    Erc1155EventService erc1155EventService;

    @Autowired
    Erc721EventService erc721EventService;

    @Autowired
    com.fingerchar.admin.service.event.RoyaltiesEventService royaltiesEventService;

    @Autowired
    FcContractService contractService;

    @Autowired
    BlindTypeService blindTypeService ;

    @Autowired
    CollectionEventService collectionEventService;
    public List<EventValuesExt> processEvent(List<EthLog.LogResult> allLogs, Map<BigInteger, EthBlock.Block> blockMap) throws Exception{
        //获取以下事件相关的合约列表
        List<String> addressList = this.getAllAddr();
        addressList = this.filterInvalidContract(addressList);
        //721 transfer事件
        List<EventValuesExt> list = this.erc721EventService.processTransferEvent(addressList, allLogs, blockMap);

        //1155 transfer single事件
        List<EventValuesExt> list1 = this.erc1155EventService.processTransferSingleEvent(addressList, allLogs, blockMap);
        list.addAll(list1);



        //1155 transfer batch事件
        list1 = this.erc1155EventService.processTransferBatchEvent(addressList, allLogs, blockMap);
        list.addAll(list1);

        //版权设置事件
        list1 = this.royaltiesEventService.processRoyaltiesEvent(addressList, allLogs, blockMap);
        list.addAll(list1);

        //collection创建事件
        list1 = this.collectionEventService.processCollection721Event(addressList, allLogs, blockMap);
        list.addAll(list1);

        list1 = this.collectionEventService.processCollection1155Event(addressList, allLogs, blockMap);
        list.addAll(list1);
        return list;
    }


    private List<String> getAllAddr() {
        List<String> list = this.contractService.findAllAddress();
        List<BlindType> blindTypeList = this.blindTypeService.getAll();
        Set<String> blindAddrs = blindTypeList.stream().map(BlindType::getAddress).collect(Collectors.toSet());
        if(!blindAddrs.isEmpty()) {
            list.addAll(blindAddrs);
        }
        return list;
    }
}
