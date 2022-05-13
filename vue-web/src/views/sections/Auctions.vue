<template>
  <div class="main-wrapper">
    <div class="head-box flex justify-between">
      <div class="blind-box">{{ $t("hotAuctions.title") }}</div>
    </div>
    <div class="new-nft-list" v-infinite-scroll="loadGoodsList">
      <div class="flex-box">
        <nft-item
          v-for="(nft, i) in nftList"
          :nft="nft"
          :key="i"
          :index="i"
          :hotState="true"
          goodTag="auction"
          @showDialog="showDialog"
          @onLike="onLike"
        >
        </nft-item>
        <nft-item-load :loadStatus="loadStatus"></nft-item-load>
      </div>
    </div>

    <sale-dialog
      :show="showSaleDialog"
      @close="closeDialog"
      @confirm="dialogConfirm"
      :asset="dialogOrder"
      :nft="dialogNft"
      :uri="dialogNftURI"
    >
    </sale-dialog>
    <cancel-sale-dialog
      :show="showCancelSaleDialog"
      @close="closeDialog"
      @confirm="dialogConfirm"
      :asset="dialogOrder"
      :nft="dialogNft"
      :uri="dialogNftURI"
    >
    </cancel-sale-dialog>
    <buy-dialog
      :show="showBuyDialog"
      @close="closeDialog"
      @confirm="dialogConfirm"
      :asset="dialogOrder"
      :nft="dialogNft"
      :uri="dialogNftURI"
    >
    </buy-dialog>

    <bid-dialog
      :show="showBidDialog"
      @close="closeDialog"
      @confirm="dialogConfirm"
      :bid="dialogOrder"
      :myAsset="dialogMyAsset"
      :nft="dialogNft"
      :uri="dialogNftURI"
    >
    </bid-dialog>
    <cancel-bid-dialog
      :show="showCancelBidDialog"
      @close="closeDialog"
      @confirm="dialogConfirm"
      :bid="dialogOrder"
      :nft="dialogNft"
      :uri="dialogNftURI"
    >
    </cancel-bid-dialog>
    <accept-dialog
      :show="showAcceptDialog"
      @close="closeDialog"
      @confirm="dialogConfirm"
      :bid="dialogOrder"
      :myAsset="dialogMyAsset"
      :nft="dialogNft"
      :uri="dialogNftURI"
    >
    </accept-dialog>
    <transfer-dialog
      :show="showTransferDialog"
      @close="closeDialog"
      @confirm="dialogConfirm"
      :asset="dialogOrder"
      :nft="dialogNft"
      :uri="dialogNftURI"
    >
    </transfer-dialog>
    <burn-dialog
      :show="showBurnDialog"
      @close="closeDialog"
      @confirm="dialogConfirm"
      :asset="dialogOrder"
      :nft="dialogNft"
      :uri="dialogNftURI"
    >
    </burn-dialog>

    <auction-detail-dialog
      :show="showAuctionDetailDialog"
      @close="closeDialog"
      @confirm="dialogConfirm"
      :token="dialogToken"
      :auction="dialogOrder"
      :uri="dialogNftURI"
      :nft="dialogNft"
    ></auction-detail-dialog>
    <sale-type-dialog
      :show="showSaleTypeDialog"
      @close="closeDialog"
      @saleType="dialogSelect"
    ></sale-type-dialog>
    <auction-dialog
      :show="showAuctionDialog"
      @close="closeDialog"
      @confirm="dialogConfirm"
      :auction="dialogOrder"
      :myAsset="dialogMyAsset"
      :uri="dialogNftURI"
      :nft="dialogNft"
    ></auction-dialog>
    <auction-cancel-dialog
      :show="showAuctionCancelDialog"
      @close="closeDialog"
      @confirm="dialogConfirm"
      :auction="dialogOrder"
      :uri="dialogNftURI"
      :nft="dialogNft"
    ></auction-cancel-dialog>
  </div>
</template>

<script>
import NftDialog from "@/mixins/NftDialog";
import NftItem from "@/mixins/NftItem";
export default {
  name: "Auctions",
  components: {},
  mixins: [NftDialog, NftItem],
  data() {
    return {
      loadStatus: "",
      nftList: [],
      query: {
        page: 1,
        limit: this.$store.state.pageLimit,
      },
    };
  },
  created() {
    this.init();
  },
  methods: {
    init() {
      this.getGoodsList();
    },
    loadGoodsList() {
      if (this.loadStatus == "over") return;
      this.getGoodsList();
    },
    getGoodsList() {
      if (this.loadStatus == "loading") return;
      this.loadStatus = "loading";
      var data = {
        page: this.query.page,
        size: this.query.limit,
      };
      this.$api("auction.newlist", data).then((res) => {
        if (this.$tools.checkResponse(res)) {
          if (data.page == 1) this.nftList = [];
          this.nftList = this.nftList.concat(res.data.list);
          this.queryFunction(res.data.list);
          if (res.data.list.length < data.size) {
            this.loadStatus = "over";
          } else {
            this.query.page += 1;
            this.loadStatus = "";
          }
        } else {
          this.$tools.message(res.errmsg);
        }
      });
    },
    toDetails() {
      this.$router.push({
        path: "/boxDetailsTwo",
      });
    },
  },
};
</script>

<style scoped>
.head-box {
  padding: 27px 0px;
  align-items: flex-end;
}

.blind-box {
  text-align: left;
  font-size: 20px;
  font-weight: bold;
  color: #000000;
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
}
.nft {
  display: flex;
  flex-wrap: wrap;
  justify-content: flex-start;
}
</style>

