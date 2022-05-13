import ethABI from 'ethereumjs-abi';
const BN = require('bn.js');
import utils_web3 from "@/util/web3/index";
import truffle_contract from "@truffle/contract";
import store from "@/store";
import constants from './constants'

const SolidityTypes = {
  Address: "address",
  Uint256: "uint256",
  Uint8: "uint8",
  Uint: "uint",
  Bytes: "bytes",
  String: "string"
}

function bigNumberToBN(value){
  return new BN(value.toString(), 10);
}

const assetTypes = [0,1,2,3,4, 5]
function isAssetTypes(assetType){
  for(var i = 0; i < assetTypes.length; i++){
    if(assetTypes[i] == assetType) return true;
  }
  return false;
}

function getOrderKey(asset){
  if(!isAssetTypes(asset.sellType)) return false;
  if(!isAssetTypes(asset.buyType)) return false;
  let orderKey = {
    owner: asset.owner,
    salt: asset.salt,
    sellAsset: {
      token: asset.sellToken,
      tokenId: asset.sellTokenId,
      assetType: asset.sellType,
    },
    buyAsset: {
      token: asset.buyToken,
      tokenId: asset.buyTokenId,
      assetType: asset.buyType,
    }
  }
  return orderKey;
}

function createOrder(asset){
  let orderKey = getOrderKey(asset);
  if(!orderKey) return;
  let order = {
    key: orderKey,
    selling: asset.sellValue,
    buying: asset.buyValue,
    sellerFee: asset.sellerFee,
  }
  return order;
}

function tupleOrderKey(orderKey){
  return [
    orderKey.owner,
    orderKey.salt,
    [
      orderKey.sellAsset.token,
      orderKey.sellAsset.tokenId,
      orderKey.sellAsset.assetType,
    ],[
      orderKey.buyAsset.token,
      orderKey.buyAsset.tokenId,
      orderKey.buyAsset.assetType,
    ]
  ]
}

function tupleOrder(order){
  return [
    tupleOrderKey(order.key),
    order.selling,
    order.buying,
    order.sellerFee
  ]
}

function createAuctionOrder(asset) {
  let orderKey = getOrderKey(asset);
  if (!orderKey) return;
  let order = {
    key: orderKey,
    selling: asset.sellValue,
    buying: asset.buyValue,
    endTime: asset.endTime,
    expiredTime: asset.expiredTime,
    encourage: asset.encourage,
    sellerFee: asset.sellerFee,
  };
  return order;
}

function tupleAuctionOrder(order) {
  return [
    tupleOrderKey(order.key),
    order.selling,
    order.buying,
    order.endTime,
    order.expiredTime,
    order.encourage,
    order.sellerFee,
  ];
}

const blindAssetTypes = [0, 1, 2, 3, 4, 5];
function isBlindAssetTypes(assetType) {
  for (var i = 0; i < blindAssetTypes.length; i++) {
    if (blindAssetTypes[i] == assetType) return true;
  }
  return false;
}

function getBlindOrderKey(asset) {
  let orderKey = {
    owner: asset.owner,
    salt: asset.salt,
    sellAssets: asset.sellAssets,
    buyAsset: {
      token: asset.buyerToken,
      tokenId: asset.buyerTokenId,
      assetType: asset.buyerType,
    },
  };
  return orderKey;
}

function createBlindOrder(asset) {
  let orderKey = getBlindOrderKey(asset);
  if (!orderKey) return;
  let order = {
    key: orderKey,
    opening: asset.opening,
    repeat: asset.repeat,
    startTime: asset.startTime,
    endTime: asset.endTime,
    buying: asset.buying,
    assetAmounts: asset.sellings,
    sellerFee: asset.sellerFee,
    uris: asset.uris,
  };
  return order;
}

function tupleBlindOrderKey(orderKey) {
  let sellAssets = [];
  for (var i = 0; i < orderKey.sellAssets.length; i++) {
    let sellAsset = orderKey.sellAssets[i];
    sellAssets.push([sellAsset.token, sellAsset.tokenId, sellAsset.assetType]);
  }
  return [
    orderKey.owner,
    orderKey.salt,
    sellAssets,
    [
      orderKey.buyAsset.token,
      orderKey.buyAsset.tokenId,
      orderKey.buyAsset.assetType,
    ],
  ];
}

function tupleBlindOrder(order) {
  return [
    tupleBlindOrderKey(order.key),
    order.opening,
    order.repeat,
    order.startTime,
    order.endTime,
    order.buying,
    order.assetAmounts,
    order.sellerFee,
    order.uris,
  ];
}


