<template>
  <el-dialog :model-value="visible" :show-close="false" :close-on-click-modal="false" @close="$emit('close')" @closed="closed" custom-class="fg-dialog" destroy-on-close>
    <template #title>
      <div class="fg-dialog-title">
        <div class="left">
          <span v-if="uri">{{ uri.name }}</span>
        </div>
        <div class="right" @click="$emit('close')">
          <img class="close-img" src="@/assets/create-img/pop_shut.png ">
        </div>
      </div>
    </template>
    <div class="fg-dialog-body" v-if="!confirm">
      <div class="process">
        <div class="step-info">
          <div class="text">
            <span>{{$t('dialog.auctionBuy')}}</span>
          </div>
        </div>

        <div class="input-group">
          <div class="input-info">
            <div class="tip">{{$t('dialog.bidPrice')}}</div>
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
              <div class="stip-item"> {{$t('dialog.serviceFee')}} <span class="bfont">{{serviceFee}}%</span> </div>
              <div class="stip-item"> {{$t('dialog.royalty')}} <span class="bfont">{{royalties}}%</span> </div>
              <div class="stip-item">{{$t('dialog.receive')}}
                <span class="bfont"> {{receive.receive}} {{auction.paytokenSymbol}}</span>
              </div>
            </div>
          </div>
        </div>

        <div class="process-btn">
          <el-button @click="onSubmit" type="primary" :loading="confirm">{{$t('dialog.accept')}}
          </el-button>
        </div>
        <div class="process-btn">
          <el-button plain type="info" @click="$emit('close')">{{$t('dialog.cancel')}}
          </el-button>
        </div>
      </div>
    </div>

    <div class="fg-dialog-body" v-else>
      <div class="all-error" v-if="error.all"> {{ error.all }} </div>

      <div class="process">
        <div class="step-info">
          <div class="text">
            <span>{{$t('dialog.stb')}}</span>
          </div>
          <span v-if="step.accept != 1" :class="step.accept == 2 ? 'finish': ''" class="step iconfont icon-seleted"></span>
          <div class="step loading" v-else>
            <img class="loading-animation" src="@/assets/img/loading.png" />
          </div>
        </div>

        <div class="process-btn" v-if="!isERC20">
          <el-button @click="onBuy" type="primary" v-if="step.accept==0">{{$t('dialog.accept')}}</el-button>
          <el-button disabled type="info" v-else-if="step.accept== 1">{{$t('dialog.inProgress')}}</el-button>
          <el-button disabled type="info" v-else>{{$t('dialog.done')}}</el-button>
        </div>
        <div class="process-error" v-if="error.accept">{{ error.accept}}</div>
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
        royalties: 0,
        confirm: false,
        step: {
          accept: 0,
        },
        error: {
          all: "",
          accept: "",
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
      bidUsers: {
        type: Array,
        default: [],
      }
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
      isERC20 () {
        if (this.auction.paytokenAddress == this.$sdk.NULL_ADDRESS()) return false;
        return true;
      },
      config () {
        return this.$store.state.config;
      },
      serviceFee () {
        let fee = this.$tools.decimal(this.$store.state.config.sellerFee / 100, 2);
        return fee;
      },
      bidPrice () {
        if (this.bid && this.auction) {
          return this.bid.singlePrice
        }
        return ""
      },
      receive () {
        var payToken = {
          address: this.auction.buyerToken,
          type: this.auction.buyerType,
          decimals: this.auction.paytokenDecimals,
        }
        let b = this.$sdk.getBalance(payToken, this.bid.owner);
        let price = this.$tools.str2num(this.bid.buying);
        price = this.$tools.singlePrice(price, 1, payToken)
        if (!price) {
          return { receive: 0 }
        }
        let serviceFee = new BigNumber(this.serviceFee).dividedBy(100).multipliedBy(price).toFixed();
        let royalties = new BigNumber(this.royalties).dividedBy(100).multipliedBy(price).toFixed();
        let encourage = this.bidUsers == 1 ? 0 : new BigNumber(this.auction.encourage).dividedBy(10000).multipliedBy(price).toFixed();
        let receive = new BigNumber(price).minus(new BigNumber(serviceFee)).minus(new BigNumber(royalties)).minus(new BigNumber(encourage)).toFixed();
        return { receive }
      },
    },
    mounted () {
      if (this.nft) {
        this.getRoyalties(this.nft)
      }
    },
    methods: {
      getRoyalties (nfts) {
        let _nfts = nfts.address + ":" + nfts.tokenId;
        let data = {
          info: _nfts,
        };
        let that = this;
        this.$api("nft.getroyalties", data).then((res) => {
          for (let key in res.data) {
            if (key == _nfts && res.data[key]) {
              let getroyalties = JSON.parse(res.data[key])
              let count = 0;
              for (let i = 0;i < getroyalties.length;i++) {
                count = count + getroyalties[i]
              }
              that.royalties = that.$tools.decimal(count / 100, 2);
            }
          }
        });
      },
      async onSubmit () {
        this.confirm = true;
        let that = this;
        setTimeout(async function () {
          await that.onBuy();
        }, 100);
      },
      async onBuy () {
        this.step.accept = 1;
        let result = await this.buyOrder();
        if (result.error) {
          this.error.accept = result.error;
          this.step.accept = 0;
        } else {
          this.error.accept = "";
          this.step.accept = 2;
          this.$emit("confirm");
        }
      },
      async buyOrder () {
        let value = "0";
        if (this.$sdk.keyAssetType(this.auction.buyerType) == "ETH") {
          let realValue = new BigNumber(this.bid.buying).dividedBy(new BigNumber(10).exponentiatedBy(this.auction.paytokenDecimals));

          let fee = realValue.multipliedBy(new BigNumber(this.config.sellerFee));
          fee = fee.dividedBy(new BigNumber(10000));
          value = realValue.plus(fee).toFixed();
        }
        let buyerValue = new BigNumber(this.auction.buying);

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
          bidBuying: this.bid.buying,
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
            type: 1,
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
                address: that.auction.buyerToken,
                type: that.auction.buyerType,
                decimals: that.auction.paytokenDecimals,
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
          accept: 0,
        };
        this.error = {
          all: '',
          accept: ''
        }
      },
    },
  };
</script>
<style lang="scss" scoped>
</style>

