var CopyERC721 = artifacts.require("CopyERC721");

module.exports = function(deployer) {
  deployer.then(async () => {
    return deployer
      .deploy(CopyERC721)
      .then((state) => {
        console.log(`CopyERC721 is deployed at ${state.address}`);
      });
  });
};
