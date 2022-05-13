<template>
  <div class="auction-bids">
    <template v-if="bids.length > 0">
      <div class="title">{{ $t('dialog.bidList') }}</div>
      <div class="bid-group">
        <div v-for="(bid, index) in bids" :key="index" class="bid-item">
          <div class="avatar cpointer">
            <avatar @click="goAccount(bid.owner)" shape="circular" :imageUrl="$filters.fullImageUrl(bid.user.avatar)" :address="bid.owner" :imgWidth="35" :imgHeight="35"></avatar>
          </div>
          <div class="info">
            <div class="label flex">
              <div class="flex1">
                {{$t('details.offered')}}
                <span class="bfont cprimary">
                  {{ bid.singlePrice}} {{ auction.paytokenSymbol }}
                </span>
              </div>
              <!--
              <span class="bid-op cancel cpointer" @click="cancelHandler(bid)" v-if=" connected && user.coinbase === bid.owner && !auction.status && bid.status !== 1">
                {{ $t("details.cancel") }}
              </span>
              <span class="bid-op canceled" v-else-if="bid.status === 1">{{$t("details.canceled")}}</span>
              -->
            </div>
            <div @click="goAccount(bid.owner)" class="name">
              <span class="text">
                {{ bid.user.nickname || $filters.ellipsisAddress(bid.owner) }}
              </span>
            </div>
          </div>
        </div>
      </div>
    </template>
    <template v-else>
      <div class="empty-content">
        <div class="title">{{ $t('dialog.bidList') }}</div>
        <div class="no-content-image">
          <img src="@/assets/img/nocontent.png" alt="">
        </div>
        <div class="tip">{{$t('dialog.noBidsTip')}}</div>

      </div>
    </template>
  </div>
</template>
<script>
  // import { ElMessageBox } from 'element-plus'
  export default {
    name: "Bids",
    data () {
      return {
      }
    },
    props: {
      bids: {
        type: Array,
        default: []
      },
      auction: {
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
    },
    methods: {
      /*
      cancelHandler(bid) {
        let that = this;
        ElMessageBox.confirm( this.$t('dialog.cancelTip'), this.$t('dialog.cancel'),
          {
            customClass: "yuumi-message",
            confirmButtonText: this.$t('dialog.confirm'),
            cancelButtonText: this.$t('dialog.close'),
            buttonSize: "medium",
            center: true,
          }
        ).then(() => {
          let data = {
            token: that.auction.sellToken,
            tokenId: that.auction.sellTokenId,
            owner: that.auction.owner,
            salt: bid.salt,
            buying: bid.buying,
          }
          that.$api("auction.cancel", data).then(res => {
            if(that.$tools.checkResponse(res)) {
              that.$tools.message(that.$t('request.cancelSuccess'), "success");
              that.$emit("cancel")
            }else {
              that.$tools.message(res.errmsg);
            }
          })
  
        }).catch(() => {
        });
      },
      */
      goAccount (address) {
        this.$router.push({ path: "/account/" + address });
      },
    }
  };
</script>

<style lang="scss" scoped>
  .auction-bids {
    .title {
      font-size: 14px;
      color: #444;
      font-weight: 400;
      margin-bottom: 10px;
    }
    .avatar {
      width: 35px;
      height: 35px;
      border-radius: 50%;
      overflow: hidden;
      margin-right: 5px;
      cursor: pointer;
    }
    .bid-item {
      display: flex;
      padding-bottom: 10px;
    }
    .bid-group {
      max-height: 300px;
      overflow: hidden;
      overflow-y: scroll;
      background: #f8f8f8;
      padding: 10px;
      border-radius: $borderRadius;
      margin-bottom: 20px;
    }
    .info {
      position: relative;
      border-bottom: 1px solid #eee;
      flex: 1;
      padding-bottom: 10px;
    }
    .bid-op {
      padding: 5px 8px;
      color: #fff;
      background: $primaryColor;
      border-radius: $borderRadius;
      display: inline-block;
      &.cancel {
        color: $primaryColor;
        background: lighten($primaryColor, 30%);
      }
      &.canceled {
        color: #999;
        background: #f0f0f0;
      }
    }
    .name {
      font-size: 15px;
      margin-bottom: 5px;
      display: flex;
      cursor: pointer;
      align-items: center;
    }
    .countdown {
      display: flex;
      align-items: center;
      justify-content: center;
      padding: 20px;
      font-size: 18px;
    }
    .no-content-image {
      width: 20%;
      margin: 0 auto;
      img {
        width: 100%;
      }
    }
    .empty-content {
      .tip {
        text-align: center;
        margin-bottom: 10px;
      }
      .op {
        text-align: center;
      }
    }
  }
</style>
