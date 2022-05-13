import store from "@/store";
import utils_web3 from "@/util/web3/index";
const truffle_contract = require("@truffle/contract");
const { ethers } = require("ethers");
import utils from "./utils";

export default {
  async multicallToken(params) {
    var abi = utils.contractAbi("ERC20");
    return await this.multicall(params, abi);
  },
  async multicallNFT(params) {
    var abi = utils.contractAbi("NFT");
    return await this.multicall(params, abi);
  },
  async multicall(params, abi) {
    if (!store.state.config?.contract?.multiCallAddress) {
      return { error: "multiCallAddress is null" };
    }
    let contractAddress = store.state.config.contract.multiCallAddress;
    let multicall_abi = utils.contractAbi("MULTICALL");
    let contract = await utils.contractAt(multicall_abi, contractAddress, true);
    if (contract.error) return contract;
    const itf = new ethers.utils.Interface(abi.abi);
    const calldata = params.map((param) => [
      param.address.toLowerCase(),
      itf.encodeFunctionData(param.name, param.params),
    ]);
    try {
      let { returnData } = await contract.aggregate(calldata);
      const res = returnData.map((param, i) => {
        return itf.decodeFunctionResult(params[i].name, param);
      });
      return res;
    } catch (e) {
      return { error: e.message };
    }
  },
};
