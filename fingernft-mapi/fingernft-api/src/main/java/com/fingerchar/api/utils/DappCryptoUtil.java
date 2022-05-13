package com.fingerchar.api.utils;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.fingerchar.api.dto.AuctionOrderInfo;
import com.fingerchar.api.dto.BlindBoxOrder;
import com.fingerchar.api.dto.VSRSignInfo;
import com.fingerchar.api.utils.encryvo.*;
import com.fingerchar.db.dto.SignOrderInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.abi.DefaultFunctionEncoder;
import org.web3j.abi.TypeEncoder;
import org.web3j.abi.datatypes.*;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.crypto.*;
import org.web3j.crypto.Sign.SignatureData;
import org.web3j.utils.Numeric;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class DappCryptoUtil {

	public static final Logger logger = LoggerFactory.getLogger(DappCryptoUtil.class);

	public static final String PERSONAL_MESSAGE_PREFIX = "\u0019Ethereum Signed Message:\n";

	private static String TRANSFER_KEY;
	private static String BUYER_FEE_KEY;
	
	private static String MINT_KEY;

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


	public static VSRSignInfo minerSign(String caddress, String tokenId, String privateKey){
		Type<BigInteger> tId = new Uint(new BigInteger(tokenId));
		String message = caddress + Numeric.cleanHexPrefix(TypeEncoder.encode(tId));
		SignatureData signatureData = sign(message, privateKey);
		VSRSignInfo signInfo = new VSRSignInfo();
		signInfo.setV(new BigInteger(signatureData.getV()).toString());
		signInfo.setS(Numeric.toHexString(signatureData.getS()));
		signInfo.setR(Numeric.toHexString(signatureData.getR()));
		return signInfo;
	}


	public static SignatureData sign(String message, String privateKey){
		ECKeyPair e = ECKeyPair.create(Numeric.hexStringToByteArray(privateKey));
		String a = Hash.sha3(message);
		byte[] b = Numeric.hexStringToByteArray(a);
		return Sign.signPrefixedMessage(b, e);
	}


	/**
	 * order签名
	 * @param order
	 * @return
	 * @throws Exception
	 */
	public static SignOrderInfo prepareOrder(SignOrderInfo order) {
		String salt = order.getSalt();
		if(StringUtils.isEmpty(order.getSalt()) || order.getSalt().equals("0")) {
			salt = getSalt();
		}
        String signStr = encodeOrder(order, salt);
        signStr = Hash.sha3(signStr);
        order.setSignature(signStr);
        order.setSalt(salt);
        return order;
	}

	/**
	 * order签名（带费用）
	 * @param order
	 * @param fee
	 * @return
	 */
	public static SignOrderInfo orderSign(SignOrderInfo order, Integer fee, String privateKey) {
//		String salt = order.getSalt();
//		String message = encodeOrder(order, salt, fee);
//		SignatureData signatureData = sign(message, privateKey);
//		order.setV(new BigInteger(signatureData.getV()).toString());
//		order.setS(Numeric.toHexString(signatureData.getS()));
//		order.setR(Numeric.toHexString(signatureData.getR()));
//		order.setSalt(salt);
//		return order;
		String salt = order.getSalt();
		String signStr = encodeOrder(order, salt, fee);
		logger.info("message=>" + signStr);
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

		ECKeyPair e = ECKeyPair.create(Numeric.hexStringToByteArray(BUYER_FEE_KEY));
		SignatureData data = Sign.signMessage(Numeric.hexStringToByteArray(signStr), e, false);
		logger.info("v=>" + new BigInteger(data.getV()).toString());
		logger.info("s=>" + Numeric.toHexString(data.getS()));
		logger.info("r=>" + Numeric.toHexString(data.getR()));
		order.setV(new BigInteger(data.getV()).toString());
		order.setS(Numeric.toHexString(data.getS()));
		order.setR(Numeric.toHexString(data.getR()));
		order.setSalt(salt);
		return order;
	}
	
	public static BlindBoxOrder blindBoxSign(BlindBoxOrder order) {
		String salt = order.getSalt();
		if(StringUtils.isEmpty(order.getSalt()) || order.getSalt().equals("0")) {
			salt = getSalt();
		}
        String signStr = encodeBlindOrder(order, salt);
        logger.info(signStr);
        signStr = Hash.sha3(signStr);
        logger.info(signStr);
        order.setSignature(signStr);
        order.setSalt(salt);
        return order;
	}
	
	public static BlindBoxOrder blindBoxSign(BlindBoxOrder order, Integer fee) {
		logger.info(JSON.toJSONString(order));
        String salt = order.getSalt();
		String signStr = encodeBlindOrder(order, salt, fee);
		logger.info("message=>" + signStr);
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

		ECKeyPair e = ECKeyPair.create(Numeric.hexStringToByteArray(BUYER_FEE_KEY));
		SignatureData data = Sign.signMessage(Numeric.hexStringToByteArray(signStr), e, false);
		logger.info("v=>" + new BigInteger(data.getV()).toString());
		logger.info("s=>" + Numeric.toHexString(data.getS()));
		logger.info("r=>" + Numeric.toHexString(data.getR()));
		order.setV(new BigInteger(data.getV()).toString());
		order.setS(Numeric.toHexString(data.getS()));
		order.setR(Numeric.toHexString(data.getR()));
		order.setSalt(salt);
		return order;
	}
	
	public static AuctionOrderInfo auctionSign(AuctionOrderInfo order) {
		String salt = order.getSalt();
		if(StringUtils.isEmpty(order.getSalt()) || order.getSalt().equals("0")) {
			salt = getSalt();
		}
		order.setSalt(salt);
        String signStr = encodeauctionOrder(order);
        logger.info(signStr);
        signStr = Hash.sha3(signStr);
        logger.info(signStr);
        //order.setSignature(signStr);
        order.setMessage(signStr);
        order.setSalt(salt);
        return order;
	}
	
	public static AuctionOrderInfo auctionSign(AuctionOrderInfo order, BigInteger buyerValue, String buyer) {
        String signStr = encodeauctionOrder(order, buyerValue, buyer);
        logger.info(signStr);
        signStr = Hash.sha3(signStr);
        logger.info(signStr);
        order.setMessage(signStr);
        //order.setSignature(signStr);
        return order;
	}
	
	public static AuctionOrderInfo auctionSign(AuctionOrderInfo order, Integer fee) {
		logger.info(JSON.toJSONString(order));
		String signStr = encodeauctionOrder(order, new BigInteger(order.getBuyerValue()), order.getBuyer(), fee);
		logger.info("message=>" + signStr);
		System.out.println(signStr);
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

		ECKeyPair e = ECKeyPair.create(Numeric.hexStringToByteArray(BUYER_FEE_KEY));
		SignatureData data = Sign.signMessage(Numeric.hexStringToByteArray(signStr), e, false);
		logger.info("v=>" + new BigInteger(data.getV()).toString());
		logger.info("s=>" + Numeric.toHexString(data.getS()));
		logger.info("r=>" + Numeric.toHexString(data.getR()));
		order.setV(new BigInteger(data.getV()).toString());
		order.setS(Numeric.toHexString(data.getS()));
		order.setR(Numeric.toHexString(data.getR()));
		return order;
	}

	/**
	 * 仿abi.encode
	 * @param order
	 * @param salt
	 * @return
	 */
	private static String encodeOrder(SignOrderInfo order, String salt) {
		return encodeOrder(order, salt, null);
	}

	/**
	 * 仿abi.encode
	 * @param order
	 * @param salt
	 * @param fee
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private static String encodeOrder(SignOrderInfo order, String salt, Integer fee) {
		//owner(address)
		List<Type> typeList = new ArrayList<>();
        Type<String> addr = new Address(order.getOwner());
        typeList.add(addr);
		//salt(uint)
		Type<BigInteger> uint = new Uint256(new BigInteger(salt));
        typeList.add(uint);
		//seller token(address)
        addr = new Address(order.getSellerToken());
        typeList.add(addr);
		//seller tokenId(uint)
        uint = new Uint256(new BigInteger(order.getSellerTokenId()));
        typeList.add(uint);
		//seller assetType(uint8)
        Type<BigInteger> uint8 = new Uint8(new BigInteger(order.getSellerAssetType()));
        typeList.add(uint8);
        //buy token(address)
        addr = new Address(order.getBuyerToken());
        typeList.add(addr);
		//buy tokenId(uint)
        uint = new Uint256(new BigInteger(order.getBuyerTokenId()));
        typeList.add(uint);
		//buy assetType(uint8)
        uint8 = new Uint8(new BigInteger(order.getBuyerAssetType()));
        typeList.add(uint8);
		//selling(uint)
        uint = new Uint256(new BigDecimal(order.getSelling()).toBigInteger());
        typeList.add(uint);
        uint = new Uint256(new BigDecimal(order.getBuying()).toBigInteger());
        typeList.add(uint);
		//sellerFee(uint)
        uint = new Uint256(new BigInteger(order.getSellerFee()));
        typeList.add(uint);
        if(null != fee) {
        	uint = new Uint256(new BigInteger(fee.toString()));
        	typeList.add(uint);
        }
        DefaultFunctionEncoder encoder = new DefaultFunctionEncoder();
        return encoder.encodeParameters(typeList);
	}
	
	private static String encodeBlindOrder(BlindBoxOrder order, String salt) {
		return encodeBlindOrder(order, salt, null);
	}
	
	@SuppressWarnings("rawtypes")
	private static String encodeBlindOrder(BlindBoxOrder order, String salt, Integer fee) {
		String owner = order.getOwner();
		BigInteger salt1 = new BigInteger(salt);
		List<Assets> sellAsset = order.getSellAssets();
		String buyerToken = order.getBuyerToken();
		BigInteger buyerTokenId = new BigInteger(order.getBuyerTokenId());
		BigInteger buyerAssetType = BigInteger.valueOf(order.getBuyerType());
		Assets buyerAsset = new Assets(buyerToken, buyerTokenId, buyerAssetType);
		BlindBoxKey key = new BlindBoxKey(owner, salt1, sellAsset, buyerAsset);
		BigInteger opening = BigInteger.valueOf(order.getOpening());
		Boolean repeat = order.getRepeat();
		BigInteger startTime = BigInteger.valueOf(order.getStartTime());
		BigInteger endTime = BigInteger.valueOf(order.getEndTime());
		BigInteger buying = new BigInteger(order.getBuying());
		List<Uint> assetAmounts = convertToUint256(order.getSellings());
		List<Utf8String> uris= convertToUtf8String(order.getUris());
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
	
	private static String encodeauctionOrder(AuctionOrderInfo order) {
		return encodeauctionOrder(order, null, null, null);
	}
	
	private static String encodeauctionOrder(AuctionOrderInfo order, BigInteger buyerValue, String buyer) {
		return encodeauctionOrder(order, buyerValue, buyer, null);
	}
	
	@SuppressWarnings("rawtypes")
	private static String encodeauctionOrder(AuctionOrderInfo order, BigInteger buyerValue, String buyer, Integer fee) {
		String owner = order.getOwner();
		BigInteger salt1 = new BigInteger(order.getSalt());
		String sellToken = order.getSellToken();
		BigInteger sellTokenId = new BigInteger(order.getSellTokenId());
		BigInteger sellAssetType = BigInteger.valueOf(order.getSellType()) ;
		Assets sellAsset = new Assets(sellToken, sellTokenId, sellAssetType);
		
		String buyerToken = order.getBuyerToken();
		BigInteger buyerTokenId = new BigInteger(order.getBuyerTokenId());
		BigInteger buyerAssetType = BigInteger.valueOf(order.getBuyerType());
		Assets buyerAsset = new Assets(buyerToken, buyerTokenId, buyerAssetType);
		AuctionKey key = new AuctionKey(owner, salt1, sellAsset, buyerAsset);
		BigInteger endTime = BigInteger.valueOf(order.getEndTime());
		BigInteger buying = new BigInteger(order.getBuying());
		BigInteger sellerFee = new BigInteger(order.getSellerFee());
		BigInteger selling = new BigInteger(order.getSellValue());
		BigInteger expiredTime = BigInteger.valueOf(order.getExpiredTime());
		BigInteger encourage = BigInteger.valueOf(order.getEncourage());
		List<Type> typeList = new ArrayList<>();
		Auction auction = new Auction(key, selling, buying, endTime, expiredTime, encourage, sellerFee);
		if(null != buyerValue) {
			if(null != fee) {
				Type<String> addr = new Address(order.getOwner());
				typeList.add(addr);
				//salt(uint)
				Type<BigInteger> uint = new Uint256(new BigInteger(order.getSalt()));
				typeList.add(uint);
				//seller token(address)
				addr = new Address(order.getSellToken());
				typeList.add(addr);
				//seller tokenId(uint)
				uint = new Uint256(new BigInteger(order.getSellTokenId()));
				typeList.add(uint);
				//seller assetType(uint8)
				Type<BigInteger> uint8 = new Uint8( BigInteger.valueOf( order.getSellType()));
				typeList.add(uint8);
				//buy token(address)
				addr = new Address(order.getBuyerToken());
				typeList.add(addr);
				//buy tokenId(uint)
				uint = new Uint256(new BigInteger(order.getBuyerTokenId()));
				typeList.add(uint);
				//buy assetType(uint8)
				uint8 = new Uint8(BigInteger.valueOf(order.getBuyerType()));
				typeList.add(uint8);
				//selling(uint)
				uint = new Uint256(new BigInteger(order.getSellValue()));
				typeList.add(uint);
				uint = new Uint256(new BigInteger(order.getBuying()));
				typeList.add(uint);
				uint = new Uint256(BigInteger.valueOf(order.getEndTime()));
				typeList.add(uint);
				uint = new Uint256(BigInteger.valueOf(order.getExpiredTime()));
				typeList.add(uint);
				uint = new Uint256(BigInteger.valueOf(order.getEncourage()));
				typeList.add(uint);
				//sellerFee(uint)
				uint = new Uint256(new BigInteger(order.getSellerFee()));
				typeList.add(uint);
				BigInteger buyerFee = BigInteger.valueOf(fee);
				//typeList.add(auction);
				Uint uBuyerValue = new Uint(buyerValue);
				typeList.add(uBuyerValue);
				Address aBuyer = new Address(buyer);
				typeList.add(aBuyer);
				DynamicArray<Address> recipientsArr = new DynamicArray<Address>(Address.class, convertToAddress(order.getRecipients()));
				typeList.add(recipientsArr);
				Uint uBuyerFee = new Uint(buyerFee);
				typeList.add(uBuyerFee);
			} else {
				AuctionBid auctionBid = new AuctionBid(auction, buyerValue);
				typeList.add(auctionBid);
			}
		} else {
			typeList.add(auction);
		}
        DefaultFunctionEncoder encoder = new DefaultFunctionEncoder();
        return encoder.encodeParameters(typeList);
	}
	
	/**
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
	
	private static List<Address> convertToAddress(List<String> list) {
		List<Address> result = new ArrayList<>();
		list.stream().forEach(data-> {
			result.add(new Address(data));
		});
		return result;
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

	public static void setTransKey(String transKey) {
		TRANSFER_KEY = transKey;
	}

	public static void setBuyerFeeKey(String buyerFeeKey){
		BUYER_FEE_KEY = buyerFeeKey;
	}

	public static void setMintKey(String mintKey) {
		MINT_KEY = mintKey;
	}

}
