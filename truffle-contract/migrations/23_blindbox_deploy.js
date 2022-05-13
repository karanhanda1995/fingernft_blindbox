var BlindBox = artifacts.require("BlindBox");
var TransferProxy = artifacts.require("TransferProxy");
var TransferProxyForDeprecated = artifacts.require("TransferProxyForDeprecated");
var ERC20TransferProxy = artifacts.require("ERC20TransferProxy");
var BlindState = artifacts.require("BlindState");
var CopyERC721 = artifacts.require("CopyERC721");

module.exports = function(deployer) {
  deployer.then(async () => {
    const beneficiary = "";   // wallet address
    const buyerFeeSigner = "";  // wallet address

    const tranferProxy = await TransferProxy.deployed();
    const transferProxyForDeprecated = await TransferProxyForDeprecated.deployed();
    const erc20TransferProxy = await ERC20TransferProxy.deployed();

    const blindState = await BlindState.deployed();
    const copyERC721 = await CopyERC721.deployed();

    return deployer
      .deploy(
        BlindBox,
        tranferProxy.address,
        transferProxyForDeprecated.address,
        erc20TransferProxy.address,
        blindState.address,
        copyERC721.address,
        beneficiary,
        buyerFeeSigner,
      )
      .then((blindbox) => {
        console.log(`BlindBox is deployed at ${blindbox.address}`);
      });
  });
};
