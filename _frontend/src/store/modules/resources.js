import axios from "axios";
import ResourceSettings from "./../../ResourceSettings";
import to from 'await-to-js';
import {
    BASE_URL,
    API_URL
} from './../../constants';
import {handleError} from './../../functions';

const getSaveUrl = (id, state) => {
    let modelSettings = state.settings[state.resource];
    let apiUrl = modelSettings.apiUrl !== undefined ? state.baseUrl + modelSettings.apiUrl : state.apiUrl;

    return apiUrl + "/" + state.resource + (id ? "/" + id + '/' : '/');
}

const settings = {};
for (let name in ResourceSettings) {
    let resource = ResourceSettings[name];
    resource.filter = resource.filter ? resource.filter : [];
    resource.title = resource.title ? resource.title : name;
    resource.fetch = resource.fetch !== undefined ? resource.fetch : true;
    resource.actions = resource.actions ? resource.actions : ['create', 'filter', 'refresh'];

    settings[name] = resource;
}

const initData = Object.keys(ResourceSettings).reduce((map, key) => {
    map[key] = {
        page: 0,
        size: 10,
        data: [],
        totalPages: 1,
        name: key,
        resource: key
    };
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
                [resource]: {}
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
        async deleteResource({
            dispatch,
            state
        }, id) {
            let [err, response] = await to(axios.delete(state.apiUrl + '/' + state.resource + '/' + id));
            if (err) {
                console.error('Error occured while removing data.');
                return;
            }
            dispatch('getResource', state.resource);
        },
        async saveResource({
            dispatch,
            state
        }, data) {
            state.relations.forEach(relation => {
                if (data[relation.name]) {
                    data[relation.name] = state.apiUrl + "/" + relation.resourceTable + '/' + data[relation.name];
                }
            });

            let links = [];
            state.pivotRelations.forEach(relation => {
                data[relation.name].forEach(d => {
                    links.push(state.apiUrl + "/" + relation.resourceTable + '/' + d);
                });
                data[relation.name] = links;
            });

            let method = data.id ? "patch" : "post";
            let [err, response] = await to(axios[method](
                getSaveUrl(data.id, state),
                data
            ));
            if (err) {
                console.error('Error occured while saving data.');
                return;
            }
        },
        async getResource({
            commit,
            dispatch,
            state
        }, payload) {
            commit("setStatus", 'loading');
            let url = payload.url ? state.baseUrl + "/" + payload.url : state.apiUrl + "/x" + payload.resource;
            let sep = url.includes('?') ? '&' : '?';
            let paginateParams = payload.page !== undefined ? sep + 'page=' + (payload.page - 1) + '&size=' + payload.size : '';

            let [err, response] = await to(axios.get(url + paginateParams));
            if (err) handleError(err, 'Error occured while fetching data.');

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
        }
    }
}