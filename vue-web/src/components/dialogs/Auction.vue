<template>
  <el-dialog :model-value="visible" :show-close="false" :close-on-click-modal="false" custom-class="fg-dialog" @open="onOpen" @close="$emit('close')" @closed="closed">
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
            <span>{{ $t("dialog.auction") }}</span>
          </div>
        </div>
        <div class="input-group flex p-bottom-0">
          <div class="input-info w50 m-right-5">
            <div class="tip">{{ $t('dialog.startTime')}}</div>
            <el-date-picker prefix-icon="false" v-model="form.startTime" type="datetime" :placeholder="$t('dialog.startTime')" :clearable="false">
            </el-date-picker>
            <div class="input-error" v-if="formError.startTime">
              {{ formError.startTime }}
            </div>
          </div>
          <div class="input-info w50">
            <div class="tip">{{$t('dialog.endTime')}}</div>
            <el-date-picker prefix-icon="false" v-model="form.endTime" type="datetime" :placeholder="$t('dialog.endTime')" :clearable="false">
            </el-date-picker>
            <div class="input-error" v-if="formError.endTime">
              {{ formError.endTime }}
            </div>
          </div>
        </div>
        <div class="tip">
          <span>{{$t('dialog.price')}}</span>
        </div>
        <div class="input-group">
          <div class="input-info">
            <el-input class="fg-dialog-input" :placeholder="$t('dialog.startPrice')" type="number" v-model="form.startPrice">
              <template #suffix>
                <el-select class="paytoken-selects" v-model="form.payAddress" v-if="!auction">
                  <el-option v-for="(token, i) in payTokens" :key="i" :label="token.symbol" :value="token.address">
                  </el-option>
                </el-select>
                <span v-else>{{ payToken ? payToken.symbol : "" }}</span>
              </template>
            </el-input>
            <div class="input-error" v-if="formError.startPrice">
              {{ formError.startPrice }}
            </div>
          </div>
          <div class="input-info m-top-10">
            <el-input class="fg-dialog-input" :placeholder="$t('dialog.lowestAcceptPrice')" type="number" v-model="form.hopePrice">
              <template #suffix>
                <span>{{ payToken ? payToken.symbol : "" }}</span>
              </template>
            </el-input>
            <div class="input-error" v-if="formError.hopePrice">
              {{ formError.hopePrice }}
            </div>
          </div>
          <div class="input-info m-top-10">
            <el-input class="fg-dialog-input" :placeholder="$t('dialog.minBidIncrement')" type="number" v-model="form.minRangeValue">
              <template #suffix>
                <span>{{ payToken ? payToken.symbol : ""}}</span>
              </template>
            </el-input>
            <div class="input-error" v-if="formError.minRangeValue">
              {{ formError.minRangeValue}}
            </div>
          </div>
          <div class="input-info">
            <div class="tip flex">
              <span>{{$t('dialog.dividendRate')}}</span>
            </div>
            <el-slider v-model="form.dividends" class="fg-dialog-input" input-size="small"></el-slider>
            <div class="input-error" v-if="formError.dividends">
              {{ formError.dividends }}
            </div>
          </div>
        </div>
        <template v-if="$sdk.keyAssetType(nft.type) != 'ERC721'">
          <div class="tip">
            {{ $t("dialog.enterQuantity") }}
            <span class="gray-font">
              ({{ myAsset.quantity }} {{ $t("dialog.available") }} )
            </span>
          </div>
          <div class="input-group">
            <div class="input-info">
              <el-input class="fg-dialog-input" placeholder="0" type="number" v-model="form.quantity">
              </el-input>
              <div class="input-error" v-if="formError.quantity">
                {{ formError.quantity }}
              </div>
            </div>
          </div>
        </template>
        <div class="process-btn">
          <el-button @click="onSubmit" type="primary" :loading="confirm">
            {{ $t("dialog.confirm") }}
          </el-button>
        </div>
      </div>
    </div>
    <div class="fg-dialog-body" v-else>
      <div class="process">
        <div class="step-info">
          <div class="text">
            <span>{{ $t("dialog.approveCollection") }}</span>
          </div>
          <span v-if="step.approve != 1" :class="step.approve == 2 ? 'finish' : ''" class="step iconfont icon-seleted"></span>
          <div class="step loading" v-else>
            <img class="loading-animation" src="@/assets/img/loading.png" />
          </div>
        </div>
        <div class="process-btn">
          <el-button @click="onApprove" type="primary" v-if="step.approve == 0">{{ $t("dialog.approve") }}</el-button>
          <el-button disabled type="info" v-else-if="step.approve == 1">{{
            $t("dialog.inProgress")
          }}</el-button>
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
            <span>{{ $t("dialog.signsAuction") }}</span>
          </div>
          <span v-if="step.auction != 1" :class="step.auction == 2 ? 'finish' : ''" class="step iconfont icon-seleted"></span>
          <div class="step loading" v-else>
            <img class="loading-animation" src="@/assets/img/loading.png" />
          </div>
        </div>
        <div class="process-btn">
          <el-button disabled type="info" v-if="step.approve != 2">{{
            $t("dialog.auction")
          }}</el-button>
          <el-button @click="onAuction" type="primary" v-else-if="step.auction == 0">{{ $t("dialog.auction") }}</el-button>
          <el-button disabled type="info" v-else-if="step.auction == 1">
            {{ $t("dialog.inProgress") }}</el-button>
          <el-button disabled type="info" v-else>{{
            $t("dialog.done")
          }}</el-button>
        </div>
        <div class="process-error" v-if="error.auction">{{ error.auction}}</div>
      </div>
    </div>
  </el-dialog>
