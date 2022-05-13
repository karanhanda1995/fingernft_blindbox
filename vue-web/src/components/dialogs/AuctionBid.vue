<template>
  <el-dialog :model-value="visible" :show-close="false" :close-on-click-modal="false" custom-class="fg-dialog" @open="onOpen" @closed="closed" @close="$emit('close')" destroy-on-close>
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
            <span>{{ $t("dialog.pab") }}</span>
          </div>
        </div>
        <div class="input-group">
          <div class="input-info">
            <div class="tip">{{ $t("dialog.bidPrice") }}</div>
            <el-input class="fg-dialog-input" placeholder="0.0" type="number" v-model="form.price">
              <template #suffix>
                <div class="paytoken-suffix">{{ auction.paytokenSymbol }}</div>
              </template>
            </el-input>
            <div class="input-error" v-if="formError.price">
              {{ formError.price }}
            </div>
            <template v-if="nft1155">
              <div class="tip">{{ $t("dialog.enterQuantity") }}</div>
              <el-input class="fg-dialog-input" :placeholder="auction.sellValue" type="number" :disabled="true">
              </el-input>
            </template>
            <div class="stip">
              <div class="stip-item">
                {{ $t("dialog.yourBalance") }}
                <span class="bfont">{{ pay.balance }}{{ auction.paytokenSymbol }}</span>
              </div>
              <div class="stip-item">
                {{ $t("dialog.serviceFee") }}
                <span class="bfont">{{ serviceFee }}%</span>
              </div>
              <div class="stip-item">
                {{ $t("dialog.pay") }}
                <span class="bfont cprimary">
                  {{ pay.pay }}{{ auction.paytokenSymbol }}</span>
              </div>
            </div>
          </div>
        </div>

        <div class="process-btn">
          <template v-if="pay && parseFloat(pay.pay) > parseFloat(pay.balance)">
            <el-button @click="onSubmit" disabled type="primary" :loading="confirm">
              {{ $t("dialog.confirm") }}</el-button>

            <div class="input-error text-center">
              {{ $t("dialog.noBalance") }}
            </div>
          </template>
          <template v-else>
            <el-button @click="onSubmit" type="primary" :loading="confirm">
              {{ $t("dialog.confirm") }}</el-button>
          </template>
        </div>
      </div>
    </div>

    <div class="fg-dialog-body" v-else>
      <div class="all-error" v-if="error.all">{{ error.all }}</div>

      <div class="process">
        <div class="step-info">
          <div class="text">
            <span>{{ $t("dialog.approve") }} {{auction.paytokenSymbol}}</span>
          </div>
          <span v-if="step.approve != 1" :class="step.approve == 2 ? 'finish' : ''" class="step iconfont icon-seleted"></span>
          <div class="step loading" v-else>
            <img class="loading-animation" src="@/assets/img/loading.png" />
          </div>
        </div>
        <div class="process-btn">
          <el-button @click="onApprove" type="primary" v-if="step.approve == 0">
            {{ $t("dialog.approve") }}</el-button>
          <el-button disabled type="info" v-else-if="step.approve == 1">
            {{ $t("dialog.inProgress") }}...</el-button>
          <el-button disabled type="info" v-else>{{
            $t("dialog.done")
          }}</el-button>
        </div>
        <div class="process-error" v-if="error.approve">
          {{ error.approve }}
        </div>
      </div>

      <div class="process">
        <div class="step-info">
          <div class="text">
            <span>{{ $t("dialog.signsBid") }}</span>
          </div>
          <span v-if="step.bid != 1" :class="step.bid == 2 ? 'finish' : ''" class="step iconfont icon-seleted"></span>
          <div class="step loading" v-else>
            <img class="loading-animation" src="@/assets/img/loading.png" />
          </div>
        </div>
        <div class="process-btn">
          <el-button disabled type="info" v-if="step.approve != 2">{{ $t("dialog.bid") }}
          </el-button>
          <el-button @click="onBid" type="primary" v-else-if="step.bid == 0">{{ $t("dialog.bid") }}
          </el-button>
          <el-button disabled type="info" v-else-if="step.bid == 1">{{ $t("dialog.inProgress") }}...
          </el-button>
          <el-button disabled type="info" v-else>{{
            $t("dialog.done")
          }}</el-button>
        </div>
        <div class="process-error" v-if="error.bid">{{ error.bid }}</div>
      </div>
    </div>
  </el-dialog>
</template>

