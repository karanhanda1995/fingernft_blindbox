<template>
  <div class="app-fullscreen" :class="!fullscreen.show ? '': 'show'">
    <component :is="currentView"></component>
  </div>
</template>
<script>
import HeaderMenu from './HeaderMenu';
import HeaderSearch from './HeaderSearch';
export default {
  name: "FullScreen",
  components: {
    HeaderMenu,
    HeaderSearch,
  },
  computed: {
    fullscreen(){
      return this.$store.state.fullscreen;
    },
    currentView(){
      return this.fullscreen.component;
    },
  },
}
</script>

<style lang="scss" scoped>
.app-fullscreen{
  position: fixed;
  left: 0;
  right: 0;
  top: 0;
  bottom: 0;
  display: none;
  background: #fff;
  flex-direction: column;
  z-index: 200;
  overflow-y: scroll;
  &.show{
    display: flex;
  }
}
</style>
