<template>
  <div class="mini-nft-item cpointer" @click="goDetail">
    <div class="cover m-right-10">
      <el-image class="cover-img" :src="$filters.fullImageUrl(nftURI.image)" fit="cover">
        <template v-slot:placeholder>
          <el-skeleton class="placeholder-image" animated>
            <template #template>
              <el-skeleton-item class="nft-image-skeleton" variant="h3" />
            </template>
          </el-skeleton>
        </template>
        <template v-slot:error>
          <el-image class="error-image" :src="require('@/assets/create-img/non-existent.png')" fit="contain"></el-image>
        </template>
      </el-image>
    </div>
    <div class="content">
      <div class="bfont c6 f15">{{ nftURI.name }}</div>
      <div class="d">
        <span class="price bfont c9" v-if="lowestAsset">
          {{ lowestAsset.singlePrice }}
          {{ lowestAsset.payToken ? lowestAsset.payToken?.symbol : "" }}
          {{ lowestAsset.sellQuantity }} of {{ nft.nft.quantity }}
        </span>
        <span class="nosale bfont c9">
          {{ $t('nftItem.nfs')}} 0 of {{ nft.nft.quantity }}
        </span>

        <span class="bid c6 bfont" v-if="highestBid">
          ·
          {{ highestBid.singlePrice }} {{ highestBid?.payToken?.symbol }}
        </span>

      </div>
    </div>
  </div>
</template>

<script>
  import MixinsNFTInfo from "@/mixins/NftInfo";
  export default {
    name: "MiniNFTItem",
    mixins: [MixinsNFTInfo],
    data: function () {
      return {};
    },
    emits: ["close"],
    props: {
      nft: {
        type: Object,
        default: {},
      },
    },
    computed: {
      lowestAsset () {
        if (!this.nft.owners) return;
        console.log("nft", this.nft);
        let lowestAsset = this.getLowestAsset(this.nft.owners);
        return lowestAsset;
      },
      highestBid () {
        if (!this.nft.bids) return;
        console.log("nft", this.nft);
        return this.getHighestBid(this.nft.bids);
      },
      nftURI () {
        if (this.nft.nft.metadataContent) {
          return this.$tools.analysis(this.nft.nft.metadataContent);
        } else if (this.nft.media) {
          return this.$tools.analysis(this.nft.media);
        }
        return {};
      },
    },
    methods: {
      goDetail () {
        this.$emit("close");
        var ids = this.nft.nft.address + ":" + this.nft.nft.tokenId;
        this.$router.push("/detail/" + ids);
      },
    }
  }
</script>
<style lang="scss" scoped>
  .mini-nft-item {
    display: flex;
    flex-direction: row;
    margin-bottom: 10px;
    .cover {
      width: 50px;
      height: 50px;
      border-radius: 5px;
      overflow: hidden;
      .cover-img {
        width: 100%;
        height: 100%;
      }
    }
    .content {
      display: flex;
      flex-direction: column;
      align-items: flex-start;
      justify-content: space-around;
    }
  }
</style>
