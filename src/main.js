/**
 * @author: wang.weili
 * @since : 2020/11/12
 */
import Vue from 'vue'
import App from './App.vue'

import router from './router'
import store from './store'
import http from './api'
import menu from './menu'
// import './plugins/element.js'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import './assets/main.css';

Vue.config.productionTip = false
Vue.use(ElementUI);
Vue.prototype.$http = http;

Window.isAuthenticated = false;
Window.messageBox; // unique error messagebox

try {
    let session = JSON.parse(localStorage.getItem('aSession'));
    // 存在token, 自动登录
    if (session.token) {
        Window.isAuthenticated = true;
    }
} catch (e) {}

router.beforeEach((to, from, next) => {
    if (to.name !== 'login' && !Window.isAuthenticated) next({ name: 'login' })
    else next()
})

Vue.filter('fixed2', function(value) {
    if (!value && value != 0) return ''
    return value.toFixed(2)
})

new Vue({
    router,
    store,
    data: {
        packageName: menu.packageName,
        mobileList: menu.mobileList,
        menuList: menu.menuList,
        requireRule: { required: true, message: '必填', trigger: 'blur' }
    },
    methods: {
        initSession(user, token) {
            Window.isAuthenticated = true;
            this.$http.token = token;
            this.$store.commit('setUser', user);
            this.$http.setConfig();
        },
        logout() {
            Window.isAuthenticated = false;
            this.$http.token = "";
            localStorage.removeItem("aSession");
            this.$http.resetConfig();
        },
        isMobile() {
            return /Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent);
        },
        /**
         * find the menu item with the param index in the list(mobileList or menuList)
         * @param {String} index 
         * @param {Array} list 
         */
        findMenuItem(index, list) {
            let item = null;
            let findIt = function(el) {
                if (el.index == index) {
                    item = el;
                } else if (el.subs) {
                    el.subs.forEach(it => {
                        findIt(it);
                    })
                }
            }
            list.forEach(el => {
                findIt(el);
            })
            return item;
        },
        getMobileTitle(path) {
            let title = "";
            if (path) {
                let item = this.findMenuItem(path, this.mobileList);
                if (item)
                    title = item.title
            }
            return title
        },
        getMobileIndex(name) {
            let idx = 0;
            this.mobileList.forEach((item, index) => {
                if (item.subs && item.subs.length) {
                    let it = item.subs.find(el => el.index == "/" + name);
                    if (it) {
                        idx = index
                    }
                }
            })
            return idx;
        },
        closeKeyboard(){
            // 隐藏foucus事件自动弹出的键盘
            if(window.cordova){
                setTimeout(function(){
                cordova.plugins.Keyboard.close()
                });
            }
        }
    },
    render: h => h(App)
}).$mount('#app')