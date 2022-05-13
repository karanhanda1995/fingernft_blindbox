const fs = require("fs");
var Web3 = require("web3");
var config = require("./config.js");
var utils = require("./utils.js");
const HDWalletProvider = require('@truffle/hdwallet-provider');

const provider = new HDWalletProvider({
  privateKeys: [ config.privateKey ],
  providerOrUrl: config.apiUrl,
  chainId: config.chainId,
});
const web3 = new Web3(provider);

const truffle_contract = require('@truffle/contract');

async function getCurrentGasPrice(){
  try{
    return await web3.eth.getGasPrice();
  }catch(e){
    return { error: e.message };
  }
}


async function getContract(abi_name){
  var abi = require(abi_name);
  var contract = truffle_contract(abi);
  contract.setProvider(web3.currentProvider);
  var gasPrice = await getCurrentGasPrice();
  if(gasPrice.error) return gasPrice;
  contract.defaults({
    gasPrice: gasPrice,
  });

  return contract;
}

async function createNFT721(){
  var contract = await getContract("../build/contracts/NFT721.json");
  if(contract.error) return contract;
  try{
    return await contract.new(
      config.NFTName,
      config.NFTName,
      config.miner,
      "ipfs:/",
      "ipfs:/",
      { from: provider.addresses[0] } );
  }catch(e){
    return { error: e.message };
  }
}


async function createNFT1155(){
  var contract = await getContract("../build/contracts/NFT1155.json");
  if(contract.error) return contract;
  try{
    return await contract.new(
      config.NFTName,
      config.NFTName,
      config.miner,
      "ipfs:/",
      "ipfs:/",
      { from: provider.addresses[0] } );
  }catch(e){
    return { error: e.message };
  }
}


async function createTransferProxy(){
  var contract = await getContract("../build/contracts/TransferProxy.json");
  if(contract.error) return contract;

  try{
    return await contract.new( {from: provider.addresses[0] });
  }catch(e){
    return { error: e.message };
  }
}

async function createTransferProxyDeprecated(){
  var contract = await getContract("../build/contracts/TransferProxy.json");
  if(contract.error) return contract;
  try{
    return await contract.new( {from: provider.addresses[0] });
  }catch(e){
    return { error: e.message };
  }
}

async function createERC20TransferProxy(){
  var contract = await getContract("../build/contracts/ERC20TransferProxy.json");
  if(contract.error) return contract;
  try{
    return await contract.new( {from: provider.addresses[0] });
  }catch(e){
    return { error: e.message };
  }
}

async function createExchangeState(){
  var contract = await getContract("../build/contracts/ExchangeState.json");
  if(contract.error) return contract;
  try{
    return await contract.new( {from: provider.addresses[0] });
  }catch(e){
    return { error: e.message };
  }
}

async function createOrderHolder(){
  var contract = await getContract("../build/contracts/ExchangeOrdersHolder.json");
  if(contract.error) return contract;
  try{
    return await contract.new( {from: provider.addresses[0] });
  }catch(e){
    return { error: e.message };
  }

}

async function createExchange(data){
  var contract = await getContract("../build/contracts/NftExchange.json");
  if(contract.error) return contract;
  try{
    return await contract.new(
      data.transferProxy.address,
      data.transferProxyForDeprecated.address,
      data.erc20TransferProxy.address,
      data.exchangeState.address,
      data.exchangeOrderHolder.address,
      config.beneficiary,
      config.buyerFeeSigner,
      {from: provider.addresses[0] });
  }catch(e){
    return { error: e.message };
  }
}


async function createBlindState(){
  var contract = await getContract("../build/contracts/BlindState.json");
  if(contract.error) return contract;
  try{
    return await contract.new( {from: provider.addresses[0] });
  }catch(e){
    return { error: e.message };
  }
}

async function createCopyERC721(){
  var contract = await getContract("../build/contracts/CopyERC721.json");
  if(contract.error) return contract;
  try{
    return await contract.new( {from: provider.addresses[0] });
  }catch(e){
    return { error: e.message };
  }
}


async function createBlindBox(data){
  var contract = await getContract("../build/contracts/BlindBox.json");
  if(contract.error) return contract;
  try{
    return await contract.new(
      data.transferProxy.address,
      data.transferProxyForDeprecated.address,
      data.erc20TransferProxy.address,
      data.blindState.address,
      data.copyERC721.address,
      config.beneficiary,
      config.buyerFeeSigner,
      {from: provider.addresses[0] });
  }catch(e){
    return { error: e.message };
  }
}


async function createAuctionState(){
  var contract = await getContract("../build/contracts/AuctionState.json");
  if(contract.error) return contract;
  try{
    return await contract.new( {from: provider.addresses[0] });
  }catch(e){
    return { error: e.message };
  }
}


async function createAuctionExchange(data){
  var contract = await getContract("../build/contracts/AuctionExchange.json");
  if(contract.error) return contract;
  try{
    return await contract.new(
      data.transferProxy.address,
      data.transferProxyForDeprecated.address,
      data.erc20TransferProxy.address,
      data.auctionState.address,
      config.beneficiary,
      config.buyerFeeSigner,
      {from: provider.addresses[0] });
  }catch(e){
    return { error: e.message };
  }
}


