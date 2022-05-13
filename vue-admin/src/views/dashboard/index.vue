<template>
  <div class="dashboard-editor-container">
    <!-- <el-row>
      <el-tag type="success">欢迎，{{ name }}-{{ roles }}</el-tag>
    </el-row> -->
    <div class="nav-tab">
      <div class="nav-tab-item" :class="tab == 'total'?'active':''" @click="tabChange('total')">{{ $t("dashboard.statistics") }}</div>
      <div class="nav-tab-item" :class="tab == 'today'?'active':''" @click="tabChange('today')">{{ $t("dashboard.todayStatistics") }}</div>
    </div>
    <div :class="tab == 'today'?'dis-no':''">
      <div class="data-box">
        <div class="box-title">{{ $t("dashboard.userData") }}</div>
        <div class="box-info">
          <div class="box-info-left">
            <div id="userData" :style="{width: '800px', height: '500px'}"></div>
          </div>
          <div class="box-info-right">
            <div class="box-item">
              <div class="box-item-title">{{ $t("dashboard.dailyActiveNumber") }}</div>
              <div class="box-item-text">{{dasheBoardDataDay.userLoginAmount}}</div>
            </div>
            <div class="box-item">
              <div class="box-item-title">{{ $t("dashboard.dailyActivePersonTimes") }}</div>
              <div class="box-item-text">{{dasheBoardDataDay.userLoginCount}}</div>
            </div>
          </div>
        </div>
      </div>
      <div class="data-box mt-30">
        <div class="box-title">{{ $t("dashboard.nftData") }}</div>
        <div class="box-text">{{ $t("dashboard.nftTotalNumber") }}: {{ dasheBoardData.nftCountTotal }}</div>
        <div class="box-progress">
          <div class="box-progress-left bg-orange">{{ $t("dashboard.nft1155Number") }}: {{ dasheBoardData.nftCount1155 }}</div>
          <div class="box-progress-right bg-pink">{{ $t("dashboard.nft721Number") }}: {{ dasheBoardData.nftCount721 }}</div>
        </div>
        <div class="box-text">{{ $t("dashboard.nftActivityDetails") }}</div>
        <div id="nftDetails" :style="{width: '800px', height: '400px'}"></div>
      </div>
      <div class="data-box mt-30">
        <div class="box-title">{{ $t("dashboard.blindboxData") }}</div>
        <el-table :data="blindboxData" border style="width: 100%">
          <el-table-column align="center" prop="total" :label="$t('dashboard.totalNumber')" />
          <el-table-column align="center" prop="opened" :label="$t('dashboard.openedNumber')" />
        </el-table>
      </div>
    </div>
    <div :class="tab == 'total'?'dis-no':''">
      <!-- <div class="head-container">
        <el-date-picker v-model="datetime" type="datetime" :placeholder="$t('dashboard.selectDate')" :clearable="false" value-format="timestamp" @change="dateChange">
        </el-date-picker>
        <i class="el-icon-download download"></i>
      </div> -->
      <div class="data-box">
        <div class="box-title">{{ $t("dashboard.todayData") }}</div>
        <div class="box-info">
          <div class="box-info-right mr-70">
            <div class="box-item">
              <div class="box-item-title">{{ $t("dashboard.additionalNumber") }}</div>
              <div class="box-item-text">{{dasheBoardDataDay.oneDayUserCount}}</div>
            </div>
            <div class="box-item">
              <div class="box-item-title">{{ $t("dashboard.dailyActiveNumber") }}</div>
              <div class="box-item-text">{{dasheBoardDataDay.userLoginAmount}}</div>
            </div>
            <div class="box-item">
              <div class="box-item-title">{{ $t("dashboard.dailyActivePersonTimes") }}</div>
              <div class="box-item-text">{{dasheBoardDataDay.userLoginCount}}</div>
            </div>
            <div class="box-item">
              <div class="box-item-title">{{ $t("dashboard.blindboxOpened") }}</div>
              <div class="box-item-text">{{dasheBoardDataDay.oneDayBlindBoxOpenCount}}</div>
            </div>
            <div class="box-item">
              <div class="box-item-title">{{ $t("dashboard.nftAddedNumber") }}</div>
              <div class="box-item-text">{{dasheBoardDataDay.oneDayNftCountTotal}}</div>
            </div>
            <div class="box-item">
              <div class="box-item-title">{{ $t("dashboard.nftSaleNumber") }}</div>
              <div class="box-item-text">{{dasheBoardDataDay.oneDayNftOnSaleCount}}</div>
            </div>
          </div>
          <div class="box-info-right">
            <div id="todayData" :style="{width: '800px', height: '400px'}"></div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  export default {
    components: {},
    data () {
      return {
        datetime: new Date().getTime(),
        dasheBoardData: {},
        dasheBoardDataDay: {},
        blindboxData: [],
        tab: "total"
      };
    },
    created () {
      this.getData()
    },
    methods: {
      init () {
        this.drawUserLine()
        this.drawNftLine()
        this.drawToydayLine()
      },
      tabChange (tab) {
        this.tab = tab
      },
      drawUserLine () {
        let _this = this;
        let myChart = this.$echarts.init(document.getElementById('userData'))
        let total = this.dasheBoardData.userCount;
        let original = this.dasheBoardData.userCount - this.dasheBoardDataDay.oneDayUserCount;
        let add = this.dasheBoardDataDay.oneDayUserCount;
        let option = {
          title: { text: _this.$t("dashboard.userDetails"), left: 'center' },
          tooltip: {
            trigger: 'item'
          },
          legend: {
            data: [
              _this.$t("dashboard.originalNumber"),
              _this.$t("dashboard.additionalNumber")
            ],
            bottom: 0
          },
          series: [
            {
              name: '',
              type: 'pie',
              selectedMode: 'single',
              radius: [0, '30%'],
              label: {
                formatter: '{c}\n{b}',
                position: 'inner',
                fontSize: 16
              },
              labelLine: {
                show: false
              },
              data: [
                { value: total, name: _this.$t("dashboard.totalUsers") }
              ]
            },
            {
              name: '',
              type: 'pie',
              radius: ['45%', '70%'],
              labelLine: {
                length: 30
              },
              label: {
                formatter: '  {b|{b}：}{c}  ',
                backgroundColor: '#F6F8FC',
                borderColor: '#8C8D8E',
                borderWidth: 1,
                borderRadius: 4,
                rich: {
                  b: {
                    color: '#4C5058',
                    fontSize: 14,
                    fontWeight: 'bold',
                    lineHeight: 33
                  }
                }
              },
              data: [
                { value: original, name: _this.$t("dashboard.originalNumber") },
                { value: add, name: _this.$t("dashboard.additionalNumber") }
              ]
            }
          ]
        };
        myChart.setOption(option);
      },
      drawNftLine () {
        let _this = this;
        let sale = this.dasheBoardData.nftOnSaleCount;
        // let buy = this.dasheBoardData.nftOnSaleCount;
        let bid = this.dasheBoardData.bidCount;
        // let sell = this.dasheBoardData.nftOnSellCount;
        let auction = this.dasheBoardData.nftOnAuctionCount;
        // let auctionBid = this.dasheBoardData.nftOnSaleCount;
        let auctionAccept = this.dasheBoardData.auctionFinishedCount;
        let myChart = this.$echarts.init(document.getElementById('nftDetails'))
        myChart.setOption({
          title: { text: _this.$t("dashboard.nftDetails"), left: 'center' },
          tooltip: {
          },
          color: ['#41c3eb'],
          xAxis: {
            data: [_this.$t('dashboard.sale'), _this.$t('dashboard.bid'), _this.$t('dashboard.auction'), _this.$t("dashboard.auctionBids"), _this.$t("dashboard.auctionAccept")]
          },
          yAxis: {},
          series: [{
            name: '',
            type: 'bar',
            barWidth: 80,
            data: [sale, bid, auction, auctionAccept],
            itemStyle: {
              normal: {
                color: function (params) {
                  var colorList = ['#2f4554', '#61a0a8', '#d48265', '#91c7ae', '#749f83', '#ca8622'];
                  return colorList[params.dataIndex]
                }
              }
            }
          }]
        });
      },
      drawToydayLine () {
        let _this = this;
        let blindboxOpen = this.dasheBoardDataDay.oneDayBlindBoxOpenCount;
        let newNft = this.dasheBoardDataDay.oneDayNftCountTotal;
        let nftSale = this.dasheBoardDataDay.oneDayNftOnSaleCount;
        let myChart = this.$echarts.init(document.getElementById('todayData'))
        myChart.setOption({
          tooltip: {},
          xAxis: {
            data: [_this.$t('dashboard.blindboxOpened'), _this.$t('dashboard.nftAddedNumber'), _this.$t('dashboard.nftSaleNumber')]
          },
          yAxis: {},
          series: [{
            name: '',
            type: 'bar',
            barWidth: 50,
            data: [blindboxOpen, newNft, nftSale],
            itemStyle: {
              normal: {
                color: function (params) {
                  var colorList = ['#2f4554', '#61a0a8', '#d48265'];
                  return colorList[params.dataIndex]
                }
              }
            }
          }]
        });
      },
      async getData () {
        await this.$api('dasheboard.stat').then(res => {
          if (this.$tool.checkResponse(res)) {
            this.dasheBoardData = res.data
            this.blindboxData = [{
              total: res.data.blindBoxCount,
              opened: res.data.blindBoxOpenCount
            }]
          }
        })
        await this.$api('dasheboard.statOneDay').then(res => {
          if (this.$tool.checkResponse(res)) {
            this.dasheBoardDataDay = res.data
          }
        })
        this.init()
      },
      dateChange (val) {
        this.datetime = val
      }
    }
  };
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
  #myChart4 {
    width: 50%;
    height: 300px;
    border: 1px solid #c8c8c8;
  }
  .read-container {
    position: relative;
  }

  .red {
    color: #da4947;
  }
  .dashboard-editor-container {
    padding: 32px;
  }
  .head-container {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-top: 20px;
  }
  .download {
    font-size: 26px;
    color: #0f0f0f;
  }
  .ml-50 {
    margin-left: 50px;
  }
  .ml-40 {
    margin-left: 40px;
  }
  .mb-20 {
    margin-bottom: 20px;
  }
  .p-5 {
    padding: 5px 0px;
  }
  .line {
    height: 1px;
    width: 100%;
    background-color: #cacaca;
    margin: 20px 0px;
  }
  .title-text {
    margin: 20px 0px 10px 0px;
  }
  .green {
    color: #15e345;
  }

  .revenue {
    width: 50%;
    padding: 5px 0px;
  }
  .revenue-container {
    display: flex;
    flex-wrap: wrap;
  }
