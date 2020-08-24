import Vue from 'vue'
import App from './App.vue'
import vuetify from './plugins/vuetify';
import store from './store'
import VueRouter from 'vue-router'
//  from 'vee-validate';
import { ValidationProvider, ValidationObserver/*, localize*/ } from 'vee-validate/dist/vee-validate.full.esm';
import Admin from "./components/Admin.vue";
import Home from "./components/Home.vue";
import Page from "./components/Page.vue";
import Article from "./components/Article.vue";
import Login from "./components/Login.vue";
import Register from "./components/Register.vue";
// import sk from 'vee-validate/dist/locale/sk.json';
/* import Upload from './components/Upload.vue';
import ParseResults from './components/ParseResults.vue';

*/
import {getToken, axiosSetInterceptors} from './functions';


//import 'vuetify/dist/vuetify.min.css' // Ensure you are using css-loader
//import '~vuetify/src/stylus/main' // Ensure you are using stylus-loader
import 'vue-multiselect/dist/vue-multiselect.min.css';

// Vue.use(require('vue-moment'));
Vue.use(VueRouter);
// localize('sk', sk);
Vue.component('ValidationObserver', ValidationObserver);
Vue.component('ValidationProvider', ValidationProvider);

/* Vue.use(VeeValidate, {
  fieldsBagName: 'veeFields',
  events: 'change'
}); */
Vue.config.productionTip = false;

const routes = [{
  path: '/crud/:resource',
  name: 'crud',
  component: Admin,
  meta: {
    adminPage: true,
    // requiresAuth: true,
    // roles: ['ROLE_ADMIN', 'ROLE_MANAGER']
    }
  },
  {
    path: '/pages/:id',
    component: Page,
    name: 'page',
  },
  {
    path: '/articles/:id?',
    component: Article,
    name: 'article',
  },
  {
    path: '',
    component: Home,
    name: 'home'
  },
  {
    path: '/login',
    component: Login,
    name: 'login'
  },
  {
    path: '/register',
    component: Register,
    name: 'register'
  },
    /*
  {
    path: '/upload',
    name: 'upload',
    component: Upload,
    meta: {
      adminPage: true,
      requiresAuth: true,
      roles: ['ROLE_ADMIN', 'ROLE_MANAGER']
    }
  },

  {
    path: '/parse',
    component: ParseResults,
    name: 'parse',
    meta: {
      adminPage: true,
      requiresAuth: true,
      roles: ['ROLE_ADMIN', 'ROLE_MANAGER']
    },
  },
   */
];

axiosSetInterceptors();

export const router = new VueRouter({routes, mode:'history'})

router.beforeEach((to, from, next) => {

  if(to.meta.requiresAuth) {
    const token = getToken();

    if(!token) {
      next({name:'login'});
    }
    else if(to.meta.roles) {
      const roles = to.meta.roles.filter(value => token.roles.includes(value));
      if(roles.length > 0) {
        next();
      } else {
        next('/login');
      }
    }
  } else {
    next();
  }
})

new Vue({
  vuetify,
  store: store,
  router,
  render: h => h(App)
}).$mount('#app')


/*import Vue from 'vue'
import App from './App.vue'
import vuetify from './plugins/vuetify';

Vue.config.productionTip = false

new Vue({
  vuetify,
  render: h => h(App)
}).$mount('#app') */