<script>
  import BigNumber from "bignumber.js";
  export default {
    components: {},
    data () {
      return {
        visible: this.show,
        confirm: false,
        form: {
          price: "",
          payAddress: "",
        },
        formError: {
          all: "",
          price: "",
        },
        step: {
          approve: 0,
          bid: 0,
        },
        error: {
          all: "",
          approve: "",
          bid: "",
        },
        order: {},
      };
    },
    emits: ['confirm'],
    props: {
      show: {
        type: Boolean,
        default: true,
      },
      highestBid: {
        type: Object,
        default: null,
      },
      auction: {
        type: Object,
        default: null,
      },
      nft: {
        type: Object,
        default: null,
      },
      uri: {
        type: Object,
        default: null,
      },
    },
    watch: {
      show () {
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
      serviceFee () {
        let fee = this.$tools.decimal(this.$store.state.config.buyerFee / 100, 2);
        return fee;
      },
      pay () {
        let balance = this.$store.getters.getBalance(this.form.payAddress);
        let price = this.$tools.str2num(this.form.price);
        if (!price) return { balance: balance, pay: 0 };
        let serviceFee = new BigNumber(price).multipliedBy(this.auction.sellValue).multipliedBy(this.serviceFee).dividedBy(100).toFixed();
        let pay = this.$tools.decimal(Number(price) + Number(serviceFee), 6);
        return { pay, balance };
      },
      nft1155 () {
        return this.$sdk.keyAssetType(this.nft.type) == "ERC1155"
      }
    },
    methods: {
      onOpen () {
        this.init();
        this.form.payAddress = this.auction.paytokenAddress.toLocaleLowerCase();
        if (!this.highestBid) {
          this.form.price = this.$tools.noDecimalsValue(this.auction.startValue, this.auction.sellValue, this.auction.paytokenDecimals);
        } else {
          var singlePrice = this.$tools.noDecimalsValue(this.highestBid.buying,
            this.auction.sellValue, this.auction.paytokenDecimals);
          var rangeValue = this.$tools.noDecimalsValue(this.auction.rangeValue, 1, this.auction.paytokenDecimals);
          this.form.price = new BigNumber(singlePrice).plus(rangeValue).toFixed();
        }
      },
      init () {
        this.confirm = false;
        this.form = {
          price: "",
          payAddress: "",
        };
        this.step = {
          approve: 0,
          bid: 0,
        };
        this.clearFormError();
        this.clearError();
        this.order = {};
      },
      clearFormError () {
        this.formError = {
          all: "",
          price: "",
        };
      },
      clearError () {
        this.error = {
          all: "",
          approve: "",
          bid: "",
        };
      },
      checkForm () {
        this.clearFormError();
        let error = false;
        if (!parseFloat(this.form.price)) {
          error = true;
          this.formError.price = this.$t("form.noPrice");
        }
        let lowestPrice = this.$tools.noDecimalsValue(
          this.auction.startValue, 
          this.auction.sellValue,
          this.auction.paytokenDecimals
        );
        if (this.highestBid) {
          var singlePrice = this.$tools.noDecimalsValue(
              this.highestBid.buying,
              this.auction.sellValue,
              this.auction.paytokenDecimals
          );
          var rangeValue = this.$tools.noDecimalsValue(
              this.auction.rangeValue, 1, this.auction.paytokenDecimals);
          lowestPrice = new BigNumber(singlePrice).plus(rangeValue).toFixed();
        }
        if (parseFloat(this.form.price) < parseFloat(lowestPrice)) {
          error = true;
          this.formError.price = this.$t("form.lessPrice");
        }
        return !error;
      },
      onSubmit () {
        if (!this.checkForm()) return;
        this.confirm = true;
        let that = this;
        setTimeout(async function () {
          await that.onApprove();
        }, 100);
      },
      async onApprove () {
        this.step.approve = 1;
        this.order = {
          address: this.auction.sellToken,
          tokenId: this.auction.sellTokenId,
          type: this.auction.sellType,
          salt: "0",
        };
        let result = await this.approvePayToken();
        if (result.error) {
          this.error.approve = result.error;
          this.step.approve = 0;
        } else {
          this.error.approve = "";
          this.step.approve = 2;
        }
      },
      async approvePayToken () {
        let order = {
          address: this.auction.paytokenAddress,
          type: this.auction.buyerType,
        };
        let isAllowance = await this.$sdk.allowancePayToken(
          order,
          this.user.coinbase,
          this.config.contract.erc20TransferProxyAddress,
        );
        if (isAllowance.error) return isAllowance;
        isAllowance = isAllowance.toString();
        if (isAllowance != "0" && isAllowance) {
          return true;
        }
        return await this.$sdk.approvePayToken(
          order,
          this.user.coinbase,
          this.config.contract.erc20TransferProxyAddress,
        );
      },
      async onBid () {
        if (this.step.approve != 2) return;
        this.step.bid = 1;
        let result = await this.bidToken();
        if (result.error) {
          this.error.bid = result.error;
          this.step.bid = 0;
        } else {
          this.error.bid = "";
          this.step.bid = 2;
          this.$emit("confirm");
        }
      },
      async bidToken () {
        return new Promise((resolve, reject) => {
          let data = {
            owner: this.auction.owner,
            sellToken: this.auction.sellToken,
            sellTokenId: this.auction.sellTokenId,
            buyerValue: this.$tools.decimalsValue(this.form.price, this.auction.sellValue, this.auction.paytokenDecimals),
            salt: this.auction.salt,
            type: this.$tools.getNotitySubTypeCode("AUCTION_BID"),
          };
          let that = this;
          this.$api("auction.prepare", data).then(async function (res) {
            if (that.$tools.checkResponse(res)) {
              let { message, salt, owner, sellToken, sellTokenId } = res.data;
              let signature = "";
              if (message.startsWith("0x")) {
                message = message.slice(2, message.length);
              }
              try {
                signature = await that.$web3.sign(message, that.user.coinbase);
                if (signature.error) {
                  return resolve(signature);
                }
              } catch (e) {
                return resolve({ error: e.message });
              }
              let _data = {
                owner,
                sellToken,
                sellTokenId,
                buyerValue: that.$tools.decimalsValue(
                  that.form.price,
                  that.auction.sellValue,
                  that.auction.paytokenDecimals),
                salt,
                message,
                signature,
              };
              that.$api("auction.bid", _data).then((res) => {
                if (that.$tools.checkResponse(res)) {
                  that.$tools.message(that.$t("request.bidSuccess"), "success");
                  resolve(res);
                } else {
                  resolve({ error: res.errmsg });
                }
              });
            } else {
              that.$tools.message(res.errmsg);
              resolve({ error: res.errmsg });
            }
          });
        });
      },
      closed () {
        this.init();
      },
    },
  };
</script>

<style lang="scss" scoped>
</style>
