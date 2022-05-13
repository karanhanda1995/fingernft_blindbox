<template>
  <div class="app-container">
    <div class="filter-container">
      <el-button class="filter-item" type="primary" icon="el-icon-edit" @click="handlerBtn('add')" v-permission="['POST /admin/blind/paytoken/add']">{{ $t("blind.add") }}</el-button>
    </div>
    <el-table v-loading="listLoading" :data="list" :element-loading-text="$t('util.loadingText')" border>
      <el-table-column label="ID" prop="id" sortable></el-table-column>
      <el-table-column label="name" prop="name"></el-table-column>
      <el-table-column label="symbol" prop="symbol"></el-table-column>
      <el-table-column label="type" prop="type">
        <template slot-scope="scope">
          <span>{{ tableType(scope.row.type) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="token" prop="token"></el-table-column>
      <el-table-column label="tokenId" prop="tokenId"></el-table-column>
      <el-table-column prop="metadataContent" :label="$t('blind.paytoken.picture')" align="center">
        <template slot-scope="scope" v-if="scope.row.metadataContent.image">
          <media :url="scope.row.metadataContent.image" type="image" :isPreview="true"></media>
        </template>
      </el-table-column>
      <el-table-column align="center" :label="$t('blind.operation')" width="300" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button type="primary" size="mini" @click="handlerBtn('edit', scope.row)" v-permission="['POST /admin/blind/paytoken/add']">{{ $t("blind.edit") }}</el-button>
          <el-button type="danger" size="mini" @click="handlerBtn('del', scope.row)" v-permission="['POST /admin/blind/paytoken/delete']">{{ $t("blind.delete") }}</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 弹窗 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogFormVisible" :width="dialogStatus === 'del' ? '30%' : '50%'">
      <template v-if="dialogStatus === 'add' || dialogStatus === 'edit'">
        <el-form ref="formData" :model="formData" status-icon label-position="left" label-width="120px" :rules="rules">
          <el-form-item label="type" prop="type">
            <el-select v-model="formData.type" placeholder="请选择" @change="selectChange">
              <el-option v-for="(type, index) in types" :key="index" :label="type.name" :value="type.value">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="token" prop="token">
            <el-input v-model="formData.token" :disabled="tokenDisable"></el-input>
          </el-form-item>
          <el-form-item label="tokenId" prop="tokenId">
            <el-input-number v-model="formData.tokenId" :controls="false" :disabled="tokenIdDisable"></el-input-number>
          </el-form-item>
        </el-form>
      </template>
      <template v-if="dialogStatus === 'del'">
        <span>{{ $t("blind.deleteTip") }}</span>
      </template>
      <!-- 底部按钮 -->
      <span slot="footer" class="dialog-footer">
        <el-button @click="handlerCancel">{{ $t("blind.cancel") }}</el-button>
        <el-button :loading="apiLoading" type="primary" @click.native.prevent="handlerConfirm">{{ $t("blind.confirm") }}</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
  import Media from "@/components/Media";
  import { mapActions } from 'vuex';
  export default {
    name: "paytoken",
    components: { Media },
    data () {
      return {
        apiLoading: false,
        listLoading: true,
        list: null,
        total: 0,
        formData: {
          type: "",
          token: "",
          tokenId: "",
        },
        types: [
          { name: "Main Coin", value: 0 },
          { name: "ERC20 coin", value: 1 },
          { name: "ERC1155", value: 2 },
        ],
        rules: {
          type: {
            required: true,
            message: "type不能为空",
            trigger: "blur",
          },
          token: {
            required: true,
            message: "token不能为空",
            trigger: "blur",
          },
          tokenId: {
            required: true,
            message: "tokenId不能为空",
            trigger: "blur",
          },
        },
        dialogFormVisible: false,
        dialogStatus: "",
        delToken: "",
        delTokenId: "",
      };
    },
    created () {
      this.getList();
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
      tableType () {
        return function (type) {
          for (let i = 0, len = this.types.length;i < len;i++) {
            if (this.types[i].value === type) {
              return this.types[i].name;
            }
          }
        }
      },
      tokenDisable () {
        if (this.formData.type != 0) return false;
        return true;
      },
      tokenIdDisable () {
        if (this.formData.type == 2) return false;
        return true;
      },
    },
    methods: {
      selectChange (val, tokenid) {
        this.formData.token = "";
        this.formData.tokenId = "";
        if (val === 0 || val === 1) {
          this.formData.tokenId = 0;
          if (val == 0) {
            this.formData.token = "0x0000000000000000000000000000000000000000";
          }
        } else {
          this.formData.tokenId = tokenid ? tokenid : "";
        }
      },
      getList () {
        this.listLoading = true;
        this.$api("blind.paytoken.list")
          .then((res) => {
            this.formatData(res.data.list)
            this.list = res.data.list;
            this.total = res.data.total;
            this.listLoading = false;
          }).catch(() => {
            this.list = [];
            this.total = 0;
            this.listLoading = false;
          });
      },
      formatData (data) {
        for (let i = 0, len = data.length;i < len;i++) {
          if (data[i].metadataContent) {
            data[i].metadataContent = JSON.parse(data[i].metadataContent);
          } else {
            data[i].metadataContent = { image: "" };
          }
        }
      },
      resetForm () {
        this.formData = {
          type: "",
          token: "",
          tokenId: "",
        };
      },
      // 按钮执行弹窗
      handlerBtn (btnType, row = {}) {
        if (btnType === "add") {
          this.resetForm();
          this.$nextTick(function () {
            this.$refs["formData"] ? this.$refs["formData"].clearValidate() : "";
          });
        }
        if (btnType === "edit") {
          this.formData = Object.assign({}, row);
          console.log("this.formData", this.formData, row);
          // this.selectChange(this.formData.type, this.formData.tokenId);
          this.$nextTick(function () {
            this.$refs["formData"] ? this.$refs["formData"].clearValidate() : "";
          });
        }
        if (btnType === "del") {
          this.delToken = row.token;
          this.delTokenId = row.tokenId;
        }
        this.dialogFormVisible = true;
        this.dialogStatus = btnType;
      },
      // 确定按钮回调
      handlerConfirm () {
        if (this.dialogStatus === "add") {
          this.addData();
        }
        if (this.dialogStatus === "edit") {
          this.editData();
        }
        if (this.dialogStatus === "del") {
          this.delData();
        }
      },
      // 取消按钮回调
      handlerCancel () {
        this.dialogFormVisible = false;
      },
      addData () {
        this.$refs["formData"].validate((valid) => {
          if (valid && !this.apiLoading) {
            this.apiLoading = true;
            this.formData.token = this.formData.token.toLowerCase();
            this.$api("blind.paytoken.add", this.formData)
              .then((res) => {
                if (this.$tool.checkResponse(res)) {
                  this.successCallback();
                  this.getList();
                } else {
                  this.failCallback();
                }
              })
              .catch((err) => {
                this.failCallback(err);
              });
          }
        });
      },
      editData () {
        this.$refs["formData"].validate((valid) => {
          if (valid && !this.apiLoading) {
            this.apiLoading = true;
            let { id, type, token, tokenId } = this.formData;
            token = token.toLowerCase();
            let sendData = { id, type, token, tokenId };
            this.$api("blind.paytoken.edit", sendData)
              .then((res) => {
                if (this.$tool.checkResponse(res)) {
                  this.successCallback();
                  this.getList();
                } else {
                  this.failCallback();
                }
              })
              .catch((err) => {
                this.failCallback(err);
              });
          }
        });
      },
      delData () {
        if (!this.apiLoading) {
          this.apiLoading = true;
        } else {
          return;
        }
        this.$api("blind.paytoken.delete", {
          token: this.delToken,
          tokenId: this.delTokenId
        })
          .then((res) => {
            if (this.$tool.checkResponse(res)) {
              this.successCallback();
              this.getList();
            } else {
              this.failCallback();
            }
          })
          .catch((err) => {
            this.failCallback(err);
          });
      },

      // 请求成功回调
      successCallback () {
        this.$notify.success({
          title: this.$t("blind.success"),
          message: this.$t("blind.messageSuccess"),
        });
        this.handlerCancel();
        this.apiLoading = false;
      },
      // 请求失败回调
      failCallback (err) {
        this.apiLoading = false;
        this.$notify.error({
          title: this.$t("blind.fail"),
          message: err
            ? this.$t("response." + err.data ? err.data.errno : "")
            : this.$t("blind.messageFail"),
        });
        if (this.dialogStatus == "del") {
          this.handlerCancel();
        }
      },
    },
  };
</script>

<style lang="scss">
</style>
