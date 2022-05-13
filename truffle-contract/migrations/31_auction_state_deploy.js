var AuctionState = artifacts.require("AuctionState");

module.exports = function(deployer) {
  deployer.then(async () => {
    return deployer
      .deploy( AuctionState )
      .then((state) => {
        console.log(`AuctionState is deployed at ${state.address}`);
      });
  });
};
