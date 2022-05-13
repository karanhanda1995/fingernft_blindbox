<template>
  <el-dialog :model-value="visible" :show-close="false" :close-on-click-modal="false" @closed="closed" custom-class="fg-dialog collection-dialog" destroy-on-close>
    <template #title>
      <div class="fg-dialog-title">
        <div class="left">{{ $t("create.title") }}</div>
        <div class="right" @click="$emit('close')">
          <img class="close-img" src="@/assets/create-img/pop_shut.png " />
        </div>
      </div>
    </template>
    <div class="fg-dialog-body" v-if="!confirm">
      <div class="process">
        <div class="upload-wrapper">
          <div class="upload-box">
            <div class="pic-preview">
              <el-image :src="tempImg" fit="cover">
                <template #error>
                  <div class="image-slot"></div>
                </template>
              </el-image>
            </div>
            <div class="pic-upload" v-if="!tempImg">
              <el-upload ref="uploadFile" class="upload-pic" accept="image/gif,image/png,image/jpeg,image/webp" :limit="1" :auto-upload="false" action=""
                  :on-change="(file, fileList) => { uploadChange(file, fileList, 'uploadFile'); }" 
                  name="image" :showFileList="false">
                <template #tip>
                  <div class="upload-tip">{{ $t("create.fileTip") }}</div>
                </template>
                <el-button size="small" class="choose-btn" type="primary" plain>
                  {{ $t("erc721.chooseFile") }}</el-button>
              </el-upload>
            </div>
            <div class="pic-upload" v-else>
              <div class="upload-pic">
                <el-button class="delete-btn" size="small" type="primary" @click="deletePic" plain>
                  {{ $t("erc721.deletePicture") }}
                </el-button>
                <span class="upload-tip">{{ $t("create.fileTip") }}</span>
              </div>
            </div>
          </div>
          <div class="form-error-tip error-position">
            {{ formError.file }}
          </div>
        </div>
        <div class="input-group">
          <div class="input-info">
            <div class="tip">
              {{ $t("create.displayName") }}
              <span class="gray-font"> ({{ $t("dialog.required") }})</span>
            </div>
            <el-input class="fg-dialog-input" :placeholder="$t('create.placeName')" v-model="form.displayName"></el-input>
            <div class="input-error" v-if="formError.displayName">
              {{ formError.displayName }}
            </div>
            <div class="stip">
              <div class="stip-item">{{ $t("create.nameTip") }}</div>
            </div>
          </div>
          <div class="input-info">
            <div class="tip">
              {{ $t("create.symbol")
              }}<span class="gray-font"> ({{ $t("dialog.required") }})</span>
            </div>
            <el-input class="fg-dialog-input" :placeholder="$t('create.placeSymbol')" v-model="form.symbol"></el-input>
            <div class="input-error" v-if="formError.symbol"> {{ formError.symbol }} </div>
          </div>
          <div class="input-info">
            <div class="tip">{{ $t("create.introduction") }}</div>
            <el-input class="fg-dialog-input" :placeholder="$t('create.placeIntro')" v-model="form.introduction"></el-input>
          </div>

        </div>
        <div class="process-btn">
          <el-button @click="onSubmit" type="primary" :loading="confirm">{{ $t("dialog.createCollection") }}
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
          <span v-if="step.upload != 1" :class="step.upload == 2 ? 'finish' : ''" class="step iconfont icon-seleted"></span>
          <div class="step loading" v-else>
            <img class="loading-animation" src="@/assets/img/loading.png" />
          </div>
        </div>
        <div class="process-btn">
          <el-button @click="onUpload" type="primary" v-if="step.upload == 0">{{ $t("dialog.upload") }}</el-button>
          <el-button disabled type="info" v-else-if="step.upload == 1">{{
            $t("dialog.inProgress")
          }}</el-button>
          <el-button disabled type="info" v-else>{{
            $t("dialog.done")
          }}</el-button>
        </div>
        <div class="process-error" v-if="error.upload">
          {{ error.upload}}
        </div>
      </div>
      <div class="process">
        <div class="step-info">
          <div class="text">
            <span>{{ $t("dialog.signsCollection") }}</span>
          </div>
          <span v-if="step.collection != 1" :class="step.collection == 2 ? 'finish' : ''" class="step iconfont icon-seleted"></span>
          <div class="step loading" v-else>
            <img class="loading-animation" src="@/assets/img/loading.png" />
          </div>
        </div>
        <div class="process-btn">
          <el-button disabled type="info" v-if="step.upload != 2">
            {{ $t("dialog.createCollection") }}
          </el-button>
          <el-button @click="onCollection" type="primary" v-else-if="step.collection == 0">
            {{ $t("dialog.createCollection") }}
          </el-button>
          <el-button disabled type="info" v-else-if="step.collection == 1">
            {{ $t("dialog.inProgress") }}
          </el-button>
          <el-button disabled type="info" v-else>
            {{ $t("dialog.done") }}
          </el-button>
        </div>
        <div class="process-error" v-if="error.collection">
          {{ error.collection }}
        </div>
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
        tempImg: null,
        form: {
          file: "",
          displayName: "",
          symbol: "",
          introduction: "",
        },
        formError: {
          file: "",
          displayName: "",
          symbol: "",
        },
        step: {
          upload: 0,
          collection: 0,
        },
        error: {
          all: "",
          upload: "",
          collection: "",
        },
      };
    },
    emits: ['confirm', 'close'],
    props: {
      show: {
        type: Boolean,
        default: false,
      },
      ercType: {
        type: Number,
        default: "",
      },
    },
    computed: {
      user: function () {
        return this.$store.state.user;
      },
      config(){
        return this.$store.state.config;
      },
    },
    watch: {
      show (val) {
        this.visible = this.show;
      },
    },
    methods: {
      closed () {
        this.tempImg = null;
        this.confirm = false;
        this.form = {
          file: "",
          displayName: "",
          symbol: "",
          introduction: "",
        };
        this.formError = {
          file: "",
          displayName: "",
          symbol: "",
        };
        this.step = {
          upload: 0,
          collection: 0,
        };
        this.error = {
          all: "",
          upload: "",
          collection: "",
        };
      },
      uploadChange (file) {
        let size = file.raw.size;
        if (size > 30 * 1024 * 1024) {
          this.errorForm.file = this.$t("form.limitFile");
          return;
        }
        this.form.file = file.raw;
        var event = event || window.event;
        var file = event.target.files[0];
        var reader = new FileReader();
        var _this = this;
        reader.onload = function (e) {
          _this.tempImg = e.target.result;
        };
        reader.readAsDataURL(file);
      },
      deletePic () {
        this.tempImg = null;
        this.form.file = null;
      },
      checkForm () {
        this.formError = {
          file: "",
          displayName: "",
          symbol: "",
        };
        let result = true;
        if (!this.form.file) {
          this.formError.file = this.$t("form.noFile");
          result = false;
        }
        if (!this.form.displayName) {
          this.formError.displayName = this.$t("form.noDisplayName");
          result = false;
        }
        if (!this.form.symbol) {
          this.formError.symbol = this.$t("form.noSymbol");
          result = false;
        }
        return result;
      },
      async onSubmit () {
        let that = this;
        if (!this.checkForm()) return;
        this.confirm = true;
        setTimeout(async function () {
          await that.onUpload();
        }, 100);
      },
      async createUpload () {
        let result = await this.uploadStorage(this.form.file);
        if (result.error) {
          return { error: result.errmsg };
        }
        let data = {
          name: this.form.displayName,
          symbol: this.form.symbol,
          description: this.form.introduction,
          storageId: result.id,
          cover: result.url,
          signer: this.config.miner,
          type: this.ercType,
          owner: this.user.coinbase,
        };
        await this.$api("contract.create", data).then((res) => {
          if (this.$tools.checkResponse(res)) {
            result = {};
          } else {
            result = { error: res.errmsg };
          }
        });
        return result;
      },
      async uploadStorage (file) {
        return new Promise((resolve) => {
          const formData = new FormData();
          formData.append("file", file);
          var that = this;
          this.$api("storage.upload", formData).then(async function (res) {
            if (that.$tools.checkResponse(res)) {
              resolve(res.data);
            } else {
              resolve({
                error: res.errmsg,
              });
            }
          });
        });
      },
      async onCollection () {
        this.step.collection = 1;
        let result = await this.createContract();
        if (result.error) {
          this.error.collection = result.error;
          this.step.collection = 0;
        } else {
          this.error.collection = "";
          this.step.collection = 2;
          this.$emit("confirm");
        }
      },
      async createContract () {
        let asset = { name: this.form.displayName, symbol: this.form.symbol };
        asset.miner = this.config.miner;
        if (this.ercType === 3) {
          asset.contractURI = "ipfs:/";
          asset.uri = "ipfs:/";
          return this.$sdk.newERC721(this.user.coinbase, asset);
        } else if (this.ercType === 2) {
          asset.contractURI = "ipfs:/";
          asset.uri = "ipfs:/";
          return this.$sdk.newERC1155(this.user.coinbase, asset);
        }
      },
      async onUpload () {
        this.step.upload = 1;
        let result = await this.createUpload();
        if (result.error) {
          this.error.upload = result.error;
          this.step.upload = 0;
        } else {
          this.error.upload = "";
          this.step.upload = 2;
        }
      },
    },
  };
</script>

<style lang="scss">
  .collection-dialog-input {
    .el-input-group__prepend {
      padding-left: 0 !important;
    }
  }
</style>

<style lang="scss" scoped>
  .upload-wrapper {
    padding-bottom: 20px;
  }
  .upload-box {
    display: flex;
  }

  .pic-preview {
    width: 100px;
    height: 100px;
    flex-shrink: 0;
    .el-image {
      width: 100%;
      height: 100%;
      border-radius: 50%;
    }
    .image-slot {
      height: 100%;
      background-color: #f6f6f6;
    }
  }
  .pic-upload {
    margin-left: 16px;
    .upload-pic {
      height: 100%;
      display: flex;
      flex-direction: column-reverse;
      justify-content: space-between;
      align-items: flex-start;
    }
  }
  .form-error-tip {
    padding-top: 5px;
  }
</style>
