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
    map[key] = { page: 0, size: 10, data: [], totalPages: 1, name: key};
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
            let dataObj = {
                name: payload.name,
                resource: payload.resource,
                page: payload.page,
                size: payload.size,
                totalPages: payload.totalPages,
                rows: payload.rows
            };

            state.data = {
                ...state.data,
                [payload.name || payload.resource]: dataObj
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
                console.log(state.resource);
                //dispatch('fetchData', state.resource)
                console.log(response);
            }).catch(error => {
                dispatch('fetchData', state.resource);
                console.log(error)
            });
        },
        fetchData({
            commit,
            dispatch,
            state
        }, payload) {
            //console.log(payload);
            commit("setStatus", 'loading');
            let url = payload.url ? state.baseUrl + "/" + payload.url : state.apiUrl + "/" + payload.resource;
            let sep = url.includes('?') ? '&' : '?';
            let paginateParams = payload.page !== undefined ? sep+'page='+(payload.page - 1)+'&size='+payload.size : '';

            return axios.get(url + paginateParams).then(
                response => {
                    var data = response.data._embedded[payload.resource];  
                                   
                    commit("setData", {
                        resource: payload.resource,
                        name: payload.name || payload.resource,
                        rows: data,
                        page: payload.page || 0,
                        size: payload.size || 10000,
                        totalPages: response.data.page ? response.data.page.totalPages : 1,
                    });
                    commit("setStatus", 'success');                    
                    //return {...response, data:data, page: response.data.page};
                },
                error => {
                    commit("setStatus", 'error');
                    console.log(error);
                }
            );
        }
    }
}