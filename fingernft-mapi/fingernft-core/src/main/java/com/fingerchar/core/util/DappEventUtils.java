package com.fingerchar.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.*;
import org.web3j.abi.datatypes.generated.Uint256;

import java.util.Arrays;
import java.util.HashMap;

public class DappEventUtils {

    private static final Logger logger = LoggerFactory.getLogger(DappEventUtils.class);

    public static final Event BUY_EVENT = new Event("Buy",
            Arrays.asList(new TypeReference<Address>(true) {
            }, new TypeReference<Uint256>(true) {
            }, new TypeReference<Uint256>() {
            }, new TypeReference<Address>() {
            }, new TypeReference<Address>() {
            }, new TypeReference<Uint256>() {
            }, new TypeReference<Uint256>() {
            }, new TypeReference<Address>() {
            }, new TypeReference<Uint256>() {
            }, new TypeReference<Uint256>() {
            }));
    public static final Event CANCEL_EVENT = new Event("Cancel",
            Arrays.asList(new TypeReference<Address>(true) {
            }, new TypeReference<Uint256>(true) {
            }, new TypeReference<Address>() {
            }, new TypeReference<Address>() {
            }, new TypeReference<Uint256>() {
            }, new TypeReference<Uint256>() {
            }));

    public static final Event TRANSFER_EVENT = new Event("Transfer",
            Arrays.asList(new TypeReference<Address>(true) {
            }, new TypeReference<Address>(true) {
            }, new TypeReference<Uint256>(true) {
            }));

    public static final Event AUCTION_EVENT = new Event("Auction",
            Arrays.asList(new TypeReference<Address>(true) {
            }, new TypeReference<Uint256>(true) {
            }, new TypeReference<Uint256>(false) {
            }, new TypeReference<Address>(false) {
            }, new TypeReference<Address>(false) {
            }, new TypeReference<Uint256>(false) {
            }, new TypeReference<Uint256>(false) {
            }, new TypeReference<Address>(false) {
            }, new TypeReference<Uint>(false) {
            }, new TypeReference<Uint256>(false) {
            }, new TypeReference<Uint256>(false) {
            }));

    public static final Event AUCTION_CANCEL_EVENT = new Event("Cancel",
            Arrays.asList(new TypeReference<Address>(true) {
            }, new TypeReference<Uint256>(true) {
            }, new TypeReference<Address>(false) {
            }, new TypeReference<Address>(false) {
            }, new TypeReference<Uint256>(false) {
            }, new TypeReference<Uint256>(false) {
            }));

