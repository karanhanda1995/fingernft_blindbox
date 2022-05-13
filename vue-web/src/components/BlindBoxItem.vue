<template>
  <div class="blindbox-item">
    <div class="inner" @click="toDetail">
      <div class="cover-padding">
        <div class="cover">
          <div class="blindbox-img cursor-pointer ">
            <el-image class="cover-image" :src="$filters.fullImageUrl(blindbox.imgUrl)" fit="cover">
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
        </div>
        <div class="hot-tip">
          <div class="hot-center">
            <img class="huomiao-icon" src="@/assets/img/home/time.png" alt="" />
            <activity-time-count :startTime="parseInt(blindbox.startTime)" :endTime="parseInt(blindbox.endTime)"></activity-time-count>
          </div>
        </div>
      </div>
      <div class="flex justify-between align-center blindbox-info">
        <div class="width-79">
          <div class="name">{{blindbox.name}}</div>
          <div class="price-text">
            {{ blindbox.price }} {{ blindbox.paytokenSymbol }}
            <span class="grey">
              {{ availableAmount }} of {{blindbox.amount}}</span>
          </div>
        </div>
        <el-button type="default" v-if="availableAmount > 0" class="buy-btn cursor-pointer" size="mini">{{ $t('blindBox.open') }}</el-button>
        <el-button type="default" v-else class="buy-btn disable cursor-pointer" size="mini">{{ $t('blindBox.sellOut') }}</el-button>
      </div>
    </div>
  </div>
</template>
<script>
  import BigNumber from "bignumber.js";
  export default {
    name: "BlindBoxItem",
    components: {},
    props: {
      blindbox: {
        type: Object,
        default: {},
      },
    },
    data () {
      return {};
    },
    computed: {
      connected () {
        return this.$store.state.connected;
      },
      availableAmount () {
        return this.blindbox.amount - this.blindbox.order.openeds;
      },
    },
    methods: {
      toDetail () {
        this.$router.push(
          "/blindbox/detail/" + this.blindbox.id,
        );
      },
    },
  };
</script>

