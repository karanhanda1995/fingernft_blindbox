<template>
  <div class="detail-wrapper">
    <side :stat="stat" @refreshStat="getUserStat" />
    <div class="right-section">
      <div class="item-page main-wrapper">
        <div class="from-section">
          <div class="title">{{$t('items.bind')}}</div>
          <div class="form-item">
            <div class="form-label">{{ $t('items.email') }}</div>
            <div class="form-value">
              <el-input class="form-input"></el-input>
            </div>
          </div>

          <div class="form-item">
            <div class="form-label">{{ $t('item.password') }}</div>
            <div class="form-value">
              <el-input type="password" class="form-input"></el-input>
            </div>
          </div>

          <div class="form-item">
            <el-button type="primary">{{ $t('items.bind') }}</el-button>
          </div>

        </div>


      </div>
    </div>
  </div>
</template>
<script>

import Side from "./components/Side";


export default {
  name: "Deposit",
  components: {
    Side,
  },
  data() {
    return {
      stat: {},
      loadStatus: "",
    };
  },
  created() {
    this.init();
  },
  computed: {
    user() {
      return this.$store.state.user;
    },
  },
  methods: {
    init() {
      this.getUserStat();
    },
    goHome() {
      this.$router.push("/");
    },
    getUserStat() {
      let params = { address: this.user.coinbase };
      this.$api("user.stat", params).then((res) => {
        if (this.$tools.checkResponse(res)) {
          this.stat = Object.assign( {},
            {
              followingCount: !res.data.followingCount ? 0 : res.data.followingCount,
              followCount: !res.data.followCount ? 0 : res.data.followCount,
            }
          );
        }
      });
    },
  },
};
</script>
<style lang="scss" scoped>

.from-section{
  padding-bottom: 200px;
  .title{
    font-size: 22px;
    font-weight: bold;
  }
  .form-item-group{
    display: flex;
  }
  .form-item{
    display: flex;
    flex: 1;
    flex-direction: column;
    margin-top: 40px;
  }
  .form-label{
    font-size: 14px;
    color: #999;
    margin-bottom: 5px;
  }
  .form-value{
  }
  .form-input{
    box-shadow: 5px 5px 10px #ebebeb;
    width: 100%;
  }
  .max-icon{
    background: $primaryColor;
    color: #fff;
    padding: 2px 5px;
    border-radius: 5px;
    margin-right: 10px;
    cursor: pointer;
  }
}

.network-arrow{
  font-size: 60px;
  color: #ddd;
  padding: 0 40px 0;
  margin-top: 62px;
  display: flex;
  align-items: center;
}

.network {
  border: 1px solid #f0f0f0;
  border-radius: 10px;
  box-shadow: 5px 5px 10px #ebebeb;
  .network-inner{
    padding: 20px 50px;
    display: flex;
  }
  .network-logo{
    width: 35px;
    height: 35px;
    border-radius: 35px;
    overflow: hidden;
    .el-image{
      width: 100%;
    }
  }
  .network-info{
    margin-left: 5px;
    font-size: 16px;
    font-weight: bold;
  }
}


.item-page {
  display: flex;
  flex-direction: column;
  margin: 3% auto;
}

@media screen and (max-width: $media_l3){
  .item-page {
    &.main-wrapper{
      padding-top:0;
      padding-bottom: 0;
    }
  }

  .item-tab{
    margin: 0;
  }
}

</style>

<style lang="scss">

.user-tab-menus {
  .el-tabs__item {
    color: #999;
    height: 35px;
    line-height: 35px;
    &:hover,
    &:active,
    &:focus {
      color: #333;
    }
    &.is-active {
      color: #333;
    }
  }
  .el-tabs__active-bar {
    background-color: #333;
  }
}
.el-tabs__active-bar {
  height: 0px !important;
}

.center-suffix{
  .el-input__suffix{
    display: flex;
    align-items: center;
  }
}

</style>
