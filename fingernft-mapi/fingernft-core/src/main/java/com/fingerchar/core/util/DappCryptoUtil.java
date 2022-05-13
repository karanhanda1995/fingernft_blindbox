package com.fingerchar.core.util;

import com.alibaba.druid.util.StringUtils;
import com.fingerchar.db.dto.BlindBoxOrder;
import com.fingerchar.db.vo.BlindBox;
import com.fingerchar.db.vo.BlindBoxAssets;
import com.fingerchar.db.vo.BlindBoxKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.abi.DefaultFunctionEncoder;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Uint;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.*;
import org.web3j.crypto.Sign.SignatureData;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class DappCryptoUtil {

    public static final Logger logger = LoggerFactory.getLogger(DappCryptoUtil.class);

    public static final String PERSONAL_MESSAGE_PREFIX = "\u0019Ethereum Signed Message:\n";

    /**
     * 验签 不带RSV，需要自己分割
     * @param signature
     * @param message
     * @param address
     * @return
     */
    public static boolean validate(String signature, String message, String address) {
        byte[] signatureBytes = Numeric.hexStringToByteArray(signature);
        byte v = signatureBytes[64];
        if (v < 27) {
            v += 27;
        }
        SignatureData signatureData = new SignatureData(v, Arrays.copyOfRange(signatureBytes, 0, 32), Arrays.copyOfRange(signatureBytes, 32, 64));
        return validate(signatureData, message, address);
    }


    /**
     * 验签  带RSV
     * @param v
     * @param s
     * @param r
     * @param address
     * @param message
     * @return
     */
    public static boolean validate(Integer v, String s, String r, String address, String message) {
        SignatureData signatureData = new SignatureData(v.toString().getBytes(), Numeric.hexStringToByteArray(r), Numeric.hexStringToByteArray(s));
        return validate(signatureData, message, address);
    }

    public static boolean validate(SignatureData signatureData, String message, String address){
        List<String> addressList = recover(signatureData, message);
        if(addressList.isEmpty()){
            return false;
        }
        for(String _address: addressList){
            if(_address.equalsIgnoreCase(address)){
                return true;
            }
        }
        return false;
    }

    public static List<String> recover(SignatureData signatureData, String message){
        if(StringUtils.isEmpty(message)) {
            return new ArrayList<>();
        }
        String prefix = PERSONAL_MESSAGE_PREFIX + message.length();
        byte[] msgHash = Hash.sha3((prefix + message).getBytes());
        String address = null;
        List<String> addressList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            BigInteger publicKey = Sign.recoverFromSignature((byte) i, new ECDSASignature(new BigInteger(1, signatureData.getR()), new BigInteger(1, signatureData.getS())), msgHash);
            if (publicKey != null) {
                address = "0x" + Keys.getAddress(publicKey);
                addressList.add(address);
            }
        }
        return addressList;
    }


    public static SignatureData sign(String message, String privateKey){
        ECKeyPair e = ECKeyPair.create(Numeric.hexStringToByteArray(privateKey));
        String a = Hash.sha3(message);
        byte[] b = Numeric.hexStringToByteArray(a);
        return Sign.signPrefixedMessage(b, e);
    }

    public static String blindBoxSign(BlindBoxOrder order, String blindBoxKey) {
        String salt = order.getSalt();
        if(StringUtils.isEmpty(order.getSalt()) || order.getSalt().equals("0")) {
            throw new RuntimeException("salt为空");
        }
        String signStr = encodeBlindOrder(order, salt);
        signStr = Hash.sha3(signStr);
        logger.info("第一次sha3=>" + signStr);
        signStr = Numeric.cleanHexPrefix(signStr);
        StringBuffer buffer1 = new StringBuffer(signStr);
        StringBuffer buffer2 = new StringBuffer(PERSONAL_MESSAGE_PREFIX);
        buffer2.append(buffer1.length()).append(buffer1);
        signStr = Numeric.toHexString(buffer2.toString().getBytes());
        logger.info("加头部=>" + signStr);
        signStr = Numeric.cleanHexPrefix(signStr);
        signStr = Hash.sha3(signStr);
        logger.info("第二次sha3=>" + signStr);

        ECKeyPair e = ECKeyPair.create(Numeric.hexStringToByteArray(blindBoxKey));
        SignatureData data = Sign.signMessage(Numeric.hexStringToByteArray(signStr), e, false);
        logger.info("v=>" + new BigInteger(data.getV()).toString());
        logger.info("s=>" + Numeric.toHexString(data.getS()));
        logger.info("r=>" + Numeric.toHexString(data.getR()));
        order.setV(new BigInteger(data.getV()).toString());
        order.setS(Numeric.toHexString(data.getS()));
        order.setR(Numeric.toHexString(data.getR()));
        return Numeric.toHexString(data.getR()) + Numeric.cleanHexPrefix(Numeric.toHexString(data.getS())) + Numeric.cleanHexPrefix(Numeric.toHexString(data.getV()));
    }

    public static String blindBoxSignMessage(BlindBoxOrder order, String salt){
        String signStr = encodeBlindOrder(order, salt);
        signStr = Hash.sha3(signStr);
        return signStr;
    }

    private static String encodeBlindOrder(BlindBoxOrder order, String salt) {
        return encodeBlindOrder(order, salt, null);
    }

    @SuppressWarnings("rawtypes")
    private static String encodeBlindOrder(BlindBoxOrder order, String salt, Integer fee) {
        String owner = order.getOwner();
        BigInteger salt1 = new BigInteger(salt);
        List<BlindBoxAssets> sellAsset = order.getSellAssets();
        String buyerToken = order.getBuyerToken();
        BigInteger buyerTokenId = new BigInteger(order.getBuyerTokenId());
        BigInteger buyerAssetType = BigInteger.valueOf(order.getBuyerType());
        BlindBoxAssets buyerAsset = new BlindBoxAssets(buyerToken, buyerTokenId, buyerAssetType);
        BlindBoxKey key = new BlindBoxKey(owner, salt1, sellAsset, buyerAsset);
        BigInteger opening = BigInteger.valueOf(order.getOpening());
        Boolean repeat = order.getRepeat();
        BigInteger startTime = BigInteger.valueOf(order.getStartTime());
        BigInteger endTime = BigInteger.valueOf(order.getEndTime());
        BigInteger buying = new BigInteger(order.getBuying());
        List<Uint> assetAmounts = convertToUint256(order.getSellings());
        List<Utf8String> uris = convertToUtf8String(order.getUris());
        BigInteger sellerFee = new BigInteger(order.getSellerFee());
        BlindBox box = new BlindBox(key, opening, repeat, startTime, endTime, buying, assetAmounts, sellerFee, uris);
        List<Type> typeList = new ArrayList<>();
        typeList.add(box);
        if(null != fee) {
            //buy fee
            Uint uint = new Uint256(new BigInteger(fee.toString()));
            typeList.add(uint);
        }
        DefaultFunctionEncoder encoder = new DefaultFunctionEncoder();
        return encoder.encodeParameters(typeList);
    }

    /**
     * 获取签名盐  77位
     * @return
     */
    public static String getSalt() {
        StringBuilder builder = new StringBuilder();
        Random random = new Random();
        for(int i=0; i<77; i++) {
            if(i == 0) {
                builder.append(random.nextInt(9) + 1);
            } else {
                builder.append(random.nextInt(10));
            }
        }
        return builder.toString();
    }

    /**
     * @param list
     * @return
     */
    private static List<Utf8String> convertToUtf8String(List<String> list) {
        List<Utf8String> result = new ArrayList<>();
        list.stream().forEach(data-> {
            result.add(new Utf8String(data));
        });
        return result;
    }

    private static List<Uint> convertToUint256(List<Long> list) {
        List<Uint> result = new ArrayList<>();
        list.stream().forEach(data-> {
            result.add(new Uint256(BigInteger.valueOf(data)));
        });
        return result;
    }
}
