import Vue from "vue";
import Vuex from "vuex";

import resources from './modules/resources';
import auth from './modules/auth';

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    message: null,
  },
  mutations: {
    setMessage(state, payload) {
        console.log(payload);
        state.message = payload;
    }
  },
  actions: {
    setMesage ({commit}, message) {
      commit('setMessage', message);
    },
    clearMessage ({commit}) {
      commit('setMessage', null);
    }
  },
  modules: {
    resources,
    auth
  },
  
});