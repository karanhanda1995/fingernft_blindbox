<template>
  <el-dialog :model-value="visible" :show-close="false" :close-on-click-modal="false" @close="$emit('close')" @closed="closed" custom-class="fg-dialog" destroy-on-close>
    <template #title>
      <div class="fg-dialog-title">
        <div class="left">
          <span v-if="uri">{{ uri.name }}</span>
        </div>
        <div class="right" @click="$emit('close')">
          <img class="close-img" src="@/assets/create-img/pop_shut.png " />
        </div>
      </div>
    </template>
    <div class="fg-dialog-body" v-if="!confirm">
      <div class="process">
        <div class="step-info">
          <div class="text">
            <span>{{ $t("dialog.auctionBuy") }}</span>
          </div>
        </div>

        <div class="input-group">
          <div class="input-info">
            <div class="tip">{{ $t("dialog.bidPrice") }}</div>
            <el-input class="fg-dialog-input" :disabled="true" type="number" v-model="bidPrice">
              <template #suffix>
                <div class="paytoken-suffix">{{auction.paytokenSymbol}}</div>
              </template>
            </el-input>
            <div class="tip">{{$t('dialog.buyQuantity')}}</div>
            <el-input class="fg-dialog-input" type="number" :disabled="true" v-model="auction.sellValue">
              <template #suffix>
              </template>
            </el-input>
            <div class="stip">
              <div class="stip-item">
                {{ $t("dialog.yourBalance") }}
                <span class="bfont">{{ pay.balance }} {{ auction.paytokenSymbol }}</span>
              </div>
              <div class="stip-item">
                {{ $t("dialog.serviceFee")
                }}<span class="bfont">{{ serviceFee }}%</span>
              </div>
              <div class="stip-item">
                {{ $t("dialog.pay") }}
                <span class="bfont">
                  {{ pay.pay }} {{ auction.paytokenSymbol }}</span>
              </div>
            </div>
          </div>
        </div>

        <div class="process-btn">
          <template v-if="pay && parseFloat(pay.pay) > parseFloat(pay.balance)">
            <el-button @click="onSubmit" disabled type="primary" :loading="confirm">{{ $t("dialog.buy") }}
            </el-button>
            <div class="input-error text-center">
              {{ $t("dialog.noBalance") }}
            </div>
          </template>
          <template v-else>
            <el-button @click="onSubmit" type="primary" :loading="confirm">{{ $t("dialog.buy") }}
            </el-button>
          </template>
        </div>
        <div class="process-btn">
          <el-button plain type="info" @click="$emit('close')">{{ $t("dialog.cancel") }}
          </el-button>
        </div>
      </div>
    </div>

    <div class="fg-dialog-body" v-else>
      <div class="all-error" v-if="error.all">{{ error.all }}</div>

      <div class="process">
        <div class="step-info">
          <div class="text">
            <span>{{ $t("dialog.stb") }}</span>
          </div>
          <span v-if="step.buy != 1" :class="step.buy == 2 ? 'finish' : ''" class="step iconfont icon-seleted"></span>
          <div class="step loading" v-else>
            <img class="loading-animation" src="@/assets/img/loading.png" />
          </div>
        </div>

        <div class="process-btn" v-if="!isERC20">
          <el-button @click="onBuy" type="primary" v-if="step.buy == 0">{{
            $t("dialog.buy")
          }}</el-button>
          <el-button disabled type="info" v-else-if="step.buy == 1">{{
            $t("dialog.inProgress")
          }}</el-button>
          <el-button disabled type="info" v-else>{{
            $t("dialog.done")
          }}</el-button>
        </div>
        <div class="process-error" v-if="error.buy">{{ error.buy }}</div>
      </div>
    </div>
  </el-dialog>
</template>

