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

const defaultSettings = {};
for (let name in ResourceSettings) {
    let resource = ResourceSettings[name];
    resource.filter = resource.filter ? resource.filter : [];
    resource.title = resource.title ? resource.title : name;
    resource.fetch = resource.fetch !== undefined ? resource.fetch : true;
    resource.actions = resource.actions ? resource.actions : ['create', 'filter', 'refresh'];

    defaultSettings[name] = resource;
}

const defaultResourceData = Object.keys(ResourceSettings).reduce((map, key) => {
    map[key] = {
        page: 0,
        size: 10,
        data: [],
        // totalPages: 1,
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
        data: defaultResourceData,
        settings: defaultSettings,
        relations: [],
        pivotRelations: [],
        status: null,
    },
    getters: {
        getResourceData: state => state.data[state.resource],
        getResourceSettings: state => ({ ...state.settings[state.resource], name: state.resource })
    },
    mutations: {
        setResource(state, resource) {
            state.resource = resource;
        },
        setRelations(state, resource) {
            const form = state.settings[resource].form;
            state.relations = form.filter(field => field.type == 'relation');
            state.pivotRelations = form.filter(field => field.type == 'pivotRelation');
        },
        clearData(state, resource) {
            state.data = {
                ...state.data,
                [resource]: { resource, data: [], totalRows: 0 },
            };
        },
        setData(state, payload) {
            const dataObj = {
                name: payload.name,
                resource: payload.resource,
                rows: payload.rows,
                totalRows: payload.totalRows,
                page: payload.page,
                size: payload.size,
                sort: payload.sort,
                // totalPages: payload.totalPages,
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
            commit("clearData", resource);
            commit("setResource", resource);
            commit("setRelations", resource);
        },

        async getResource({
            commit,
            state
        }, payload) {
            commit("setStatus", 'loading');

            const url = payload.url ? state.baseUrl + "/" + payload.url : state.apiUrl + "/" + payload.resource;
            const sep = url.includes('?') ? '&' : '?';
            const pagination = payload.page !== undefined ? sep + 'offset=' + (payload.page - 1) + '&limit=' + payload.size : '';
            const sorting = payload.sort !== undefined ? '&sort='+payload.sort+','+(payload.desc ? 'desc' : 'asc') : '';
            const filter = payload.filter ? '&'+payload.filter : '';

            let [err, response] = await to(axios.get(url + pagination + sorting + filter));
            if (err) return handleError(err, 'Error occurred while fetching resource data.');

            let { rows, totalRows } = response.data;

            commit("setData", {
                resource: payload.resource,
                name: payload.name || payload.resource,
                rows: rows,
                totalRows: totalRows,

                page: payload.page || null,
                size: payload.size || null,
                sort: payload.sort,
                // totalPages: response.data.page ? response.data.page.totalPages : 4,
            });
            commit("setStatus", 'success');
        },
        async saveResource({state}, data) {
            const url = getSaveResourceUrl(data.id, state);

            let [err, response] = await to(axios[data.id ? "patch" : "post"](url, data));
            if (err) return handleError(err, 'Error occurred while saving resource data.');
        },

        async fetchData({
            state
        }, url) {
            let [err, response] = await to(axios.get(state.apiUrl+'/'+url));
            if (err) return handleError(err, 'Error occurred while saving resource data.');

            return response.data;
        },

        async deleteResource({
            state
        }, id) {
            let [err, response] = await to(axios.delete(state.apiUrl + '/' + state.resource + '/' + id));
            if (err) return handleError(err, 'Error occurred while deleting resource data.');
        },
    }
}