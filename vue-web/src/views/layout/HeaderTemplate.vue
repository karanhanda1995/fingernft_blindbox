<template>
  <div class="home-header" :class="!blindboxPage ? '' : 'head-blindbox'">
  
    <div class="header-container header-padding" :style="style">
      <router-link to="/" class="head-logo header-margin-r">
        <img fit="contain" class="logo-image"
          :src="require('@/assets/img/logo.png')"
        />
      </router-link>

      
<div class="head-navs">

        <router-link
          class="nav-link header-margin-r"
          to="/">
          Home</router-link>

        <router-link class="nav-link header-margin-r"
          :class="$route.name == 'Items' ? 'active' : ''"
          :to="!connected ? '/connect' : '/items'"
        >
         Collections
        </router-link>

        <router-link v-if="connected"
          class="nav-link header-margin-r" to="/message?tab=unread"
          :class="$route.name == 'Message' ? 'active' : ''"
        >
          {{$t('navigation.news')}}
          <span v-if="message.unread" class="red-tip"></span>
        </router-link>

        <router-link
          class="nav-link header-margin-r"
          :class="$route.name =='blindbox' ? 'active':''"
          to="/blindbox">
          {{$t('navigation.blindBox')}}</router-link>

          <router-link class="nav-link header-margin-r"
          :class="$route.name == 'Items' ? 'active' : ''"
          :to="!connected ? '/connect' : '/items'"
        >
          Profile
        </router-link>

        <router-link class="nav-link header-margin-r" to="/create" >
          
           Create Item
         
        </router-link>

        <!-- <el-popover v-model:visible="languagePopover" placement="bottom" trigger="click" :show-arrow="false"
          popper-class="nav-popover" :offset="-8">
          <template #reference>
            <div class="nav-link header-margin-r">
               {{$t('footer.language')}} -->{{language}}
              <!-- <span class="iconfont icon-arrow-down bfont"></span>
            </div>
          </template>
          <div class="popover-menu-item" @click="languageSelect('English')" :class="language =='English' ? 'active':''">English</div>
          <div class="popover-menu-item" @click="languageSelect('中文')" :class="language =='中文' ? 'active':''">中文</div>
        </el-popover> --> -->
      </div>

<div class="header-search header-margin-r">
        <el-input
          class="search-input"
          v-model="keyword"
          @keyup.enter="searchClick"
          placeholder="Search"
        >
          <template #prefix>
            <span class="iconfont icon-search"></span>
          </template>
        </el-input>
      </div>

    

      <div class="head-menus">

        <div class="search-link round-link header-margin-l" @click="showFullScreen('HeaderSearch')">
          <span class="iconfont icon-search"></span>
        </div>


        <router-link to="/connect" class="head-connect align-center header-margin-l" v-if="!connected" style="background-color:#252b71">
          <span class="iconfont icon-wallet" style="font-size: 29px !important;font-weight: 100 !important;"></span>{{ $t("navigation.connectWallet") }}
        </router-link>

      

        <div class="user-link header-margin-l" v-else>
          <div class="user-popover">
            <profile-popover></profile-popover>
          </div>
          <div class="user-dialog">
            <profile-dialog></profile-dialog>
          </div>
        </div>

        <div class="menu-link round-link header-margin-l" @click="showFullScreen('HeaderMenu')">
          <span class="iconfont icon-category"></span>
        </div>

      </div>
    </div>

    <follow-popup
      :show="showFollowing"
      ftype="following"
      @close="showFollowing = false"
      v-if="connected"
      :address="user.coinbase"
    >
    </follow-popup>

  </div>
 
</template>
<script>
import FollowPopup from "@/components/FollowPopup";

