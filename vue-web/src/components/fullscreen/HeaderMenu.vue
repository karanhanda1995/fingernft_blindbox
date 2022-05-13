<template>
<div>
  <div class="header-section header-padding">
    <div class="head-logo m-right-20">
      <img fit="contain" class="logo-image" :src="require('@/assets/img/logo.png')"/>
    </div>
    <div class="right">
      <div class="close-link" @click="close">
        <img class="close-img" src="@/assets/create-img/pop_shut.png ">
      </div>
    </div>
  </div>
  <div class="menu-section">
    <div class="navs">
      <div class="nav-item"
        :class="$route.name == 'home' ? 'active' : ''"
        @click="go('/')"
      > {{ $t('navigation.explore') }} </div>
 
      <div class="nav-item"
        :class="$route.name == 'blindbox' ? 'active' : ''"
        @click="go('/blindbox')"
      > {{ $t('navigation.blindBox') }} </div>

      <div class="nav-item"
        :class="$route.name == 'Items' ? 'active' : ''"
        @click="!connected ? go('/connect') : go('/items')"
      > {{ $t('navigation.myItems') }} </div>

      <div class="nav-item"
        v-if="connected"
        :class="$route.name == 'Message' ? 'active' : ''"
        @click="go('/message?tab=unread')"
      > {{ $t('navigation.news') }} </div>
      <div class="nav-item">
        <el-collapse v-model="activeNames">
          <el-collapse-item :title="$t('footer.language')" name="1">
            <div class="nav-sub-item" @click="languageSelect('English')" :class="language == 'English'? 'active': ''">English</div>
            <div class="nav-sub-item" @click="languageSelect('中文')" :class="language == '中文'? 'active': ''">中文</div>
          </el-collapse-item>
        </el-collapse>
      </div>
    </div>

    <div class="footer-section">
      <template v-if="!connected">
        <el-button @click="go('/create')" class="create-button" round>
          {{ $t("navigation.create") }}
        </el-button>
        <el-button @click="go('/connect')" class="connect-button flex1" type="primary" plain round>
          {{ $t("navigation.connectWallet") }}
        </el-button>
 
      </template>
      <template v-else>

        <el-button @click="go('/create')" class="create-button" round>
          {{ $t("navigation.create") }}
        </el-button>

      </template>

    </div>
  </div>
</div>
</template>
<script>
export default {
  name: "HeaderMenu",
  data: function(){
    return {
      networks: this.$store.state.networks,
      activeNames: false
    };
  },
  computed:{
    connected(){
      return this.$store.state.connected;
    },
    language() {
      return this.$store.state.language;
    }
  },
  created() {
    if (localStorage.getItem("locale") == "zh") {
      this.$store.state.language = "中文";
    } else {
      this.$store.state.language = "English";
    }
  },
  methods:{
    close(){
      this.$store.commit("FULLSCREEN", { show: false, component: "" });
    },
    go(path){
      this.close();
      this.$router.push(path);
    },
    languageSelect(parameter) {
      this.$store.state.language = parameter;
      if (parameter == "English") {
        localStorage.setItem("locale", "en");
        this.$i18n.locale = localStorage.getItem("locale");
      } else if (parameter == "中文") {
        localStorage.setItem("locale", "zh");
        this.$i18n.locale = localStorage.getItem("locale");
      }
    },
  },
}
</script>

<style lang="scss" scoped>

.header-section{
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  line-height: 40px;
  height: 60px;
  justify-content: space-between;
  .head-logo{
    display: flex;
    align-items: center;
  }

  .close-link{
    height: 32px;
    display: flex;
    align-items: center;
    justify-content: center;
    line-height: 40px;
    cursor: pointer;
    .close-img{
      height: 100%;
    }
  }
 
}
.menu-section{
  padding: 20px;
  .nav-item{
    padding: 5px 0;
    margin-bottom: 10px;
    font-size: 15px;
    cursor: pointer;
    &.active{
      color: $primaryColor;
    }
  }
  .nav-sub-item{
    margin-top: 10px;
    margin-left: 20px;
    padding: 5px 0;
    &.active{
      color: $primaryColor;
    }
  }
}

.footer-section{
  margin-top: 40px;
  padding-top: 40px;
  border-top: $border;
  display: flex;
  flex-direction: column;
  .el-button{
    margin: 0;
    margin-bottom: 10px;
  }
  .create-button{
    color: #ffffff;
    border: none;
    background: $gradientColor;
    flex: 1;
    &:hover,
    &:focus,
    &:active {
      background: $gradientColor !important;
      border: none !important;
      color: #fff !important;
    }
  }
  .connect-button{
    color: #ffffff;
    border: none;
    background: $gradientColor1;
    flex: 1;
    &:hover,
    &:focus,
    &:active {
      background: $gradientColor1 !important;
      border: none !important;
      color: #fff !important;
    }
 
  }
}

</style>
