import axios from "axios";
import ResourceSettings from "./../../ResourceSettings";
import {
    BASE_URL,
    API_URL
} from './../../constants';

const axiosUriListRequest = axios.create({
    headers: {
        'Content-Type': 'text/uri-list'
    }
});

const getSaveDataUrl = (id, state) => {
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
        data: emptyDataObject
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
        },
        setData(state, payload) {
            state.data = {
                ...state.data,
                [payload.resource]: payload.data
            };
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
            //data.runDate = '2019-06-08';
            let formData = Object.assign({}, data);
            state.relations.forEach(relation => delete data[relation.name]);

            axios[data.id ? "put" : "post"](
                getSaveDataUrl(data.id, state),
                data
            ).then(response => {
                dispatch('updateRelations', formData);
                console.log(response);
            }).catch(error => console.log(error));
        },
        updateRelations({
            state,
            dispatch
        }, data) {
            console.log(data);
            state.relations.forEach(relation => {
                axiosUriListRequest.put(
                        state.apiUrl + "/" + state.resource + '/' + data.id + '/' + relation.name,
                        state.apiUrl + "/" + relation.resourceTable + '/' + data[relation.name].id)
                    .then(response => dispatch('fetchData', state.resource));
            });
        },
        fetchData({
            commit,
            state
        }, resource) {
            return axios.get(state.apiUrl + "/" + resource + "/").then(
                response => {
                    var data = response.data._embedded[resource];
                    commit("setData", {
                        resource,
                        data: data
                    });
                    return response;
                },
                error => {
                    console.log(error);
                }
            );
        }
    }
}