function generateOrderHash(order){
  let sellA = [
    {value: order.orderkey.sellAsset.token, type: SolidityTypes.Address},
    {value: order.orderkey.sellAsset.tokenId, type: SolidityTypes.Uint},
    {value: order.orderkey.sellAsset.assetType, type: SolidityTypes.Uint8},
  ];
  let sellB = [
    {value: order.orderkey.buyAsset.token, type: SolidityTypes.Address},
    {value: order.orderkey.buyAsset.tokenId, type: SolidityTypes.Uint},
    {value: order.orderkey.buyAsset.assetType, type: SolidityTypes.Uint8},
  ];
  const typesA = _.map(sellA, o => o.type);
  const valuesA = _.map(sellA, o => o.value);
  const hashBufA = ethABI.soliditySHA3(typesA, valuesA);
  const typesB = _.map(sellB, o => o.type);
  const valuesB = _.map(sellB, o => o.value);
  const hashBufB = ethABI.soliditySHA3(typesB, valuesB);
  let orderKey = [
    {value: order.orderkey.owner, type: SolidityTypes.Address},
    {value: bigNumberToBN(order.orderkey.salt), type: SolidityTypes.Uint},
    {value: new Buffer(hashBufA, 'hex'), type: SolidityTypes.Bytes},
    {value: new Buffer(hashBufB, 'hex'), type: SolidityTypes.Bytes},
  ]
  const typesKey = _.map(orderKey, o => o.type);
  const valuesKey = _.map(orderKey, o => o.value);
  const hashBufKey = ethABI.soliditySHA3(typesKey, valuesKey);
  let _order = [
    {value: new Buffer(hashBufKey, 'hex'), type: SolidityTypes.Bytes},
    {value: order.selling, type: SolidityTypes.Uint},
    {value: order.buying, type: SolidityTypes.Uint},
    {value: order.sellerFee, type: SolidityTypes.Uint},
  ];
  const typesOrder = _.map(_order, o => o.type);
  const valuesOrder = _.map(_order, o => o.value);
  const hashBufOrder = ethABI.soliditySHA3(typesOrder, valuesOrder);
  var eth_util = require("ethereumjs-util");
  return eth_util.bufferToHex(hashBufOrder);
}

function contractAbi(type){
  let file = null;
  switch(type){
    case "ERC721":
      file = require('./abi/NFT721.json')
      break;
    case "ERC1155":
      file = require("./abi/NFT1155.json");
      break;
    case "ERC20":
      file = require("./abi/IERC20.json")
      break;
    case "EXCHANGE":
      file = require("./abi/NftExchange.json");
      break;
    case "CreateNFT721":
      file = require("./abi/CustomNFT721.json");
      break;
    case "CreateNFT1155":
      file = require("./abi/CustomNFT1155.json");
      break;
    case "AUCTION":
      file = require("./abi/AuctionExchange.json");
      break;
    case "BLINDBOX":
      file = require("./abi/BlindBox.json");
      break;
    case "AUCTIONS_EXCHANGE":
      file = require("./abi/AuctionExchange.json");
      break;
    case "MULTICALL":
      file = require("./abi/Multicall.json");
      break;
    case "CustomNFT721":
      file = require("./abi/CustomNFT721.json");
      break;
    case "CustomNFT1155":
      file = require("./abi/CustomNFT1155.json");
      break;
    case "NFT":
      file = require("./abi/NFT.json");
      break;
  }
  return file || {};
}

async function contractAt(abi, address, onlyRead){
    let contract = truffle_contract(abi);
    var web3 = utils_web3.getWeb3();
    contract.setProvider(web3.currentProvider);
    try {
      if(!onlyRead){
        var lastBlock = await web3.eth.getBlockNumber();

        var gasTracker = store.state.gasTracker;
        if(gasTracker && 
          parseFloat(gasTracker.lastBlock) > (parseFloat(lastBlock - 50))
        ){
          contract.defaults({
            gasPrice: gasTracker.medium,
          });
        }else{
          var gasPrice = await web3.eth.getGasPrice();
          contract.defaults({
            gasPrice: gasPrice,
          });
        }
      }
      if(address){
        return contract.at(address);
      }else{
        return contract;
      }
    } catch (e) {
      return { error: e.message };
    }
}


export default {
  contractAbi,
  generateOrderHash,
  createOrder,
  getOrderKey,
  createAuctionOrder,
  tupleOrderKey,
  tupleOrder,
  tupleAuctionOrder,

  getBlindOrderKey,
  createBlindOrder,
  tupleBlindOrderKey,
  tupleBlindOrder,

  contractAt,
}

