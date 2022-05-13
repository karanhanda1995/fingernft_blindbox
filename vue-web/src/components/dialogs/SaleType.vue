<template>
  <el-dialog
    :model-value="visible"
    :show-close="false"
    :close-on-click-modal="false"
    custom-class="fg-dialog"
    top="20vh"
  >
  <template #title>
      <div class="fg-dialog-title">
        <div class="right" @click="$emit('close')">
          <img class="close-img" src="@/assets/create-img/pop_shut.png " />
        </div>
      </div>
    </template>
    <div class="fg-dialog-body">
      <el-button type="primary" @click="$emit('saleType', 0)">{{$t('dialog.sale')}}</el-button>
      <el-button type="primary" @click="$emit('saleType', 1)">{{$t('dialog.auction')}}</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  data() {
    return {
      visible: this.show,
    }
  },
  emits: ['saleType'],
  props: {
    show: {
      type: Boolean,
      default: false,
    },
  },
  watch: {
    show() {
      this.visible = this.show;
    },
  },
}
</script>

<style lang="scss" scoped>
  .fg-dialog-title {
    justify-content: right;
  }
  .el-button {
    width: 40%;
  }
  .el-button + .el-button {
    margin-left: 20%;
  }
</style>

