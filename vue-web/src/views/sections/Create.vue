<template>
  <!-- 创建页面 -->
  <div class="main-wrapper">
    <div class="content-create">
      <div class="title">{{ $t("create.title") }}</div>
      <div class="introduce">{{ $t("create.introduce") }}</div>

      <div class="item-page">
        <div class="p-bottom-100">
            <div class="detail-sign" span="10">
              <div class="select">
                <div @click="to721" class="sel-row" to="/create/erc721">
                  <div class="inner">
                    <div class="cover">
                      <div class="pic-img">
                        <img src="@/assets/create-img/single.png" />
                      </div>
                    </div>
                    <div class="enter">
                      <div class="text">{{ $t("create.single") }}</div>
                      <img
                        class="pic-icon"
                        src="@/assets/img/enter.png"
                        alt=""
                      />
                    </div>
                  </div>
                </div>

                <div @click="to1155" class="sel-row" to="/create/erc1155">
                  <div class="inner">
                    <div class="cover">
                      <div class="pic-img">
                        <img src="@/assets/create-img/multiple.png" />
                      </div>
                    </div>
                    <div class="enter">
                      <div class="text">{{ $t("create.multiple") }}</div>
                      <img
                        class="pic-icon"
                        src="@/assets/img/enter.png"
                        alt=""
                      />
                    </div>
                  </div>
                </div>
              </div>

              <div class="introduce">
                <img src="@/assets/create-img/igw-f-sign.png" />
                <span>{{ $t("create.prompt") }}</span>
              </div>
          </div>
        </div>
      </div>

    </div>
  </div>
</template>

<script>
import { reactive, onMounted, toRefs, computed } from "vue";

export default {
  name: "Create",
  data: function () {
    return {};
  },
  created() {},
  methods: {
    goBack() {
      this.$router.go(-1);
    },
    to721() {
      if (!this.$tools.needLogin()) return;
      this.$router.push("/create/erc721");
    },
    to1155() {
      if (!this.$tools.needLogin()) return;
      this.$router.push("/create/erc1155");
    },
  },
};
</script>

<style lang="scss" scoped>
.content-create{
  max-width: 720px;
  width: 100%;
  margin: 0 auto;
}
.p-bottom-100{
  padding-bottom:100px;
}
.title {
  font-size: 32px;
  font-weight: bold;
  color: #333;
  margin: 20px 0 20px;
}

.introduce {
  font-size: 16px;
  font-weight: 400;
  color: #999;
}
.select {
  margin: 45px 0 0;
  display: flex;
  justify-content: space-between;
  flex-wrap: wrap;
}
.sel-row {
  display: block;
  width: 100%;
  border-radius: 10px;
  background: #fff;
  cursor: pointer;
  margin-bottom: 60px;
  box-shadow: 0px 12px 18px 2px rgba(130, 130, 130, 0.06);
  .inner {
    display: flex;
    cursor: pointer;
    width: 100%;
    padding: 35px 70px;
    align-items: center;
    justify-content: space-between;
  }
  .cover {
    display: flex;
    align-items: center;
    .pic-img {
      flex: 1;
      img {
        width: 120px;
      }
    }
  }
  .enter {
    display: flex;
    align-items: center;
    .text {
      font-size: 16px;
      justify-content: center;
      display: flex;
      align-items: center;
      font-weight: bold;
      margin-right: 50px;
    }
    .pic-icon {
      width: 30px;
    }
  }
}

@media screen and (max-width: $media_l5){
  .title {
    font-size: 28px;
  }

  .select {
    margin: 35px 0 0;
  }
  .sel-row {
    .inner {
      padding: 20px 50px;
    }
  }
}

@media screen and (max-width: $media_l6){
  .title {
    font-size: 22px;
  }

  .introduce {
    font-size: 14px;
  }
  .select {
    margin: 25px 0 0;
  }
  .sel-row {
    margin-bottom: 40px;
    .inner {
      padding: 20px 30px;
    }
    .enter{
      .text{
        font-size: 15px;
      }
      .pic-icon{
        width: 20px;
      }
    }
    .cover{
      .pic-img{
        img{
          width: 60%;
        }
      }
    }
  }

}


</style>

