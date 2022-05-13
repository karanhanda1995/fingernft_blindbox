
<script>
  export default {
    data () {
      return {
        blindBoxList: [],
      };
    },
    methods: {
      setBlindBoxOrders (orders, blindBoxList) {
        for (var i = 0;i < blindBoxList.length;i++) {
          let blindbox = blindBoxList[i];
          for (var l = 0;l < orders.length;l++) {
            let order = orders[l];
            if (order.blindBoxId == blindbox.id) {
              blindBoxList[i].order = order;
            }
          }
        }
      },
      getBlindBoxOrders (blindboxs) {
        if (!blindboxs.length) return;
        let ids = [];
        for (var i = 0;i < blindboxs.length;i++) {
          let blindbox = blindboxs[i];
          ids.push(blindbox.id);
        }
        ids = ids.join(",");
        let data = {
          blindBoxIds: ids,
        };
        this.$api("blind.order.listbyboxid", data).then((res) => {
          if (this.$tools.checkResponse(res)) {
            this.setBlindBoxOrders(res.data.list, this.blindBoxList);
          }
        });
      },
    },
  }
</script>
