<template>
  <el-dialog :model-value="visible" :show-close="false" :close-on-click-modal="false" :custom-class=" blindBoxSigned && isOver && openList.length > 2 ? 'fg-dialog bg-fg-dialog' : 'fg-dialog' " @close="$emit('close')" @closed="closed" @open="onOpen" destroy-on-close>
    <template #title>
      <div class="fg-dialog-title">
        <div class="left">
          <span>{{ blindbox.name }}</span>
        </div>
        <div class="right" @click="$emit('close')">
          <img class="close-img" src="@/assets/create-img/pop_shut.png ">
        </div>
      </div>
    </template>
    <div class="fg-dialog-body" v-if="!confirm">
      <div class="all-error" v-if="formError.all"> {{ formError.all }} </div>
      <div class="process">
        <div class="step-info">
          <div class="text">
            <span>{{$t('dialog.open')}}</span>
          </div>
        </div>

        <div class="input-group">
          <div class="input-info">
            <div class="tip">{{$t('dialog.salePrice')}}</div>
            <el-input class="fg-dialog-input" placeholder="0.0" :disabled="true" type="number" v-model="form.price">
              <template #suffix>
                <div class="paytoken-suffix">{{ payToken.symbol }}</div>
              </template>
            </el-input>

            <div class="stip">
              <div class="stip-item">{{$t('dialog.yourBalance')}} <span class="bfont">{{ pay.balance }} {{ payToken.symbol }}</span> </div>
              <div class="stip-item">{{$t('dialog.serviceFee')}} <span class="bfont">{{serviceFee}}%</span> </div>
              <div class="stip-item"> {{$t('dialog.pay')}}
                <span class="bfont"> {{pay.pay}} {{ payToken.symbol }}</span>
              </div>
            </div>
          </div>

          <div class="input-info">
            <div class="tip"><span>{{$t('dialog.buyQuantity')}}</span>
              <span class="gray-font">({{ !blindbox.order.openeds ?  blindbox.amount : blindbox.amount - blindbox.order.openeds}} {{$t('dialog.available')}})</span>
            </div>
            <el-input class="fg-dialog-input" placeholder="0.0" type="number" v-model="form.quantity">
            </el-input>

            <div class="input-error" v-if="formError.quantity">{{ formError.quantity}}</div>
          </div>
        </div>

        <div class="process-btn">
          <template v-if="pay && parseFloat(pay.pay) > parseFloat(pay.balance)">
            <el-button @click="onSubmit" disabled type="primary" :loading="confirm"> {{ $t('dialog.open')}}
            </el-button>
            <div class="input-error center-font">{{$t('dialog.noBalance')}}</div>
          </template>
          <template v-else>
            <el-button @click="onSubmit" type="primary" :loading="confirm">{{$t('dialog.open')}}</el-button>
          </template>

        </div>
        <div class="process-btn">
          <el-button plain type="info" @click="$emit('close')">{{$t('dialog.cancel')}}
          </el-button>
        </div>
      </div>
    </div>

    <div class="fg-dialog-body" v-else-if="confirm && !blindBoxSigned">
      <div class="all-error" v-if="error.all"> {{ error.all }} </div>

      <div class="process" v-if="isContract">
        <div class="step-info">
          <div class="text">
            <span>{{$t('dialog.approve')}} {{ payToken.symbol }}</span>
          </div>
          <span v-if="step.approve != 1" :class="step.approve== 2 ? 'finish': ''" class="step iconfont icon-seleted"></span>
          <div class="step loading" v-else>
            <img class="loading-animation" src="@/assets/img/loading.png" />
          </div>

        </div>
        <div class="process-btn">
          <el-button @click="onApprove" type="primary" v-if="step.approve == 0">{{$t('dialog.approve')}}</el-button>
          <el-button disabled type="info" v-else-if="step.approve == 1">{{$t('dialog.inProgress')}}</el-button>
          <el-button disabled type="info" v-else>{{$t('dialog.done')}}</el-button>
        </div>
        <div class="process-error" v-if="error.approve">{{ error.approve }}</div>
      </div>

      <div class="process">
        <div class="step-info">
          <div class="text">
            <span>{{$t('dialog.sto')}}</span>
          </div>
          <span v-if="step.open != 1" :class="step.open == 2 ? 'finish': ''" class="step iconfont icon-seleted"></span>
          <div class="step loading" v-else>
            <img class="loading-animation" src="@/assets/img/loading.png" />
          </div>
        </div>

        <div class="process-btn" v-if="!isContract">
          <el-button @click="onBuy" type="primary" v-if="step.open == 0">{{$t('dialog.open')}}</el-button>
          <el-button disabled type="info" v-else-if="step.open == 1">{{$t('dialog.inProgress')}}</el-button>
          <el-button disabled type="info" v-else>{{$t('dialog.done')}}</el-button>
        </div>

        <div class="process-btn" v-else>
          <el-button disabled type="info" v-if="step.approve != 2">{{$t('dialog.open')}}</el-button>
          <el-button @click="onBuy" type="primary" v-else-if="step.open == 0">{{$t('dialog.open')}}</el-button>
          <el-button disabled type="info" v-else-if="step.open == 1">{{$t('dialog.inProgress')}}</el-button>
          <el-button disabled type="info" v-else>{{$t('dialog.done')}}</el-button>
        </div>
        <div class="process-error" v-if="error.open">{{ error.open }}</div>
      </div>

    </div>
    <div v-else-if="confirm && blindBoxSigned">
      <div class="middle-box">
        <div v-if="!isOver" class="open-status">
          <img v-if="!isExplosion" :src="openingGif" class="videoClass" />
          <img v-else :src="openedGif" class="videoClass" />
        </div>
        <div class="flex justify-around open-nft-items" :class="openList.length > 5 ? 'flex-wrap' : ''" v-else>
          <div class="open-nft-item" v-for="(nft, i) in openList" :key="i">
            <img class="nft-img" :src="$filters.fullImageUrl( $tools.analysis(nft.metadataContent).image)" />
            <div class="nft-name">{{ nft.name }}</div>
          </div>
        </div>
        <div v-if="!isOver" class="describe-box">{{ $t('dialog.openTip') }}</div>
      </div>

      <div class="form-footer ">
        <el-button v-if="!isOver" class="open-btn" :loading="loading" type="primary" disabled>
          {{$t('dialog.opening')}}</el-button>
      </div>
    </div>
  </el-dialog>