    public static final Event TRANSFERBATCH_EVENT = new Event("TransferBatch",
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {
            }, new TypeReference<Address>(true) {
            }, new TypeReference<Address>(true) {
            }, new TypeReference<DynamicArray<Uint256>>() {
            }, new TypeReference<DynamicArray<Uint256>>() {
            }));

    public static final Event TRANSFERSINGLE_EVENT = new Event("TransferSingle",
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {
            }, new TypeReference<Address>(true) {
            }, new TypeReference<Address>(true) {
            }, new TypeReference<Uint256>() {
            }, new TypeReference<Uint256>() {
            }));

    public static final Event BLIND_OPEN_EVENT = new Event("Open",
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {
            }, new TypeReference<Address>(false) {
            }, new TypeReference<Uint256>(false) {
            }, new TypeReference<Uint256>(false) {
            }, new TypeReference<Address>(false) {
            }, new TypeReference<Uint256>(false) {
            }, new TypeReference<Uint256>(false) {
            }));

    public static final Event BLIND_OPEN_INDEX_EVENT = new Event("OpenIndex",
            Arrays.<TypeReference<?>>asList(
                    new TypeReference<Uint>(false) {}
            ));

    public static final Event SECONDARYSALEFEES_EVENT = new Event("SecondarySaleFees",
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
            }, new TypeReference<DynamicArray<Address>>() {
            }, new TypeReference<DynamicArray<Uint256>>() {
            }));

    public static final Event CREATE_ERC721_EVENT = new Event("CreateERC721",
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {
            }, new TypeReference<Address>(false) {
            }, new TypeReference<Utf8String>(false) {
            }, new TypeReference<Utf8String>(false) {
            }));

    public static final Event CREATE_ERC1155_EVENT = new Event("CreateERC1155",
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {
            }, new TypeReference<Address>(false) {
            }, new TypeReference<Utf8String>(false) {
            }, new TypeReference<Utf8String>(false) {
            }));


    public static final String TRANSFER_TOPIC = EventEncoder.encode(TRANSFER_EVENT);

    public static final String CANCEL_TOPIC = EventEncoder.encode(CANCEL_EVENT);

    public static final String BUY_TOPIC = EventEncoder.encode(BUY_EVENT);

    public static final String AUCTION_TOPIC = EventEncoder.encode(AUCTION_EVENT);

    public static final String AUCTION_CANCEL_TOPIC = EventEncoder.encode(AUCTION_CANCEL_EVENT);

    public static final String BLIND_OPEN_EVENT_TOPIC = EventEncoder.encode(BLIND_OPEN_EVENT);
    public static final String BLIND_OPEN_INDEX_EVENT_TOPIC = EventEncoder.encode(BLIND_OPEN_INDEX_EVENT);

    public static final String TRANSFERBATCH_TOPIC = EventEncoder.encode(TRANSFERBATCH_EVENT);

    public static final String TRANSFERSINGLE_TOPIC = EventEncoder.encode(TRANSFERSINGLE_EVENT);

    public static final String SECONDARYSALEFEES_TOPIC = EventEncoder.encode(SECONDARYSALEFEES_EVENT);

    public static final String CREATE_ERC721_EVENT_TOPIC = EventEncoder.encode(CREATE_ERC721_EVENT);

    public static final String CREATE_ERC1155_EVENT_TOPIC = EventEncoder.encode(CREATE_ERC1155_EVENT);

    @SuppressWarnings("serial")
    public static final HashMap<String, Event> eventMap = new HashMap<String, Event>() {
        {
            put(TRANSFER_TOPIC, TRANSFER_EVENT);
            put(CANCEL_TOPIC, CANCEL_EVENT);
            put(BUY_TOPIC, BUY_EVENT);
            put(AUCTION_TOPIC, AUCTION_EVENT);
            put(AUCTION_CANCEL_TOPIC, AUCTION_CANCEL_EVENT);
            put(BLIND_OPEN_EVENT_TOPIC, BLIND_OPEN_EVENT);
            put(BLIND_OPEN_INDEX_EVENT_TOPIC, BLIND_OPEN_INDEX_EVENT);
            put(TRANSFERBATCH_TOPIC, TRANSFERBATCH_EVENT);
            put(TRANSFERSINGLE_TOPIC, TRANSFERSINGLE_EVENT);
            put(SECONDARYSALEFEES_TOPIC, SECONDARYSALEFEES_EVENT);
            put(CREATE_ERC721_EVENT_TOPIC, CREATE_ERC721_EVENT);
            put(CREATE_ERC1155_EVENT_TOPIC, CREATE_ERC1155_EVENT);
        }
    };

    /*
    private static Web3j web3j = null;

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static List<EventValuesExt> decodeLog(List<LogResult> logList, String topic, Event event) {
        List<EventValuesExt> list = new ArrayList<>();
        if (null != logList && !logList.isEmpty()) {
            logList.stream().forEach(log-> {
                if(((Log) log.get()).getTopics().contains(topic)) {
                    list.add(decodeLog(log, event));
                }
            });
        }
        return list;
    }

    private static EventValuesExt decodeLog(LogResult<Log> logResult, Event event) {
        EventValues eventValues = Contract.staticExtractEventParameters(event, logResult.get());
        EventValuesExt val = new EventValuesExt(eventValues, logResult.get().getTransactionHash(), logResult.get().getAddress(),
                logResult.get().getBlockNumber());
        return val;
    }

    @SuppressWarnings("rawtypes")
    public static List<LogResult> getEthLogs(BigInteger start, BigInteger end) throws InterruptedException, ExecutionException, IOException {
        EthFilter filter = new EthFilter(new DefaultBlockParameterNumber(start), new DefaultBlockParameterNumber(end),
                new ArrayList<>());
        EthLog log = web3j.ethGetLogs(filter).send();
        if(log.hasError()) {
            logger.error(log.getError().getMessage());
            throw new RuntimeException(log.getError().getMessage());
        } else {
            return log.getLogs();
        }
    }


    @SuppressWarnings("rawtypes")
    public static List<LogResult> getEthLogsWithTopics(BigInteger start, BigInteger end, String[] topics) throws InterruptedException, ExecutionException, IOException {
        EthFilter filter = new EthFilter(new DefaultBlockParameterNumber(start), new DefaultBlockParameterNumber(end), new ArrayList<String>());
        filter.addOptionalTopics(topics);
        EthLog log = web3j.ethGetLogs(filter).send();
        if(log.hasError()) {
            logger.error(log.getError().getMessage());
            throw new RuntimeException(log.getError().getMessage());
        } else {
            return log.getLogs();
        }
    }

    @SuppressWarnings("rawtypes")
    public static List<LogResult> getEthLogs(BigInteger start, BigInteger end, String address) throws InterruptedException, ExecutionException, IOException {
        EthFilter filter = new EthFilter(new DefaultBlockParameterNumber(start), new DefaultBlockParameterNumber(end),
                address);
        EthLog log = web3j.ethGetLogs(filter).send();
        if(log.hasError()) {
            logger.error(log.getError().getMessage());
            throw new RuntimeException(log.getError().getMessage());
        } else {
            return log.getLogs();
        }
    }

    public static BigInteger getLastBlock() throws InterruptedException, ExecutionException, IOException {
        EthBlockNumber ebn = web3j.ethBlockNumber().send();
        if(ebn.hasError()) {
            throw new RuntimeException("get block number error");
        } else {
            return ebn.getBlockNumber();
        }
    }

    public static Long getLastTimestamp(Long blockNumber) throws IOException {
        EthBlock block = web3j.ethGetBlockByNumber(new DefaultBlockParameterNumber(blockNumber), false).send();
        if(block.hasError()) {
            throw new RuntimeException("get block time error");
        } else {
            return block.getBlock().getTimestamp().longValue();
        }
    }

    public static void initWeb3j(String url) {
        if (null == web3j) {
            web3j =  Web3j.build(new HttpServiceExt(url));
        }
    }
     */

}