</style>
<style lang="scss" scoped>
  .nav-tab {
    width: 100%;
    display: flex;
    .nav-tab-item {
      width: 50%;
      text-align: center;
      font-size: 20px;
      border-top-left-radius: 5px;
      border-top-right-radius: 5px;
      padding: 10px 0;
      cursor: pointer;
      font-weight: 600;
    }
  }
  .data-box {
    width: 100%;
    .box-title {
      font-weight: 600;
      line-height: 40px;
      font-size: 24px;
      color: #333;
      margin: 20px 0;
    }
    .box-text {
      font-size: 18px;
      line-height: 30px;
    }
    .box-info {
      width: 100%;
      display: flex;
      align-items: center;
      font-size: 22px;
      line-height: 3rem;
      flex-direction: column;
      .box-info-left {
        .box-circular {
          width: 250px;
          height: 250px;
          margin: 0 auto;
          border-radius: 50%;
          background: #c8c8c8;
          display: flex;
          flex-direction: column;
          justify-content: center;
          align-items: center;
        }
      }
      .box-info-right {
        display: flex;
        flex-direction: column;
        align-items: flex-start;
        justify-content: center;
        background: #ddd;
        margin-top: 20px;
        .box-item {
          display: flex;
          align-items: center;
          justify-content: flex-start;
          text-align: center;
          border-bottom: 1px solid #fff;
          .box-item-title {
            width: 300px;
            padding: 10px 20px;
            border-right: 1px solid #fff;
          }
          .box-item-text {
            width: 200px;
            padding: 10px 20px;
          }
          .box-item-title-today {
            width: 500px;
            padding: 10px 20px;
            border-right: 1px solid #fff;
          }
          .box-item-text-today {
            width: 400px;
            padding: 10px 20px;
          }
        }
      }
    }
    .box-progress {
      width: 100%;
      display: flex;
      align-items: center;
      font-size: 20px;
      text-align: center;
      line-height: 36px;
      margin: 30px 0;
      cursor: pointer;
      .box-progress-left {
        width: 40%;
        height: 36px;
        border-radius: 5px;
      }
      .box-progress-right {
        width: 20%;
        height: 36px;
        border-radius: 5px;
      }
    }
  }
  .bg-orange {
    background: orange;
  }
  .bg-pink {
    background: pink;
  }
  .mt-30 {
    margin-top: 30px;
  }
  .active {
    background: rgb(17, 166, 235);
    font-weight: 600;
    color: #fff;
  }
  .dis-no {
    display: none;
  }
  .mr-70 {
    margin-right: 70px;
  }
</style>
