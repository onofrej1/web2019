import axios from "axios";
import ResourceSettings from "./../../ResourceSettings";
import to from 'await-to-js';
import {
    BASE_URL,
    API_URL
} from './../../constants';
import {
    handleError
} from './../../functions';

const getSaveResourceUrl = (id, state) => {
    let resourceSettings = state.settings[state.resource];
    let apiUrl = resourceSettings.apiUrl !== undefined ? state.baseUrl + resourceSettings.apiUrl : state.apiUrl;

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

const defaultResourceData = Object.keys(ResourceSettings).reduce((map, key) => {
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
        data: defaultResourceData,
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

        async getResource({
            commit,
            state
        }, payload) {
            commit("setStatus", 'loading');
            let url = payload.url ? state.baseUrl + "/" + payload.url : state.apiUrl + "/" + payload.resource;
            let sep = url.includes('?') ? '&' : '?';
            let pagination = payload.page !== undefined ? sep + 'page=' + (payload.page - 1) + '&size=' + payload.size : '';

            let [err, response] = await to(axios.get(url + pagination));
            if (err) return handleError(err, 'Error occurred while fetching resource data.');

            let data = response.data._embedded[payload.resource];

            commit("setData", {
                resource: payload.resource,
                name: payload.name || payload.resource,
                rows: data,
                page: payload.page || 0,
                size: payload.size || 10000,
                totalPages: response.data.page ? response.data.page.totalPages : 1,
            });
            commit("setStatus", 'success');
        },

        /*async getResourceOptions({
            commit,
            state
        }, payload) {
            //commit("setStatus", 'loading');                              
            let [err, response] = await to(axios.get(state.baseUrl + "/"+payload.url+'?search='+payload.search));
            if (err) return handleError(err, 'Error occurred while fetching options data.');

            let data = response.data._embedded[payload.resource];

            commit("setData", {
                resource: payload.resource,
                name: payload.name || payload.resource,
                rows: data,
                page: payload.page || 0,
                size: payload.size || 10000,
                totalPages: response.data.page ? response.data.page.totalPages : 1,
            });
            //commit("setStatus", 'success');
        },*/

        async saveResource({
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

            let url = getSaveResourceUrl(data.id, state);

            let [err] = await to(axios[data.id ? "patch" : "post"](url, data));
            if (err) return handleError(err, 'Error occurred while saving resource data.');
        },

        async deleteResource({
            state
        }, id) {
            let [err, response] = await to(axios.delete(state.apiUrl + '/' + state.resource + '/' + id));
            if (err) return handleError(err, 'Error occurred while deleting resource data.');
        },
    }
}