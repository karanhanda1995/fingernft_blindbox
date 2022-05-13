<template>
  <div class="scroll-button">
    <span class="iconfont icon-arrow-left icon999" v-if="scrollData.page == 1"></span>
    <span class="iconfont icon-arrow-left" v-else @click="clickScroll(-1)"></span>
    <span class="iconfont icon-arrow-right icon999" v-if="scrollData.page == scrollData.pages"></span>
    <span class="iconfont icon-arrow-right" v-else @click="clickScroll(1)"></span>
  </div>
</template>

<script>
export default {
  name: "ScrollButton",
  props: {
    scrollData: {
      type: Object,
      default: {},
    }
  },
  data: function () {
    return {};
  },
  computed: {
  },
  methods: {
    clickScroll(page) {
      if(page == 1){
        this.$emit('scrollRight',this.scrollData.type,page)
      }else {
        this.$emit('scrollLeft',this.scrollData.type,page)
      }
    }
  },
};
</script>

<style lang="scss" scoped>
.scroll-button {
  font-size: 36px;
  font-weight: bold;
}
.icon999 {
  color: #999;
}
</style>
