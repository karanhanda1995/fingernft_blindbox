var BlindState = artifacts.require("BlindState");

module.exports = function(deployer) {
  deployer.then(async () => {
    return deployer
      .deploy( BlindState )
      .then((state) => {
        console.log(`BlindState is deployed at ${state.address}`);
      });
  });
};
