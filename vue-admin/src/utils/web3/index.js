import Web3 from "web3";
import store from "@/store";
import i18n from "@/i18n/i18n";
import tools from "@/utils/tools.js";
import { Notification } from "element-ui";

const promisify = (inner) =>
  new Promise((resolve, reject) =>
    inner((err, res) => {
      if (err) {
        reject(err);
      } else {
        resolve(res);
      }
    })
  );

export default {
  checkNetwork() {
    const web3 = this.getWeb3();
    let ret = false;
    let config = {};
    if (!store.state.network.connected) {
      Notification({
        type: "error",
        message: i18n.t("global.walletNotConnected"),
      });
      return ret;
    }

    if (store.state.app.config && store.state.app.config.configNetwork) {
      config = store.state.app.config.configNetwork;
      ret = parseInt(config.chainId) === parseInt(web3.currentProvider.chainId);
    }
    if (!ret) {
      Notification({
        type: "error",
        title: i18n.t("global.errNetwork"),
        message: i18n.t("global.changeNetworkTo") + config.name,
      });
    }
    return ret;
  },
  getAccount() {
    return store.state.network.web3.coinbase;
  },
  async connectWeb3() {
    var error = "";
    if (window.ethereum) {
      try {
        var t = await window.ethereum.request({
          method: "eth_requestAccounts",
        });
        if (!t) {
          error = "MetaMask enable Error";
          return { error };
        }
        var web3 = new Web3(window.ethereum);
        window.wallet = web3;
        var networkId = await promisify((cb) => web3.eth.getChainId(cb));
        var coinbase = await promisify((cb) => web3.eth.getCoinbase(cb));

        window.ethereum.once("accountsChanged", this.accountsChanged);
        window.ethereum.on("chainChanged", this.chainChanged);
        window.ethereum.on("disconnect", this.disconnect);
        let walletType = "metamask";
        return { networkId, coinbase, walletType };
      } catch (e) {
        error = e.message;
      }
    } else {
      error = "MetaMask not Install";
    }
    return { error };
  },
  accountsChanged(accounts) {
    if (!store.state.network.connected) return;
    store.dispatch("disconnect");
    if (accounts.length) {
      store.dispatch("connect");
    }
  },
  chainChanged(channelId) {
    let config = {};
    if (store.state.app.config && store.state.app.config.configNetwork) {
      config = store.state.app.config.configNetwork;
    }
    if (parseInt(channelId) != parseInt(config.networkId)) {
      tools.messageBox(
        i18n.t("global.errNetwork"),
        i18n.t("global.changeNetworkTo") + config.name
      );
    }
  },
  disconnect(error) {
    if (!store.state.network.connected) return;
    store.dispatch("disconnect");
  },
  async getTransaction(tx) {
    let web3 = this.getWeb3();
    try {
      return await promisify((cb) => web3.eth.getTransaction(tx, cb));
    } catch (e) {
      return { error: e.message };
    }
  },
  async getTransactionReceipt(tx) {
    let web3 = this.getWeb3();
    try {
      return await promisify((cb) => web3.eth.getTransactionReceipt(tx, cb));
    } catch (e) {
      return { error: e.message };
    }
  },
  async decodeLog(inputs, hexString, options) {
    let web3 = this.getWeb3();
    try {
      return await promisify((cb) =>
        web3.eth.abi.decodeLog(inputs, hexString, options, cb)
      );
    } catch (e) {
      return { error: e.message };
    }
  },
  getWeb3() {
    return window.wallet;
  },
  async sign(message, address) {
    var web3 = window.wallet;
    try {
      address = web3.utils.toChecksumAddress(address);
      var signature = await promisify((cb) =>
        web3.eth.personal.sign(message, address, cb)
      );
      return signature;
    } catch (e) {
      return { error: e.message };
    }
  },
  async checkWeb3(web3) {
    if (window.ethereum) {
      try {
        var result = window.ethereum.isConnected();
        if (!result) return false;
        return true;
      } catch (e) {
        console.log("checkWeb3 error", e);
      }
    }
    return false;
  },
};