<style lang="scss" scoped>
  .d-right {
    .iconfont {
      font-size: 18px;
      margin-left: 5px;
      color: #aaa;
      cursor: pointer;
      &.active {
        color: $redColor;
      }
    }
  }
  .blindbox-item {
    width: 25%;
    position: relative;
    display: flex;
    flex-direction: column;
    box-sizing: border-box;
    margin-bottom: 20px;
    padding: 0 10px;
    .inner {
      position: relative;
      border-radius: $borderRadius;
      overflow: hidden;
      cursor: pointer;
      --coverWidth: 100%;
    }
    .like {
      position: absolute;
      z-index: 100;
      right: 5px;
      top: 5px;
      .iconfont {
        cursor: pointer;
        margin-right: 5px;
        margin-top: 6px;
        font-size: 18px;
        border-radius: 18px;
        color: #aaa;
        &.active {
          color: $redColor;
        }
      }
    }
    .cover-padding {
      cursor: pointer;
      position: relative;
      background: #fff;
      padding-bottom: calc(var(--coverWidth) / 0.9);
    }
    .cover {
      position: absolute;
      top: 0;
      left: 0;
      width: 100%;
      height: 100%;
      cursor: pointer;
      overflow: hidden;
      display: flex;
      align-items: center;
      justify-content: center;
      border-radius: $borderRadius;
      flex-direction: column;
    }
    .collection-btn {
      width: 23px;
      height: 23px;
      text-align: center;
      line-height: 23px;
      position: absolute;
      right: 0;
      top: 0;
      background: #fff;
      border-bottom-left-radius: 5px;
      font-size: 16px;
      color: #666;
      &.active {
        color: #ffd200;
      }
    }
    .avatar {
      margin-right: 3px;
    }
    .descript {
      padding: 5px 0;
    }
    .d {
      padding: 5px;
      display: flex;
    }
    .d-left {
      display: flex;
      flex: 1;
      align-items: center;
    }
    .d-right {
      display: flex;
      flex: 1;
      align-items: center;
      justify-content: flex-end;
    }
    .bid {
      white-space: nowrap;
      color: $primaryColor;
      cursor: pointer;
      font-weight: 400;
      font-size: 12px;
    }
    .price {
      text-overflow: ellipsis;
      overflow: hidden;
      white-space: nowrap;
      font-size: 12px;
      font-weight: 400;
      color: #1d1e22;
      margin-right: 5px;
    }
    .nosale {
      text-overflow: ellipsis;
      font-size: 12px;
      font-weight: 400;
      color: $grayColor;
    }
    .stock {
      display: flex;

      flex: 1;
      margin-left: 4px;
      margin-right: 6px;
      white-space: nowrap;
      font-size: 12px;
      font-weight: 400;
      color: #999;
      text-align: center;
    }
  }
  .bfont {
    text-overflow: ellipsis;
    overflow: hidden;
    white-space: nowrap;
  }
  .d-top {
    width: 50%;
    flex: 0 !important;
  }

  .more-menu-popover {
    .menu {
      cursor: pointer;
      padding: 8px 7px;
      font-size: 13px;
      font-weight: normal;
      border-radius: $borderRadius;
      &:hover,
      &:active {
        background: #f0f0f0;
      }
    }
  }

  .sale-bid {
    display: flex;
    flex-direction: column;
    button {
      border: none;
      background: none;
      font-size: 14px;
      font-weight: 500;
      color: #1d1e22;
      line-height: 26px;
      cursor: pointer;
      &:hover {
        color: red;
        border-color: inherit;
        background-color: inherit;
      }
      &:focus {
        color: red;
        border-color: inherit;
        background-color: inherit;
      }
    }
  }

  @media screen and (max-width: $media_l1) {
    .blindbox-item {
      width: 25%;
    }
  }

  @media screen and (max-width: $media_l2) {
    .blindbox-item {
      width: 33.33%;
    }
  }

  @media screen and (max-width: $media_l5) {
    .blindbox-item {
      width: 50%;
    }
  }
  @media screen and (max-width: $media_l6) {
    .blindbox-item {
      width: 100%;
    }
  }

  .margin-bottom-xs {
    margin-bottom: 10px;
  }
  .cursor-pointer {
    cursor: pointer;
  }

  .blindbox-img {
    width: 100%;
    height: 100%;
    border-top-left-radius: 5px;
    border-top-right-radius: 5px;
  }

  .biankuang-box {
    width: 100%;
  }

  .cover-image {
    border-top-left-radius: 5px;
    border-top-right-radius: 5px;
  }

  .good-name {
    position: relative;
    font-family: Arial;
    left: 150px;
    bottom: 50px;
    font-size: 16px;
    color: #000;
  }

  .price-text {
    text-align: left;
    font-size: 14px;
    font-weight: bold;
    color: $primaryColor;
    margin-top: 2px;
    .grey {
      margin-left: 5px;
      color: rgba(153, 153, 153, 1);
    }
  }

  .buy-btn {
    background: linear-gradient(90deg, #d3721c 0%, #8da419 100%);
    border-radius: 3px;
    font-size: 14px;
    color: #fff;
    &.disable {
      background: linear-gradient(90deg, #c4c8c9 0%, #a0a1a3 100%) !important;
    }
    &:hover {
      color: #fff;
    }
  }
  .align-center {
    align-items: center;
  }
  .status-box {
    left: 0;
    right: 0;
    bottom: 0;
    position: absolute;
    .relative {
      position: relative;
      .text {
        color: white;
        position: absolute;
        z-index: 1;
        left: 50%;
        top: 50%;
        transform: translate(-31%, -50%);
      }
    }
  }

  .blindbox-info {
    padding: 5px;
    background: #fff;
    padding-top: 15px;
    .name {
      font-size: 16px;
      line-height: 30px;
      font-weight: bold;
      color: #333;
      width: 90%;
      text-overflow: ellipsis;
      overflow: hidden;
      white-space: nowrap;
    }
  }
  .hot-tip {
    color: #fff;
    font-weight: 600;
    position: absolute;
    bottom: 0;
    left: 0;
    width: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
    .hot-center {
      margin: 0 auto;
      display: inline-flex;
      align-items: center;
      padding: 5px 20px;
      background-color: #f06f6f;
      justify-content: center;
      border-radius: 20px;
      line-height: 30px;
      height: 30px;
      margin-bottom: -15px;
    }
    .huomiao-icon {
      width: 17px;
      height: 16px;
      margin-right: 5px;
    }
  }
  .right-bottom-img {
    color: #fff;
    height: 35px;
    padding: 5px;
    background: $primaryColor;
    background-size: cover;
    display: flex;
    align-items: center;
    justify-content: center;
  }
  .nft-image-skeleton {
    width: 90%;
    height: 90%;
  }
  .width-79 {
    width: 79%;
  }
</style>