export default {
  name: "HeaderTemplate",
  components: {
    FollowPopup,
  },
  data: function () {
    return {
      style: {
        backgroundColor: "",
      },
      keyword: this.$route.query.keyword,
      showFollowing: false,
      networks: this.$store.state.networks,
      searchShow: false,
      menuShow: false,
      languagePopover: false
    };
  },
  computed: {
    notice() {
      return this.$store.state.notice;
    },
    connected() {
      return this.$store.state.connected;
    },
    user: function () {
      var user = this.$store.state.user;
      return user;
    },
    message(){
      return this.$store.state.message;
    },
    blindboxPage(){
      var name = this.$route.name;
      if(name == "blindboxDetail") return true;
      return false;
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
    window.addEventListener("scroll", this.handleScroll);
  },
  methods: {
    handleScroll(){
      let scrollTop = window.pageYOffset ||
        document.documentElement.scrollTop ||
        document.body.scrollTop;
      if (scrollTop) {
        if(scrollTop < 112){
          this.style.backgroundColor = `rgba(255, 255, 255,${scrollTop / (scrollTop + 112)})`;
        }else{
          this.style.backgroundColor = "#fff";
        }
      } else if (scrollTop == 0) {
        this.style.backgroundColor = "transparent";
      }
    },
    async searchClick() {
      this.$router.push({ name: "Search", query: { keyword: this.keyword} });
    },
    goProfile(){
      if(!this.$tools.needLogin()) return;
      this.$router.push("/profile");
    },
    goItems() {
      this.$router.push({ name: "Items" });
    },
    logout() {
      this.$web3.disconnect();
    },
    showFullScreen(component){
      this.$store.commit("FULLSCREEN", { show: true, component: component });
    },
    closeFullScreen(){
      this.$store.commit("FULLSCREEN", { show: false, component: "" });
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
      this.languagePopover = false;
    },
  },
};
</script>

<style lang="scss">

//  .header__search {
//     background: #e2eef1;
//     height: 3rem;
//     width: 20rem;
//     border-radius: 10px;
//     position: relative;
//     -webkit-transition: all 0.2s ease-in-out;
//     transition: all 0.2s ease-in-out;
//     border: solid 1px transparent;
// }

.header-search{
  .search-input {
    border-radius: 10px;
    overflow: hidden;
    background: #e2eef1;
    height: 50px;
    color: #fff;
    display: flex;
    align-items: center;
    font-size: 16px;
    width: 6rem !important;
    .el-input__inner {
      font-size:16px;
      padding-left: 45px !important;
      height: 100%;
      border: none;
      background: #e2eef1;
    }
    .el-input__prefix {
      line-height: 40px;
      padding-top: 4px;
      padding-left: 5px;
    }
    .iconfont {
      font-size: 24px;
      font-weight: 500;
      color: #000;
      background: #fff;
      border-radius: 50%;
    }
  }

  .nav-popover {
    width: fit-content !important;
    height: fit-content !important;
    min-width: unset !important;
    padding-bottom: 0px !important;
    z-index: 15000 !important;
    :hover {
      color: grey;
    }
  }

}


</style>

<style lang="scss" scoped>

.home-header{
  position: fixed;
  right: 0;
  left: 0;
  top: 0;
  z-index: 100;
  background: #fff;
  border-bottom: solid 1px #e2eef1;

  &.head-blindbox{
    .header-container{
      background: #fff !important;
    }
  }
  .header-container {
    display: flex;
    flex-wrap: wrap;
    align-items: center;
    font-size: 15px;
    line-height: 40px;
    height: 112px;
    color: #fff;
  }
}

.head-navs{
  display: flex;
  align-items: center;
  flex: 1;
  justify-content: flex-end;
}

.head-menus{
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  .round-link{
    color: #333;
    font-size: 28px;
    cursor: pointer;
  }
  .create-link {
    font-size: 16px;
  }
}
.link-btn {
  background: #0D3079;
  border-radius: 10px;
  color: #fff;
  border: 0;
}
.header-search {
  display: flex;
  flex: 0.5;
  align-items: center;
  color: #333;;
  font-size: 15px;
  font-weight: 900;
  width: 430px;
}
.nav-link {
  position: relative;
  white-space: nowrap;
  color: #000;
  font-size: 15px;
  cursor: pointer;
  &.active {
    border-bottom: 2px solid #333;
    line-height: 23px;
  }
}

.search-link{
  display: none;
}
.menu-link{
  display: none;
}

.user-popover{
  display: flex;
}
.user-dialog{
  display: none;
}

.user-link {
  cursor: pointer;
  display: flex;
}

.head-logo{
  display: flex;
  align-items: center;
}

.head-connect {
  display: flex;
  padding: 0 15px;
  color: #fff;
  background: #0D3079;
  border-radius: 10px;
  font-size: 14px;
}

.red-tip {
  position: absolute;
  cursor: pointer;
  top: 8px;
  height: 8px;
  width: 8px;
  right: -5px;
  background: #dd3b3b;
  border-radius: 5px;
  font-size: 12px;
  font-weight: bold;
  color: #ffffff;
  padding: 1px 3px;
}
@media only screen and (max-width: $media_l1){
  .header-search {
    display: none;
  }
  .search-link{
    display: inline-block;
  }
}


@media only screen and (max-width: $media_l2){
  .logo-image {
    height: 36px;
  }
}

@media only screen and (max-width: $media_l3){
  .header-container{
    justify-content: space-between;
    .logo-image {
      height: 28px;
    }
    .head-navs{
      display: none;
    }
    .head-connect{
      display: none;
    }
    .create-link{
      display: none;
    }
    .menu-link{
      display: inline-block;
    }
    .user-popover{
      display: none;
    }
    .user-dialog{
      display: inline-block;
    }
  }
}

.popover-menu-item{
  display: flex;
  flex: 1;
  cursor: pointer;
  padding: 5px 0;
  font-size: 14px;
  color: #666;
  &.active {
    color: $primaryColor;
  }
}

.btn-grad {
    color: #ffffff;
    border: none !important;
    -webkit-transition: all 0.4s ease-in-out !important;
    transition: all 0.4s ease-in-out !important;
    background: radial-gradient(100% 100% at 53.13% 0%, #31e7fa 0%, #4477ff 52.6%, #db74ff 100%);
}
</style>
