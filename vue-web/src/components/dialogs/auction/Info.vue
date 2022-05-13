<template>
<div class="auction-info">

  <div class="info">
    <div class="avatar cpointer">
      <avatar @click="goAccount(auction.owner)" shape="circular" :imageUrl="$filters.fullImageUrl(auction.avatar)" :address="auction.owner" :imgWidth="35" :imgHeight="35"></avatar>
    </div>
    <div class="m-left-10">
      <div class="label">
        {{ auction.sellValue }} {{ $t("details.text3") }}
        <span class="bfont cprimary"> {{ singlePrice }} {{ auction.paytokenSymbol }} </span>
      </div>
      <div class="name flex">
        <div class="m-right-5"> {{$t("details.by")}} </div>
        <div class="nickname cpointer" @click="goAccount(auction.owner)">{{ $filters.ellipsisAddress(auction.owner) }}</div>
      </div>
    </div>
  </div>

  <div class="metas">
    <div class="meta">
      <span class="v cprimary">{{ singlePrice }} {{auction.paytokenSymbol}}</span>
      <span class="k">{{ $t('dialog.lowest') }}</span>
    </div>
    <div class="meta" v-if="auction.encourage > 0">
      <span class="v cred">{{ auction.encourage / 100}}%</span>
      <span class="k">{{ $t('dialog.dividend') }}</span>
    </div>
    <div class="meta last">
      <span class="v cgreen">{{rangeValue}} {{ auction.paytokenSymbol}}</span>
      <span class="k"> {{$t('dialog.increment')}} </span>
    </div>
  </div>
</div>
</template>
<script>
export default {
  name: "Info",
  props: {
    auction: {
      type: Object,
      default: {}
    },
  },
  computed: {
    singlePrice() {
      return this.$tools.noDecimalsValue(
        this.auction.startValue,
        this.auction.sellValue,
        this.auction.paytokenDecimals
      );
    },
    rangeValue(){
      return this.$tools.noDecimalsValue(
        this.auction.rangeValue,
        1,
        this.auction.paytokenDecimals
      );
    },
  },
  methods: {
    goAccount(address) {
      this.$router.push({ path: "/account/" + address });
    },
  }
}
</script>

<style lang="scss" scoped>
  .auction-info {
    margin-bottom: 20px;
    .info {
      display: flex;
      flex-direction: row;
    }

    .metas {
      display: flex;
      flex-direction: row;
      padding: 10px 0;
      margin-top: 10px;
      background: #f8f8f8;
      border-radius: $borderRadius;
    }
    .meta {
      border-right: $border;
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      flex: 1;
      &.last {
        border-right: none;
      }
      .v {
        font-size: 13px;
        line-height: 20px;
      }
    }
  }
</style>

