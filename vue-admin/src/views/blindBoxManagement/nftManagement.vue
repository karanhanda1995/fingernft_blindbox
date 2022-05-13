<template>
	<div class="app-container">
		<div class="filter-container">
			<el-input v-model="listQuery.name" clearable class="filter-item w-200 mr-10" :placeholder="$t('blind.placeholder')" />
			<el-button class="filter-item" type="primary" icon="el-icon-search" @click="handlerFilter">{{ $t("blind.select") }}</el-button>
			<el-button class="filter-item" type="primary" icon="el-icon-edit" v-permission="['POST /admin/blind/nft/add']" @click.native.prevent="handlerBtn('add')">{{ $t("blind.add") }}</el-button>
		</div>
		<!-- 表格 -->
		<el-table v-loading="listLoading" :data="list" :element-loading-text="$t('util.loadingText')" border>
			<el-table-column type="expand">
				<template slot-scope="props">
					<el-form label-position="left" inline class="table-expand">
						<el-form-item :label="$t('blind.nft.blindType')">
							<span>{{ blindTypeName(props.row.address) }}</span>
						</el-form-item>
						<el-form-item :label="$t('blind.nft.property')">
							<span>{{ props.row.propertys }}</span>
						</el-form-item>
						<el-form-item :label="$t('blind.nft.brief')">
							<span>{{ props.row.description }}</span>
						</el-form-item>
						<el-form-item :label="$t('blind.nft.address')">
							<span>{{ props.row.address }}</span>
						</el-form-item>
						<el-form-item :label="$t('blind.nft.tokenId')">
							<span>{{ props.row.tokenId }}</span>
						</el-form-item>
					</el-form>
				</template>
			</el-table-column>
			<el-table-column align="center" label="ID" prop="id" sortable></el-table-column>
			<el-table-column align="center" :label="$t('blind.nft.name')" prop="name"></el-table-column>
			<el-table-column align="center" :label="$t('blind.nft.picture')" prop="metadataContent" width="200px">
				<template slot-scope="scope" v-if="scope.row.metadataContent.image">
					<media :isPreview="true" :url="scope.row.metadataContent.image" type="image"></media>
				</template>
			</el-table-column>
			<el-table-column align="center" :label="$t('blind.nft.audioORvideo')" prop="metadataContent">
				<template slot-scope="scope" v-if="scope.row.metadataContent.animation_url">
					<media :url="scope.row.metadataContent.animation_url" :isPreview="true"></media>
				</template>
			</el-table-column>
			<el-table-column align="center" :label="$t('blind.operation')" width="300" class-name="small-padding fixed-width">
				<template slot-scope="scope">
					<el-button type="danger" size="mini" @click.native.prevent="handlerBtn('del', scope.row)" v-permission="['POST /admin/blind/nft/deleted']">{{ $t("blind.delete") }}</el-button>
				</template>
			</el-table-column>
		</el-table>

		<pagination v-show="total > 0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />

		<el-dialog :title="dialogTitle" :visible.sync="dialogFormVisible" :width="dialogStatus === 'del' ? '30%' : '50%'">
			<template v-if="dialogStatus === 'add'">
				<el-form ref="formData" :model="formData" status-icon label-position="left" label-width="120px" :rules="rules">
					<el-form-item :label="$t('blind.nft.blindType')" prop="address">
						<el-select v-model="formData.address" :placeholder="$t('blind.nft.pleaseSelect')">
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
						<!-- <el-upload ref="upload" action="" :auto-upload="false" :show-file-list="false" :file-list="fileListImage" :limit="1" class="avatar-uploader"
              accept="image/jpg, image/jpeg, image/png, image/gif" :on-change="(file) => { uploadChange(file, 'uploadImage'); }">
              <template v-if="formData.imgUrl">
                <media :url="formData.imgUrl" type="image"></media>
              </template>
              <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            </el-upload> -->
						<upload-unit v-if="dialogFormVisible" :refName="'blindboxNftImgRef'" :limitNum="1" :imageData="formData.imgUrl" :uploadStatus="dialogStatus == 'add'" @updateData="updateNftImg"></upload-unit>
					</el-form-item>
					<el-form-item :label="$t('blind.nft.audioORvideo')" prop="animUrl">
						<!-- <el-upload ref="upload" action="" :auto-upload="false" :show-file-list="false" :file-list="fileListVideo" :limit="1" class="avatar-uploader"
              accept="audio/mp3, audio/wav, audio/oga, video/webm, video/mp4, video/m4v, video/ogg, video/ogv, video/mov" :on-change=" (file) => { uploadChange(file, 'uploadVideo'); }">
              <template v-if="formData.animUrl">
                <media :url="formData.animUrl" :type="formData.videoType"></media>
              </template>
              <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            </el-upload> -->
						<upload-unit v-if="dialogFormVisible" :refName="'blindboxNftGifRef'" :limitNum="1" :imageData="formData.animUrl" :uploadStatus="dialogStatus == 'add'" @updateData="updateNftGif"></upload-unit>
					</el-form-item>
					<el-form-item :label="$t('blind.nft.property')">
						<div class="property-group" v-for="(property, i) in formData.properties" :key="i">
							<div class="property left">
								<el-input v-model="property.key" :placeholder="$t('blind.property.key')" @input="propertyInput">
								</el-input>
							</div>
							<div class="property right">
								<el-input v-model="property.value" :placeholder="$t('blind.property.value')" @input="propertyInput">
								</el-input>
							</div>
						</div>
					</el-form-item>
				</el-form>
			</template>
			<!-- 删除 -->
			<template v-if="dialogStatus === 'del'">
				<span>{{ $t("blind.deleteTip") }}</span>
			</template>
			<!-- 底部按钮 -->
			<span slot="footer" class="dialog-footer">
				<el-button @click.native.prevent="handlerCancel">{{
          $t("blind.cancel")
        }}</el-button>
				<el-button :loading="apiLoading" type="primary" @click.native.prevent="handlerConfirm">{{ $t("blind.confirm") }}</el-button>
			</span>
		</el-dialog>
	</div>
