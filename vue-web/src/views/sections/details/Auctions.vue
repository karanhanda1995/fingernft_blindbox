<template>
  <div class="tab-content-list">
    <div class="owner-bid-switch">
      <div class="status-switch cpointer">
        <span class="item" :class="{'active': !finished }" @click="finished = false"> {{ $t("statusCode.live") }} </span>
        <span class="item" :class="{ 'active': finished }" @click="finished = true">{{ $t("statusCode.finished") }}</span>
      </div>
    </div>
    <div v-for="(auction, index) in validAuctions" :key="index" class="info-item">
      <div class="avatar">
        <avatar @click="goAccount(auction.user.address)" shape="circular" :imageUrl="$filters.fullImageUrl(auction.user.avatar)" :address="auction.user.address" :imgWidth="35" :imgHeight="35"></avatar>
      </div>
      <div class="info">
        <div class="label flex">
          <div class="label-left">
            {{ auction.sellValue }} {{ $t("details.text3") }}
            <span class="bfont"> {{ $tools.singlePrice(auction.startValue, auction.sellValue, auction.payToken) }} </span>
            {{ auction.payToken.symbol }}
          </div>
          <span class="nft-op" @click="buy(auction)" style="margin-right:15px">{{
            $t("details.detail")
          }}</span>
          <span v-if="connected && user.coinbase == auction.owner && !auction.status" class="nft-op cancel" @click="cancel(auction)">{{ $t("details.cancel") }}</span>
        </div>
        <div @click="goAccount(auction.user.address)" class="name">
          <span class="text">
            {{ auction.user.nickname || $filters.ellipsisAddress(auction.user.address) }}
          </span>
          <span class="status live" v-if="!auction.status">
            {{ $t('statusCode.live') }}
          </span>
          <span class="status" :class="codeStr(auction)" v-else>
            {{ codeDesc(auction) }}
          </span>

          <span class="t">{{ $filters.timeFormat(auction.createTime) }}</span>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
  import { computed } from "vue";

  export default {
    name: "Owners",
    props: {
      auctions: {
        type: Array,
        default: [],
      },
      nft: {
        type: Object,
        default: {}
      }
    },
    data () {
      return {
        finished: false,
      };
    },
    computed: {
      connected () {
        return this.$store.state.connected;
      },
      user () {
        return this.$store.state.user;
      },
      paytoken () {
        return (paytoken) => {
          return this.$store.getters.payToken(paytoken);
        }
      },
      validAuctions () {
        if (!this.finished) {
          return this.auctions.filter(item => {
            return !item.status;
          })
        } else {
          return this.auctions.filter(item => {
            return item.status;
          })
        };
      },
    },
    created () {
      let validCount = this.countValidAuction();
      if (!validCount) this.finished = true;
    },
    methods: {
      countValidAuction () {
        let auctions = this.auctions.filter(item => {
          return !item.status;
        });
        return auctions.length;
      },
      cancel (auction) {
        this.$emit('cancel', auction);
      },
      buy (auction) {
        this.$emit("buy", auction);
      },
      codeDesc (auction) {
        let codeStr = this.$tools.getAuctionStatusCode(auction.statusCode);
        return this.$t("statusCode." + codeStr);
      },
      codeStr (auction) {
        return this.$tools.getAuctionStatusCode(auction.statusCode);
      },
      goAccount (address) {
        this.$router.push({ path: "/account/" + address });
      },
    },
  };
</script>

<style lang="scss" scoped>
  .owner-bid-switch {
    padding-bottom: 10px;
    margin-bottom: 20px;
    border-bottom: $border;
  }
  .status-switch {
    border: 1px solid #ddd;
    display: inline-block;
    border-radius: 15px;
    background: #f0f0f0;
    .item {
      padding: 5px 10px;
      display: inline-block;
      &.active {
        background: $primaryColor;
        color: #fff;
        border-radius: 16px;
      }
    }
  }

  .tab-content-list {
    .status {
      margin-right: 10px;
      font-size: 13px;
      padding: 1px 5px;
      border-radius: 5px;

      &.live {
        color: $primaryColor;
        background: lighten($primaryColor, 50%);
        border: 1px solid lighten($primaryColor, 20%);
      }
      &.cancel {
        color: $redColor;
        background: lighten($redColor, 50%);
        border: 1px solid lighten($redColor, 20%);
      }

      &.expired {
        color: $grayColor;
        background: #f8f8f8;
        border: 1px solid #ddd;
      }
      &.finished {
        color: $greenColor;
        background: lighten($greenColor, 50%);
        border: 1px solid lighten($greenColor, 20%);
      }
    }
  }
</style>
