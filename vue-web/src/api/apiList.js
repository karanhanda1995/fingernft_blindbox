export default {
  blind: {
    paytoken: {
      list: {
        url: "/blind/paytoken/list",
        method: "post",
      },
    },
    box: {
      list: {
        url: "/blind/box/list",
        method: "post",
      },
      detail: {
        url: "/blind/box/detail",
        method: "post",
      },
      nfts: {
        url: "/blind/box/nfts",
        method: "post",
      },
      history: {
        url: "/blind/box/history",
        method: "post",
      },
    },
    order: {
      get: {
        url: "/blind/order/get",
        method: "post",
      },
      prepare: {
        url: "/blind/order/prepare",
        method: "post",
      },
      buyfee: {
        url: "/blind/order/buyfee",
        method: "post",
      },
      listbyboxid: {
        url: "/blind/order/listbyboxid",
        method: "post",
      },
    },
  },
  home: {
    list: {
      url: "/home/list",
      method: "post",
    },
    search: {
      url: "/home/search",
      method: "post",
    },
    searchuser: {
      url: "/home/searchuser",
      method: "post",
    },
    indexlist: {
      url: "/home/indexlist",
      method: "post",
    },
  },
  config: {
    fetch: {
      url: "/config/fetch",
      method: "post",
    },
    gasTracker: {
      url: "/config/gasTracker",
      method: "post",
    },
  },
  user: {
    setbanner: {
      url: "/user/setbanner",
      method: "post",
    },
    login: {
      url: "/user/login",
      method: "post",
    },
    stat: {
      url: "/user/stat",
      method: "post",
    },
    reload: {
      url: "/user/reload",
      method: "post",
    },
    profile: {
      url: "/user/profile",
      method: "post",
    },
    setprofile: {
      url: "/user/setprofile",
      method: "post",
    },
    info: {
      url: "/user/info",
      method: "post",
    },
    match: {
      url: "/user/match",
      method: "post",
    },
    listbyaddr: {
      url: "/user/listbyaddr",
      method: "post",
    },
    contract: {
      url: "/user/contract",
      method: "post",
    },
    onsales: {
      url: "/user/onsales",
      method: "post",
    },
    collections: {
      url: "/user/collections",
      method: "post",
    },
    listcontract: {
      url: "/user/listcontract",
      method: "post",
    },
    created: {
      url: "/user/created",
      method: "post",
    },
    like: {
      url: "/user/like",
      method: "post",
    },
    following: {
      url: "/user/following",
      method: "post",
    },
    follows: {
      url: "/user/follows",
      method: "post",
    },
    bid: {
      url: "/user/bid",
      method: "post",
    },
  },
  contract: {
    getinfo: {
      url: "/contract/getinfo",
      method: "post",
    },
    list: {
      url: "/contract/list",
      method: "post",
    },
    listbyaddr: {
      url: "/contract/listbyaddr",
      method: "post",
    },
    listitems: {
      url: "/contract/listitems",
      method: "post",
    },
    info: {
      url: "/contract/info",
      method: "post",
    },
    stat: {
      url: "/contract/stat",
      method: "post",
    },
    create: {
      url: "/contract/create",
      method: "post",
    },
    listall: {
      url: "/contract/listall",
      method: "post",
    },
  },
  paytoken: {
    list: {
      url: "/paytoken/list",
      method: "post",
    },
  },
  follow: {
    add: {
      url: "/follow/add",
      method: "post",
    },
    delete: {
      url: "/follow/delete",
      method: "post",
    },
    match: {
      url: "/follow/match",
      method: "post",
    },
    follows: {
      url: "/follow/follows",
      method: "post",
    },
    followers: {
      url: "/follow/followers",
      method: "post",
    },
  },
  notice: {
    read: {
      url: "/notices/read",
      method: "post",
    },
    readall: {
      url: "/notices/readall",
      method: "post",
    },
    count: {
      url: "/notices/count",
      method: "post",
    },
    list: {
      url: "/notices/list",
      method: "post",
    },
    unread: {
      url: "/notices/unread",
      method: "post",
    },
    countunread: {
      url: "/notices/countunread",
      method: "post",
    },
  },
  dapp: {
    sign: {
      url: "/dapp/sign",
      method: "post",
    },
  },
  nft: {
    add: {
      url: "/nft/add",
      method: "post",
    },
    list: {
      url: "/home/list",
      method: "post",
    },
    detail: {
      url: "/nft/detail",
      method: "post",
    },
    owners: {
      url: "/nft/owners",
      method: "post",
    },
    bids: {
      url: "/nft/bids",
      method: "post",
    },
    history: {
      url: "/nft/history",
      method: "post",
    },
    activebids: {
      url: "/nft/activebids",
      method: "post",
    },
    activesales: {
      url: "/nft/activesales",
      method: "post",
    },
    getmedia: {
      url: "/nft/getmedia",
      method: "post",
    },
    getroyalties: {
      url: "/nft/getroyalties",
      method: "post",
    },
  },
  like: {
    add: {
      url: "/like/add",
      method: "post",
    },
    remove: {
      url: "/like/remove",
      method: "post",
    },
    listuserlike: {
      url: "/like/listuserlike",
      method: "post",
    },
  },
  order: {
    submit: {
      url: "/order/submit",
      method: "post",
    },
    list: {
      url: "/order/list",
      method: "post",
    },
    prepare: {
      url: "/order/prepare",
      method: "post",
    },
    buyerFee: {
      url: "/order/buyerFee",
      method: "post",
    },
    add: {
      url: "/order/add",
      method: "post",
    },
    get: {
      url: "/order/get",
      method: "post",
    },
  },
  category: {
    list: {
      url: "/category/list",
      method: "post",
    },
  },
  storage: {
    list: {
      url: "/storage/list",
      method: "post",
    },
    upload: {
      url: "/storage/upload",
      method: "post",
    },
    read: {
      url: "/storage/read",
      method: "post",
    },
    update: {
      url: "/storage/update",
      method: "post",
    },
    delete: {
      url: "/storage/delete",
      method: "post",
    },
    multiupload: {
      url: "/storage/multiupload",
      method: "post",
    },
  },
  auction: {
    list: {
      url: "/auction/list",
      method: "post",
    },
    listbymulti: {
      url: "/auction/activeauctions",
      method: "post",
    },
    prepare: {
      url: "/auction/prepare",
      method: "post",
    },
    add: {
      url: "/auction/add",
      method: "post",
    },
    get: {
      url: "/auction/get",
      method: "post",
    },
    bids: {
      url: "/auction/bids",
      method: "post",
    },
    bid: {
      url: "/auction/bid",
      method: "post",
    },
    cancel: {
      url: "/auction/cancel",
      method: "post",
    },
    edit: {
      url: "/auction/edit",
      method: "post",
    },
    buyerFee: {
      url: "/auction/buyerFee",
      method: "post",
    },
    newlist: {
      url: "/auction/newlist",
      method: "post",
    },
  },
};
