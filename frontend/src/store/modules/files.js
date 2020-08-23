import axios from "axios";
import to from 'await-to-js';
import {
    handleError
} from './../../functions';
import {
    BASE_URL,
    API_URL
} from './../../constants';

const axiosFormData = axios.create({
    headers: {
        'Content-Type': 'multipart/form-data'
    }
});

export default {
    namespaced: true,
    state: {
        baseUrl: BASE_URL,
        apiUrl: API_URL,
        files: []
    },
    mutations: {
        setFiles(state, files) {
            state.files = files;
        },
    },
    actions: {
        async uploadFile({
            dispatch
        }, formData) {
            let [err] = await to(axiosFormData.post(BASE_URL + '/upload', formData));
            if (err) return handleError(err, 'File upload error.');

            dispatch('fetchFiles');
        },
        async fetchFiles({
            commit,
            state
        }) {
            let [err, response] = await to(axios.get(state.baseUrl + "/files"));
            if (err) return handleError(err, 'Error occurred while fetching files.');
            
            commit("setFiles", response.data);
        }
    }
};