</template>

<script>
  import BigNumber from "bignumber.js";
  export default {
    emits: ["confirm", "close"],
    name: "",
    data () {
      return {
        visible: this.show,
        form: {
          price: "",
          quantity: "",
        },
        confirm: false,
        formError: {
          all: "",
          quantity: "",
        },
        step: {
          approve: 0,
          open: 0,
        },
        error: {
          all: "",
          approve: "",
          open: "",
        },
        openList: [],
        isOver: false,
        isExplosion: false,
      };
    },
    props: {
      show: {
        type: Boolean,
        default: false,
      },
      blindbox: {
        type: Object,
        default: {},
      },
      nfts: {
        type: [Array, Object],
        default: [],
      }
    },
    watch: {
      show (val) {
        this.visible = this.show;
      },
    },
    computed: {
      blindBoxSigned () {
        return this.$store.state.blindBoxSigned;
      },
      user () {
        return this.$store.state.user;
      },
      config () {
        return this.$store.state.config;
      },
      payToken () {
        let payToken = {
          address: this.blindbox.paytokenAddress,
          name: this.blindbox.paytokenName,
          symbol: this.blindbox.paytokenSymbol,
          type: this.blindbox.paytokenType,
          tokenId: this.blindbox.paytokenTokenId,
          decimals: this.blindbox.paytokenDecimals
        }
        return payToken;
      },
      pay () {
        let price;
        if (!this.form.price) {
          return { pay: 0, balance: 0 }
        }
        price = this.form.price * this.form.quantity;

        let balance = this.$store.getters.getBalanceV2(this.payToken);
        balance = this.$tools.decimal(balance, 4)

        let pay = this.$tools.decimal(price + (price * this.serviceFee / 100));
        return { pay, balance }
      },
      serviceFee () {
        let fee = this.$tools.decimal(this.blindbox.order.buyerfee / 100, 2);
        if (this.$sdk.keyAssetType(this.payToken.type) == "ERC721" ||
          this.$sdk.keyAssetType(this.payToken.type) == "ERC1155") {
          return 0;
        }
        return fee;
      },
      isContract () {
        if (this.payToken.address == this.$sdk.NULL_ADDRESS()) return false;
        return true;
      },
      openingGif () {
        if (!this.blindbox.anim1) return require('@/assets/img/blindbox/opening.gif')
        return this.blindbox.anim1;
      },
      openedGif () {
        if (!this.blindbox.anim2) return require('@/assets/img/blindbox/opened.gif')
        return this.blindbox.anim2;
      },
    },
    methods: {
      onOpen () {
        this.form.quantity = 1;
        this.form.price = this.blindbox.price;
        this.form = Object.assign({}, this.form);
      },
      checkForm () {
        let error = false;
        let quantity = parseInt(this.form.quantity);
        if (!quantity) {
          this.formError.quantity = this.$t("form.noQuantity");
          error = true;
        }
        if (quantity > 10) {
          this.formError.quantity = this.$t("form.maxAmount10Limit");
          error = true;
        }

        let amount = this.blindbox.amount;
        if (this.blindbox.order.openeds) {
          amount = amount - this.blindbox.order.openeds;
        }
        if (parseFloat(this.form.quantity) > amount) {
          error = true;
          this.formError.quantity = this.$t("form.exceedValue");
        }
        return !error;
      },
      async onSubmit () {
        if (!this.$tools.needLogin(this.$route.path)) return;
        if (!this.checkForm()) return;
        this.confirm = true;
        let that = this;

        setTimeout(async function () {
          if (that.isContract) {
            await that.onApprove();
          } else {
            await that.onBuy();
          }
        }, 100);
      },
      async onApprove () {
        this.step.approve = 1;
        let result;
        if (this.$sdk.keyAssetType(this.payToken.type) == "ERC20") {
          result = await this.approvePayToken();
        } else {
          result = await this.setApproveAll();
        }
        if (result.error) {
          this.error.approve = result.error;
          this.step.approve = 0;
        } else {
          this.error.approve = "";
          this.step.approve = 2;
        }
      },
      async approvePayToken () {
        let asset = {
          address: this.payToken.address,
          type: this.payToken.type,
        }
        let isAllowance = await this.$sdk.allowancePayToken(asset,
          this.user.coinbase,
          this.config.contract.erc20TransferProxyAddress,
        );
        if (isAllowance.error) {
          return isAllowance;
        }
        isAllowance = isAllowance.toString();
        if (isAllowance != '0' && isAllowance) {
          return true;
        }
        return await this.$sdk.approvePayToken(asset,
          this.user.coinbase,
          this.config.contract.erc20TransferProxyAddress,
        );
      },
      async setApproveAll () {
        let order = {
          address: this.payToken.address,
          type: this.payToken.type,
          tokenId: this.payToken.tokenId,
        }
        let isApproved = await this.$sdk.isApprovedForAll(
          order,
          this.user.coinbase,
          this.config.contract.transferProxyAddress,
        );
        if (typeof isApproved == "object" && isApproved.error) {
          return isApproved;
        }
        if (isApproved) return true;
        let result = await this.$sdk.setApprovalForAll(
          order,
          this.user.coinbase,
          this.config.contract.transferProxyAddress,
          true
        );
        return result;
      },
      async onBuy () {
        this.isOver = false;
        this.isExplosion = false;
        if (this.isContract && this.step.approve != 2) return;
        this.step.open = 1;
        let order = this.blindbox.order;
        let result = await this.buyOrder(order);
        if (!result.error) {
          let that = this;
          that.isExplosion = true;
          that.error.open = "";
          that.step.open = 2;
          setTimeout(() => {
            that.isExplosion = false;
            that.isOver = true;
            that.error.open = result.error;
            that.step.open = 0;
          }, 1500)
        } else {
          this.error.open = result.error;
          this.step.open = 0;
          this.$store.commit("BLINDBOX_SIGNED", false);
        }
      },
      async buyOrder (asset) {
        return new Promise((resolve, reject) => {
          var that = this;
          var data = {
            salt: asset.salt,
          };
          this.$api("blind.order.buyfee", data).then(async function (res) {
            if (that.$tools.checkResponse(res)) {
              asset.buyerFeeSig = {
                r: res.data.r,
                s: res.data.s,
                v: res.data.v,
              };

              asset.blindBoxAddress = that.config.contract.blindBoxAddress
              asset.nfts = that.nfts;
              if (typeof (asset.sellAssets) == 'string') {
                asset.sellAssets = JSON.parse(asset.sellAssets)
              }
              if (typeof (asset.uris) == "string") {
                asset.uris = JSON.parse(asset.uris);
              }
              let sellings = asset.sellings;
              if (typeof (asset.sellings) == 'string') {
                sellings = JSON.parse(asset.sellings);
              }
              asset.sellings = [];
              for (var i = 0;i < sellings.length;i++) {
                asset.sellings.push(sellings[i].toString());
              }
              asset.buying = res.data.buying
              asset.sellerFee = res.data.sellerFee;
              asset.buyerFee = res.data.buyerFee;
              asset.amount = that.form.quantity;
              asset.buyer = that.user.coinbase;
              asset.value = '0';
              if (that.$sdk.keyAssetType(asset.buyerType) == "ETH") {
                let price = new BigNumber(that.form.price).multipliedBy(new BigNumber(that.form.quantity));
                let fee = price.multipliedBy(new BigNumber(asset.buyerFee));
                fee = fee.dividedBy(new BigNumber(10000));
                asset.value = price.plus(fee).toFixed();
              }
              let result = await that.$sdk.openBlindBox(that.user.coinbase, asset);
              if (result.error) {
                return resolve(result);
              }
              that.$store.dispatch("updatePayToken", that.payToken);
              that.getOpenList(result.logs);
              resolve(result);
            } else {
              resolve({ error: res.errmsg });
            }
          });
        });
      },
      async getOpenList (logs) {
        let openList = [];
        for (var i = 0;i < logs.length;i++) {
          let log = logs[i];
          if (log.event != "OpenIndex") continue;
          let index = log.args.index.toString();
          openList.push(this.nfts[index]);
        }
        this.openList = openList;
      },
      closed () {
        this.step = {
          approve: 0,
          open: 0,
        };
        this.form = {
          price: "",
          quantity: "",
        }
        this.formError = {
          all: "",
          quantity: "",
        };
        this.error = {
          all: "",
          approve: "",
          open: "",
        }
        this.confirm = false;
        this.isOver = false;
        this.isExplosion = false;
        this.$store.commit("BLINDBOX_SIGNED", false);
      },

    },
  };
