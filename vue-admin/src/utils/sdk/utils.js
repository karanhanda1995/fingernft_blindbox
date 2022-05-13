const truffle_contract = require("@truffle/contract");
import util_web3 from "@/utils/web3/index";
import store from "@/store";

export default {
  async contractAt(abiName, address) {
    try {
      if (!store.state.network.connected) {
        return {
          error: "wallet not connected",
        };
      }
      const abi = require(`./abi/${abiName}.json`);
      const contract = truffle_contract(abi);
      const web3 = util_web3.getWeb3();
      const provider = web3.currentProvider;
      contract.setProvider(provider);
      var lastBlock = await web3.eth.getBlockNumber();

      var gasTracker = store.state.app.config.gasTracker;
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
      if(!address){
        return contract;
      }
      return await contract.at(address);
    } catch (e) {
      return {
        error: e.message,
      };
    }
  },
  async getContract(abiName) {
    return this.contractAt(abiName);
  },
  getAccount() {
    return util_web3.getAccount();
  },
};
