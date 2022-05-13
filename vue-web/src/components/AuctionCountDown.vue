<template>
  <div class="auction-count-down">
    {{countTime}}
  </div>
</template>
<script>
export default {
  name: "countDown",
  props:{
    startTime:{
      type: Number,
      default: 0,
    },
    endTime:{
      type: Number,
      default: 0,
    },
    expiredTime: {
      type: Number,
      default: 0,
    },
  },
  data() {
    return {
      countTime:'00:00:00',
    }
  },
  created(){
    this.showTime();
  },
  methods:{
    showTime(){
      let startTime = this.startTime ;
      let endTime = this.endTime;
      let expiredTime = this.expiredTime;
      let timestamp;
      let leftTime;
      let timer;
      let that=this;   
      let status;
      timer = setInterval(function () {
        timestamp = parseInt(new Date().getTime() / 1000);

        leftTime = startTime - timestamp;
        if(leftTime > 0){
          status = 'begin';
        } else if (endTime - timestamp > 0){
          leftTime = endTime - timestamp;
          status = 'end';
        } else if(expiredTime - timestamp > 0){
          status = 'expired'
          leftTime = expiredTime - timestamp;
        }else{
          clearInterval(timer);
          that.$emit("status", "");
          that.countTime = that.$t('time.expired');
          return;
        }
        that.$emit("status", status);
        that.countTime = that.$tools.formatSecond(leftTime) + " " + that.$t('time.' + status);
      }, 1000);
    },
  }
}

</script>

<style lang="scss" scoped>
</style>


