<template>
  <div v-if="isFound" class="blindbox-wrapper">
    <div class="boxs" :style=" 'background: url('+$filters.fullImageUrl(blindbox.imgUrl)+') no-repeat; background-size: cover;' ">
    </div>
    <div class="mask-box">
    </div>
    <div class="preview-section">
      <div class="preview-section-image">
        <nft-preview :image="blindbox.imgUrl"></nft-preview>
      </div>
    </div>
    <el-skeleton :loading="loading" animated>
      <template #template>
        <div class="info-section">
          <div class="info-section-inner">
            <div class="info-main">
              <div class="info-base">
                <el-skeleton-item class="nft-name el-skeleton-item1" /><br>
                <el-skeleton-item class="nft-name el-skeleton-item2" /><br>
                <el-skeleton-item class="nft-name el-skeleton-item3" /><br>
              </div>
              <el-skeleton-item class="nft-name el-skeleton-item4" /><br>
              <el-skeleton-item class="nft-name el-skeleton-item5" /><br>
              <el-skeleton-item class="nft-name el-skeleton-item4" /><br>
              <el-skeleton-item class="nft-name el-skeleton-item6 m-top-10" /><br>
              <el-skeleton-item class="nft-name el-skeleton-item6" /><br>
            </div>
            <div class="info-footer-wrap">
            </div>
          </div>
        </div>
      </template>
      <template #default>
        <div class="info-section">
          <div class="info-section-inner">
            <div class="info-main">
              <div class="info-base">
                <div class="flex justify-between align-center m-bottom-10">
                  <div class="nft-name">{{blindbox.name}}</div>
                  <el-popover placement="left" trigger="click" width="220px" :show-arrow="false" popper-class="share-popover">
                    <template #reference>
                      <img class="ico-share cpointer" src="@/assets/img/nft/ico_share.png" />
                    </template>
                    <share></share>
                  </el-popover>
                </div>
                <div class="nft-metas">
                  <span class="nft-meta">{{ blindbox.price }} {{ blindbox.paytokenSymbol}}</span>
                  <span class="nft-meta cblue"> {{ availableAmount }} of {{blindbox.amount}}</span>
                </div>
              </div>
              <div class="description">
                {{ blindbox.describe || $t('blindboxDetail.brief') }}
              </div>
              <div class="creator-box flex justify-between align-center" v-if="creator.address">
                <div class="text">{{$t('blindboxDetail.creator')}}</div>
                <div class="flex align-center cpointer" @click="goAccount(creator.address)">
                  <avatar class="u-info-avatar" :imageUrl="$filters.fullImageUrl(creator.avatar)" :address="creator.address" :imgWidth="35" :imgHeight="35" shape="circular"></avatar>
                  <div class="name x-text-1b1b1b">
                    {{ creator.nickname || $filters.ellipsisAddress(creator.address) }}
                  </div>
                </div>
              </div>
              <div class="creator-box flex justify-between align-center" v-if="$sdk.keyAssetType(blindbox.paytokenType) == 'ERC721' || $sdk.keyAssetType(blindbox.paytokenType) == 'ERC1155'">
                <div class="creator-text">{{$t('blindboxDetail.payToken')}}</div>
                <div class="flex align-center cpointer" @click="goNFT">
                  <avatar class="u-info-avatar"
                      :imageUrl="$filters.fullImageUrl(metadata.image)"
                      :address="blindbox.paytokenAddress"
                      :imgWidth="35" :imgHeight="35" shape="circular"
                      ></avatar>
                  <div class="name x-text-1b1b1b">
                    {{ metadata.name || $filters.ellipsisAddress(blindbox.paytokenAddress) }}
                  </div>
                </div>
              </div>
              <el-tag class="blind-tag" type="success">{{$t('blindboxDetail.oneBoxContain')}}{{blindbox.nftAmount}}</el-tag>
              <el-tag class="blind-tag" :type="blindbox.isRepetition?'success':'info'">{{blindbox.isRepetition?$t('blindboxDetail.repeat'):$t('blindboxDetail.noRepeat')}}</el-tag>
              <div class="tab-section">
                <el-tabs v-model="tabIndex" @tab-click="clickTab">
                  <el-tab-pane :label="$t('nftDetail.info')" name="info">
                    <info-tab :nfts="nfts" :blindbox="blindbox"></info-tab>
                  </el-tab-pane>
                  <el-tab-pane v-if="historys.length" :label="$t('nftDetail.history')" name="history">
                    <history-tab :historys="historys" :blindbox="blindbox"></history-tab>
                  </el-tab-pane>
                </el-tabs>
              </div>
            </div>
            <div class="info-footer-wrap">
              <div class="info-footer">
                <div class="info-footer-body">
                  <el-button class="cpointer" type="primary" disabled v-if="status == 'begin'">
                    {{$t('dialog.open')}}
                  </el-button>

                  <template v-else-if="status == 'end'">
                    <el-button class="cpointer" type="primary" v-if="availableAmount > 0" @click="showOpen = true">
                      {{$t('dialog.open')}}
                    </el-button>
                    <el-button class="cpointer" disabled type="primary" v-else>
                      {{$t('dialog.sellOut')}}
                    </el-button>

                  </template>
                  <el-button class="cpointer" type="primary" v-else disabled>
                    {{$t('dialog.finished')}}
                  </el-button>

                </div>
                <div class="blind-box-state" v-if="status == 'begin' || status == 'end' ">
                  <activity-time-count :startTime="parseInt(blindbox.startTime)" :endTime="parseInt(blindbox.endTime)"></activity-time-count>
                </div>
              </div>
            </div>
          </div>
        </div>
      </template>
    </el-skeleton>
    <open-dialog @confirm="showOpen = false" :show="showOpen" :nfts="nfts" :blindbox="blindbox" @close="showOpen = false"></open-dialog>
  </div>
  <div class="no-found" v-else>
    <no-found></no-found>
  </div>
