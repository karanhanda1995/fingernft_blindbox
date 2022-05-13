<template>
  <el-dialog :model-value="visible" :show-close="false" :close-on-click-modal="false" custom-class="fg-dialog" @open="onOpen" @closed="closed">
    <template #title>
      <div class="fg-dialog-title">
        <div class="left">
          <span v-if="uri">{{ uri.name }}</span>
        </div>
        <div class="right" @click="$emit('close')">
          <img class="close-img" src="@/assets/create-img/pop_shut.png " />
        </div>
      </div>
    </template>
    <div class="fg-dialog-body">
      <div class="process">
        <div class="step-info">
          <div class="text">
            <span>{{ $t("dialog.auctionDetail") }}</span>
          </div>
        </div>
        <info v-if="auction.owner" :auction="auction" :uri="uri" :nft="nft">
        </info>
        <bids v-if="timeStatus != 'start'" :bids="bids" :auction="auction" @cancel="getBids"></bids>
        <div class="process-btn" :class="{ wait: timeStatus == 'start' }" v-if="parseInt(ownerQuantity) > 0 && parseInt(auction.sellValue) <= parseInt(ownerQuantity)">
          <div class="highest" v-if="highestBid">
            <div class="info m-right-5">
              <div class="title">
                <div class="m-bottom-5"> {{ $t("dialog.highestBidBy") }} </div>
                <div class="c3 cpointer">
                  {{ highestBid.nickname || $filters.ellipsisAddress(highestBid.owner) }}
                </div>
              </div>
              <div class="label">
                <span class="cprimary m-right-5">
                  {{ highestBid.singlePrice || "" }}
                  {{ auction.paytokenSymbol }}
                </span>
              </div>
            </div>
            <div class="info-right" v-if="
                !auction.status &&
                timeStatus == 'expired' &&
                connected &&
                (user.coinbase == auction.owner ||
                  user.coinbase == highestBid.owner)
              ">
              <el-button @click="showAcceptDialog = true" size="mini" plain type="primary" v-if="user.coinbase == auction.owner">
                {{ $t("nftDetail.accept") }}
              </el-button>
              <el-button @click="showBuyDialog = true" size="mini" type="primary" v-else>
                {{ $t("nftDetail.buy") }}
              </el-button>
            </div>
          </div>

          <el-button type="info" disabled v-if="auction.status">
            {{ codeDesc }}
          </el-button>
          <template v-else>
            <el-button type="primary" v-if="
                connected &&
                user.coinbase != auction.owner &&
                timeStatus == 'end' &&
                isBid
              " @click="showBidDialog = true">
              {{ $t("dialog.bid") }}
            </el-button>
            <el-button type="primary" disabled v-else>
              {{ $t("dialog.bid") }}
            </el-button>
            <div class="count-down">
              <auction-count-down class="cgreen" @status="setTimeStatus" :startTime="parseInt(auction.startTime)" :endTime="parseInt(auction.endTime)" :expiredTime="parseInt(auction.expiredTime)">
              </auction-count-down>
            </div>
          </template>
        </div>
      </div>
    </div>

    <auction-bid-dialog :show="showBidDialog" @close="closeDialog" @confirm="dialogConfirm" :highestBid="highestBid" :auction="auction" :nft="nft" :uri="uri">
    </auction-bid-dialog>
    <auction-buy-dialog :show="showBuyDialog" @close="closeDialog" @confirm="dialogConfirm" :bid="highestBid" :auction="auction" :nft="nft" :uri="uri">
    </auction-buy-dialog>
    <auction-accept-dialog :show="showAcceptDialog" @close="closeDialog" @confirm="dialogConfirm" :bid="highestBid" :auction="auction" :nft="nft" :uri="uri" :bidUsers="bidUsers">
    </auction-accept-dialog>
  </el-dialog>
</template>

