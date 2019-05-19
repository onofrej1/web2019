import Vue from 'vue'
import Vuex from 'vuex'
import crud from './modules/crud'

Vue.use(Vuex)

export default new Vuex.Store({
  modules: {
    crud
  },
})