</template>

<script>
  import InfoTab from "@/views/sections/blindbox/details/Info";
  import HistoryTab from "@/views/sections/blindbox/details/History";
  import Share from "@/components/Share";
  import NftPreview from "@/components/NFTPreview";


  import BigNumber from "bignumber.js";
  export default {
    name: "NFTDetail",
    components: {
      NftPreview,
      InfoTab,
      HistoryTab,
      Share,
    },
    data: function () {
      var ids = this.$route.params.ids;
      return {
        showOpen: false,
        loading: true,
        token: {
          id: ids,
        },
        tabIndex: "info",
        creator: {},
        blindbox: {},
        historys: [],
        nfts: [],
        isFound: true,
      };
    },
    created () {
      this.getDetails();
    },
    computed: {
      metadata(){
        if(!this.blindbox.paytokenMetadataContent) return {};
        var metadata = JSON.parse(this.blindbox.paytokenMetadataContent);
        return metadata;
      },
      user () {
        return this.$store.state.user;
      },
      connected () {
        return this.$store.state.connected;
      },
      status () {
        if (!this.blindbox.startTime) {
          return ""
        };
        let now = new Date();
        now = parseInt(now.getTime() / 1000);
        if (this.blindbox.startTime > now) {
          return "begin";
        } else if (this.blindbox.startTime < now && this.blindbox.endTime > now) {
          return "end";
        }
        return "over";
      },
      availableAmount () {
        return this.blindbox.amount - this.blindbox.order.openeds;
      },
    },
    methods: {
      goAccount (address) {
        this.$router.push({ path: "/account/" + address });
      },
      goNFT () {
        var ids = this.blindbox.paytokenAddress + ":" + this.blindbox.paytokenTokenId;
        this.$router.push("/detail/" + ids);
      },
      getDetails () {
        var data = {
          blindBoxId: this.token.id,
        };
        let that = this;
        this.$api("blind.box.detail", data).then(async function (res) {
          that.loading = false;
          if (that.$tools.checkResponse(res)) {
            if (!res.data) {
              that.isFound = false;
            }
            that.blindbox = res.data;
            that.creator.address = that.blindbox.order.owner;
            that.getNfts();
            that.getHistory();
            that.getCreator();
          } else {
            that.$tools.message(res.errmsg);
          }
        });
      },
      getCreator () {
        let params = {
          address: this.creator.address
        }
        this.$api("user.info", params).then((res) => {
          if (this.$tools.checkResponse(res)) {
            if (JSON.stringify(res.data) != "{}") {
              this.creator = res.data;
            }
          } else {
            this.$tools.message(res.errmsg);
          }
        });
      },
      getNfts () {
        var data = {
          blindBoxId: this.token.id,
        }
        let that = this;
        this.$api("blind.box.nfts", data).then(async function (res) {
          if (that.$tools.checkResponse(res)) {
            that.nfts = res.data.list;
          }
        });
      },
      getHistory () {
        var data = {
          blindBoxId: this.token.id,
        }
        let that = this;
        this.$api("blind.box.history", data).then(async function (res) {
          if (that.$tools.checkResponse(res)) {
            that.historys = res.data.list;
            // that.getHistoryBuyers();
          }
        });
      },
      getHistoryBuyers () {
        let addrList = [];
        for (var i = 0;i < this.historys.length;i++) {
          var history = this.historys[i];
          addrList.push(history.buyer);
        }
        addrList = addrList.join(",");
        if (!addrList) return;
        let params = {
          addrList: addrList,
        }
        let that = this;
        this.$api("user.listbyaddr", params).then((res) => {
          if (that.$tools.checkResponse(res)) {
            let addresss = {};
            for (var i = 0;i < res.data.length;i++) {
              let address = res.data[i];
              addresss[address.address.toLocaleLowerCase()] = address;
            }
            for (var i = 0;i < that.historys.length;i++) {
              let history = that.historys[i];
              that.historys[i].user = addresss[history.buyer.toLocaleLowerCase()];
            }
            that.historys = [].concat(that.historys);
          }
        });
      },
    },
  };
