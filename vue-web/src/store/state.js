import settings from "@/settings";
export default {
  webLoading: false,
  currentRoute: null,
  currentView: null,
  token: null,
  connected: false,
  language: "English",
  isLogin: false,
  heartbeatTimer: null,
  notice_unread: 0,
  fullscreen: {
    show: false,
    component: "",
  },
  message: {
    total: 0,
    unread: 0,
  },
  config: {},
  gasTracker: null,
  web3: {
    address: null,
    coinbase: null,
    error: null,
    instance: null,
    isInjected: false,
    walletType: "",
    networkId: null,
  },
  ethBalance: "0",
  erc20Balance: {},
  nftBalance: {},
  user: {
    coinbase: "",
    avatar: "",
    brief: "",
    nickname: "",
    shortUrl: "",
    loginType: "",
    bannerUrl: "",
    id: "",
  },
  payTokens: [],
  blindPayTokens: [],
  defalutPayToken: null,
  categorys: [],
  blindBoxSigned: false,
  ...settings,
};