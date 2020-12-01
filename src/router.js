/**
 * @author: wang.weili
 * @since : 2020/11/26
 */
import Vue from 'vue'
import Router from 'vue-router'
import Home from './views/Home.vue'
import Login from './views/Login.vue'

Vue.use(Router)

export default new Router({
    mode: 'history',
    base: process.env.BASE_URL,
    routes: [{
            path: '/', // support querys for mobile: ?index=0
            name: 'home',
            component: Home
        },
        {
            path: '/index.html', // support cordova apps
            name: 'home',
            component: Home
        },
        {
            path: '/login',
            name: 'login',
            component: Login
        }, {
            path: '/mainBoms',
            name: 'mainBoms',
            component: () =>
                import ( /* webpackChunkName: "template" */ './views/MainBoms.vue')
        }, {
            path: '/mainBom/:type/:id', // type: edit, detail
            name: 'mainBomEdit',
            component: () =>
                import ( /* webpackChunkName: "template" */ './views/MainBom.vue')
        }, {
            path: '/mainBom/:type', // type: add
            name: 'mainBomAdd',
            component: () =>
                import ( /* webpackChunkName: "template" */ './views/MainBom.vue')
        },
        {
            path: '/supplier',
            name: 'supplier',
            component: () =>
                import ( /* webpackChunkName: "template" */ './views/Supplier.vue')
        }
    ]
})