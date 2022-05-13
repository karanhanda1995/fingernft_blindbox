<template>
  <div class="set-wrap">
    <div class="table-container" v-if="tab != 'config'" :key="'nfts'">
      <div class="table-header">
        <div class="left">
          <span class="bfont mr-10" :class="!box.isRepetition ? 'cblue' : 'cgreen' ">
            {{ !box.isRepetition ? $t('blind.box.unRepeat') : $t('blind.box.isRepeat') }}</span>
          <template v-if="!box.isRepetition">
            <span class="bfont mr-10" :class="!repeatTip.max ? 'cred' : 'cgreen'">
              {{ $t('blind.box.amount') }} &le; {{ box.amount }}</span>
            <span class="bfont mr-10" :class="!repeatTip.typeAmount ? 'cred' : 'cgreen'">
              {{ $t('blind.box.NFTTypes') }} &ge; {{ box.nftAmount}}</span>
          </template>
          <span class="bfont mr-10" :class="box.amount * box.nftAmount != useAmount ? 'cred' : 'cgreen'">
            {{$t('blind.box.totalAmount')}}:{{ box.amount * box.nftAmount}}({{ useAmount }})
          </span>
          <span class="cgreen bfont mr-10" v-if="useAmount == box.amount * box.nftAmount">{{$t('blind.box.success')}}</span>
        </div>
        <div class="right">
          <el-button @click="goConfig" size="mini" type="primary">
            {{ $t("blind.set.select") }}
          </el-button>
        </div>
      </div>
      <el-table class="table-body" v-loading="nftsLoading" :data="nfts" :element-loading-text="$t('util.loadingText')" border tooltip-effect="dark" style="width: 100%">
        <el-table-column align="center" label="ID" prop="id" sortable></el-table-column>
        <el-table-column align="center" :label="$t('blind.nft.name')">
          <template slot-scope="scope" v-if="scope.row.metadataContent.name">
            {{scope.row.metadataContent.name}}
          </template>
        </el-table-column>

        <el-table-column align="center" :label="$t('blind.nft.property')">
          <template slot-scope="scope" v-if="scope.row.metadataContent.attributes">
            <div v-for="(attribute, i) in scope.row.metadataContent.attributes" :key="i">
              {{ attribute.key}}: {{ attribute.value }}
            </div>
          </template>
        </el-table-column>

        <el-table-column align="center" :label="$t('blind.set.picture')" prop="metadataContent" width="200px">
          <template slot-scope="scope" v-if="scope.row.metadataContent.image">
            <media :isPreview="true" :url="scope.row.metadataContent.image" type="image"></media>
          </template>
        </el-table-column>
        <el-table-column align="center" :label="$t('blind.set.audioORvideo')" prop="metadataContent">
          <template slot-scope="scope" v-if="scope.row.metadataContent.animation_url">
            <media :url="scope.row.metadataContent.animation_url" :isPreview="true"></media>
          </template>
        </el-table-column>
        <el-table-column :label="$t('blind.set.amount')" width="120px">
          <template slot-scope="scope">
            <el-input @input="(e) => { onInput(scope, e) }" type="number" v-model="scope.row.amount" :min="1"></el-input>
          </template>
        </el-table-column>
        <el-table-column :label="$t('blind.box.probability')" width="100" align="center">
          <template slot-scope="scope">
            <span class="cred bfont" v-if="useAmount != box.amount * box.nftAmount"> - </span>
            <span class="cgreen bfont" v-else>{{ getProbability(scope.row) }}%</span>
          </template>
        </el-table-column>

        <el-table-column :label="$t('blind.operation')">
          <template slot-scope="scope">
            <el-button type="danger" size="mini" @click="delNFT(scope.$index)">{{ $t("blind.delete") }}</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="table-footer">
        <div class="left">
          <span class="cred bfont" v-if="error"> {{ error}} </span>
        </div>
        <div class="right">
          <el-button :loading="apiLoading" @click="addData" type="primary">{{ $t("blind.confirm") }}</el-button>
        </div>

      </div>
    </div>
    <div class="table-container" v-else :key="'config'">
      <div class="table-header">
        <div class="left">
          <el-button type="text" @click="goNfts">
            {{ $t("blind.set.back") }}
          </el-button>
        </div>
        <div class="right">
          <el-button size="mini" type="primary" @click="dialogVisible = true">
            {{ $t("blind.box.addNFT") }}
          </el-button>
        </div>
      </div>
      <el-table class="table-body" ref="multipleTable" v-loading="listLoading" :data="list" :element-loading-text="$t('util.loadingText')" border tooltip-effect="dark" style="width: 100%"
        @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55"> </el-table-column>
        <el-table-column align="center" label="ID" prop="id" sortable></el-table-column>

        <el-table-column align="center" :label="$t('blind.nft.name')">
          <template slot-scope="scope" v-if="scope.row.metadataContent.name">
            {{scope.row.metadataContent.name}}
          </template>
        </el-table-column>
        <el-table-column align="center" :label="$t('blind.nft.property')">
          <template slot-scope="scope" v-if="scope.row.metadataContent.attributes">
            <div v-for="(attribute, i) in scope.row.metadataContent.attributes" :key="i">
              {{ attribute.key}}: {{ attribute.value }}
            </div>
          </template>
        </el-table-column>

        <el-table-column align="center" :label="$t('blind.set.picture')" prop="metadataContent" width="200px">
          <template slot-scope="scope" v-if="scope.row.metadataContent.image">
            <media :isPreview="true" :url="scope.row.metadataContent.image" type="image"></media>
          </template>
        </el-table-column>
        <el-table-column align="center" :label="$t('blind.set.audioORvideo')" prop="metadataContent" width="200px">
          <template slot-scope="scope" v-if="scope.row.metadataContent.animation_url">
            <media :url="scope.row.metadataContent.animation_url" :isPreview="true"></media>
          </template>
        </el-table-column>
      </el-table>
      <div class="table-footer">
        <div class="right">
          <el-button @click="selNFTs" type="primary">
            {{ $t("blind.confirm") }}
          </el-button>
        </div>
      </div>
    </div>
    <el-dialog :title="$t('blind.add')" :visible.sync="dialogVisible" @open="onOpen" append-to-body>
      <el-form ref="formData" :model="formData" status-icon label-position="left" label-width="120px" :rules="rules">
        <el-form-item :label="$t('blind.nft.blindType')" prop="address">
          <el-select disabled v-model="formData.address" :placeholder="$t('blind.nft.pleaseSelect')">
            <el-option v-for="(blindType, index) in blindTypes" :key="index" :label="blindType.name" :value="blindType.address">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('blind.nft.name')" prop="name">
          <el-input v-model="formData.name"></el-input>
        </el-form-item>
        <el-form-item :label="$t('blind.nft.brief')" prop="description">
          <el-input type="textarea" v-model="formData.description"></el-input>
        </el-form-item>
        <el-form-item :label="$t('blind.nft.picture')" prop="imgUrl">
          <el-upload ref="upload" action="" :auto-upload="false" :show-file-list="false" :file-list="fileListImage" :limit="1" class="avatar-uploader"
            accept="image/jpg, image/jpeg, image/png, image/gif" :on-change="(file) => { uploadChange(file, 'uploadImage'); }">
            <template v-if="formData.imgUrl">
              <media :url="formData.imgUrl" type="image"></media>
            </template>
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </el-form-item>
        <el-form-item :label="$t('blind.nft.audioORvideo')" prop="animUrl">
          <el-upload ref="upload" action=""
              :auto-upload="false"
              :show-file-list="false"
              :file-list="fileListVideo"
              :limit="1"
              class="avatar-uploader"
              accept="audio/mp3, audio/wav, audio/oga, video/webm, video/mp4, video/m4v, video/ogg, video/ogv, video/mov"
              :on-change=" (file) => { uploadChange(file, 'uploadVideo'); }"
              >
            <template v-if="formData.animUrl">
              <media :url="formData.animUrl" :type="formData.animType"></media>
            </template>
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </el-form-item>
        <el-form-item :label="$t('blind.nft.property')">
          <div class="property-group" v-for="(property, i) in formData.properties" :key="i">
            <div class="property left">
              <el-input v-model="property.key" :placeholder="$t('blind.property.key')" @input="propertyInput"> </el-input>
            </div>
            <div class="property right">
              <el-input v-model="property.value" :placeholder="$t('blind.property.value')" @input="propertyInput"> </el-input>
            </div>
          </div>
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click.native.prevent="dialogVisible=false">
          {{ $t("blind.cancel") }}
        </el-button>
        <el-button :loading="apiLoading" type="primary" @click.native.prevent="addForm">{{ $t("blind.confirm") }}
        </el-button>
      </div>

    </el-dialog>
  </div>
