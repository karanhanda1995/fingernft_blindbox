<template>
  <div class="user-avatar">
    <avatar @click="visible=true" class="avatar-img" :imageUrl="$filters.fullImageUrl(user.avatar)" :address="user.coinbase" :imgWidth="30" :imgHeight="30" shape="circular">
    </avatar>
    <el-dialog v-model="visible" :show-close="false" :close-on-click-modal="false" :modal="false" custom-class="fg-dialog" destroy-on-close>
      <template #title>
        <div class="fg-dialog-title">
          <div class="left">
            <div class="profile-header">
              <div @click="goItems" class="cpointer bfont c3" v-if="user && user.nickname"> {{ user.nickname }}</div>
              <div class="cpointer c6 f15">
                <span @click="goItems"> {{ $filters.ellipsisAddress(user.coinbase) }} </span>
                <span class="el-icon-copy-document m-left-5" v-clipboard:copy="user.coinbase" v-clipboard:success="onSuccessCopy" v-clipboard:error="onErrorCopy"></span>
              </div>
              <div @click="goProfile" class="cpointer cprimary bfont f12" v-if="!user || !user.nickname">
                {{ $t('navigation.sdn')}}
              </div>
            </div>

          </div>
          <div class="right" @click="visible=false">
            <img class="close-img" src="@/assets/create-img/pop_shut.png ">
          </div>
        </div>
      </template>
      <div class="fg-dialog-body">

        <div class="profile-wrapper">
          <div class="paytoken-item">
            <img class="paytoken-img" src="@/assets/img/FINGER.jpg" />
            <div class="m-left-10">
              <div class="paytoken-t">
                {{ $t("navigation.balance") }}
              </div>
              <div class="paytoken-v">
                <div class="c3">
                  {{ erc20Balance.balance }}
                  {{ defaultPaytoken.symbol }}
                </div>
              </div>
            </div>
          </div>

          <div class="profile-line"></div>

          <router-link class="link-item" to="/items">
            {{ $t("navigation.myItems") }}</router-link>
          <div class="link-item" @click="goProfile">
            {{ $t("navigation.editProfile") }}
          </div>
          <div class="link-item" @click="logout">
            {{ $t("navigation.disconnect") }}
          </div>

        </div>

      </div>

    </el-dialog>
  </div>
</template>
<script>
  export default {
    name: "ProfileDialog",
    data () {
      return {
        visible: false,
      };
    },
    computed: {
      user: function () {
        return this.$store.state.user;
      },
      defaultPaytoken () {
        return this.$store.getters.defaultSalePayToken();
      },
      erc20Balance () {
        let paytoken = this.$store.getters.payToken(this.defaultPaytoken.address);
        if (!paytoken) return { balance: 0 };
        let balance = this.$store.getters.getBalance(this.defaultPaytoken.address);
        balance = !balance ? 0 : balance;
        return {
          balance: this.$tools.decimal(balance, 3)
        };
      },
    },
    methods: {
      goItems () {
        this.$router.push({ name: "Items" });
      },
      goProfile () {
        if (!this.$tools.needLogin()) return;
        this.$router.push("/profile");
      },
      onSuccessCopy () {
        this.$tools.message(this.$t("request.copySuccess"), "success");
      },
      onErrorCopy () {
        this.$tools.message(this.$t("request.copyError"));
      },
      logout () {
        this.$web3.disconnect();
      },
    },
  }
</script>

<style lang="scss" scoped>
  .user-avatar {
    display: flex;
    align-items: center;
    height: 40px;
  }

  .profile-header {
    display: flex;
    flex-direction: column;
    line-height: initial;
  }

  .profile-wrapper {
    display: flex;
    flex-direction: column;
    line-height: initial;
    .profile-wrapper-inner {
      display: flex;
      align-items: center;
      flex-wrap: wrap;
    }

    .avatar-img {
      display: flex;
    }

    .paytoken-item {
      display: flex;
      align-items: center;
      padding-bottom: 20px;
      .paytoken-img {
        width: 35px;
        height: 35px;
        border-radius: 50%;
      }
      .paytoken-t {
        font-size: 12px;
        font-weight: 400;
        color: #999;
      }
      .paytoken-v {
        display: flex;
        flex-direction: column;
        align-items: flex-start;
      }
      .usdt {
        background: #eeeeee;
        border-radius: 5px;
        padding: 0 5px;
        font-size: 12px;
        line-height: 20px;
      }
    }

    .link-item {
      cursor: pointer;
      font-weight: 400;
      margin-top: 15px;
    }

    .profile-line {
      margin: 10px 0;
      border: 1px solid #eeeeee;
    }
  }
</style>

