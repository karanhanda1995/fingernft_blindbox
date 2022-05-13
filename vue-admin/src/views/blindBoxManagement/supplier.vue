<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="listQuery.name" clearable class="filter-item w-200 mr-10" :placeholder="$t('blind.placeholder')" />
      <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handlerFilter">{{$t("blind.select")}}</el-button>
      <el-button class="filter-item" type="primary" icon="el-icon-edit" @click="handlerBtn('add')" v-permission="['POST /admin/blind/type/add']">{{$t('global.add')}}</el-button>
    </div>
    <el-table v-loading="listLoading" :data="list" :element-loading-text="$t('util.loadingText')" border>
      <el-table-column label="ID" prop="id" sortable></el-table-column>
      <el-table-column :label="$t('blind.type.name')" prop="name"></el-table-column>
      <el-table-column :label="$t('global.contractAddress')" prop="address"></el-table-column>
      <el-table-column align="center" :label="$t('blind.operation')" width="300" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button type="primary" size="mini" @click="handlerBtn('edit', scope.row)" v-permission="['POST /admin/blind/type/edit']">{{$t('blind.edit')}}</el-button>
          <el-button type="danger" size="mini" @click="handlerBtn('del', scope.row)" v-permission="['POST /admin/blind/type/delete']">{{$t('blind.delete')}}</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />
    <!-- 弹窗 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogFormVisible" :width="dialogStatus === 'del' ? '30%' : '50%'" @closed="resetForm">
      <template v-if="dialogStatus === 'add' || dialogStatus === 'edit'">
        <el-form ref="formData" :model="formData" status-icon label-position="left" label-width="120px" :rules="rules">
          <el-form-item :label="$t('blind.type.name')" prop="name">
            <el-input v-model="formData.name"></el-input>
          </el-form-item>
          <el-form-item :label="$t('global.contractAddress')" prop="address">
            <el-input v-model="formData.address"></el-input>
            <div class="m-top-10">
              <el-button size="small" v-if="!connected" type="primary" @click="connect">{{ $t("navbar.connectWallet") }}</el-button>
              <el-button size="small" v-else :loading="deployLoading" type="primary" @click="deploy">{{ $t("global.deploy") }}</el-button>
            </div>

          </el-form-item>
          <div class="error-tip text-center" v-if="formError">
            {{ formError}}
          </div>
        </el-form>
      </template>
      <template v-if="dialogStatus === 'del'">
        <span>{{$t('blind.deleteTip')}}</span>
      </template>
      <!-- 底部按钮 -->
      <span slot="footer" class="dialog-footer">
        <el-button @click="handlerCancel">{{$t('blind.cancel')}}</el-button>
        <el-button :loading="apiLoading" type="primary" @click.native.prevent="handlerConfirm">{{$t('blind.confirm')}}</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
  import Pagination from "@/components/Pagination";
  import deploy from "@/utils/sdk/deploy.js";
  import { mapActions } from 'vuex';
  export default {
    name: "supplier",
    components: { Pagination },
    data () {
      return {
        apiLoading: false,
        listLoading: true,
        list: null,
        total: 0,
        address: null,
        listQuery: {
          page: 1,
          limit: 10,
          name: undefined
        },
        formData: {
          name: "",
          address: "",
        },
        formError: "",
        deployLoading: false,
        rules: {
          name: [
            { required: true, message: this.$t('blind.type.emptyName'), trigger: "blur" },
          ],
          address: [
            { required: true, message: this.$t('blind.type.emptyContractAddress'), trigger: "blur" },
          ],
        },
        dialogFormVisible: false,
        dialogStatus: "",
        delId: "",
      };
    },
    created () {
      this.getList();
    },
    computed: {
      connected () {
        var connected = this.$store.state.network.connected;
        return connected;
      },
      configContract(){
        var config = this.$store.state.app.config;
        console.log("config", config);
        return config.configContract;
      },
      dialogTitle () {
        let dialogTitle = "";
        switch (this.dialogStatus) {
          case "add":
            dialogTitle = this.$t('blind.add');
            break;
          case "edit":
            dialogTitle = this.$t('blind.edit');
            break;
          case "del":
            dialogTitle = this.$t('blind.delete');
            break;
          default:
            break;
        }
        return dialogTitle;
      },
    },
    methods: {
      ...mapActions(['blindTypes']),
      getList () {
        this.listLoading = true;
        this.$api("blind.type.list", this.listQuery)
          .then((res) => {
            this.list = res.data.list;
            this.total = res.data.total;
            this.listLoading = false;
          })
          .catch(() => {
            this.list = [];
            this.total = 0;
            this.listLoading = false;
          });
      },
      resetForm () {
        this.formData = {
          name: "",
          address: "",
        };
        this.formError = "";
        this.deployLoading = false;
      },
      connect () {
        this.$store.dispatch("connect");
      },
      async deploy () {
        if (!this.formData.name) {
          this.formError = this.$t('blind.type.emptyName');
          return;
        }
        console.log("this.configContract", this.configContract);
        if(!this.configContract.copyErc721Address){
          this.formError = this.$t('blind.type.emptyCopyERC721');
          return;
        }
        this.deployLoading = true;
        this.formError = "";
        var params = [
          this.formData.name,
          this.formData.name,
          this.configContract.copyErc721Address,
          "ipfs:/",
          "ipfs:/"
        ];
        const result = await deploy("Box721", params);
        this.deployLoading = false;
        if (result.error) {
          this.formError = result.error;
          return;
        }
        this.formData = Object.assign({}, this.formData, {
          address: result.address,
        });
      },
      handlerFilter () {
        this.listQuery.page = 1;
        this.getList();
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
          this.$nextTick(function () {
            this.$refs["formData"] ? this.$refs["formData"].clearValidate() : "";
          });
        }
        if (btnType === "del") {
          this.address = row.address;
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
            this.$api("blind.type.add", this.formData)
              .then((res) => {
                if (this.$tool.checkResponse(res)) {
                  this.successCallback()
                  this.getList();
                  this.blindTypes()
                } else {
                  this.failCallback()
                }
              })
              .catch((err) => {
                this.failCallback(err)
              });
          }
        });
      },
      editData () {
        this.$refs["formData"].validate((valid) => {
          if (valid && !this.apiLoading) {
            this.apiLoading = true;
            this.$api("blind.type.edit", this.formData)
              .then((res) => {
                if (this.$tool.checkResponse(res)) {
                  this.successCallback()
                  this.getList();
                  this.blindTypes()
                } else {
                  this.failCallback()
                }
              })
              .catch((err) => {
                this.failCallback(err)
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
        this.$api("blind.type.delete", { address: this.address })
          .then((res) => {
            if (this.$tool.checkResponse(res)) {
              this.successCallback()
              let { page, limit } = this.listQuery;
              if (page > 1 && this.total - 1 - (page - 1) * limit <= 0) {
                this.listQuery.page -= 1;
              }
              this.total -= 1;
              this.getList();
              this.blindTypes()
            } else {
              this.failCallback()
            }
          })
          .catch((err) => {
            this.failCallback(err)
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
          this.handlerCancel()
        }
      },
    },
  };
</script>

<style lang="scss" scoped>
  .el-form-item__label {
    width: 200px !important;
  }
</style>
