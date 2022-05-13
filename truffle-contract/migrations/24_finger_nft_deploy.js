var Finger721 = artifacts.require("Finger721");
var CopyERC721 = artifacts.require("CopyERC721");

module.exports = function(deployer) {
  deployer.then(async () => {
    const beneficiary = "";   // wallet address
    const copyERC721 = await CopyERC721.deployed();

    return deployer
      .deploy(
        Finger721,
        "FingerNFTBox",
        "FingerNFTBox",
        copyERC721.address,
        beneficiary,
        "ipfs:/",
        "ipfs:/"
      )
      .then((finger721) => {
        console.log(`Finger721 is deployed at ${finger721.address}`);
      });
  });
};
