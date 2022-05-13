<template>
<div class="tab-content-list flex align-center justify-between m-bottom-10" 
  v-for="(item, index) in nfts"
  :key="index"
>
    <avatar class="u-info-avatar" :imageUrl="$filters.fullImageUrl($tools.analysis(item.metadataContent).image)" :imgWidth="50"
      :imgHeight="50"></avatar>
    <div class="creator-box">
      <div class="name x-text-1b1b1b">{{item.name}}</div>
      <div class="name font-class flex justify-between">
        <span>{{item.amount}}</span>
        <div class="flex text-center x-text-C1C1C1 justify-end">
          {{ getProbability(item) }}%
        </div>
      </div>
    </div>
  
</div>
</template>

<script>
export default {
  name: "Info",
  props: {
    blindbox: {
      type: Object,
      default: {},
    },
    nfts: {
      type: Array,
      default: [],
    },
  },
  data: function () {
    return {};
  },
  methods: {
    getProbability(nft){
      if(!this.blindbox.isRepetition){
        return this.$tools.decimal( (nft.amount * 100) / this.blindbox.amount, 2);
      }

      let totalAmount = this.blindbox.amount * this.blindbox.nftAmount;
      let probability = 0;
      for(var i = 0; i < this.blindbox.nftAmount; i++){
        if(!probability){
          probability = (totalAmount - nft.amount - i) / totalAmount;
        }else{
          probability = probability * (totalAmount - nft.amount - i) / totalAmount;
        }
      }
      return this.$tools.decimal(( 1 - probability ) * 100, 2);
    },
  },
};
</script>

<style lang="scss" scoped>
.creator-box{
  width:80%;
  .name {
    margin-left: 8px;
    white-space: nowrap;
    text-overflow: ellipsis;
    overflow: hidden;
    word-break: break-all;
  }
}

.font-class{
  line-height:30px;
  color:grey;
}
</style>
