import Vue from "vue";
import Vuex from "vuex";

import resources from './modules/resources';
import auth from './modules/auth';
import modal from './modules/modal';

Vue.use(Vuex);

export default new Vuex.Store({
  
  modules: {
    resources,
    auth,
    modal
  },
  
});