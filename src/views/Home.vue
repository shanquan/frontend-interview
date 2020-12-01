<!--
@author: wang.weili
@since : 2020/11/12
-->
<template>
  <!-- web -->
  <div class="home" v-if="pageType==0">
    <h3>Welcome to MES3.0</h3>
  </div>
  <!-- mobile -->
  <el-container class="mobile-menu" v-else>
    <!-- level 1 -->
    <div v-if="levelTop" class="padding">
      <el-card shadow="hover" v-for="(item,index) in mobileList" :key="index">
        <div class="list-item" @click="goLevel(index,item)"><i :class="item.icon"></i><span>{{item.title}}</span></div>
      </el-card>
    </div>
    <!-- level 2 -->
    <div v-else>
      <router-link class="el-collapse-item__header" v-for="(item,index) in subList" :key="index" :to="item.index"><span>{{item.title}}</span><i class="el-collapse-item__arrow el-icon-arrow-right"></i></router-link>
      <div class="center mt10"><el-button round @click="goLevel1">返回</el-button></div>
    </div>
  </el-container>
</template>
<script>
export default {
  name: 'home',
  created(){
    let query = this.$route.query;
    if(query.index>=0&&query.index<this.mobileList.length){
      this.levelTop = this.mobileList[query.index].subs==undefined?true:false;
      this.subList = this.mobileList[query.index].subs;
    }
    if(this.$root.isMobile()){
      this.$store.commit('setPageType',1)
    }else{
      this.$store.commit('setPageType',0)
    }
  },
  computed:{
    pageType(){
      return this.$store.state.pageType
    }
  },
  data(){
    return{
      mobileList:this.$root.mobileList,
      levelTop: true,
      subList:[]
    }
  },
  methods:{
    goLevel1(){
      this.levelTop=true;
      if(Window.messageBox)
      Window.messageBox.close();
    },
    goLevel(idx,item){
      this.levelTop=false;
      if(item.subs){
        this.subList = this.mobileList[idx].subs;
      }else{
        this.$router.push({ path: item.index })
      }
    }
  },
  beforeRouteLeave (to,from,next) {
    if(this.pageType!=0&&this.levelTop==false&&to.name!="login"){
      this.$store.commit('setPageType',2)
      this.$root.$children[0].title=this.$root.getMobileTitle(to.path);
    }
    next();
  }
}
</script>
<style>
.home{height: 100%;
    display: flex;
    justify-content: center;
    align-items: center;}
.mobile-menu .el-container{
  flex-direction:column;
  margin-top:20px
}
.mobile-menu .el-card{
  margin-top:10px
}
.mobile-menu .el-card__body{padding:0px}
.list-item{line-height: 45px;
    padding-left: 48px;
    font-size:18px;
    position: relative;}
.list-item i:first-child{font-size:32px;position:absolute;top:7px;left:6px;color:#409EFF}
.mobile-menu .el-collapse-item__header{padding-left:10px}
.mobile-menu .el-collapse-item__header span{font-size:16px}
.mobile-menu div{width:100%}
</style>