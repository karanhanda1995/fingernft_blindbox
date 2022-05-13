var Web3 = require("web3");
var config = require("./config.js");
var utils = require("./utils.js");
var abi = require('../build/contracts/OwnableOperatorRole.json');
const HDWalletProvider = require('@truffle/hdwallet-provider');

const provider = new HDWalletProvider({
  privateKeys: [ config.privateKey ],
  providerOrUrl: config.apiUrl,
  chainId: config.chainId,
});


const web3 = new Web3(provider);
const truffle_contract = require('@truffle/contract');
let contract = truffle_contract(abi);
contract.setProvider(web3.currentProvider);


async function getCurrentGasPrice(){
  try{
    return await web3.eth.getGasPrice();
  }catch(e){
    return { error: e.message };
  }
}


async function contractAt(address){
  try{
    var gasPrice = await getCurrentGasPrice();
    if(gasPrice.error) return gasPrice;
    contract.defaults({
      gasPrice: gasPrice,
    });
    return await contract.at(address);
  }catch(e){
    return { error: e.message }
  }
}

async function addOperator(operator, address){
  var _contract = await contractAt(address);
  if(_contract.error) return _contract;
  try{
    return await _contract.addOperator(operator, { "from" : provider.addresses[0]});
  }catch(e){
    return { error: e.message }
  }
}

