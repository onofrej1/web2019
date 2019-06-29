import Vue from 'vue'
import App from './App.vue'
import Vuetify from 'vuetify'
import store from './store'
import VueRouter from 'vue-router'

import CrudAdmin from "./components/CrudAdmin.vue";
import HelloWorld from "./components/HelloWorld.vue";
import Login from "./components/Login.vue";
import {getToken, axiosSetToken} from './functions';

import 'vuetify/dist/vuetify.min.css' // Ensure you are using css-loader
import auth from './store/modules/auth';
//import '~vuetify/src/stylus/main' // Ensure you are using stylus-loader

Vue.use(require('vue-moment'));
Vue.use(Vuetify)
Vue.use(VueRouter)

//Vue.config.productionTip = false

const routes = [{
    path: '/crud/:resource',
    name: 'crud',
    component: CrudAdmin,
    meta: {
      adminPage: true,
      requiresAuth: true,
      roles: ['ROLE_ADMIN', 'ROLE_MANAGER']
    }
  },
  {
    path: '/login',
    component: Login,
    name: 'login'
  },
  {
    path: '/hello',
    component: HelloWorld
  }
];

axiosSetToken();
const router = new VueRouter({routes,mode:'history'})  

router.beforeEach((to, from, next) => {
  //console.log(to);
  if(to.meta.requiresAuth) {
    const token = getToken();
    
    if(!token) {
      next({name:'login'});
    }
    else if(to.meta.roles) {
      const roles = to.meta.roles.filter(value => token.roles.includes(value));
      if(roles.length > 0) {
        next();
      }else {
        next('/login');
      }
    } 
  }else {
    next();
  }
})

new Vue({
  el: '#app',
  store: store,
  router,
  render: h => h(App),
});