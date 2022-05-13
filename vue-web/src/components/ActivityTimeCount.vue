<template>
  <div class="activity-time-count">
    {{countTime}} {{$t("time."+status)}}
  </div>
</template>
<script>
export default {
  name: "ActivityTimeCount",
  props:{
    startTime:{
      type:Number,
      default:null
    },
    endTime:{
      type:Number,
      default:null
    },
  },
  data() {
    return {
      countTime:'00:00:00',
      status: "begin", // begin, end, over
      timer: null,
      destory: false,
    }
  },
  created(){
    this.showTime();
  },
  beforeUnmount(){
    this.destory = true;
    clearTimeout(this.timer);
  },
  beforeDestroy(){
    this.destory = true;
    clearTimeout(this.timer);
  },
  methods:{
    setStatus(startTime, endTime){
      let now = new Date();
      now = parseInt(now.getTime()/1000);
      let seconds = 0;
      if(startTime > now){
        this.status = 'begin';
        seconds = startTime - now;
      }else if(startTime < now && endTime > now){
        this.status = "end";
        seconds = endTime - now;
      }else{
        this.status = "over";
      }
      this.countTime = this.$tools.formatSecond(seconds);
    },
    showTime(){
      if(this.timer){
        clearTimeout(this.timer);
      }
      if(this.destory) return;
      this.setStatus(this.startTime, this.endTime);

      let that = this;
      this.timer = setTimeout(() => {
        that.showTime();
      }, 1000);
    },
  }
}
</script>
<style lang="scss" scoped>
.activity-time-count{
}
</style>