</template>

<script>
  import Media from "@/components/Media";
  export default {
    name: "nftSet",
    components: { Media },
    data () {
      return {
        tab: "nfts",
        listLoading: true,
        list: [],
        nftsLoading: true,
        nfts: [],
        multipleSelection: [],
        apiLoading: false,
        error: "",
        dialogVisible: false,
        formData: {
          address: "",
          name: "",
          description: "",
          storageId: "",
          imgUrl: "",
          animStorageId: "",
          animUrl: "",
          animType: "",
          properties: [{ key: "", value: "" }],
        },
        rules: {
          address: {
            required: true,
            message: this.$t("blind.nft.emptyType"),
            trigger: "change",
          },
          imgUrl: {
            required: true,
            message: this.$t("blind.nft.emptyPicture"),
            trigger: "blur",
          },
        },
        fileImage: "",
        fileVideo: "",
        fileListImage: [],
        fileListVideo: [],
      };
    },
    props: ["box"],
    computed: {
      useAmount () {
        let amount = 0;
        for (var i = 0;i < this.nfts.length;i++) {
          var _amount = parseInt(this.nfts[i].amount)
          amount += !_amount ? 0 : _amount;
        }
        return amount;
      },
      repeatTip () {
        if (this.box.isRepetition) return;
        let max = 0;
        let typeAmount = this.nfts.length;
        for (var i = 0;i < this.nfts.length;i++) {
          var _amount = parseInt(this.nfts[i].amount)
          max = _amount && _amount > max ? _amount : max;
        }
        return {
          max: max <= parseInt(this.box.amount),
          typeAmount: typeAmount >= parseInt(this.box.nftAmount)
        }
      },
      blindTypes () {
        return this.$store.state.user.blindTypes;
      },
    },
    created () {
      this.getNfts();
    },
    methods: {
      getProbability (nft) {
        let amount = parseInt(nft.amount)
        if (!amount) return 0;
        if (!this.box.isRepetition) {
          return this.$tool.decimal((amount * 100) / this.box.amount, 2);
        }
        let totalAmount = this.box.amount * this.box.nftAmount;
        let probability = 0;
        for (var i = 0;i < this.box.nftAmount;i++) {
          if (!probability) {
            probability = (totalAmount - amount - i) / totalAmount;
          } else {
            probability = probability * (totalAmount - amount - i) / totalAmount;
          }
        }
        return this.$tool.decimal((1 - probability) * 100, 2);
      },
      onInput (scope, e) {
        this.nfts = [].concat(this.nfts);
      },
      getNfts () {
        this.nftsLoading = true;
        this.$api("blind.box.nfts", { blindBoxId: this.box.id })
          .then((res) => {
            if (this.$tool.checkResponse(res)) {
              for (var i = 0;i < res.data.list.length;i++) {
                let nft = res.data.list[i];
                nft.metadataContent = JSON.parse(
                  nft.metadataContent
                );
                this.nfts.push(Object.assign({}, nft));
              }
            }
            this.nftsLoading = false;
          })
          .catch(() => {
            this.nftsLoading = false;
          });
      },
      getList () {
        this.listLoading = true;
        this.$api("blind.nft.all", { address: this.box.address })
          .then((res) => {
            if (this.$tool.checkResponse(res)) {
              this.formatData(res.data.list);
              this.list = res.data.list;
              this.checkSelected();
            }
            this.listLoading = false;
          })
          .catch((err) => {
            this.listLoading = false;
          });
      },
      checkForm () {
        let error = false
        let total = this.box.amount * this.box.nftAmount;
        if (total < this.useAmount) {
          this.error = this.$t('blind.box.limitTotalAmount') + total;
          error = true;
        }
        if (!this.box.isRepetition && !this.repeatTip.max) {
          this.error = this.$t('blind.box.limitMaxAmount') + this.box.amount;
          error = true;
        }

        return !error;
      },
      addData () {
        if (!this.checkForm()) return;

        if (this.apiLoading) return;
        this.apiLoading = true;

        let nfts = [];
        for (var i = 0;i < this.nfts.length;i++) {
          var nft = this.nfts[i];
          nfts.push({
            blindNftId: this.nfts[i].id,
            amount: this.nfts[i].amount,
            blindBoxId: this.box.id,
            ...nft
          });
        }
        nfts = JSON.stringify(nfts);
        let data = {
          blindBoxId: this.box.id,
          nfts: nfts,
        };
        this.$api("blind.box.addNfts", data)
          .then((res) => {
            this.apiLoading = false;
            if (this.$tool.checkResponse(res)) {
              this.$notify.success({
                title: this.$t("blind.success"),
                message: this.$t("blind.messageSuccess"),
              });
              this.$emit("confirm");
            }
          })
          .catch((err) => {
            this.apiLoading = false;
            this.$notify.error({
              title: this.$t("blind.fail"),
              message: this.$t("response." + err.data ? err.data.errno : ""),
            });
          });
      },
      goNfts () {
        this.tab = "nfts";
      },
      goConfig () {
        this.error = "";
        this.tab = "config";
        this.getList();
      },
      handleSelectionChange (val) {
        this.multipleSelection = val;
      },
      selNFTs () {
        this.nfts = this.multipleSelection;
        var nfts = [];
        console.log("this.nfts", this.nfts);
        for(var i = 0; i < this.nfts.length; i++){
          console.log("i", i);
          var nft = Object.assign({}, this.nfts[i]);
          nft.amount = 1;
          nfts.push(nft);
        }
        this.nfts = nfts;
        this.goNfts();
      },
      delNFT (index) {
        this.nfts.splice(index, 1);
        this.nfts = [].concat(this.nfts);
      },
      onOpen () {
        this.formData = {
          address: this.box.address,
          name: "",
          description: "",
          imgUrl: "",
          storageId: "",
          animUrl: "",
          animStorageId: "",
          properties: [{ key: "", value: "" }],
        };
        this.fileImage = "";
        this.fileVideo = "";
      },
      formatData (data) {
        for (let i = 0, len = data.length;i < len;i++) {
          data[i].metadataContent = JSON.parse(data[i].metadataContent);
        }
      },
      checkSelected () {
        this.$nextTick(function () {
          for (var i = 0;i < this.list.length;i++) {
            let row = this.list[i];
            if (!this.existsNFT(row.id)) {
              this.$refs.multipleTable.toggleRowSelection(row, false);
            } else {
              this.$refs.multipleTable.toggleRowSelection(row, true);
            }
          }
        });
      },
      existsNFT (id) {
        for (var i = 0;i < this.nfts.length;i++) {
          let nft = this.nfts[i];
          if (nft.blindNftId == id) return true;
        }
        return false;
      },
      propertyInput (e) {
        let emptyC = 0;
        for (var i = 0;i < this.formData.properties.length;i++) {
          var property = this.formData.properties[i];
          if (!property.key || !property.value) emptyC += 1;
        }
        if (emptyC != 1) {
          this.formData.properties = this.formData.properties.filter(function (
            property
          ) {
            return property.key || property.value;
          });
          this.formData.properties.push({ key: "", value: "" });
        }
        this.formData = JSON.parse(JSON.stringify(this.formData));
      },
      fullProperties (properties) {
        var _properties = [];
        for (var i = 0;i < properties.length;i++) {
          var key = properties[i].key.trim();
          var value = properties[i].value.trim();
          if (!key || !value) continue;
          var trait_type = key;
          _properties.push({ key, value, trait_type });
        }
        return _properties;
      },
      uploadChange (files, uploadType) {
        if (uploadType === "uploadImage") {
          this.fileListImage = [];
          this.fileImage = files.raw;
        }
        if (uploadType === "uploadVideo") {
          this.fileListVideo = [];
          this.fileVideo = files.raw;
        }
        var event = event || window.event;
        var file = event.target.files[0];
        var reader = new FileReader();
        var _this = this;
        reader.onload = function (e) {
          if (uploadType === "uploadImage") {
            _this.formData.imgUrl = e.target.result;
          }
          if (uploadType === "uploadVideo") {
            _this.formData.animUrl = e.target.result;
            _this.formData.animType = file.type;
            console.log("animType", file.type);
          }
        };
        reader.readAsDataURL(file);
      },
      handlerUploadImage () {
        return new Promise((resolve, reject) => {
          let formData = new FormData();
          formData.append("file", this.fileImage);
          formData.append("flag", "ipfs");
          this.$api("storage.create", formData)
            .then((res) => {
              if (this.$tool.checkResponse(res)) {
                resolve(res.data);
              } else {
                reject(res);
              }
            })
            .catch((err) => {
              reject(err);
            });
        });
      },
      handlerUploadVideo () {
        return new Promise((resolve, reject) => {
          let formData = new FormData();
          formData.append("files", this.fileVideo);
          formData.append("files", this.fileImage);
          this.$api("storage.multiupload", formData)
            .then((res) => {
              if (this.$tool.checkResponse(res)) {
                resolve(res.data);
              } else {
                reject(res);
              }
            })
            .catch((err) => {
              reject(err);
            });
        });
      },
      addForm () {
        this.$refs["formData"].validate((valid) => {
          if (valid && !this.apiLoading) {
            this.apiLoading = true;
            let promiseArr = [];
            if (this.fileVideo) {
              promiseArr = [this.handlerUploadVideo()];
            } else {
              promiseArr = [this.handlerUploadImage()];
            }
            Promise.all(promiseArr)
              .then((res) => {
                this.addNFT(res);
              })
              .catch((err) => {
                this.failCallback();
              });
          }
        });
      },
      addNFT (fileData) {
        let {
          address,
          name,
          description,
          storageId,
          imgUrl,
          animStorageId,
          animUrl,
          properties,
        } = this.formData;
        if (fileData) {
          storageId = this.fileVideo ? fileData[0][1].id : fileData[0].id;
          imgUrl = this.fileVideo
            ? fileData[0][1].ipfshash
            : fileData[0].ipfshash;
          animStorageId = this.fileVideo ? fileData[0][0].id : "";
          animUrl = this.fileVideo ? fileData[0][0].ipfshash : "";
        } else {
          storageId = imgUrl = animStorageId = animUrl = "";
        }
        let propertys = this.fullProperties(properties);
        propertys = JSON.stringify(propertys);
        let sendData = {
          address,
          name,
          description,
          type: 5,
          storageId,
          imgUrl,
          animStorageId,
          animUrl,
          propertys,
        };
        this.$api("blind.nft.add", sendData)
          .then((res) => {
            this.apiLoading = false;
            if (this.$tool.checkResponse(res)) {
              this.$notify.success({
                title: this.$t("blind.success"),
                message: this.$t("blind.messageSuccess"),
              });
              this.dialogVisible = false;
              this.getList();
            }
          })
          .catch((err) => {
            this.failCallback(err);
          });
      },
      failCallback (err) {
        this.apiLoading = false;
        this.$notify.error({
          title: this.$t("blind.fail"),
          message: err
            ? this.$t("response." + err.data ? err.data.errno : "")
            : this.$t("blind.messageFail"),
        });
        this.dialogVisible = false;
      },
    },
  };
</script>

<style lang="scss" scoped>
  .table-container {
    position: relative;
    height: 60vh;
    .table-body {
      position: absolute;
      top: 35px;
      left: 0;
      right: 0;
      bottom: 50px;
      overflow-y: scroll;
    }
    .table-footer {
      position: absolute;
      left: 0;
      right: 0;
      bottom: 0;
    }
  }

  .el-table__row audio {
    width: 80%;
  }
  .table-header {
    display: flex;
    margin-bottom: 10px;
    align-items: center;
    .right {
      flex: 1;
      text-align: right;
    }
  }
  .table-footer {
    display: flex;
    margin-top: 10px;
    .right {
      flex: 1;
      text-align: right;
    }
  }
  .set-wrap {
    max-height: 60vh;
    overflow: hidden;
    overflow-y: auto;
  }

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

  .el-table__row audio {
    width: 80%;
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
  .el-upload audio {
    width: 300px;
  }
</style>