</script>

<style lang="scss" scoped>
  .open-btn {
    width: 50%;
    height: 27px;
    background: #4f00ff;
    border-radius: 4px;
  }
  .form-footer {
    margin-top: 20px;
    text-align: center;
  }

  /*
        .el-input--suffix .el-input__inner {
          padding-right: 0px !important;
          border-radius: 4px !important;
          width: 100% !important;
          padding: 0px 8px !important;
          height: 40px !important;
        }
        .el-input__inner {
          padding-right: 0px !important;
          border-radius: 4px !important;
          width: 100% !important;
          padding: 0px 8px !important;
          padding-right: 30px !important;
          height: 40px !important;
        }
        */
  .videoClass {
    width: 100%;
    border-radius: 10px;
  }
  .open-status{
    text-align: center;
  }
  .describe-box {
    font-size: 9px;
    color: #000000;
    margin: 8px 0px 27px;
  }
  .open-nft-items {
    &.flex-wrap {
      flex-wrap: wrap;
      justify-content: flex-start;
      .open-nft-item {
        width: 20%;
        flex: none;
      }
    }
  }
  .open-nft-item {
    display: flex;
    flex: 1;
    flex-direction: column;
    align-items: center;
    margin-bottom: 10px;
    .nft-img {
      border-radius: $borderRadius;
      width: 90%;
      margin: 0 5px;
    }
    .nft-name {
      width: 90%;
      text-align: center;
      margin: 0 5px;
      margin-top: 10px;
    }
  }
  .fg-dialog {
    &.bg-fg-dialog {
      height: auto !important;
      width: 800px !important;
    }
  }
</style>


