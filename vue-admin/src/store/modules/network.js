import util_web3 from "@/utils/web3/index.js";
import i18n from "@/i18n/i18n";

import { Notification } from "element-ui";
const network = {
  state: {
    web3: {
      coinbase: null,
      networkId: null,
    },
    connected: false,
  },
  mutations: {
    CONNECT(state, payload) {
      state.web3 = Object.assign({}, state.web3, {
        networkId: payload.networkId,
        coinbase: payload.coinbase,
      });
      state.connected = true;
    },
    DISCONNECT(state) {
      state.connected = false;
      state.web3 = {
        coinbase: null,
        networkId: "",
      };
    },
  },
  actions: {
    connect({ state, commit, dispatch }) {
      return new Promise(async function (resolve, reject) {
        let result = await util_web3.connectWeb3();
        if (result.error) {
          Notification({
            type: "error",
            title: i18n.t("global.fail"),
            message: result.error.message || result.error,
          });
          return resolve();
        }
        commit("CONNECT", result);
        resolve(result);
      });
    },
    disconnect({ state, commit, dispatch }) {
      if (!state.connected) return;
      return new Promise((resolve, reject) => {
        commit("DISCONNECT");
      });
    },
  },
};

export default network;
