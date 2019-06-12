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
    resourceData: {},
    models: CrudModels,
  },
  getters: {
    getActiveResourceData: state => state.resourceData[state.activeResource]
  },
  mutations: {
    setActiveResource(state, payload) {
      state.activeResource = payload;

      let form = state.models[payload].form;
      state.activeResourceRelations = form.filter(field => field.type == 'relation')
        .map(field => field.name);
      console.log('aaaaa');
      console.log(state.activeResourceRelations);
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
      
      data.runDate = '2019-06-08';
      let formData = data;
      //data = data.filter(d)
      console.log(data);

      axios[method](
        apiUrl + "/" + state.activeResource + param,
        data
      ).then(
        response => {
            this.updateRelations(data, state);
            dispatch('fetchResourceData', state.activeResource);
            console.log("saveResourceDataResponse", response);
        },
        error => {
          console.log(error);
        }
      );
    },
    updateRelations(data, state) {
      let formFields = state.models[state.activeResource].form;
      const axiosUriList = axios.create({        
        headers: {'Content-Type': 'text/uri-list'}
      });

      for(let i in formFields)
      {
          console.log(formFields[i]);
          let field = formFields[i];
          if(field.type == 'relation') {
            axiosUriList.put(
              state.apiUrl + "/" + state.activeResource + '/'+data.id +'/'+field.name,
              state.apiUrl + "/"+field.resourceTable+'/'+data[field.name]
            );
            //delete data[field.name];
          }
      }
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