<script>
  import Bids from "./auction/Bids";
  import Info from "./auction/Info";
  import BigNumber from "bignumber.js";
  export default {
    components: {
      Bids,
      Info,
    },
    data () {
      return {
        visible: this.show,
        bids: [],
        bidUsers: [],
        timeStatus: "",
        highestBid: null,
        showBidDialog: false,
        showCancelBidDialog: false,
        showBuyDialog: false,
        showAcceptDialog: false,
        ownerQuantity: 0,
      };
    },
    emits: ['confirm'],
    props: {
      show: {
        type: Boolean,
        default: true,
      },
      auction: {
        type: Object,
        default: {},
      },
      uri: {
        type: Object,
        default: null,
      },
      nft: {
        type: Object,
        default: {},
      },
      token: {
        type: Object,
        default: {},
      },
    },
    computed: {
      connected () {
        return this.$store.state.connected;
      },
      user () {
        return this.$store.state.user;
      },
      codeDesc () {
        let codeStr = this.$tools.getAuctionStatusCode(this.auction.statusCode);
        return this.$t("statusCode." + codeStr);
      },
      codeStr () {
        return this.$tools.getAuctionStatusCode(this.auction.statusCode);
      },
      isBid () {
        if (this.highestBid) {
          if (this.highestBid.owner === this.user.coinbase) return false;
          return true
        }
        return true;
      },
    },
    methods: {
      onOpen () {
        this.closeDialog();
        this.initData();
        this.getBids();
        this.getOwners();
      },
      closeDialog () {
        this.showBidDialog = false;
        this.showCancelBidDialog = false;
        this.showBuyDialog = false;
        this.showAcceptDialog = false;
      },
      dialogConfirm () {
        this.getBids();
        this.closeDialog();
      },
      initData () {
        this.bids = [];
        this.highestBid = null;
      },
      setTimeStatus (status) {
        this.timeStatus = status;
      },
      getBids () {
        let data = {
          token: this.auction.sellToken,
          tokenId: this.auction.sellTokenId,
          owner: this.auction.owner,
          salt: this.auction.salt,
        };
        let that = this;
        this.$api("auction.bids", data).then((res) => {
          if (that.$tools.checkResponse(res)) {
            that.bids = that.formatBids(res.data);
            that.bidUsers = res.data.users;
            that.highestBid = that.getHighestBid(that.bids);
          }
        });
      },
      formatBids (bids) {
        for (var i = 0;i < bids.length;i++) {
          bids[i].singlePrice = this.$tools.noDecimalsValue(
            bids[i].buying,
            this.auction.sellValue,
            this.auction.paytokenDecimals
          );
        }
        return bids;
      },
      getOwners () {
        let that = this;
        let data = {
          token: this.token.token,
          tokenId: this.token.tokenId,
        };
        this.$api("nft.owners", data).then((res) => {
          if (that.$tools.checkResponse(res)) {
            let address = "";
            if (that.auction.owner && that.auction.owner != "") {
              address = that.auction.owner.toLocaleLowerCase();
            }
            let owner = res.data.list.find((item) => {
              return address === item.itemOwner.toLocaleLowerCase();
            });
            that.ownerQuantity = owner ? owner.quantity : 0;
          }
        });
      },
      getHighestBid (bids) {
        let highestBid;
        for (var i = 0;i < bids.length;i++) {
          var bid = bids[i];
          if (bid.status != 0) continue;
          bid.singlePrice = this.$tools.noDecimalsValue(
            bid.buying,
            this.auction.sellValue,
            this.auction.paytokenDecimals
          );
          if (
            !highestBid ||
            parseFloat(highestBid.singlePrice) < parseFloat(bid.singlePrice)
          ) {
            highestBid = bid;
          }
        }
        return highestBid;
      },
      closed () {
        this.initData()
      }
    },
    watch: {
      show () {
        this.visible = this.show;
      },
    },
  };
</script>

<style lang="scss" scoped>
  .input-flex {
    display: flex;
    justify-content: space-between;
  }
  .input-flex .process-btn {
    padding: 0;
  }
  .auctionPay {
    width: 100%;
    display: block !important;
  }

  .count-down {
    text-align: center;
    margin-top: 10px;
  }

  .highest {
    display: flex;
    padding-bottom: 20px;
    .title {
      display: flex;
      font-size: 14px;
      color: #aaa;
      margin-bottom: 5px;
      flex-direction: column;
    }
    .info {
      flex: 1;
    }
    .op {
      background: $secondPrimaryColor;
      font-weight: normal;
      font-size: 13px;
      padding: 2px 5px;
      border-radius: $borderRadius;
      margin-left: 5px;
    }

    .info {
      flex: 1;
    }
    .label {
      margin-bottom: 2px;
    }
  }
</style>
