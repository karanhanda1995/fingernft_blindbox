<template>
  <div class="auction-item" :class="{'full': full}">
    <div class="title">{{ $t("nftDetail.auction") }}
      <span v-if="!full" class="op cprimary cpointer" @click="showAuction">
        {{ $t('details.view')}}
      </span>
    </div>

    <div class="auction-body">
      <div class="avatar cpointer">
        <avatar @click="goAccount(auction.owner)" :imageUrl="$filters.fullImageUrl(auction.user.avatar)" :address="auction.owner" :imgWidth="35" :imgHeight="35" shape="circular">
        </avatar>
      </div>
      <div class="info">
        <div class="label">
          <span class="c3 cpointer" @click="goAccount(auction.owner)">
            {{ auction.user.nickname || $filters.ellipsisAddress(auction.owner) }}
          </span>
          <!--
        {{$t('details.countDownAuction')}}
        -->
        </div>

        <div class="name">
          <span class="text">
            {{ $t('nftDetail.lowest') }}
            <span class="cprimary m-right-5">{{ $tools.singlePrice(auction.startValue,auction.sellValue,auction.payToken) }}
              {{ auction.payToken.symbol }} </span>
            <!-- <span class="c9">
              {{ auction.lowestUsdtPrice || "" }} USDT
            </span> -->
          </span>
        </div>

      </div>
      <div class="info-right" v-if="full">
        <el-button size="mini" @click="showAuction" plain type="primary">
          {{ $t('details.buy2')}}
        </el-button>
      </div>

    </div>
    <div class="countdown" :class=" {'full': full }">
      <auction-count-down class="cgreen" :startTime="parseInt(auction.startTime)" :endTime="parseInt(auction.endTime)" :expiredTime="parseInt(auction.expiredTime)">
      </auction-count-down>
    </div>

  </div>
</template>

<script>
  export default {
    name: "LowestAuction",
    props: {
      auction: {
        type: Object,
        default: {},
      },
      myAsset: {
        type: Object,
        default: null,
      },
      full: {
        type: Boolean,
        default: false,
      },
    },
    methods: {
      goAccount (address) {
        this.$router.push({ path: "/account/" + address });
      },
      showAuction () {
        this.$emit("detail", this.auction);
      },
    },
  }
</script>

<style lang="scss" scoped>
  .auction-item {
    flex: 1;
    padding-left: 10px;
    &.full {
      padding-left: 0;
      .countdown {
        padding: 4px 5px;
      }
    }
    .title {
      display: flex;
      font-size: 14px;
      color: #aaa;
      margin-bottom: 5px;
      justify-content: space-between;
    }
    .info {
      margin-left: 5px;
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
    .auction-body {
      display: flex;
    }
    .countdown {
      background: #f0f0f0;
      padding: 2px 5px;
      border-radius: 5px;
      margin-top: 5px;
      align-items: center;
      display: flex;
      justify-content: center;
    }
  }
</style>
