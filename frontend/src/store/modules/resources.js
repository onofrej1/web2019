import axios from "axios";
import ResourceSettings from "./../../ResourceSettings";
import {
    BASE_URL,
    API_URL
} from './../../constants';

const uriListHeader = {
    headers: {
        'Content-Type': 'text/uri-list'
    }
};

const getSaveUrl = (id, state) => {
    let modelSettings = state.settings[state.resource];
    let apiUrl = modelSettings.apiUrl !== undefined ? state.baseUrl + modelSettings.apiUrl : state.apiUrl;

    return apiUrl + "/" + state.resource + (id ? "/" + id + '/' : '/');
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
        settings: ResourceSettings,
        relations: [],
        pivotRelations: [],
        data: initData,
        status: null,
    },
    getters: {
        getResourceData: state => state.data[state.resource],
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
                [payload.resource]: payload.data
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
            //console.log('save resource');
            console.log(data);
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
        fetchData({
            commit,
            dispatch,
            state
        }, resource) {
            commit("setStatus", 'loading');
            return axios.get(state.apiUrl + "/" + resource).then(
                response => {
                    var data = response.data._embedded[resource];
                    commit("setData", {
                        resource,
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
        }
    }
}