</template>

<script>
	import Pagination from "@/components/Pagination";
	import Media from "@/components/Media";
	import UploadUnit from "@/components/Upload.vue"
	export default {
		name: "nft",
		components: { Pagination, Media, UploadUnit },
		data () {
			return {
				listLoading: true,
				apiLoading: false,
				list: [],
				total: 0,
				listQuery: {
					page: 1,
					limit: 10,
					name: "",
				},
				// 弹出框
				dialogFormVisible: false,
				dialogStatus: "",
				delId: "",
				//   表单
				formData: {
					address: "",
					name: "",
					description: "",
					storageId: "",
					imgUrl: "",
					animStorageId: "",
					animUrl: "",
					videoType: "",
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
				// 上传
				fileImage: "",
				fileVideo: "",
				fileListImage: [],
				fileListVideo: [],
			};
		},
		computed: {
			blindTypes () {
				return this.$store.state.user.blindTypes;
			},
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
					case "sync":
						dialogTitle = this.$t("blind.sync");
						break;
					default:
						break;
				}
				return dialogTitle;
			},
			blindTypeName () {
				return function (address) {
					for (let i = 0, len = this.blindTypes.length;i < len;i++) {
						if (this.blindTypes[i].address === address)
							return this.blindTypes[i].name;
					}
				};
			},
		},
		created () {
			this.getList();
		},
		methods: {
			getList () {
				this.listLoading = true;
				this.$api("blind.nft.list", this.listQuery)
					.then((res) => {
						this.formatData(res.data.list);
						this.list = res.data.list;
						this.total = res.data.total;
						this.listLoading = false;
					})
					.catch((err) => {
						this.list = [];
						this.total = 0;
						this.listLoading = false;
					});
			},
			updateNftImg (file) {
				this.formData.imgUrl = file
				this.fileImage = file
			},
			updateNftGif (file) {
				this.formData.animUrl = file
				this.fileVideo = file
			},
			formatData (data) {
				for (let i = 0, len = data.length;i < len;i++) {
					if (data[i].metadataContent) {
						data[i].metadataContent = JSON.parse(data[i].metadataContent);
					} else {
						data[i].metadataContent = { image: "", animation_url: "" };
					}
				}
			},
			resetForm () {
				this.formData = {
					address: "",
					name: "",
					description: "",
					storageId: "",
					imgUrl: "",
					animStorageId: "",
					animUrl: "",
					videoType: "",
					properties: [{ key: "", value: "" }],
				};
				this.fileImage = "";
				this.fileVideo = "";
			},
			// handler--property
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
			// 图片改变
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
						_this.formData.videoType = file.type;
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
			// 表单数据增、删、改、查
			addData () {
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
								this.addForm(res);
							})
							.catch((err) => {
								this.failCallback();
							});
					}
				});
			},
			addForm (fileData) {
				let {
					address,
					name,
					description,
					storageId,
					imgUrl,
					animStorageId,
					animUrl,
					videoType,
					properties,
				} = this.formData;
				if (fileData) {
					storageId = this.fileVideo ? fileData[0][1].id : fileData[0].id;
					imgUrl = this.fileVideo
						? fileData[0][1].ipfshash
						: fileData[0].ipfshash;
					animStorageId = this.fileVideo ? fileData[0][0].id : "";
					animUrl = this.fileVideo ? fileData[0][0].ipfshash : "";
					videoType = this.fileVideo ? fileData[0][0].type : "";
				} else {
					storageId = imgUrl = animStorageId = animUrl = videoType = "";
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
					videoType,
					propertys,
				};
				this.$api("blind.nft.add", sendData)
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
			delData () {
				if (!this.apiLoading) {
					this.apiLoading = true;
				} else {
					return;
				}
				this.$api("blind.nft.deleted", { id: this.delId })
					.then((res) => {
						if (this.$tool.checkResponse(res)) {
							this.successCallback();
							let { page, limit } = this.listQuery;
							if (page > 1 && this.total - 1 - (page - 1) * limit <= 0) {
								this.listQuery.page -= 1;
							}
							this.total -= 1;
							this.getList();
						} else {
							this.failCallback();
						}
					})
					.catch((err) => {
						this.failCallback(err);
					});
			},
			handlerFilter () {
				this.listQuery.page = 1;
				this.getList();
			},
			/*
			 * 按钮执行弹窗
			 * #handlerBtn
			 */
			handlerBtn (btnType, row = {}) {
				switch (btnType) {
					case "add":
						this.resetForm();
						this.$nextTick(function () {
							this.$refs["formData"]
								? this.$refs["formData"].clearValidate()
								: "";
						});
						break;
					case "del":
						this.delId = row.id;
						break;
					default:
						break;
				}
				this.dialogFormVisible = true;
				this.dialogStatus = btnType;
			},
			// 确定按钮回调
			handlerConfirm () {
				switch (this.dialogStatus) {
					case "add":
						this.addData();
						break;
					case "edit":
						this.editData();
						break;
					case "del":
						this.delData();
						break;
					case "sync":
						this.syncData();
						break;
					default:
						break;
				}
			},
			// 取消按钮回调
			handlerCancel () {
				this.dialogFormVisible = false;
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
