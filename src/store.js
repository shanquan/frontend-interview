/**
 * @author: wang.weili
 * @since : 2020/11/12
 */
import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({

  state: {
    menuCollapse: true,
    pageType: 0, // 0: web menu 1: mobile menu 2: mobile page
    user: {}
  },
  mutations: {
    changeMenu(state) {
      state.menuCollapse = !state.menuCollapse;
    },
    setUser(state,user){
      Object.assign(state.user,user);
    },
    setPageType(state,type){
      state.pageType = type;
    }
  }
})
