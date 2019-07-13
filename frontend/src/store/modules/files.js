import axios from "axios";
import {router} from './../../main'
import {
    BASE_URL,
    API_URL
} from './../../constants';

const axiosForm = axios.create({
    headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
    }
});
export default {
    namespaced: true,
    state: {
        baseUrl: BASE_URL,
        apiUrl: API_URL,
    },
    getters: {

    },
    mutations: {
       
    },
    actions: {
        login({
            state
        }, data) {
            return axios({
                method: 'post',
                url: state.baseUrl + "/login",
                data: {
                    username: data.username,
                    password: data.password,
                },
            }).then(
                response => {
                    localStorage.setItem('token', response.data);
                    //router.push({name: 'home'});
                },
                error => {
                    console.log(error);
                    return Promise.reject(error);
                }
            );
        },
    }
};