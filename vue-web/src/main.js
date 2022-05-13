import { createApp } from "vue";
import App from "./App.vue";
import router from "./router";
import i18n from "./i18n/i18n.js";
import store from "./store";
import "lib-flexible/flexible";
import api from "@/api/index.js";
import tools from "@/util/tools.js";
import web3 from "@/util/web3/index.js";
import NoContent from "@/components/NoContent";
import NFTItem from "@/components/NFTItem";
import NFTItemLoad from "@/components/loading/NFTItemLoad";
import FollowLoad from "@/components/loading/FollowLoad";
import LoadStatus from "@/components/LoadStatus";
import Avatar from "@/components/Avatar";
import sdk from "@/util/sdk/index.js";
import Window from "@/components/Window.vue";


import ProfilePopover from "@/components/ProfilePopover";
import ProfileDialog from "@/components/ProfileDialog";
import Placeholder from "@/components/Placeholder";

import SaleDialog from "@/components/dialogs/Sale";
import CancelSaleDialog from "@/components/dialogs/CancelSale";
import BuyDialog from "@/components/dialogs/Buy";

import BidDialog from "@/components/dialogs/Bid";
import CancelBidDialog from "@/components/dialogs/CancelBid";
import AcceptDialog from "@/components/dialogs/Accept";

import TransferDialog from "@/components/dialogs/Transfer";
import BurnDialog from "@/components/dialogs/Burn";
import OpenDialog from "@/components/dialogs/Open";
import AuctionCountDown from "@/components/AuctionCountDown";
import ActivityTimeCount from "@/components/ActivityTimeCount";
import NoFound from "@/components/NoFound";

import SaleTypeDialog from "@/components/dialogs/SaleType";
import AuctionDialog from "@/components/dialogs/Auction";
import AuctionDetailDialog from "@/components/dialogs/AuctionDetail";
import AuctionBidDialog from "@/components/dialogs/AuctionBid";
import AuctionCancelDialog from "@/components/dialogs/AuctionCancel";
import AuctionBuyDialog from "@/components/dialogs/AuctionBuy";
import AuctionAcceptDialog from "@/components/dialogs/AuctionAccept";
import CollectionDialog from "@/components/dialogs/Collection";

import "@/styles/myicon/iconfont.css";
import "element-plus/dist/index.css";

import { VueClipboard } from "@soerenmartius/vue3-clipboard";
import * as filters from "@/filters";

import ElementPlus from "element-plus";
import "./permission";
import "../theme/index.css";

import "@/styles/index.scss";

import "../static/font/font.css";

const app = createApp(App); // 创建实例
app.config.globalProperties.$web3 = web3;
app.config.globalProperties.$api = api;
app.config.globalProperties.$tools = tools;
app.config.globalProperties.$sdk = sdk;
app.config.globalProperties.$filters = filters;

app.use(VueClipboard);
app.use(ElementPlus);

app.component("Avatar", Avatar);
app.component("popup-window", Window);
app.component("no-content", NoContent);
app.component("nft-item", NFTItem);
app.component("nft-item-load", NFTItemLoad);
app.component("follow-load", FollowLoad);
app.component("load-status", LoadStatus);
app.component("sale-dialog", SaleDialog);
app.component("cancel-sale-dialog", CancelSaleDialog);
app.component("buy-dialog", BuyDialog);
app.component("bid-dialog", BidDialog);
app.component("cancel-bid-dialog", CancelBidDialog);
app.component("accept-dialog", AcceptDialog);
app.component("transfer-dialog", TransferDialog);
app.component("burn-dialog", BurnDialog);
app.component("open-dialog", OpenDialog);
app.component("profile-popover", ProfilePopover);
app.component("profile-dialog", ProfileDialog);

app.component("placeholder", Placeholder);

app.component("auction-count-down", AuctionCountDown);
app.component("activity-time-count", ActivityTimeCount);
app.component("no-found", NoFound);
app.component("sale-type-dialog", SaleTypeDialog);
app.component("auction-dialog", AuctionDialog);
app.component("auction-bid-dialog", AuctionBidDialog);
app.component("auction-detail-dialog", AuctionDetailDialog);
app.component("auction-cancel-dialog", AuctionCancelDialog);
app.component("auction-buy-dialog", AuctionBuyDialog);
app.component("auction-accept-dialog", AuctionAcceptDialog);
app.component("collection-dialog", CollectionDialog);

app
  .use(store)
  .use(router)
  .use(i18n)
  .mount("#app");

export default app;
