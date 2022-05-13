<template>
  <div class="switch-wrap" id="switchWrap">
    <div class="switch-inner">
      <span
        v-for="(item, index) in activeText"
        :key="index"
        @click="switchHandler(item.state, $event)"
        >{{ item.text }}</span
      >
    </div>
    <div class="switch-mask" id="switchMask"></div>
  </div>
</template>

<script>
export default {
  name: "AuctionSwitch",
  data() {
    return {
    };
  },
  props: {
    activeText: {
      type: Array,
      default: [],
    },
  },
  mounted() {
    this.init();
  },
  methods: {
    init() {
      if (this.activeText.length > 1) {
        document.getElementById("switchMask").style.left = -60 + "px";
      } else {
        document.getElementById("switchMask").style.left = 0;
      }
      document.querySelector(".switch-inner span").style.color = "#fff";
      this.$emit('switchState', this.activeText[0].state)
    },
    switchHandler(state, event) {
      if (this.activeText.length > 1) {
        if (state === 0) {
          document.getElementById("switchMask").style.left = -60 + "px";
        } else if (state === 1) {
          document.getElementById("switchMask").style.left = 60 + "px";
        }
        document.querySelectorAll(".switch-inner span").forEach((item) => {
          item.style.color = "#000";
        });
        event.target.style.color = "#fff";
        this.$emit('switchState', state)
      }
    },
  },
  watch: {
    activeText() {
      this.init();
    },
  },
};
</script>

<style lang="scss" scoped>
.switch-wrap {
  display: inline-block;
  position: relative;
  height: 50px;
  border: 1px solid #000;
  border-radius: 15px;
  cursor: pointer;
  overflow: hidden;
}
.switch-inner {
  position: relative;
  z-index: 11;
  & span {
    position: relative;
    display: inline-block;
    width: 60px;
    line-height: 48px;
    text-align: center;
    border-radius: 15px;
  }
}
.switch-mask {
  position: absolute;
  top: 0;
  bottom: 0;
  left: 0;
  width: 100%;
  background-color: brown;
  border-radius: 15px;
  transition: left 0.3s;
}
</style>