<template>
<div class="fullscreen-inner">
  <div class="input-section">
    <div class="inner">
      <div class="left">
        <el-input
          class="search-input"
          v-model="keyword"
          @keyup.enter="searchClick"
          @input="onInput"
          :placeholder="$t('navigation.searchTip')"
        >
          <template #prefix>
            <span class="iconfont icon-search"></span>
          </template>
        </el-input>
      </div>
      <div class="right">
        <div class="close-link" @click="close">
          <img class="close-img" src="@/assets/create-img/pop_shut.png ">
        </div>
      </div>
    </div>
  </div>
  <div class="output-section">
    <div class="no-search" v-if="!keyword">
      {{ $t('search.inputTip')}}
    </div>
    <div class="results-container" v-else>
      <div class="results" v-if="nftList.length > 0">
        <div class="title">{{ $t('search.nfts') }}</div>
        <mini-nft-item v-for="(nft, i) in nftList" :nft="nft" :key="i" @close="close">
        </mini-nft-item>
      </div>
      <div class="results" v-if="accountList.length > 0">
        <div class="title">{{ $t('search.users') }}</div>
        <mini-account-item v-for="(account, i) in accountList" :account="account" :key="i" @close="close">
        </mini-account-item>
      </div>

      <div class="cpointer bfont f15 results-link" @click="searchClick"> All results </div>

    </div>
  </div>
</div>
</template>
<script>
import NftItem from '@/mixins/NftItem';
import MiniNftItem from  './items/MiniNFTItem';
import MiniAccountItem from './items/MiniAccountItem';
export default {
  name: "HeaderSearch",
  components:{
    MiniNftItem,
    MiniAccountItem,
  },
  mixins: [NftItem],
  data: function(){
    return {
      keyword: "",
      results: [],
      accountList: [],
      nftList: [],
    }
  },
  methods: {
    async onInput(keyword){
      this.getNFTs(keyword);
      this.getAccounts(keyword);
    },
    searchClick(){
      this.close();
      this.$router.push({ name: "Search", query: { keyword: this.keyword } });
    },
    close(){
      this.$store.commit("FULLSCREEN", { show: false, component: "" });
    },
    getNFTs(keyword){
      var data = {
        search: keyword,
        page: 1,
        limit: 5,
      }
      var that = this;
      return new Promise(function(resolve, reject){
        that.$api("home.search", data).then((res) => {
          if(that.$tools.checkResponse(res)){
            that.nftList = res.data.list;
            that.queryFunction(res.data.list);
          }
        });
      });
    },
    getAccounts(keyword){
      var data = {
        search: keyword,
        page: 1,
        limit: 5
      }
      var that = this;
      return new Promise(function(resolve, reject){
        that.$api("home.searchuser", data).then((res) => {
          if(that.$tools.checkResponse(res)){
            that.accountList = res.data.list;
          }
          resolve(res);
        });
      });
    }

  },
}
</script>
<style lang="scss">
.fullscreen-inner{
  .search-input{
    background: #f0f0f0;
    border-radius: 32px;
    overflow: hidden;
    border: $border;
    line-height: 32px;
    .el-input__inner{
      height: 32px;
      background: #f0f0f0;
      padding-left: 38px;
    }
    .el-input__prefix{
      font-size: 25px;
    }
  }
}
</style>
<style lang="scss" scoped>
.input-section{
  padding: 20px 20px 0;
  .inner{
    display: flex;
    flex-direction: row;
    padding-bottom: 20px;
    border-bottom: $border;
  }
  .left{
    flex: 1;
    display: flex;
  }
  .right{
    padding-left: 10px;
    .close-link{
      height: 32px;
      display: flex;
      align-items: center;
      justify-content: center;
      line-height: 32px;
      cursor: pointer;
      .close-img{
        height: 100%;
      }
    }
  }
}
.output-section{
  padding: 20px;
  .no-search{
    font-size: 16px;
    color: #777;
    text-align: center;
    padding: 20px 0;
  }
  .results{
    .title{
      color: #777;
      font-size: 14px;
      margin-bottom: 10px;
    }
  }
  .results-link{
    border: $border;
    padding: 10px;
    border-radius: 50px;
    text-align: center;
    margin-top: 20px;
  }
}
</style>


