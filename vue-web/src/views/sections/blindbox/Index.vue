<template>
  <div class="main-wrapper">
    <div class="head-box flex justify-between">
      <div v-if="$i18n.locale == 'en'">
        <text class="blind-box t-primary">B</text>
        <text class="blind-box">lind Box</text>
      </div>
      <div v-else class="blind-box">{{ $t('blindBox.title') }}</div>
    </div>
    <div class="new-blindbox-list" v-infinite-scroll="loadList">
      <div class="blindbox-group">
        <blind-box-item v-for="(blindbox, i) in blindBoxList" :blindbox="blindbox" :key="i" :index="i"></blind-box-item>
        <nft-item-load :loadStatus="loadStatus"></nft-item-load>
      </div>
    </div>
  </div>
</template>

<script>
	import BlindBoxItem from "@/components/BlindBoxItem";

	export default {
		name: "Blind box",
		components: {
			BlindBoxItem,
		},
		data () {
			return {
				loadStatus: "",
				blindBoxList: [],
				query: {
					page: 1,
					limit: this.$store.state.pageLimit,
				},
			};
		},
		created () {
			this.init();
		},
		methods: {
			init () {
				this.getList();
			},
			loadList () {
				if (this.loadStatus == "over") return;
				this.getList();
			},
			getList () {
				if (this.loadStatus == "loading") return;
				this.loadStatus = "loading";
				var data = {
					page: this.query.page,
					limit: this.query.limit,
				};
				this.$api("blind.box.list", data).then((res) => {
					if (this.$tools.checkResponse(res)) {
						if (data.page == 1) this.blindBoxList = [];
						this.blindBoxList = this.blindBoxList.concat(res.data.list);
						if (res.data.list.length < data.limit) {
							this.loadStatus = "over";
						} else {
							this.query.page += 1;
							this.loadStatus = "";
						}
					} else {
						this.$tools.message(res.errmsg);
					}
				});
			},
		},
	};
</script>

<style lang="scss" scoped>
	.t-primary {
		color: $primaryColor !important;
	}

  .head-box {
    padding: 27px 0px;
    align-items: flex-end;
  }

  .blind-box {
    min-width: 100px;
    text-align: left;
    font-size: 20px;
    font-weight: bold;
    color: #000000;
  }

	.new-blindbox-list {
		margin: 0 -10px;
	}
	.blindbox-group {
		display: flex;
		flex-wrap: wrap;
		justify-content: flex-start;
	}
</style>

