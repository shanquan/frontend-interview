<!--
@author: wang.weili
@since : 2020/11/26
-->
<template>
  <el-container style="height:100vh;border: 1px solid #eee">
    <!-- web page header -->
    <el-header v-if="pageType==0||pageType==1" class="main">
      <router-link to="/"><img src="./assets/byd.png" alt="byd-logo" class="header-logo" /></router-link>
      <div class="title">
        MES 3.0
      </div>
      <div v-if="isAuthenticated" class="ad">
        <span>{{user.name}}</span>
        <el-button type="text" @click="logout()">退出</el-button>
      </div>
    </el-header>
    <!-- mobile page header -->
    <el-header height="45px" class="mb-header" v-else><el-page-header @back="goBack" :content="title"></el-page-header></el-header>
    <el-main>
    <el-container class="frame" :class="{ topper: pageType==2 }">
      <!-- web side-menu -->
      <el-aside v-if="isAuthenticated&&pageType==0" style="background-color: rgb(238, 241, 246);width:auto">
        <el-menu :router="true" :default-active="$route.path" :unique-opened="true" class="admin-menu" :collapse="isCollapse">
          <el-menu-item index="" @click="menuChange()">
            <i class="el-icon-menu"></i>
            <span slot="title">导航</span>
          </el-menu-item>
          <!-- <el-menu-item index="/">
            <i class="el-icon-star-on"></i>
            <span slot="title">首页</span>
          </el-menu-item> -->
          <el-submenu v-for="(item,index) in menuList" :index="item.index" :key="index">
            <template slot="title">
              <i :class="item.icon"></i>
              <span slot="title">{{item.title}}</span>
            </template>
            <div v-for="(subit,subidx) in item.subs" :key="subidx">
              <el-submenu v-if="subit.subs" :index="subit.index">
                <span slot="title">{{subit.title}}</span>
                <el-menu-item v-for="(sub2it,sub2idx) in subit.subs" :key="sub2idx" :index="sub2it.index">{{sub2it.title}}</el-menu-item>
              </el-submenu>
              <el-menu-item v-else :index="subit.index">{{subit.title}}</el-menu-item>
            </div>
          </el-submenu>
        </el-menu>
      </el-aside>
      <el-main>
        <el-breadcrumb v-if="pageType!=2&&breadcrumbs.length" class="padding" separator-class="el-icon-arrow-right">
          <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
          <el-breadcrumb-item v-for="(item,idx) in breadcrumbs" :to="item.path?{path: item.path}:''" :key="idx">{{item.title}}</el-breadcrumb-item>
        </el-breadcrumb>
        <div :class="pageType==0&&$route.path!='/'?'has-breadcrumb':'main-view'">
          <router-view></router-view>
        </div>
      </el-main>
    </el-container>
    </el-main>
    <el-footer v-if="pageType!=2&&$route.path=='/'" class="center">开发支持：ALPHA组装事业部 IT管理部&nbsp;&nbsp;V0.1</el-footer>
  </el-container>
</template>

<script>

export default {
  name: 'app',
  created(){
    try{
      if(this.$root.isMobile()){
        if(this.$route.name=="home"||this.$route.name=="login"){
          this.$store.commit('setPageType',1);
        }else{
          this.$store.commit('setPageType',2);
          this.title = this.$root.getMobileTitle(this.$route.path);
        }
      }
      let session = JSON.parse(localStorage.getItem('aSession'));
      if(session.token){
        this.$root.initSession(session.user,session.token);
      }
    }catch(e){}
  },
  watch:{
    $route(oVal){
      if(oVal.path=="/"||!this.isAuthenticated){
        this.breadcrumbs = [];
      }else{
        let item = this.$root.findMenuItem(oVal.path,this.menuList);
        // console.log(oVal.path,item)
        if(item){
          this.title = item.title;
          this.breadcrumbs = [item];
        }
      }
    }
  },
  computed:{
    isCollapse(){
      return this.$store.state.menuCollapse
    },
    pageType(){
      return this.$store.state.pageType
    }
  },
  data(){
    return{
      user:this.$store.state.user,
      isAuthenticated:Window.isAuthenticated,
      menuList: this.$root.menuList,
      breadcrumbs:[],
      title:""
    }
  },
  methods:{
    menuChange(){
      this.$store.commit('changeMenu');
    },
    logout(){
      this.isAuthenticated = false;
      this.$root.logout();
      this.$router.push('login')
    },
    goBack(){
      this.$store.commit('setPageType',1)
      let index = this.$root.getMobileIndex(this.$route.name);
      this.$router.push({'path':`/?index=${index}`});
    }
  }
}
</script>