<script>
  import BigNumber from "bignumber.js";
  export default {
    data: function () {
      return {
        visible: this.show,
        confirm: false,
        step: {
          buy: 0,
        },
        error: {
          all: "",
          buy: "",
        },
      };
    },
    emits: ['confirm'],
    props: {
      show: {
        type: Boolean,
        default: false,
      },
      nft: {
        type: Object,
        default: {},
      },
      auction: {
        type: Object,
        default: {},
      },
      bid: {
        type: Object,
        default: {},
      },
      uri: {
        type: Object,
        default: {},
      },
    },
    watch: {
      show (val) {
        this.visible = this.show;
      },
    },
    computed: {
      user () {
        return this.$store.state.user;
      },
      config () {
        return this.$store.state.config;
      },
      isERC20(){
        if(this.auction.paytokenAddress == this.$sdk.NULL_ADDRESS()) return false;
        return true;
      },
      serviceFee () {
        let fee = this.$tools.decimal(this.$store.state.config.buyerFee / 100, 2);
        return fee;
      },
      bidPrice () {
        if (this.bid && this.auction) {
          return this.$tools.noDecimalsValue(this.bid.buying, this.auction.sellValue, this.auction.paytokenDecimals)
        }
        return ""
      },
      pay () {
        let price = this.$tools.str2num(this.bid.buying);
        if (!price) {
          return { pay: 0, balance: 0 };
        }
        let balance = this.$store.getters.getBalance(this.auction.buyerToken);
        balance = this.$tools.decimal(balance, 4);
        let serviceFee = new BigNumber(price).multipliedBy(this.serviceFee).dividedBy(100).toFixed();
        let pay = new BigNumber(price).plus(new BigNumber(serviceFee)).toFixed();
        pay = this.$tools.noDecimalsValue(pay, 1, this.auction.paytokenDecimals);

        return { pay, balance };
      },
    },
    methods: {
      async onSubmit () {
        this.confirm = true;
        let that = this;
        setTimeout(async function () {
          await that.onBuy();
        }, 100);
      },
      async onBuy () {
        this.step.buy = 1;
        let result = await this.buyOrder();
        if (result.error) {
          this.error.buy = result.error;
          this.step.buy = 0;
        } else {
          this.error.buy = "";
          this.step.buy = 2;
          this.$emit("confirm");
        }
      },
      async buyOrder () {
        let value = "0";
        if (this.$sdk.keyAssetType(this.auction.buyerType) == "ETH") {
          let realValue = new BigNumber(this.bid.buying).multipliedBy(
            new BigNumber(10).exponentiatedBy(this.auction.paytokenDecimals)
          );

          let fee = realValue.multipliedBy(new BigNumber(this.config.sellerFee));
          fee = fee.dividedBy(new BigNumber(10000));
          value = realValue.plus(fee).toFixed();
        }
        let buyerValue = new BigNumber(this.auction.buying);
        let bidBuying = new BigNumber(this.bid.buying);
        bidBuying = bidBuying.multipliedBy(
          new BigNumber(10).exponentiatedBy(this.auction.paytokenDecimals)
        );

        var order = {
          owner: this.auction.owner,
          salt: this.auction.salt,
          sellToken: this.auction.sellToken,
          sellTokenId: this.auction.sellTokenId,
          sellType: this.auction.sellType,
          sellValue: this.auction.sellValue,

          buyToken: this.auction.buyerToken,
          buyTokenId: this.auction.buyerTokenId,
          buyType: this.auction.buyerType,
          buyValue: buyerValue.toFixed(),
          bidBuying: bidBuying.toFixed(),
          endTime: this.auction.endTime,
          expiredTime: this.auction.expiredTime,
          encourage: this.auction.encourage,
          buyerFee: this.config.buyerFee,
          sellerFee: this.sellerFee,

          signature: this.auction.signature,
          bidSignature: this.bid.signature,
          caddress: this.nft.address,
          tokenId: this.nft.tokenId,
          buyerFee: this.config.buyerFee,
          sellerFee: this.config.sellerFee,
          buyer: this.bid.owner,
          value: value,
        };
        return await this.exchangeToken(order);
      },
      async exchangeToken (data) {
        return new Promise((resolve, reject) => {
          var that = this;
          this.$api("auction.buyerFee", {
            owner: data.owner,
            salt: data.salt,
            token: data.caddress,
            tokenId: data.tokenId,
            type: 2,
          }).then(async function (res) {
            if (that.$tools.checkResponse(res)) {
              data.buyerFeeSig = {
                r: res.data.r,
                s: res.data.s,
                v: res.data.v,
              };
              data.auctionAddress = that.config.contract.auctionExchangeAddress;
              data.recipients = res.data.recipients;
              let result = await that.$sdk.exchangeAuction(
                that.user.coinbase,
                data
              );
              if (result.error) {
                return resolve(result);
              }
              var payToken = {
                address: this.auction.buyerToken,
                type: this.auction.buyerType,
                decimals: this.auction.paytokenDecimals,
              }
              that.$store.dispatch("updatePayToken", payToken);
              resolve(result);
            } else {
              resolve({ error: res.errmsg });
            }
          });
        });
      },
      closed () {
        this.confirm = false;
        this.step = {
          buy: 0,
        };
        this.error = {
          all: "",
          buy: "",
        };
      },
    },
  };
</script>
<style lang="scss" scoped>
</style>

