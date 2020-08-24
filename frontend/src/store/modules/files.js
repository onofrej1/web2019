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
            console.log(formData);
            let [err] = await to(axiosFormData.post(API_URL + '/upload', formData));
            if (err) return handleError(err, 'File upload error.');

            dispatch('fetchFiles');
        },
        async fetchFiles({
            commit,
        }) {
            let [err, response] = await to(axios.get(API_URL + "/files"));
            if (err) return handleError(err, 'Error occurred while fetching files.');
            
            commit("setFiles", response.data);
        }
    }
};