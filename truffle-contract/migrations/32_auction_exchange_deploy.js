var AuctionExchange = artifacts.require("AuctionExchange");
var TransferProxy = artifacts.require("TransferProxy");
var TransferProxyForDeprecated = artifacts.require("TransferProxyForDeprecated");
var ERC20TransferProxy = artifacts.require("ERC20TransferProxy");
var AuctionState = artifacts.require("AuctionState");

module.exports = function(deployer) {
  deployer.then(async () => {
    const beneficiary = ""; // wallet address
    const buyerFeeSigner = "";  // wallet address
    const tranferProxy = await TransferProxy.deployed();
    const transferProxyForDeprecated = await TransferProxyForDeprecated.deployed();
    const erc20TransferProxy = await ERC20TransferProxy.deployed();

    const auctionState = await AuctionState.deployed();
 
    return deployer
      .deploy(
        AuctionExchange,
        tranferProxy.address,
        transferProxyForDeprecated.address,
        erc20TransferProxy.address,
        auctionState.address,
        beneficiary,
        buyerFeeSigner
      )
      .then((exchange) => {
        console.log(`AuctionExchange is deployed at ${exchange.address}`);
      });
  });
};