</script>
<style lang="scss" scoped>
  .blindbox-wrapper {
    width: 100% !important;
    background: #000;
    max-width: none;
    padding: 0;
    justify-content: flex-end;
    position: fixed;
    left: 0;
    right: 0;
    top: 0;
    bottom: 0;
  }

  .align-center {
    align-items: center !important;
  }
  .ico-share {
    width: 14px;
    height: 14px;
  }
  .creator-box {
    background: #f3f4fa;
    border-radius: $borderRadius;
    padding: 10px;
    margin: 10px 0px;
    .text {
      color: #929292;
    }
    .name {
      margin-left: 8px;
    }
  }

  .boxs {
    position: absolute;
    top: 0;
    bottom: 0;
    left: 0;
    right: 0;
    filter: blur(30px);
  }

  .mask-box {
    position: absolute;
    top: 0;
    bottom: 0;
    left: 0;
    right: 0;
    background-color: rgba(0, 0, 0, 0.7);
  }

  .preview-section {
    position: fixed;
    left: 0;
    right: 474px;
    bottom: 0;
    top: $headerHeight;
  }
  .info-section {
    width: 474px;
    position: fixed;
    right: 24px;
    top: 88px;
    bottom: 24px;
    overflow: hidden;
    border-radius: 5px;
    background: #fff;
    .info-section-inner {
      height: 100%;
      overflow-y: auto;
    }
  }

  .preview-section-image {
    width: 100%;
    height: 100%;
    display: flex;
    padding: 40px;
    justify-content: center;
    padding-right: 80px;
  }

  .info-main {
    padding: 20px 20px 200px;
  }

  .info-base {
    display: flex;
    flex-direction: column;
  }

  .description {
    margin: 14px 0 10px;
    font-size: 8px;
    font-weight: 400;
    color: #333;
  }

  .nft-name {
    font-size: 18px;
    font-weight: bold;
    color: #333;
    padding-bottom: 10px;
  }

  .nft-metas {
    display: flex;
    padding-bottom: 10px;
    flex-wrap: wrap;
    .nft-meta {
      line-height: 20px;
      font-size: 9px;
      font-weight: bold;
      margin-right: 10px;
      &.b {
        font-weight: bold;
      }
      &.c3 {
        color: #333;
      }
      &.c9 {
        color: #999;
      }
      &.cblue {
        color: $primaryColor;
      }
    }
  }

  .info-footer-wrap {
    display: flex;
    position: absolute;
    bottom: 0;
    left: 0;
    right: 0;
    height: 120px;
    border-top: $border;
    background: #fff;
    z-index: 2;
    box-shadow: rgb(4 4 5 / 5%) 0px -5px 12px;
    flex-direction: column;
  }
  .info-footer {
    padding: 10px 20px;
    border-radius: 5px;
    width: 100%;
    display: flex;
    flex-direction: column;
    justify-content: center;
    flex: 1;
    .blind-box-state {
      display: flex;
      padding-top: 10px;
      align-items: center;
      justify-content: center;
    }
  }

  .info-footer-body {
    display: flex;
    justify-content: center;
    .el-button {
      flex: 1;
    }
  }

  .no-found {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 100vw;
    height: 100vh;
  }

  .el-skeleton-item1 {
    width: 300px !important;
    height: 22px !important;
  }
  .el-skeleton-item2 {
    width: 180px !important;
    height: 15px !important;
  }
  .el-skeleton-item3 {
    width: 50px !important;
    height: 15px !important;
  }
  .el-skeleton-item4 {
    width: 180px !important;
    height: 15px !important;
  }
  .el-skeleton-item5 {
    width: 300px !important;
    height: 18px !important;
  }
  .el-skeleton-item6 {
    margin-bottom: 10px !important;
    width: 434px !important;
    height: 54px !important;
  }

  .blind-tag {
    font-size: 12px;
    margin-right: 10px;
    letter-spacing: -0.5px;
    height: 24px !important;
    padding: 0 6px !important;
    line-height: 22px !important;
  }

  @media screen and (max-width: $media_l2) {
    .preview-section {
      right: 480px;
    }
    .info-section {
      width: 480px;
    }
    .info-footer-wrap {
      width: 480px;
    }
  }

  @media screen and (max-width: $media_l5) {
    .blindbox-wrapper {
      position: initial;
      left: auto;
      right: auto;
      top: auto;
      bottom: auto;
      padding-top: 60px;
      .boxs {
        display: none !important;
      }
      .mask-box {
        display: none !important;
      }
      .preview-section {
        position: inherit;
        left: auto;
        right: auto;
        bottom: auto;
        padding: 20px;
        .preview-section-image {
          padding: 0 0 20px;
        }
        .preview-menus {
          position: initial;
          left: auto;
          right: auto;
          bottom: auto;
          margin: 0;
          padding: 10px 0;
          background: transparent;
          box-shadow: none;
        }
      }
      .info-section {
        position: inherit;
        left: auto;
        right: auto;
        top: auto;
        bottom: auto;
        overflow: auto;
        width: auto;
        .info-section-inner {
          height: auto;
          overflow: auto;
        }
      }
      .info-footer-wrap {
        width: auto;
        position: fixed;
      }
    }
  }
</style>

<style lang="scss">
  .tab-section {
    min-height: 400px !important;
    .el-tabs__header {
      margin-bottom: 20px;
    }
    .el-tabs__item {
      color: $grayColor;
      height: auto;
      line-height: 30px;
      padding: 0 8px;
      font-weight: 400;
      &:hover,
      &:active,
      &:focus,
      &.is-active,
      &is-focus {
        box-shadow: none !important;
        color: #333;
      }
    }
    .el-tabs__active-bar {
      background-color: #333;
    }
  }
</style>

