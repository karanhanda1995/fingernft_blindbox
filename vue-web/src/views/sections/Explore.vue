<template>
  <div>
    <div class="explore-main">
      <div class="explore-filter" :class="filterHidden?'dis-block':''">
        <div class="explore-filter-top">
          <span class="iconfont bfont icon-nav-list"></span>{{$t('explore.filter')}}
        </div>
        <div class="explore-filter-item c-pointer" @click="priceClick">
          <div>{{$t('explore.price')}}</div><span v-if="!filterPrice" class="iconfont bfont icon-arrow-down"></span><span v-else class="iconfont bfont icon-arrow-up"></span>
        </div>
        <div class="explore-filter-detail" v-if="filterPrice">
          <div class="explore-filter-priceItem">
            <el-select class="explore-select" v-model="goodsQuery.payToken" placeholder="Select">
              <el-option v-for="item in payTokens" :key="item.symbol" :label="item.symbol" :value="item.address">
              </el-option>
            </el-select>
          </div>
          <div class="explore-filter-priceItem">
            <input type="text" class="price" v-model="goodsQuery.minPrice" :placeholder="$t('explore.min')" /> to <input type="text" class="price" v-model="goodsQuery.maxPrice"
              :placeholder="$t('explore.max')" />
          </div>
          <button @click="filterPriceApply()" class="explore-filter-priceBtn c-pointer">{{$t('explore.apply')}}</button>
        </div>
        <div class="explore-filter-item c-pointer" @click="saleTypeClick">
          <div>{{$t('explore.saleType')}}</div><span v-if="!filterSale" class="iconfont bfont icon-arrow-down"></span><span v-else class="iconfont bfont icon-arrow-up"></span>
        </div>
        <div class="explore-filter-detail" v-if="filterSale">
          <button @click="handleSale(2)" class="explore-filter-status c-pointer mb-10" :class="filterSaleType==2?'active':''">{{$t('explore.timedAuction')}}</button>
          <button @click="handleSale(1)" class="explore-filter-status ml-5 c-pointer mb-10" :class="filterSaleType==1?'active':''">{{$t('explore.fixedPrice')}}</button>
          <button @click="handleSale(3)" class="explore-filter-status c-pointer" :class="filterSaleType==3?'active':''">{{$t('explore.nfs')}}</button>
        </div>
        <div class="explore-filter-item c-pointer" @click="collectionsClick">
          <div>{{$t('explore.collections')}}</div><span v-if="!filterCollections" class="iconfont bfont icon-arrow-down"></span><span v-else class="iconfont bfont icon-arrow-up"></span>
        </div>
        <div class="explore-filter-detail" v-if="filterCollections">
          <div v-infinite-scroll="loadCollectionsList">
            <el-scrollbar style="height:150px;">
              <template v-for="(item, index) in collections" :key="index">
                <div class="collection-list" @click="lookCollection(item)">
                  <avatar class="bflex cpointer" :imageUrl="$filters.fullImageUrl(item.cover)" :address="item.owner" :imgWidth="20" :imgHeight="20" shape="circular"></avatar>
                  <span style="margin-left:10px;">{{item.name}}</span>
                </div>
              </template>
            </el-scrollbar>
          </div>
        </div>
      </div>
      <div class="explore-content">
        <div class="head-box flex justify-between">
          <div class="blind-box">{{$t('explore.explore')}}
            <div class="explore-content-top" @click="filterClick">
              <span class="iconfont bfont icon-nav-list"></span>{{$t('explore.filter')}}
            </div>
          </div>
          <div class="line"></div>
          <div class="filter">
            <div class="result-num">{{total}} {{$t('explore.results')}}</div>
            <div class="filter-box">
              <div class="items">
                <input type="text" v-model="goodsQuery.search" placeholder="Search" />
                <div class="items-btn" @click="getGoodsList()" @keyup.enter="getGoodsList()">
                  <el-icon size="20" color="#fff" class="el-icon-search c-pointer"></el-icon>
                </div>
              </div>
              <div class="filter-box-right">
                <div class="items">
                  <input type="text" disabled placeholder="Price" />
                  <div class="items-btn" @click="getGoodsList('usdt_price')">
                    <el-icon size="20" color="#fff" class="el-icon-sort c-pointer"></el-icon>
                  </div>
                </div>
                <div class="items" @click="getGoodsList('create_time')">
                  <input type="text" disabled placeholder="Time" />
                  <div class="items-btn">
                    <el-icon size="20" color="#fff" class="el-icon-sort c-pointer"></el-icon>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="new-nft-list" v-infinite-scroll="loadGoodsList">
          <div class="nft">
            <nft-item v-for="(goods, i) in goodsList" :nft="goods" :key="i" :index="i" @showDialog="showDialog" @onLike="onLike" size="big">
            </nft-item>
            <nft-item-load size="big" :loadStatus="goodsLoadStatus"></nft-item-load>
          </div>
        </div>
      </div>
    </div>

    <sale-dialog :show="showSaleDialog" @close="closeDialog" @confirm="dialogConfirm" :asset="dialogOrder" :nft="dialogNft" :uri="dialogNftURI">
    </sale-dialog>
    <cancel-sale-dialog :show="showCancelSaleDialog" @close="closeDialog" @confirm="dialogConfirm" :asset="dialogOrder" :nft="dialogNft" :uri="dialogNftURI">
    </cancel-sale-dialog>
    <buy-dialog :show="showBuyDialog" @close="closeDialog" @confirm="dialogConfirm" :asset="dialogOrder" :nft="dialogNft" :uri="dialogNftURI">
    </buy-dialog>

    <bid-dialog :show="showBidDialog" @close="closeDialog" @confirm="dialogConfirm" :bid="dialogOrder" :myAsset="dialogMyAsset" :nft="dialogNft" :uri="dialogNftURI">
    </bid-dialog>
    <cancel-bid-dialog :show="showCancelBidDialog" @close="closeDialog" @confirm="dialogConfirm" :bid="dialogOrder" :nft="dialogNft" :uri="dialogNftURI">
    </cancel-bid-dialog>
    <accept-dialog :show="showAcceptDialog" @close="closeDialog" @confirm="dialogConfirm" :bid="dialogOrder" :myAsset="dialogMyAsset" :nft="dialogNft" :uri="dialogNftURI">
    </accept-dialog>
    <transfer-dialog :show="showTransferDialog" @close="closeDialog" @confirm="dialogConfirm" :asset="dialogOrder" :nft="dialogNft" :uri="dialogNftURI">
    </transfer-dialog>
    <burn-dialog :show="showBurnDialog" @close="closeDialog" @confirm="dialogConfirm" :asset="dialogOrder" :nft="dialogNft" :uri="dialogNftURI">
    </burn-dialog>

    <auction-detail-dialog :show="showAuctionDetailDialog" @close="closeDialog" @confirm="dialogConfirm" :token="dialogToken" :auction="dialogOrder" :uri="dialogNftURI" :nft="dialogNft">
    </auction-detail-dialog>
    <sale-type-dialog :show="showSaleTypeDialog" @close="closeDialog" @saleType="dialogSelect"></sale-type-dialog>
    <auction-dialog :show="showAuctionDialog" @close="closeDialog" @confirm="dialogConfirm" :auction="dialogOrder" :myAsset="dialogMyAsset" :uri="dialogNftURI" :nft="dialogNft"></auction-dialog>
    <auction-cancel-dialog :show="showAuctionCancelDialog" @close="closeDialog" @confirm="dialogConfirm" :auction="dialogOrder" :uri="dialogNftURI" :nft="dialogNft"></auction-cancel-dialog>
  </div>
