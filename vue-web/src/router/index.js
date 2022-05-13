import { createRouter, createWebHistory } from "vue-router";
import Common from "@/views/Common";
import NFooter from "@/views/NFooter";
import { markRaw, toRaw } from "vue";

import HIndex from "../views/sections/HIndex.vue";
import NFTDetail from "../views/sections/NFTDetail.vue";
import Profile from "@/views/sections/Profile.vue";
import Create from "../views/sections/Create.vue";
import ERC721 from "../views/sections/ERC721.vue";
import ERC1155 from "../views/sections/ERC1155.vue";
import Connect from "../views/sections/Connect.vue"
import Items from "../views/sections/Items.vue"
import Account from "../views/sections/Account.vue"

import Search from "../views/sections/Search.vue"
import Collection from "../views/sections/Collection.vue"

import Message from "../views/sections/Message.vue"
import BlindBox from '@/views/sections/blindbox/Index.vue';
import BlindBoxDetail from '@/views/sections/blindbox/Detail.vue';
import Auctions from '@/views/sections/Auctions.vue';


import NoFound from '@/views/sections/NoFound.vue';

const routes = [
  {
    path: "/",
    name: "Root",
    component: Common,
    children: [
      {
        path: '/',
        name: "home",
        component: HIndex
      },
      {
        path: '/items',
        component: Items,
        name: "items",
        meta: {
          auth: true
        }
      },
      {
        path: '/auctions',
        name: "auctions",
        component: Auctions
      },
      {
        path: '/create',
        name: "create",
        component: Create,
        meta: {
          auth: true
        }
      },
      {
        path: '/create/erc721',
        name: "erc721",
        component: ERC721,
        meta: {
          auth: true
        }
      },
      {
        path: '/create/erc1155',
        name: "erc1155",
        component: ERC1155,
        meta: {
          auth: true
        }
      },
      {
        path: '/profile',
        name: "profile",
        component: Profile,
        meta: {
          auth: true
        }
      },
      {
        path: '/account/:address',
        name: "account",
        component: Account
      },
      {
        path: '/search',
        name: "Search",
        component: Search
      },
      {
        path: '/collection/:address',
        name: "collection",
        component: Collection
      },
      {
        path: '/message',
        name: "message",
        component: Message,
        meta: {
          auth: true
        }
      },
      {
        path: '/blindbox',
        name: "blindbox",
        component: BlindBox
      },
      {
        path: '/404',
        name: "404",
        component: NoFound
      },
    ]
  },
  {
    path: "/blindbox/detail",
    component: NFooter,
    children:[
      {
        path: '/blindbox/detail/:ids',
        name: "blindDetail",
        component: BlindBoxDetail
      },
      {
        path: '/detail/:ids',
        name: "detail",
        component: NFTDetail
      },
    ]
  },
  {
    path: '/connect',
    name: 'connect',
    component: Connect,
  },
  {
    path: "/:pathMatch(.*)",
    name: "NoFound",
    redirect: "/404",
  },
];

const router = createRouter({
  scrollBehavior() {
    document.getElementById('app').scrollIntoView();
  },
  history: createWebHistory(),
  routes
});

export default router;

