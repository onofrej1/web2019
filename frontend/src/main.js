import Vue from 'vue'
import App from './App.vue'
import Vuetify from 'vuetify'
import store from './store'
import VueRouter from 'vue-router'

//import Test from "./components/Test.vue";
import CrudAdmin from "./components/CrudAdmin.vue";
import Login from "./components/Login.vue";
import HelloWorld from "./components/HelloWorld.vue";



import 'vuetify/dist/vuetify.min.css' // Ensure you are using css-loader
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
    }
  },
  {
    path: '/login',
    component: Login
  }
];

const router = new VueRouter({
  routes // short for `routes: routes`
});

new Vue({
  el: '#app',
  store: store,
  router,
  render: h => h(App),
})//.$mount('#app')