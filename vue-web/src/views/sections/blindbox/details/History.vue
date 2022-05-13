<template>
<div class="tab-content-list">
  <div class="info-item tab-content-list flex align-center justify-between m-bottom-10" 
    v-for="(history, index) in historys"
    :key="index"
  >
    <div class="avatar">
      <avatar @click="goAccount(history.buyer)"
        :address="history.buyer" :imgWidth="35" :imgHeight="35" shap="circular" v-if="!history.user"></avatar>
      <avatar @click="goAccount(history.buyer)" :imageUrl="$filters.fullImageUrl(history.user.avatar)" 
        :address="history.user.address" :imgWidth="35" :imgHeight="35" shap="circular" v-else></avatar>
    </div>
    <div class="info">
      <div class="label">
        <span class="link">
        {{ history.user && history.user.nickname ? history.user.nickname : $filters.ellipsisAddress(history.buyer)  }}
        </span>
        {{ $t('blindboxDetail.pay')}}
        <span class="bfont">{{ getPrice(history) }} {{ history.paytokenSymbol }}</span>
      </div>
      <div>
        {{$t('blindboxDetail.open')}}
        <span class="bfont">{{ history.amount }} </span>
        {{ $t('blindboxDetail.blindbox') }}
      </div>

    </div>

  </div>
</div>
</template>

<script>
export default {
  name: "History",
  props: {
    historys: {
      type: Array,
      default: [],
    },
    blindbox: {
      type: Object,
      default: {},
    },
  },
  methods: {
    goAccount(address){
      this.$router.push({ path: "/account/"+address });
    },
    getPrice(history){
      let price = this.blindbox.price * history.amount;
      return this.$tools.decimal(price, 2);
    },
  },
};
</script>

<style lang="scss" scoped>


</style>
