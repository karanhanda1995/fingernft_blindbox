var NFT1155 = artifacts.require("NFT1155");

module.exports = function(deployer) {
  deployer.then(function() {
    const singer = "";   // minter Singer address
    return deployer
      .deploy(NFT1155, "FingerNFT", "FingerNFT", singer, "ipfs:/", "ipfs:/")
      .then(function(token) {
        console.log(`NFT1155 is deployed at ${token.address}`);
      });
  });
};
