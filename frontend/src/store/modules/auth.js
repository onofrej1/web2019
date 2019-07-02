import axios from "axios";
import {
    BASE_URL,
    API_URL
} from './../../constants';
import jwt_decode from 'jwt-decode';

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
        /*setXXX(state, payload) {
            state.activeResource = payload;
        },*/
    },
    actions: {
        login({
            state
        }, data) {
            axios({
                method: 'post',
                url: state.baseUrl + "/login",
                data: {
                    username: data.username,
                    password: data.password,
                },
            }).then(
                response => {
                    localStorage.setItem('token', response.data);
                },
                error => {
                    console.log(error);
                }
            );
        },
        logout({
            state
        }) {
            localStorage.removeItem('token');
        },
        register({
            state
        }, data) {
            /*var formData = new FormData();
            formData.append('username', 'user');
            formData.append('password', 'user');*/

            axios({
                method: 'post',
                url: state.baseUrl + "/registration",
                data: data
                //config: { headers: {'Content-Type': 'application/x-www-form-urlencoded' }}
            }).then(
                response => {
                    console.log("data", response.data);
                },
                error => {
                    console.log(error);
                }
            );
        },

    }
};