</template>
<script>
  export default {
    data () {
      return {
        visible: this.show,
        confirm: false,
        tag: "form",
        dateVal: "",
        form: {
          startTime: "",
          endTime: "",
          startPrice: "",
          hopePrice: "",
          payAddress: "",
          quantity: "",
          dividends: "",
          minRangeValue: "",
        },
        formError: {
          startTime: "",
          endTime: "",
          startPrice: "",
          hopePrice: "",
          payAddress: "",
          quantity: "",
          dividends: "",
          minRangeValue: "",
        },
        step: {
          approve: 0,
          auction: 0,
        },
        error: {
          all: "",
          approve: "",
          auction: "",
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
      nft: {
        type: Object,
        default: {},
      },
      uri: {
        type: Object,
        default: {},
      },
      auction: {
        type: Object,
        default: null,
      },
      myAsset: {
        type: Object,
        default: null,
      },
    },
    computed: {
      user () {
        return this.$store.state.user;
      },
      config () {
        return this.$store.state.config;
      },
      payTokens () {
        var payTokens = this.$store.state.payTokens;
        let tokens = [];
        for (var i = 0;i < payTokens.length;i++) {
          let payToken = payTokens[i];
          if (payToken.address.toLocaleLowerCase() == this.$sdk.NULL_ADDRESS().toLocaleLowerCase()) continue;
          tokens.push(payToken);
        }
        return tokens;
      },
      payToken () {
        let payToken = this.$store.getters.payToken(this.form.payAddress);
        return payToken;
      },
    },
    methods: {
      onOpen () {
        this.init();
        if (!this.auction) {
          if (this.$sdk.keyAssetType(this.nft.type) == "ERC721") {
            this.form.quantity = "1";
          } else {
            this.form.quantity = "";
          }
        } else {
          this.form = {
            startTime: new Date(this.auction.startTime * 1000),
            endTime: new Date(this.auction.endTime * 1000),
            startPrice: this.auction.startValue,
            hopePrice: this.auction.buying,
            payAddress: this.$store.getters.payToken(this.auction.buyerToken).address.toLocaleLowerCase(),
            quantity: this.auction.sellValue,
            dividends: parseInt(this.auction.encourage / 100),
            minRangeValue: this.auction.rangeValue,
          }
        }
      },
      optChange (val) {
        if (val === 0) {
          this.tag = "date";
          this.dateVal = this.dateVal ? new Date(this.dateVal) : new Date();
          this.$nextTick(function () {
            this.$refs.date.focus();
          });
        }
      },
      formatDate (d) {
        let str_date = "";
        if (d) {
          let d_date = d.getDate() < 10 ? "0" + d.getDate() : d.getDate(),
            d_month =
              d.getMonth() + 1 < 10 ? "0" + (d.getMonth() + 1) : d.getMonth() + 1;
          let time = d.toTimeString().substr(0, 8);
          str_date = d.getFullYear() + "-" + d_month + "-" + d_date + " " + time;
        }
        return str_date;
      },
      changeCallback (d) {
        this.dateVal = this.form.endTime = this.formatDate(d);
        this.tag = "form";
      },
      blurCallback () {
        this.$nextTick(function () {
          this.$refs.date.focus();
        });
      },
      checkForm () {
        var error = false;
        this.clearFormError();
        if (!this.form.startTime) {
          error = true;
          this.formError.startTime = this.$t('form.noStartTime');
        }
        if (!this.form.endTime) {
          error = true;
          this.formError.endTime = this.$t('form.noEndTime');
        }
        if (!parseFloat(this.form.startPrice)) {
          error = true;
          this.formError.startPrice = this.$t('form.noStartPrice');
        }
        if (!parseFloat(this.form.hopePrice)) {
          error = true;
          this.formError.hopePrice = this.$t('form.noLowestAcceptPrice');
        }
        if (
          parseFloat(this.form.startPrice) > parseFloat(this.form.hopePrice)
        ) {
          error = true;
          this.formError.hopePrice = this.$t('form.limitLowestAcceptPrice');
        }
        if (!parseInt(this.form.quantity)) {
          error = true;
          this.formError.quantity = this.$t('form.noQuantity');
        }
        if (parseInt(this.form.quantity) > parseInt(this.myAsset.quantity)) {
          error = true;
          this.formError.quantity = this.$t('form.exceedValue');
        }
        if (!parseFloat(this.form.minRangeValue)) {
          error = true;
          this.formError.minRangeValue = this.$t('form.noMinBidIncrement');
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
        let payToken = this.$store.getters.payToken(this.form.payAddress);
        this.order = {
          address: this.nft.address,
          tokenId: this.nft.tokenId,
          type: this.nft.type,
          payToken: payToken,
          salt: "0",
        };
        if (this.auction) {
          this.order.salt = this.auction.salt;
        }
        let approved = await this.setApproveAll();
        if (approved.error) {
          this.error.approve = approved.error;
          this.step.approve = 0;
        } else {
          this.error.approve = "";
          this.step.approve = 2;
        }
      },
      async setApproveAll () {
        let order = {
          type: this.order.type,
          address: this.order.address,
          tokenId: this.order.tokenId,
        };
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
      async onAuction () {
        this.step.auction = 1;
        let order = {
          ...this.order,
        };
        let result = await this.auctionToken(order);
        if (result.error) {
          this.error.auction = result.error;
          this.step.auction = 0;
        } else {
          this.error.auction = "";
          this.step.auction = 2;
          this.$emit("confirm");
        }
      },
      async auctionToken (order) {
        return new Promise((resolve, reject) => {
          let { startTime, endTime, startPrice, hopePrice, quantity } = this.form;
          let data = {
            owner: this.user.coinbase,
            sellToken: order.address,
            sellTokenId: order.tokenId,
            sellType: order.type,
            buyerTokenId: "0",
            buyerType: order.payToken.type,
            buyerToken: order.payToken.address,
            buying: this.$tools.decimalsValue(hopePrice, quantity, order.payToken.decimals),
            sellValue: quantity,
            startValue: this.$tools.decimalsValue(startPrice, quantity, order.payToken.decimals),
            startTime: parseInt(startTime / 1000),
            endTime: parseInt(new Date(endTime).getTime() / 1000),
            encourage: this.form.dividends ? this.form.dividends * 100 : 0,
            rangeValue: this.$tools.decimalsValue(this.form.minRangeValue, 1, order.payToken.decimals),
            salt: order.salt,
            type: this.$tools.getNotitySubTypeCode("AUCTION"),
            id: order.id ? order.id : "",
          };
          let that = this;
          this.$api("auction.prepare", data).then(async function (res) {
            if (that.$tools.checkResponse(res)) {
              let { message, salt } = res.data;
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
              data.signature = signature;
              data.salt = salt;
              data.message = message;
              data.sellerFee = that.config.sellerFee;
              data.buyerFee = that.config.buyerFee;
              if (!that.auction) {
                data.type = that.$tools.getNotitySubTypeCode("AUCTION");
              } else {
                data.type = that.$tools.getNotitySubTypeCode("EDIT_AUCTION");
              }

              that.$api("auction.add", data).then((res) => {
                if (that.$tools.checkResponse(res)) {
                  that.$tools.message(
                    that.$t("request.saleSuccess"),
                    "success"
                  );
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
      init () {
        let paytoken = this.$store.getters.defaultBidPayToken();
        this.confirm = false;
        this.tag = "form";
        this.dateVal = "";
        this.form = {
          startTime: "",
          endTime: "",
          startPrice: "",
          hopePrice: "",
          payAddress: paytoken ? paytoken.address : "",
          quantity: "",
          dividends: "",
          minRangeValue: "",
        };
        this.step = {
          approve: 0,
          auction: 0,
        };
        this.error = {
          all: "",
          approve: "",
          auction: "",
        };
        this.order = {};
        this.clearFormError();
      },
      clearFormError () {
        this.formError = {
          startTime: "",
          endTime: "",
          startPrice: "",
          hopePrice: "",
          payAddress: "",
          quantity: "",
          dividends: "",
          minRangeValue: "",
        };
      },
    },
    watch: {
      show () {
        this.visible = this.show;
      },
    },
  };
</script>

<style lang="scss" scoped>
  .process .tip {
    line-height: 40px;
  }
  .paytoken-selects .el-input {
    border-bottom: none !important;
  }
</style>
