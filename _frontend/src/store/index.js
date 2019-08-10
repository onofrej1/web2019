import Vue from "vue";
import Vuex from "vuex";

import resources from './modules/resources';
import auth from './modules/auth';
import modal from './modules/modal';
import files from './modules/files';

Vue.use(Vuex);

export default new Vuex.Store({
  
  modules: {
    resources,
    files,
    auth,
    modal
  },
  
});