async function deploy(){
  var address = provider.addresses[0].toLocaleLowerCase();
  var filename = "./" + address + "_" + config.chainId +  ".json";
  var data = await utils.readJsonFile(filename);
  console.log("data", data);
  console.log("start deploying...");
  
  var result = null;
  if(!data.NFT721){
    console.log("create NFT721...");
    result = await createNFT721();
    if(result.error){
      console.log("create NFT721 error", result.error);
      process.exit();
    }
    console.log("create NFT721 address:", result.address);
    data.NFT721 = {
      address: result.address
    }
    await utils.writeJsonFile(filename, data);
  }

  if(!data.NFT1155){
    console.log("create NFT1155...");
    result = await createNFT1155();
    if(result.error){
      console.log("create NFT1155 error", result.error);
      process.exit();
    }
    console.log("create NFT1155 address:", result.address);
    data.NFT1155 = {
      address: result.address
    }
    await utils.writeJsonFile(filename, data);
  }



  if(!data.transferProxy){
    console.log("create TransferProxy...");
    result = await createTransferProxy();
    if(result.error){
      console.log("create TransferProxy error", result.error);
      await utils.writeJsonFile(filename, data);
      process.exit();
    }
    console.log("create TransferProxy address:", result.address);
    data.transferProxy = {
      address: result.address,
    };
    await utils.writeJsonFile(filename, data);
  }

  if(!data.transferProxyForDeprecated){
    console.log("create TransferProxyDeprecated...");
    result = await createTransferProxyDeprecated();
    if(result.error){
      console.log("create TransferProxyDeprecated error", result.error);
      await utils.writeJsonFile(filename, data);
      process.exit();
    }
    console.log("create TransferProxyDeprecated address:", result.address);
    data.transferProxyForDeprecated = {
      address: result.address,
    }
    await utils.writeJsonFile(filename, data);
  }

  if(!data.erc20TransferProxy){
    console.log("create ERC20TransferProxy...");
    result = await createERC20TransferProxy();
    if(result.error){
      console.log("create ERC20TransferProxy error", result.error);
      await utils.writeJsonFile(filename, data);
      process.exit();
    }
    console.log("create ERC20TransferProxy address:", result.address);
    data.erc20TransferProxy = {
      address: result.address
    }
    await utils.writeJsonFile(filename, data);
  }
  
  if(!data.exchangeState){
    console.log("create ExchangeState...");
    result = await createExchangeState();
    if(result.error){
      console.log("create ExchangeState error", result.error);
      await utils.writeJsonFile(filename, data);
      process.exit();
    }
    console.log("create ExchangeState address:", result.address);
    data.exchangeState = {
      address: result.address,
    }
    await utils.writeJsonFile(filename, data);
  }

  if(!data.exchangeOrderHolder){
    console.log("create ExchangeOrderHolder...");
    result = await createOrderHolder();
    if(result.error){
      console.log("create ExchangeOrderHolder error", result.error);
      await utils.writeJsonFile(filename, data);
      process.exit();
    }
    console.log("create ExchangeOrderHolder address:", result.address);
    data.exchangeOrderHolder = {
      address: result.address,
    }
    await utils.writeJsonFile(filename, data);
  }

  if(!data.exchange){
    console.log("create Exchange...");
    result = await createExchange(data);
    if(result.error){
      console.log("create Exchange error", result.error);
      await utils.writeJsonFile(filename, data);
      process.exit();
    }
    console.log("create Exchange address:", result.address);
    data.exchange = {
      address: result.address,
    }
    await utils.writeJsonFile(filename, data);
  }

  if(!data.blindState){
    console.log("create BlindState...");
    result = await createBlindState();
    if(result.error){
      console.log("create BlindState error", result.error);
      await utils.writeJsonFile(filename, data);
      process.exit();
    }
    console.log("create BlindState address:", result.address);
    data.blindState = {
      address: result.address
    }
    await utils.writeJsonFile(filename, data);
  }

  if(!data.copyERC721){
    console.log("create CopyERC721...");
    result = await createCopyERC721();
    if(result.error){
      console.log("create CopyERC721 error", result.error);
      await utils.writeJsonFile(filename, data);
      process.exit();
    }
    console.log("create CopyERC721 address:", result.address);
    data.copyERC721 = {
      address: result.address,
    }
    await utils.writeJsonFile(filename, data);
  }
  
  if(!data.blindBox){
    console.log("create BlindBox...");
    result = await createBlindBox(data);
    if(result.error){
      console.log("create BlindBox error", result.error);
      await utils.writeJsonFile(filename, data);
      process.exit();
    }
    console.log("create BlindBox address:", result.address);
    data.blindBox = {
      address: result.address
    }
    await utils.writeJsonFile(filename, data);
  }

  if(!data.auctionState){
    console.log("create AuctionState...");
    result = await createAuctionState();
    if(result.error){
      console.log("create AuctionState error", result.error);
      await utils.writeJsonFile(filename, data);
      process.exit();
    }
    console.log("create AuctionState address:", result.address);
    data.auctionState = {
      address: result.address,
    }
    await utils.writeJsonFile(filename, data);
  }
  
  if(!data.auctionExchange){
    console.log("create AuctionExchange...");
    result = await createAuctionExchange(data);
    if(result.error){
      console.log("create AuctionExchange error", result.error);
      await utils.writeJsonFile(filename, data);
      process.exit();
    }
    console.log("create AuctionExchange address:", result.address);
    data.auctionExchange = {
      address: result.address,
    };
    await utils.writeJsonFile(filename, data);
  }

  process.exit();
}


deploy();


