const axios = require('axios');
// initial state
const state = {
  data: [],
  meno: 'vue',
}

// getters
const getters = {}

// actions
const actions = {
  fetchData({ commit }) { 
    console.log('fetch');
    axios.get("http://localhost:8000/posts").then(response => {
      this.results = response.data;
      commit("setData", response.data);
    });

      //commit('setData', [{'aaa': 'bbb'}, {'ccc': 'dddd'}]);  
  }
}

// mutations
const mutations = {
  setData (state, data) {
    state.data = data;
    console.log(state.data);
  },

  decrementProductInventory (state, { id }) {
    const product = state.all.find(product => product.id === id)
    product.inventory--
  }
}

export default {
  namespaced: true,
  state,
  getters,
  actions,
  mutations
}
