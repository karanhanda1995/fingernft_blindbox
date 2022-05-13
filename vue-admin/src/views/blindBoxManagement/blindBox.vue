<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="listQuery.blindTypeName" clearable class="filter-item w-200 mr-10" :placeholder="$t('blind.placeholder')" />
      <el-select v-model="listQuery.sync" class="filter-item w-200 mr-10" :placeholder="$t('blind.box.pleaseSelectSync')" :clearable="true">
        <el-option v-for="item in syncOptions" :key="item.value" :label="item.label" :value="item.value" />
      </el-select>
      <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handlerFilter">{{ $t("blind.select") }}</el-button>
      <el-button class="filter-item" type="primary" icon="el-icon-edit" @click="handlerBtn('add')" v-permission="['POST /admin/blind/box/add']">{{ $t("blind.add") }}</el-button>
    </div>

    <el-table v-loading="listLoading" :data="list" :element-loading-text="$t('util.loadingText')" border>
      <el-table-column type="expand">
        <template slot-scope="props">
          <el-form label-position="left" inline class="table-expand" label-width="160px">
            <el-form-item :label="$t('blind.box.blindType')">
              <span>{{ tableBlindType(props.row.address) }}</span>
            </el-form-item>
            <el-form-item label="payToken">
              <span>{{ props.row.payToken }}</span>
            </el-form-item>
            <el-form-item label="payTokenId">
              <span>{{ props.row.payTokenId }}</span>
            </el-form-item>
            <el-form-item :label="$t('blind.box.times')">
              <span>{{ tableTime(props.row.startTime, props.row.endTime) }}</span>
            </el-form-item>
            <el-form-item :label="$t('blind.box.anim1')" v-if="props.row.anim1">
              <media :url="props.row.anim1" type="image" :isPreview="true"></media>
            </el-form-item>
            <el-form-item :label="$t('blind.box.anim2')" v-if="props.row.anim2">
              <media :url="props.row.anim2" type="image" :isPreview="true"></media>
            </el-form-item>

            <el-form-item :label="$t('blind.box.salt')">
              <span>{{ props.row.salt }}</span>
            </el-form-item>
          </el-form>
        </template>
      </el-table-column>

      <el-table-column prop="id" label="ID" sortable> </el-table-column>
      <el-table-column prop="name" :label="$t('blind.box.name')">
      </el-table-column>
      <el-table-column prop="amount" :label="$t('blind.box.amount')">
      </el-table-column>
      <el-table-column prop="nftAmount" :label="$t('blind.box.NFTAmount')">
      </el-table-column>
      <el-table-column :label="$t('blind.box.NFTListInfo')" align="center" width="200">
        <template slot-scope="scope">
          <div class="bfont" :class="!scope.row.isRepetition ? 'cblue' : 'cgreen' ">
            {{ !scope.row.isRepetition? $t('blind.box.unRepeat') : $t('blind.box.isRepeat') }}
          </div>

          <div class="bfont" :class="scope.row.amount * scope.row.nftAmount != scope.row.usedAmount ? 'cred' : 'cgreen' ">
            {{$t('blind.box.totalAmount')}}: {{ scope.row.amount * scope.row.nftAmount }}({{ scope.row.usedAmount }})
          </div>
          <div class="cgreen bfont" v-if="scope.row.amount * scope.row.nftAmount == scope.row.usedAmount">
            {{$t('blind.box.success')}}
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="price" :label="$t('blind.box.price')">
      </el-table-column>
      <el-table-column width="150" prop="imgUrl" :label="$t('blind.box.picture')" align="center">
        <template slot-scope="scope">
          <media :url="scope.row.imgUrl" type="image" :isPreview="true"></media>
        </template>
      </el-table-column>
      <el-table-column prop="status" :label="$t('blind.status')" align="center">
        <template slot-scope="scope">
          <el-tag type="success" v-if="scope.row.sync">
            {{ $t("blind.sign") }}
          </el-tag>
          <el-tag type="info" v-else>{{ $t("blind.unsign") }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column align="center" :label="$t('blind.operation')" width="300" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button type="success" size="mini" @click="handlerBtn('set', scope.row)" v-permission="['POST /admin/blind/box/addNfts']">{{ $t("blind.box.config") }}</el-button>
          <el-button type="primary" size="mini" @click="handlerBtn('edit', scope.row)" v-permission="['POST /admin/blind/box/edit']">{{ $t("blind.edit") }}</el-button>

          <el-button type="primary" size="mini" @click="handlerBtn('sign', scope.row)" v-permission="['POST /admin/blind/box/sign']">{{ $t("blind.box.sign") }}</el-button>

          <el-button type="danger" size="mini" @click="handlerBtn('del', scope.row)" v-permission="['POST /admin/blind/box/deleted']">{{ $t("blind.delete") }}</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />

    <el-dialog :title="dialogTitle" :visible.sync="dialogFormVisible" :width="dialogStatus === 'del' || dialogStatus === 'sync' ? '30%' : '50%'" @closed="callbackCloseDialog">
      <template v-if="dialogStatus === 'add' || dialogStatus === 'edit'">
        <el-form ref="formData" :model="formData" status-icon label-position="left" label-width="120px" :rules="rules">
          <el-form-item :label="$t('blind.box.blindType')" prop="address">
            <el-select v-model="formData.address" :placeholder="$t('blind.box.pleaseSelect')">
              <el-option v-for="(blindType, index) in blindTypes" :key="index" :label="blindType.name" :value="blindType.address">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item :label="$t('blind.box.name')" prop="name">
            <el-input v-model="formData.name"></el-input>
          </el-form-item>
          <el-form-item :label="$t('blind.box.brief')" prop="describe">
            <el-input type="textarea" v-model="formData.describe"></el-input>
          </el-form-item>

          <el-form-item :label="$t('blind.box.times')" prop="times">
            <el-date-picker v-model="formData.times" type="datetimerange" :range-separator="$t('blind.time.to')" :start-placeholder="$t('blind.time.start')" :end-placeholder="$t('blind.time.end')" picker-options="pickerOptions">
            </el-date-picker>
          </el-form-item>

          <el-form-item :label="$t('blind.box.repetition')" prop="isRepetition">
            <el-switch v-model="formData.isRepetition"></el-switch>
          </el-form-item>

          <el-form-item :label="$t('blind.box.amount')" prop="amount">
            <el-input-number v-model="formData.amount"></el-input-number>
          </el-form-item>
          <el-form-item :label="$t('blind.box.NFTAmount')" prop="nftAmount">
            <el-input-number v-model="formData.nftAmount"></el-input-number>
          </el-form-item>
          <div class="form-tip">{{ $t('blind.box.totalAmount') }} :
            <span class="cblue"> {{ totalAmount }}</span>
          </div>
          <el-form-item :label="$t('blind.box.price')" prop="price">
            <el-input-number v-model="formData.price" :controls="false"></el-input-number>
          </el-form-item>
          <el-form-item :label="$t('blind.box.address')" prop="paytokenId">
            <el-select v-model="formData.paytokenId" :placeholder="$t('blind.box.pleaseSelect')">
              <el-option v-for="(paytoken, index) in paytokens" :key="index" :label="paytoken.symbol" :value="paytoken.id">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item :label="$t('blind.box.picture')" prop="imgUrl">
            <el-upload ref="upload" action="" :auto-upload="false" :show-file-list="false" :file-list="filelist" :limit="1" class="avatar-uploader" accept="image/jpg, image/jpeg, image/png, image/gif" :on-change=" (file) => { uploadChange(file, 'cover'); }">
              <template v-if="formData.imgUrl">
                <media :url="formData.imgUrl" type="image"></media>
              </template>
              <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            </el-upload>
          </el-form-item>
          <el-form-item :label="$t('blind.box.openGif')" prop="anim1">
            <el-upload ref="upload" action="" :auto-upload="false" :show-file-list="false" :file-list="anim1List" :limit="1" class="avatar-uploader" accept="image/jpg, image/jpeg, image/png, image/gif" :on-change=" (file) => { uploadChange(file, 'open');}">
              <template v-if="formData.anim1">
                <media :url="formData.anim1" type="image"></media>
              </template>
              <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            </el-upload>
          </el-form-item>
          <el-form-item :label="$t('blind.box.openedGif')" prop="anim2">
            <el-upload ref="upload" action="" :auto-upload="false" :show-file-list="false" :file-list="anim2List" :limit="1" class="avatar-uploader" accept="image/jpg, image/jpeg, image/png, image/gif" :on-change=" (file) => { uploadChange(file, 'opened');}">
              <template v-if="formData.anim2">
                <media :url="formData.anim2" type="image"></media>
              </template>
              <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            </el-upload>
          </el-form-item>
        </el-form>
      </template>
      <!-- 删除 -->
      <template v-if="dialogStatus === 'del'">
        <span>{{ $t("blind.deleteTip") }}</span>
      </template>
      <template v-if="dialogStatus === 'sign'">
        <p class="mb-20">
          <span class="bfont"> {{ $t('blind.box.signTip') }} </span>
        </p>
        <div class="error-tip text-center mb-10" v-if="error"> {{ error }} </div>
        <div class="mb-20 text-center">
          <el-button type="warning" v-if="!connected" @click="connectWallet"> {{ $t("global.connectWallet") }}</el-button>
          <el-button :loading="apiLoading" type="primary" v-else @click="prepareSign"> {{ $t('blind.box.sign') }}</el-button>
        </div>
      </template>
      <template v-if="dialogStatus === 'set'">
        <box-set ref="comSet" :box="box" @confirm="onConfirm"></box-set>
      </template>
      <!-- 底部按钮 -->
      <div slot="footer" class="flex align-center" v-if="dialogStatus != 'set' && dialogStatus != 'sign'">
        <div class="" v-if="error">
          <span class="cred bfont">{{ error }}</span>
        </div>
        <div class="flex1">
          <el-button @click="handlerCancel">{{ $t("blind.cancel") }}</el-button>
          <el-button :loading="apiLoading" type="primary" @click.native.prevent="handlerConfirm">{{ $t("blind.confirm") }}</el-button>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import Pagination from "@/components/Pagination";
  import boxSet from "./blindBoxSet";
  import Media from "@/components/Media";
  import web3 from '@/utils/web3/index';
  export default {
    name: "blindBox",
    components: { Pagination, boxSet, Media },
    data () {
      var validatePrice = (rule, value, callback) => {
        if (this.payType === 2) {
          if (parseInt(value) !== parseFloat(value)) {
            callback(new Error(this.$t("blind.box.integer")));
          } else {
            callback();
          }
        } else {
          callback();
        }
      };
      return {
        list: null,
        total: 0,
        listLoading: false,
        apiLoading: false,
        listQuery: {
          page: 1,
          limit: 10,
          sort: "create_time",
          order: "desc",
          blindTypeName: undefined,
          sync: undefined,
        },
        syncOptions: [
          {
            value: true,
            label: this.$t("blind.sync"),
          },
          {
            value: false,
            label: this.$t("blind.unsync"),
          },
        ],
        dialogFormVisible: false,
        dialogStatus: "",
        error: "",
        formData: {
          address: "",
          name: "",
          describe: "",
          owner: "",
          times: [],
          isRepetition: false,
          amount: "",
          nftAmount: "",
          price: "",
          paytokenId: "",
          imgId: "",
          imgUrl: "",
          anim1: "",
          anim2: "",
        },
        paytokens: [],
        rules: {
          address: [
            {
              required: true,
              message: this.$t("blind.box.emptyType"),
              trigger: "blur",
            },
          ],
          name: [
            {
              required: true,
              message: this.$t("blind.box.emptyName"),
              trigger: "blur",
            },
          ],
          owner: [
            {
              required: true,
              message: this.$t("blind.box.emptyOwner"),
              trigger: "blur",
            },
          ],
          paytokenId: [
            {
              required: true,
              message: this.$t("blind.box.emptyPayToken"),
              trigger: "blur",
            },
          ],
          amount: [
            {
              required: true,
              message: this.$t("blind.box.limitAmount"),
              trigger: "blur",
            },
          ],
          nftAmount: [
            {
              required: true,
              message: this.$t("blind.box.emptyOpenAmount"),
              trigger: "blur",
            },
          ],
          price: [
            {
              required: true,
              message: this.$t("blind.box.limitPrice"),
              trigger: "blur",
            },
            {
              validator: validatePrice,
              trigger: "blur",
            },
          ],
          imgUrl: {
            required: true,
            message: this.$t("blind.box.emptyPicture"),
            trigger: "change",
          },
          times: {
            required: true,
            message: this.$t("blind.box.emptyTimes"),
            trigger: "change",
          },
        },
        pickerOptions: {
          disabledDate (time) {
            return time.getTime() < Date.now() - 8.64e7;
          }
        },
        deleteId: "",
        box: null,
        fileImage: "",
        filelist: [],
        anim1Image: "",
        anim1List: [],
        anim2Image: "",
        anim2List: [],
        // 授权
        blindBoxId: "",
      };
    },
    computed: {
      dialogTitle () {
        let dialogTitle = "";
        switch (this.dialogStatus) {
          case "add":
            dialogTitle = this.$t("blind.add");
            break;
          case "edit":
            dialogTitle = this.$t("blind.edit");
            break;
          case "del":
            dialogTitle = this.$t("blind.delete");
            break;
          default:
            break;
        }
        return dialogTitle;
      },
      connected(){
        return this.$store.state.network.connected;
      },
      web3(){
        return this.$store.state.network.web3;
      },
      config () {
        return this.$store.state.user.config;
      },
      blindTypes () {
        return this.$store.state.user.blindTypes;
      },
      tableBlindType () {
        return function (address) {
          for (let i = 0, len = this.blindTypes.length;i < len;i++) {
            if (this.blindTypes[i].address === address)
              return this.blindTypes[i].name;
          }
        };
      },
      tableTime () {
        return function (start, end) {
          let activeDate = "";
          if (start && end) {
            let startDate = new Date(start * 1000),
              startDate_date = startDate.toLocaleDateString(),
              startDate_time = startDate.toTimeString().slice(0, 8),
              endDate = new Date(end * 1000),
              endDate_date = endDate.toLocaleDateString(),
              endDate_time = endDate.toTimeString().slice(0, 8);
            activeDate =
              startDate_date +
              " " +
              startDate_time +
              "一" +
              endDate_date +
              " " +
              endDate_time;
          }
          return activeDate;
        };
      },
      payType () {
        for (let item of this.paytokens) {
          if (item.id === this.formData.paytokenId) {
            return item.type;
          }
        }
        return null
      },
      totalAmount () {
        if (!this.formData.nftAmount || !this.formData.amount) return 0;
        let amount = parseInt(this.formData.nftAmount * this.formData.amount)
        return !amount ? 0 : amount;
      },
    },
    created () {
      this.init();
    },
    methods: {
      connectWallet(){
        this.$store.dispatch("connect");
      },
      onConfirm () {
        this.dialogFormVisible = false;
        this.getList();
      },
      init(){
        this.paytokenList();
        this.getList();
      },
      paytokenList(){
        this.$api("blind.paytoken.list").then(res => {
          if(this.$tool.checkResponse(res)){
            this.paytokens = res.data.list;
          }
        });
      },
      getList () {
        this.listLoading = true;
        this.$api("blind.box.list", this.listQuery)
          .then((res) => {
            this.list = res.data.list;
            this.getNftList();
            this.total = res.data.total;
            this.listLoading = false;
          })
          .catch(() => {
            this.list = [];
            this.total = 0;
            this.listLoading = false;
          });
      },
      getNftList(){
        var boxList = [];
        for(var i = 0; i < this.list.length; i++){
          boxList.push(this.list[i].id);
        }
        boxList = boxList.join(",");
        this.$api("blind.box.nftsbymulti",  { boxList }).then(res => {
          if(this.$tool.checkResponse(res)){
            var nfts = res.data.list;
            var amountDict = {};
            for(var i = 0; i < nfts.length; i++){
              var nft = nfts[i];
              if(!amountDict[nft.blindBoxId]) amountDict[nft.blindBoxId] = 0;
              amountDict[nft.blindBoxId] += nft.amount;
            }
            for(var i = 0; i < this.list.length; i++){
              var usedAmount = amountDict[this.list[i].id];
              this.list[i].usedAmount = usedAmount ? usedAmount : 0;
            }
            this.list = [].concat(this.list);
          }
        });
      },
      checkForm () {
        let error = false;
        let amount = parseInt(this.formData.amount);
        if (!amount || amount < 1) {
          this.error = this.$t('blind.box.limitAmount')
          error = true;
        }
        let nftAmount = parseInt(this.formData.nftAmount);
        if (!nftAmount || nftAmount < 1) {
          this.error = this.$t('blind.box.limitOpenAmount')
          error = true;
        }
        let price = parseFloat(this.formData.price);
        if (!price || price < 0) {
          this.error = this.$t('blind.box.limitPrice')
          error = true;
        }
        if (!this.fileImage && !this.formData.imgId) {
          this.error = this.$t('blind.box.emptyCover')
          error = true;
        }
        return !error;
      },
      addBox (data) {
        let {
          address,
          name,
          describe,
          owner,
          times,
          isRepetition,
          amount,
          nftAmount,
          price,
          paytokenId,
          imgId,
          imgUrl,
          anim1,
          anim2,
        } = this.formData;
        let startTime = !times[0] ? 0
          : parseInt(new Date(times[0]).getTime() / 1000);
        let endTime = !times[1] ? 0
          : parseInt(new Date(times[1]).getTime() / 1000);
        if (data.cover) {
          imgId = data.cover.id;
          imgUrl = data.cover.url;
        }
        if (data.anim1) anim1 = data.anim1.url;

        if (data.anim2) anim2 = data.anim2.url;

        let sendData = {
          address,
          name,
          describe,
          owner,
          startTime,
          endTime,
          isRepetition,
          amount,
          nftAmount,
          price,
          paytokenId,
          imgId,
          imgUrl,
          anim1,
          anim2,
        };
        this.apiLoading = true;
        this.$api("blind.box.add", sendData)
          .then((res) => {
            this.apiLoading = false;
            if (this.$tool.checkResponse(res)) {
              this.successCallback();
              this.getList();
            }
          })
          .catch((err) => {
            this.failCallback(err);
          });
      },
      editBox (data) {
        let { times, imgId, imgUrl, anim1, anim2 } = this.formData;
        let startTime = !times[0] ? 0
          : parseInt(new Date(times[0]).getTime() / 1000);
        let endTime = !times[1] ? 0
          : parseInt(new Date(times[1]).getTime() / 1000);
        if (data.cover) {
          imgId = data.cover.id;
          imgUrl = data.cover.url;
        }
        if (data.anim1) {
          anim1 = data.anim1.url;
        }
        if (data.anim2) {
          anim2 = data.anim2.url;
        }

        let sendData = {
          id: this.formData.id,
          address: this.formData.address,
          name: this.formData.name,
          describe: this.formData.describe,
          owner: this.formData.owner,
          isRepetition: this.formData.isRepetition,
          amount: this.formData.amount,
          nftAmount: this.formData.nftAmount,
          price: this.formData.price,
          paytokenId: this.formData.paytokenId,
          startTime,
          endTime,
          imgId,
          imgUrl,
          anim1,
          anim2,
        };
        this.apiLoading = true;
        this.$api("blind.box.edit", sendData)
          .then((res) => {
            this.apiLoading = false;
            if (this.$tool.checkResponse(res)) {
              this.successCallback();
              this.getList();
            }
          })
          .catch((err) => {
            this.failCallback(err);
          });
      },
      delBox () {
        this.apiLoading = true;
        this.$api("blind.box.deleted", { id: this.deleteId })
          .then((res) => {
            this.apiLoading = false;
            if (this.$tool.checkResponse(res)) {
              this.successCallback();
              let { page, limit } = this.listQuery;
              if (page > 1 && this.total - 1 - (page - 1) * limit <= 0) {
                this.listQuery.page -= 1;
              }
              this.total -= 1;
              this.getList();
            }
          })
          .catch((err) => {
            this.failCallback(err);
          });
      },
      prepareSign(){
        this.apiLoading = true;
        var _this = this;
        this.$api("blind.box.prepare", {
          id: this.blindBoxId,
          owner: this.web3.coinbase,
        }).then(async function(res){
          _this.apiLoading = false;
          if(_this.$tool.checkResponse(res)){
            var order = res.data;
            var message = order.message;
            message = message.replace("0x", "");
            var result = await web3.sign(message, _this.web3.coinbase);
            if(result.error){
              _this.error = result.error;
            }
            order.signature = result;
            _this.signData(order);
          }
        });
      },
      signData (order) {
        this.apiLoading = true;
        this.$api("blind.box.sign", order).then((res) => {
          this.apiLoading = false;
          if (this.$tool.checkResponse(res)) {
            this.successCallback();
            this.getList();
          }
        }).catch((err) => {
          this.failCallback(err);
        });
      },
      successCallback () {
        this.$notify.success({
          title: this.$t("blind.success"),
          message: this.$t("blind.messageSuccess"),
        });
        this.handlerCancel();
      },
      failCallback (err, msg) {
        this.apiLoading = false;
        this.$notify.error({
          title: this.$t("blind.fail"),
          message: err
            ? (!msg ? this.$t("response." + err.data ? err.data.errno : "") : msg)
            : this.$t("blind.messageFail"),
        });
        if (this.dialogStatus == "del" || this.dialogStatus == "sign") {
          this.handlerCancel();
        }
      },
      callbackCloseDialog () {
        this.resetForm();
        this.$nextTick(function () {
          this.$refs["formData"] ? this.$refs["formData"].clearValidate() : "";
        });

        this.dialogStatus = "";
        this.error = "";
      },
      uploadChange (file, name) {
        var event = event || window.event;
        var files = event.target.files[0];
        var reader = new FileReader();
        var _this = this;
        switch (name) {
          case "cover":
            this.filelist = [];
            this.fileImage = file.raw;
            reader.onload = function (e) {
              _this.formData.imgUrl = e.target.result;
            };
            break;
          case "open":
            this.anim1List = [];
            this.anim1Image = file.raw;
            reader.onload = function (e) {
              _this.formData.anim1 = e.target.result;
            };
            break;
          case "opened":
            this.anim2List = [];
            this.anim2Image = file.raw;
            reader.onload = function (e) {
              _this.formData.anim2 = e.target.result;
            };
            break;
        }
        reader.readAsDataURL(files);
      },
      resetForm () {
        this.formData = {
          address: "",
          name: "",
          describe: "",
          times: [],
          isRepetition: false,
          amount: "",
          nftAmount: "",
          price: "",
          paytokenId: "",
          imgId: "",
          imgUrl: "",
          anim1: "",
          anim2: "",
        };
        this.fileImage = "";
        this.anim1Image = "";
        this.anim2Image = "";
        this.error = "";
      },
      handlerBtn (btnType, row = {}) {
        switch (btnType) {
          case "add":
            this.$nextTick(function () {
              this.$refs["formData"]
                ? this.$refs["formData"].clearValidate()
                : "";
            });
            break;
          case "edit":
            this.fileImage = "";
            this.anim1Image = "";
            this.anim2Image = "";
            this.formData = Object.assign({}, row);
            let startTime = !this.formData.startTime
              ? ""
              : new Date(this.formData.startTime * 1000);
            let endTime = !this.formData.endTime
              ? ""
              : new Date(this.formData.endTime * 1000);
            this.$set(this.formData, "times", [startTime, endTime]);
            this.$nextTick(function () {
              this.$refs["formData"]
                ? this.$refs["formData"].clearValidate()
                : "";
            });
            break;
          case "del":
            this.deleteId = row.id;
            break;
          case "sign":
            this.blindBoxId = row.id;
            break;
          case "set":
            this.box = row;
            break;
        }
        this.dialogFormVisible = true;
        this.dialogStatus = btnType;
      },
      async handlerConfirm () {
        if (this.apiLoading) return;

        switch (this.dialogStatus) {
          case "add":
            if (!this.checkForm()) return;
            var data = {};
            if (this.fileImage) {
              var result = await this.uploadStorage(this.fileImage);
              data.cover = result;
            }
            if (this.anim1Image) {
              var result = await this.uploadStorage(this.anim1Image);
              data.anim1 = result;
            }
            if (this.anim2Image) {
              var result = await this.uploadStorage(this.anim2Image);
              data.anim2 = result;
            }
            this.addBox(data);
            break;
          case "edit":
            if (!this.checkForm()) return;
            var data = {};
            if (this.fileImage) {
              var result = await this.uploadStorage(this.fileImage);
              data.cover = result;
            }
            if (this.anim1Image) {
              var result = await this.uploadStorage(this.anim1Image);
              data.anim1 = result;
            }
            if (this.anim2Image) {
              var result = await this.uploadStorage(this.anim2Image);
              data.anim2 = result;
            }
            this.editBox(data);
            break;
          case "del":
            this.delBox();
            break;
          case "sign":
            this.signData();
            break;
          case "set":
            this.$nextTick(() => {
              if (this.$refs.comSet) {
                this.$refs.comSet.addBox();
              }
            });
            break;
        }
      },
      handlerCancel () {
        this.dialogFormVisible = false;
      },
      async uploadStorage (file) {
        return new Promise((resolve, reject) => {
          const formData = new FormData();
          formData.append("file", file);
          var that = this;
          this.$api("storage.create", formData)
            .then((res) => {
              if (this.$tool.checkResponse(res)) {
                resolve(res.data);
              } else {
                reject(res);
              }
            }).catch((err) => {
              reject(err);
            });
        });
      },
      handlerFilter () {
        this.listQuery.page = 1;
        this.getList();
      },
    },
  };
</script>

<style lang="scss" scoped>
  .avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }
  .avatar-uploader .el-upload:hover {
    border-color: #20a0ff;
  }
  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 120px;
    height: 120px;
    line-height: 120px;
    text-align: center;
  }
  .avatar {
    width: 145px;
    height: 145px;
    display: block;
  }

  .property-group {
    display: flex;
    flex-direction: row;
    margin-bottom: 15px;
    .property {
      flex: 1;
    }
    .left {
      padding-right: 40px;
    }
  }
  .form-tip {
    font-weight: bold;
    padding: 0 0 10px 120px;
  }
</style>
