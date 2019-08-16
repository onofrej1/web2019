import axios from "axios";
import ResourceSettings from "./../../ResourceSettings";
import {
    BASE_URL,
    API_URL
} from './../../constants';

const getSaveUrl = (id, state) => {
    let modelSettings = state.settings[state.resource];
    let apiUrl = modelSettings.apiUrl !== undefined ? state.baseUrl + modelSettings.apiUrl : state.apiUrl;

    return apiUrl + "/" + state.resource + (id ? "/" + id + '/' : '/');
}

const settings = {};
for(let name in ResourceSettings) {
    let resource = ResourceSettings[name];
    resource.filter = resource.filter ? resource.filter : [];
    resource.title = resource.title ? resource.title : name;
    resource.fetch = resource.fetch !== undefined ? resource.fetch : true;
    resource.actions = resource.actions ? resource.actions : ['create', 'filter', 'refresh'];
    
    settings[name] = resource;
}

const initData = Object.keys(ResourceSettings).reduce((map, key) => {
    map[key] = [];
    return map;
}, {});

export default {
    namespaced: true,
    state: {
        baseUrl: BASE_URL,
        apiUrl: API_URL,
        resource: "users",
        settings: settings,
        relations: [],
        pivotRelations: [],
        data: initData,
        status: null,
    },
    getters: {
        getResourceData: state => {
            return state.data[state.resource];
        },
        getResourceSettings: state => state.settings[state.resource]
    },
    mutations: {
        setResource(state, resource) {
            state.resource = resource;
        },
        setRelations(state, resource) {
            let form = state.settings[resource].form;
            state.relations = form.filter(field => field.type == 'relation');
            state.pivotRelations = form.filter(field => field.type == 'pivotRelation');
        },
        clearData(state, resource) {
            state.data = {
                ...state.data,
                [resource]: []
            };
        },
        setData(state, payload) {
            state.data = {
                ...state.data,
                [payload.name || payload.resource]: payload.data
            };
        },
        setStatus(state, status) {
            state.status = status;
        }
    },
    actions: {
        setResource({
            commit
        }, resource) {
            commit("setResource", resource);
            commit("setRelations", resource);
        },
        deleteResource({
            dispatch,
            state
        }, id) {
            axios.delete(
                state.apiUrl + '/' + state.resource + '/'+id
            ).then(response => {
                dispatch('fetchData', state.resource)
            }).catch(error => {
                dispatch('fetchData', state.resource);
                console.log(error)
            });
        },
        saveResource({
            dispatch,
            state
        }, data) {
            state.relations.forEach(relation =>  {
                if(data[relation.name]) {
                    data[relation.name] = state.apiUrl + "/" + relation.resourceTable + '/' + data[relation.name];
                }  
            });

            let links = [];
            state.pivotRelations.forEach(relation =>  {  
                data[relation.name].forEach(d => {
                    links.push(state.apiUrl + "/" + relation.resourceTable + '/' + d);
                });
                data[relation.name] = links;
            });
            
            axios[data.id ? "patch" : "post"](
                getSaveUrl(data.id, state),
                data
            ).then(response => {
                dispatch('fetchData', state.resource)
                console.log(response);
            }).catch(error => {
                dispatch('fetchData', state.resource);
                console.log(error)
            });
        },
        fetchCustomData({
            commit,
            dispatch,
            state
        }, payload) {
            commit("setStatus", 'loading');
            return axios.get(state.baseUrl + "/" + payload.url).then(
                response => {
                    var data = response.data._embedded[payload.resource];

                    data.length = 40;
                    console.log(data.length);
                    
                    commit("setData", {
                        resource: payload.resource,
                        name: payload.name,
                        data: data
                    });
                    commit("setStatus", 'success');                    
                    return {...response, data:data};
                },
                error => {
                    commit("setStatus", 'error');
                    console.log(error);
                }
            );
        },
        fetchData({
            commit,
            dispatch,
            state
        }, resource) {
            commit("setStatus", 'loading');
            return axios.get(state.apiUrl + "/" + resource).then(
                response => {
                    var data = response.data._embedded[resource];                    
                    console.log(response.data.page);
                    commit("setData", {
                        resource,
                        data: data
                    });
                    commit("setStatus", 'success');                    
                    return {...response, data:data, page: response.data.page};
                },
                error => {
                    commit("setStatus", 'error');
                    console.log(error);
                }
            );
        }
    }
}