</template>

<script>
  import NftDialog from "@/mixins/NftDialog";
  import NftItem from "@/mixins/NftItem";
  export default {
    name: "Explore",
    mixins: [NftDialog, NftItem],
    data () {
      return {
        goodsLoadStatus: "",
        goodsList: [],
        goodsQuery: {
          page: 1,
          limit: this.$store.state.pageLimit,
          sort: "", //price or time
          order: "", // asc or desc
          minPrice: '',
          maxPrice: '',
          payToken: '',
          sellType: 1,
          search: '',
          address: ''
        },
        collections: [],
        collectionsQuery: {
          page: 1,
          limit: this.$store.state.pageLimit,
        },
        total: 0,
        filterStatus: false,
        filterStatusType: 0,
        filterPrice: false,
        pricePayToken: 'ETH',
        minPrice: '',
        maxPrice: '',
        paytokenSelect: false,
        filterChains: false,
        filterSell: false,
        filterSellType: '',
        filterChainsType: '',
        filterSale: false,
        filterCollections: false,
        filterSaleType: '',
        collectionsLoadStatus: '',
        filterHidden: false
      };
    },
    computed: {
      payTokens () {
        return this.$store.state.payTokens
      },
    },
    created () {
      this.init();
    },
    methods: {
      init () {
        this.getGoodsList();
        this.getCollections();
      },
      filterClick () {
        this.filterHidden = !this.filterHidden
      },
      selectChange () {
      },
      loadGoodsList () {
        if (this.goodsLoadStatus == "over") return;
        this.getGoodsList();
      },
      loadCollectionsList () {
        if (this.collectionsLoadStatus == "over") return;
        this.getCollections();
      },
      lookCollection (item) {
        this.goodsQuery = {
          page: 1,
          limit: this.$store.state.pageLimit,
          sort: "", //price or time
          order: "", // asc or desc
          minPrice: '',
          maxPrice: '',
          payToken: '',
          sellType: 3,
          search: '',
          address: item.address
        }
        this.getGoodsList();
      },
      selectSellType (item) {
        this.goodsQuery = {
          page: 1,
          limit: this.$store.state.pageLimit,
          sort: "", //price or time
          order: "", // asc or desc
          minPrice: '',
          maxPrice: '',
          payToken: '',
          sellType: 1,
          search: '',
          address: ''
        }
        this.filterSellType = item.symbol;
        this.goodsQuery.payToken = item.address;
        this.goodsQuery.page = 1;
        this.getGoodsList();
      },
      filterPriceApply () {
        let min = this.goodsQuery.minPrice
        let max = this.goodsQuery.maxPrice
        let token = this.goodsQuery.payToken
        this.goodsQuery = {
          page: 1,
          limit: this.$store.state.pageLimit,
          sort: "", //price or time
          order: "", // asc or desc
          minPrice: min,
          maxPrice: max,
          payToken: token,
          sellType: 1,
          search: '',
          address: ''
        }
        this.getGoodsList()
      },
      getGoodsList (sort) {
        if (this.goodsLoadStatus == "loading") return;
        this.goodsLoadStatus = "loading";
        if (sort == 'usdt_price' || sort == 'create_time') {
          this.goodsQuery.page = 1
          this.goodsQuery.sort = sort;
          if (this.goodsQuery.order == 'asc') {
            this.goodsQuery.order = 'desc'
          } else {
            this.goodsQuery.order = 'asc'
          }
        }
        var data = this.goodsQuery
        this.$api("home.list", data).then((res) => {
          if (this.$tools.checkResponse(res)) {
            if (data.page == 1) this.goodsList = [];
            this.goodsList = this.goodsList.concat(res.data.list);
            this.queryFunction(res.data.list);
            this.total = res.data.total
            if (res.data.page == res.data.pages || res.data.pages == 0) {
              this.goodsLoadStatus = "over";
            } else {
              this.goodsQuery.page += 1;
              this.goodsLoadStatus = "";
            }
          } else {
            this.$tools.message(res.errmsg);
          }
        });
      },
      getCollections () {
        if (this.collectionsLoadStatus == "over") return;
        let data = this.collectionsQuery
        this.$api("contract.listall", data).then((res) => {
          if (this.$tools.checkResponse(res)) {
            if (data.page == 1) this.collections = [];
            this.collections = this.collections.concat(res.data.list);
            if (res.data.page == res.data.pages || res.data.pages == '0') {
              this.collectionsLoadStatus = "over";
            } else {
              this.collectionsQuery.page += 1;
              this.collectionsLoadStatus = "";
            }
          } else {
            this.$tools.message(res.errmsg);
          }
        });
      },
      handleStatus (type) {
        this.goodsQuery = {
          page: 1,
          limit: this.$store.state.pageLimit,
          sort: "", //price or time
          order: "", // asc or desc
          minPrice: '',
          maxPrice: '',
          payToken: '',
          sellType: 1,
          search: '',
          address: ''
        }
        this.filterStatusType = type
        this.goodsQuery.sellType = type
        let data = {
          page: 1,
          limit: 20,
          sellType: type
        }
        this.$api("home.list", data).then((res) => {
          if (this.$tools.checkResponse(res)) {
            if (data.page == 1) this.goodsList = [];
            this.goodsList = this.goodsList.concat(res.data.list);
            this.queryFunction(res.data.list);
            this.total = res.data.total
            if (res.data.page == res.data.pages || res.data.pages == 0) {
              this.goodsLoadStatus = "over";
            } else {
              this.goodsQuery.page = 2;
              this.goodsLoadStatus = "";
            }
          } else {
            this.$tools.message(res.errmsg);
          }
        });
      },
      handleSale (type) {
        this.goodsQuery = {
          page: 1,
          limit: this.$store.state.pageLimit,
          sort: "", //price or time
          order: "", // asc or desc
          minPrice: '',
          maxPrice: '',
          payToken: '',
          sellType: 1,
          search: '',
          address: ''
        }
        this.filterSaleType = type
        this.goodsQuery.sellType = type
        let data = {
          page: 1,
          limit: 20,
          sellType: type
        }
        this.$api("home.list", data).then((res) => {
          if (this.$tools.checkResponse(res)) {
            if (data.page == 1) this.goodsList = [];
            this.goodsList = this.goodsList.concat(res.data.list);
            this.queryFunction(res.data.list);
            this.total = res.data.total
            if (res.data.page == res.data.pages || res.data.pages == 0) {
              this.goodsLoadStatus = "over";
            } else {
              this.goodsQuery.page = 2;
              this.goodsLoadStatus = "";
            }
          } else {
            this.$tools.message(res.errmsg);
          }
        });
      },
      statusClick () {
        this.filterStatus = !this.filterStatus
      },
      priceClick () {
        this.filterPrice = !this.filterPrice
      },
      chainsClick () {
        this.filterChains = !this.filterChains
      },
      sellClick () {
        this.filterSell = !this.filterSell
      },
      saleTypeClick () {
        this.filterSale = !this.filterSale
      },
      collectionsClick () {
        this.filterCollections = !this.filterCollections
      },
      toDetails () {
        this.$router.push({
          path: "/boxDetailsTwo",
        });
      },
    },
  };
