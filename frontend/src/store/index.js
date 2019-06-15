import Vue from "vue";
import Vuex from "vuex";

import resources from './modules/resources';
import auth from './modules/auth';

Vue.use(Vuex);

export default new Vuex.Store({
  modules: {
    resources,
    auth
  },
  
});