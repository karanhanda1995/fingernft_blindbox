<template>
<div class="mini-account-item cpointer" @click="goUser">
  <div class="m-right-10">
    <avatar :imageUrl="$filters.fullImageUrl(account.avatar)" :address="account.address" :imgWidth="50" :imgHeight="50">
    </avatar>
  </div>
  <div class="content">
    <div class="d">
      <div class="bfont c6 f15" v-if="account.nickname">{{ account.nickname}}</div>
    </div>
    <div class="d">
      <div class="bfont c9">{{ $filters.ellipsisAddress(account.address) }}</div>
    </div>
  </div>
</div>
</template>
<script>
export default {
  name: "MiniAccountItem",
  data: function(){
    return {};
  },
  emits:["close"],
  props:{
    account: {
      type: Object,
      default: {},
    },
  },
  methods: {
    goUser(){
      this.$emit("close");
      this.$router.push({ path: "/account/" + this.account.address });
    },
  },
}
</script>

<style lang="scss" scoped>
.mini-account-item{
  display: flex;
  flex-direction: row;
  margin-bottom: 10px;
  .content{
    display: flex;
    flex-direction: column;
    align-items: flex-start;
    justify-content: space-around;
  }

}
</style>
