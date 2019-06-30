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

const emptyDataObject = Object.keys(ResourceSettings).reduce((map, key) => {
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
        data: emptyDataObject,
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
        saveData({
            dispatch,
            state
        }, data) {
            let formData = Object.assign({}, data);
            state.relations.forEach(relation => delete data[relation.name]);
            state.pivotRelations.forEach(relation => delete data[relation.name]);

            axios[data.id ? "put" : "post"](
                getSaveUrl(data.id, state),
                data
            ).then(response => {
                dispatch('fetchData', state.resource)
                dispatch('updateRelations', formData);
                console.log(response);
            }).catch(error => {
                dispatch('fetchData', state.resource);
                console.log(error)
            });
        },
        updateRelations({
            state,
            dispatch
        }, data) {
            state.relations.forEach(relation => {
                axiosUriListRequest.put(
                        state.apiUrl + "/" + state.resource + '/' + data.id + '/' + relation.name,
                        state.apiUrl + "/" + relation.resourceTable + '/' + data[relation.name],
                        uriListHeader)
                    .then(response => dispatch('fetchData', state.resource));
            });
            state.pivotRelations.forEach(relation => {
                let urls = data[relation.name].reduce((accum, d, ) => {
                    return accum += state.apiUrl + "/" + relation.resourceTable + '/' + d + '\n';
                }, '');
                axios.put(
                        state.apiUrl + "/" + state.resource + '/' + data.id + '/' + relation.name,
                        urls, uriListHeader)
                    .then(response => dispatch('fetchData', state.resource));
            });
        },
        fetchData({
            commit,
            dispatch,
            state
        }, resource) {
            commit("setStatus", 'loading');
            return axios.get(state.apiUrl + "/" + resource + "/").then(
                response => {
                    var data = response.data._embedded[resource];
                    commit("setData", {
                        resource,
                        data: data
                    });
                    commit("setStatus", 'success');
                    //commit("setMessage", resource, {root: true});
                    //dispatch('setMessage', resource);
                    return response;
                },
                error => {
                    commit("setStatus", 'error');
                    console.log(error);
                }
            );
        }
    }
}