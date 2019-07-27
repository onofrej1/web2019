import axios from "axios";
import {
    router
} from './../../main'
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
    getters: {

    },
    mutations: {
        setFiles(state, files) {
            state.files = files;
        },
    },
    actions: {
        uploadFile({dispatch}, formData) {
            axiosFormData.post(BASE_URL + '/upload',
                    formData,
                ).then(function () {
                    dispatch('fetchFiles');
                    console.log('SUCCESS!!');
                })
                .catch(function () {
                    console.log('FAILURE!!');
                });
        },
        fetchFiles({
            commit,
            dispatch,
            state
        }, resource) {
            //commit("setStatus", 'loading');
            return axios.get(state.baseUrl + "/files").then(
                response => {
                    var data = response.data;
                    commit("setFiles", data);
                    //commit("setStatus", 'success');                    
                    return {...response, data:data};
                },
                error => {
                    //commit("setStatus", 'error');
                    console.log(error);
                }
            );
        }
    }
};