async function set_operator(){
  console.log("start runing...");
  var address = provider.addresses[0].toLocaleLowerCase();
  var filename = "./" + address + "_" + config.chainId +  ".json";
  var data = await utils.readJsonFile(filename);
  if(!data.transferProxy || !data.transferProxyForDeprecated ||
      !data.erc20TransferProxy || !data.exchangeState || !data.exchange){
    console.log("transferProxy || transferProxyForDeprecated || erc20TransferProxy || exchangeState || exchange contract not deploy");
    process.exit();
  }

  var result = null;

  if(!data.exchange.transferProxyTx){
    console.log("exchange transferProxy operator...");
    result = await addOperator(data.exchange.address, data.transferProxy.address);
    if(result.error){
      console.log("exchange transferProxy operator error", result.error);
      process.exit();
    }
    console.log("add exchange transferProxy operator tx", result.tx);
    data.exchange.transferProxyTx = result.tx;
    await utils.writeJsonFile(filename, data);
  }

  if(!data.exchange.transferProxyForDeprecatedTx){
    console.log("exchange transferProxyForDeprecated operator...");
    result = await addOperator(data.exchange.address, data.transferProxyForDeprecated.address);
    if(result.error){
      console.log("exchange transferProxyForDeprecated operator error", result.error);
      await utils.writeJsonFile(filename, data);
      process.exit();
    }
    console.log("add exchange transferProxyForDeprecated operator tx", result.tx);
    data.exchange.transferProxyForDeprecatedTx = result.tx;
    await utils.writeJsonFile(filename, data);
  }

  if(!data.exchange.erc20TransferProxyTx){
    console.log("exchange erc20TransferProxy operator...");
    result = await addOperator(data.exchange.address, data.erc20TransferProxy.address);
    if(result.error){
      console.log("exchange erc20TransferProxy operator error", result.error);
      await utils.writeJsonFile(filename, data);
      process.exit();
    }
    console.log("add exchange erc20TransferProxy operator tx", result.tx);
    data.exchange.erc20TransferProxyTx = result.tx;
    await utils.writeJsonFile(filename, data);
  }

  if(!data.exchange.exchangeStateTx){
    console.log("exchange exchangeState operator...");
    result = await addOperator(data.exchange.address, data.exchangeState.address);
    if(result.error){
      console.log("exchange exchangeState operator error", result.error);
      await utils.writeJsonFile(filename, data);
      process.exit();
    }
    console.log("add exchange exchangeState operator tx", result.tx);
    data.exchange.exchangeStateTx = result.tx;
    await utils.writeJsonFile(filename, data);
  }

  /*************************************************************/
  /********************* BlindBox Operator *********************/
  /*************************************************************/

  if(!data.blindBox.transferProxyTx){
    console.log("blindBox transferProxy operator...");
    result = await addOperator(data.blindBox.address, data.transferProxy.address);
    if(result.error){
      console.log("blindBox transferProxy operator error", result.error);
      process.exit();
    }
    console.log("add blindBox transferProxy operator tx", result.tx);
    data.blindBox.transferProxyTx = result.tx;
    await utils.writeJsonFile(filename, data);
  }

  if(!data.blindBox.transferProxyForDeprecatedTx){
    console.log("blindBox transferProxyForDeprecated operator...");
    result = await addOperator(data.blindBox.address, data.transferProxyForDeprecated.address);
    if(result.error){
      console.log("blindBox transferProxyForDeprecated operator error", result.error);
      process.exit();
    }
    console.log("add blindBox transferProxyForDeprecated operator tx", result.tx);
    data.blindBox.transferProxyForDeprecatedTx = result.tx;
    await utils.writeJsonFile(filename, data);
  }

  if(!data.blindBox.erc20TransferProxyTx){
    console.log("blindBox erc20TransferProxy operator...");
    result = await addOperator(data.blindBox.address, data.erc20TransferProxy.address);
    if(result.error){
      console.log("blindBox erc20TransferProxy operator error", result.error);
      process.exit();
    }
    console.log("add blindBox erc20TransferProxy operator tx", result.tx);
    data.blindBox.erc20TransferProxyTx = result.tx;
    await utils.writeJsonFile(filename, data);
  }

  if(!data.blindBox.blindStateTx){
    console.log("blindBox blindState operator...");
    result = await addOperator(data.blindBox.address, data.blindState.address);
    if(result.error){
      console.log("blindBox blindState operator error", result.error);
      process.exit();
    }
    console.log("add blindBox blindState operator tx", result.tx);
    data.blindBox.blindStateTx = result.tx;
    await utils.writeJsonFile(filename, data);
  }

  if(!data.blindBox.copyERC721Tx){
    console.log("blindBox copyERC721 operator...");
    result = await addOperator(data.blindBox.address, data.copyERC721.address);
    if(result.error){
      console.log("blindBox copyERC721 operator error", result.error);
      process.exit();
    }
    console.log("add blindBox copyERC721 operator tx", result.tx);
    data.blindBox.copyERC721Tx = result.tx;
    await utils.writeJsonFile(filename, data);
  }


  /*************************************************************/
  /********************* Auction Operator *********************/
  /*************************************************************/

  if(!data.auctionExchange.transferProxyTx){
    console.log("auctionExchange transferProxy operator...");
    result = await addOperator(data.auctionExchange.address, data.transferProxy.address);
    if(result.error){
      console.log("auctionExchange transferProxy operator error", result.error);
      process.exit();
    }
    console.log("add auctionExchange transferProxy operator tx", result.tx);
    data.auctionExchange.transferProxyTx = result.tx;
    await utils.writeJsonFile(filename, data);
  }

  if(!data.auctionExchange.transferProxyForDeprecatedTx){
    console.log("auctionExchange transferProxyForDeprecated operator...");
    result = await addOperator(data.auctionExchange.address, data.transferProxyForDeprecated.address);
    if(result.error){
      console.log("auctionExchange transferProxyForDeprecated operator error", result.error);
      process.exit();
    }
    console.log("add auctionExchange transferProxyForDeprecated operator tx", result.tx);
    data.auctionExchange.transferProxyForDeprecatedTx = result.tx;
    await utils.writeJsonFile(filename, data);
  }

  if(!data.auctionExchange.erc20TransferProxyTx){
    console.log("auctionExchange erc20TransferProxy operator...");
    result = await addOperator(data.auctionExchange.address, data.erc20TransferProxy.address);
    if(result.error){
      console.log("auctionExchange erc20TransferProxy operator error", result.error);
      process.exit();
    }
    console.log("add auctionExchange erc20TransferProxy operator tx", result.tx);
    data.auctionExchange.erc20TransferProxyTx = result.tx;
    await utils.writeJsonFile(filename, data);
  }

  if(!data.auctionExchange.auctionStateTx){
    console.log("auctionExchange auctionState operator...");
    result = await addOperator(data.auctionExchange.address, data.auctionState.address);
    if(result.error){
      console.log("auctionExchange auctionState operator error", result.error);
      process.exit();
    }
    console.log("add auctionExchange auctionState operator tx", result.tx);
    data.auctionExchange.auctionStateTx = result.tx;
    await utils.writeJsonFile(filename, data);
  }

  process.exit();
}

set_operator();


