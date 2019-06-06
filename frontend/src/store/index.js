import Vue from "vue";
import Vuex from "vuex";
import axios from "axios";
import CrudModels from "./../CrudModels";

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    baseUrl: "http://localhost:8083",
    apiUrl: "http://localhost:8083/api",
    activeResource: "books",
    resourceData: {}
  },
  getters: {
    getActiveResourceData: state => state.resourceData[state.activeResource]
  },
  mutations: {
    setActiveResource(state, payload) {
      state.activeResource = payload;
    },
    setResourceData(state, payload) {
      //state.resourceData[payload.resourceName] = payload.resourceData;
      state.resourceData = {
        ...state.resourceData,
        [payload.resourceName]: payload.resourceData
      };
    }
  },
  actions: {
    setActiveResource({ commit }, resourceName) {
      commit("setActiveResource", resourceName);
    },
    login({ state }) {
      console.log('login');
      //var datax = {username: "user", password: "user"};
      var formData = new FormData();
      formData.append('username', 'user');
      formData.append('password', 'user');
      
      axios({
        method: 'post',
        url: state.baseUrl + "/login",
        data: formData,
        config: { headers: {'Content-Type': 'application/x-www-form-urlencoded' }}
      }).then(
        response => {
          console.log("data", response.data);
        },
        error => {
          console.log(error);
        }
      );
    },
    register({ state }) {
      console.log('register');
      var datax = {username: "user", password: "user"};
      var formData = new FormData();
      formData.append('username', 'user');
      formData.append('password', 'user');
      console.log('loginxxx');
      axios({
        method: 'post',
        url: state.baseUrl + "/registration",
        data: datax
        //config: { headers: {'Content-Type': 'application/x-www-form-urlencoded' }}
      }).then(
        response => {
          console.log("data", response.data);
        },
        error => {
          console.log(error);
        }
      );
    },
    saveResourceData({ dispatch, state }, data) {
      let method = data.id ? "put" : "post";
      let param = data.id ? "/" + data.id +"/" : "/";
      let modelSettings = CrudModels[state.activeResource];
      let apiUrl = modelSettings.apiUrl !== undefined ? state.baseUrl+modelSettings.apiUrl : state.apiUrl;
      
      axios[method](
        apiUrl + "/" + state.activeResource + param,
        data
      ).then(
        response => {
            dispatch('fetchResourceData', state.activeResource);
            console.log("saveResourceDataResponse", response);
        },
        error => {
          console.log(error);
        }
      );
    },
    fetchResourceData({ commit, state }, resourceName) {
      return axios.get(state.apiUrl + "/" + resourceName +"/").then(
        response => {          
          var data = response.data._embedded[resourceName];          
          commit("setResourceData", {
            resourceName,
            resourceData: data
          });
          return response;
        },
        error => {
          console.log(error);
        }
      );
    }
  }
});