</script>

<style lang="scss" scoped>
  .explore-bg {
    width: 100%;
    margin-top: 0;
    background: url('../../assets/img/home/banner.jpg') no-repeat;
    height: 330px;
    line-height: 420px;
    font-size: 44px;
    font-weight: 600;
    color: #000;
    text-align: center;
    background-size: 100% auto;
    font-family: Poppins-SemiBold;
  }
  .explore-main {
    display: flex;
    justify-content: space-between;
    margin: 0;
    max-width: 1600px;
    position: relative;
    .explore-filter {
      width: 25%;
      background-color: #fff;
      margin-top: 30px;
      .explore-filter-top {
        font-family: Poppins-SemiBold;
        padding: 20px;
        .filter-img {
          width: 26px;
          height: 18px;
          margin-right: 10px;
        }
        color: #000;
        font-size: 26px;
      }
      .explore-filter-item {
        font-family: Poppins-SemiBold;
        padding: 20px;
        color: #000;
        font-size: 22px;
        margin-bottom: 3px;
        display: flex;
        justify-content: space-between;
        align-items: center;
        border-top: 3px solid #ddd;
      }
      .explore-filter-detail {
        background-color: #ddd;
        padding: 20px;
        .explore-filter-status {
          width: 47%;
          display: inline-block;
          height: 40px;
          line-height: 40px;
          border: 1px solid #ddd;
          text-align: center;
          color: #000;
          border-radius: 8px;
          font-size: 16px;
          font-family: Poppins-Regular;
          background-color: #fff;
          word-break: break-all;
          overflow: hidden;
        }
        .active {
          background-color: $primaryColor;
        }
        .select-box {
          width: 100%;
          height: 40px;
          border-radius: 8px;
          color: #000;
          background: #fff;
          text-indent: 5px;
          margin-bottom: 20px;
          outline: none;
          .select-option {
            height: 30px;
            line-height: 30px;
          }
        }
        .select-box:focus {
          outline: none;
        }
        .explore-filter-priceItem {
          display: flex;
          justify-content: space-between;
          align-items: center;
          margin-bottom: 20px;
          color: #000;
          input.price {
            width: 45%;
            display: inline-block;
            height: 40px;
            line-height: 40px;
            border: 1px solid #eee;
            text-indent: 5px;
            color: #000;
            border-radius: 8px;
            background-color: #fff;
          }
          input.price::placeholder {
            color: #c0c4cc;
          }
          input.price:focus {
            outline: none;
          }
        }
        .paytoken {
          width: 100%;
          display: inline-block;
          height: 40px;
          line-height: 40px;
          border: 1px solid #eee;
          text-align: center;
          color: #000;
          border-radius: 8px;
          background-color: #fff;
          overflow: hidden;
          .select-trigger .el-input input.el-input__inner {
            width: 100%;
            display: inline-block;
            height: 40px;
            line-height: 40px;
            border: 1px solid #eee;
            text-indent: 5px;
            color: #000;
            border-radius: 8px;
            background-color: #fff;
          }
        }
        .explore-filter-priceBtn {
          width: 100%;
          height: 40px;
          line-height: 40px;
          text-align: center;
          color: #000;
          border-radius: 20px;
          background-color: $primaryColor;
          border: 0;
        }
      }
    }
    .explore-content {
      width: 75%;
      padding: 30px;
    }
  }
  .explore-filter-select {
    width: 100%;
    padding: 0 5px;
    border: 1px solid #eee;
    background-color: #fff;
    margin-top: -20px;
    border-bottom-right-radius: 8px;
    border-bottom-left-radius: 8px;
    color: #000;
    border-top: 0;
  }
  .head-box {
    display: flex;
    flex-direction: column;
    .blind-box {
      min-width: 100px;
      text-align: left;
      font-size: 36px;
      font-weight: bold;
      color: #000;
      font-family: Poppins-SemiBold;
      display: flex;
      justify-content: space-between;
      align-items: center;
      .explore-content-top {
        border: 1px solid #837d7d;
        border-radius: 10px;
        padding: 8px 12px;
        font-size: 14px;
        display: none;
        align-items: center;
        cursor: pointer;
        .explore-content-img {
          width: 19px;
          margin-right: 10px;
        }
      }
    }
    .line {
      width: 100%;
      height: 5px;
      border-top: 1px solid #666;
      border-bottom: 1px solid #666;
      border-left: 50px solid #a742f5;
      margin: 10px 0;
    }
    .filter {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 10px 0 30px;
      position: relative;
      .result-num {
        line-height: 50px;
        color: #000;
        font-size: 16px;
      }
      .filter-box {
        display: flex;
        .filter-box-right {
          display: flex;
          margin-left: 10px;
          .items {
            width: 120px;
          }
        }
      }
      .items {
        display: flex;
        margin: 5px 0;
        justify-content: space-between;
        align-items: center;
        border-radius: 8px;
        height: 45px;
        border: 1px solid #837d7d;
        width: 340px;
        .items-btn {
          border-radius: 8px;
          width: 33px;
          height: 33px;
          border: 0px solid #fff;
          background-color: #a742f5;
          text-align: center;
          cursor: pointer;
          margin-right: 6px;
          display: flex;
          justify-content: center;
          align-items: center;
        }
        input {
          background-color: rgba($color: #000000, $alpha: 0);
          width: calc(100% - 40px);
          border: 0;
          line-height: 36px;
          color: #000;
          font-size: 22px;
          text-indent: 5px;
        }
        input:focus {
          outline: none;
        }
      }
      .items + .items {
        margin-left: 10px;
      }
    }
  }
  .collection-list {
    display: flex;
    align-items: center;
    color: #000;
    font-size: 16px;
    padding: 10px;
    background: #fff;
    border-radius: 10px;
    cursor: pointer;
  }
  .collection-list + .collection-list {
    margin-top: 10px;
  }

  .w-250 {
    width: 250px;
  }
  .w-100 {
    width: 100px;
  }

  .filter-sort {
    min-width: 80px;
    font-size: 9px;
    color: #000000;
    cursor: pointer;
  }

  .sort-icon {
    width: 7px;
    height: 4px;
    margin-left: 5px;
  }

  .new-nft-list {
    margin: 0 -10px;
    .tab-content-none {
      width: 100%;
      display: flex;
      justify-content: center;
      text-align: center;
      margin: 43px auto;
    }
    .tab-info {
      .tab-title {
        font-size: 24px;
        font-weight: bold;
        color: #1d1e22;
        margin: 45px auto 23px;
      }
      .tab-intro {
        width: 313px;
        font-size: 16px;
        font-weight: 400;
        color: #1d1e22;
        line-height: 22px;
        opacity: 0.5;
      }
      .tab-btn {
        width: 250px;
        height: 54px;
        margin-top: 21px;
      }
    }
    .nft {
      display: flex;
      flex-wrap: wrap;
      justify-content: flex-start;
    }
  }

  .c-pointer {
    cursor: pointer;
  }
  .ml-5 {
    margin-left: 5%;
  }
  .mb-10 {
    margin-bottom: 10px;
  }
  .dis-block {
    display: block !important;
  }
  @media screen and (max-width: $media_l2) {
    .explore-main {
      .explore-filter {
        position: absolute;
        display: none;
        top: 85px;
        width: 100%;
        z-index: 100;
        .explore-filter-top {
          display: none;
        }
      }
      .explore-content {
        width: 100%;
      }
    }
    .head-box {
      .blind-box {
        font-size: 20px;
        .explore-content-top {
          display: flex;
        }
      }
    }
  }
  @media screen and (max-width: $media_l5) {
    .explore-bg {
      background-size: 100% 100%;
    }
    .head-box {
      .filter {
        flex-direction: column-reverse;
        align-items: flex-start;
        .filter-box {
          flex-direction: column;
          align-items: flex-end;
          width: 100%;
          .items {
            width: 100%;
          }
        }
      }
    }
  }
</style>

