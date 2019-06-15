import axios from "axios";
import {BASE_URL, API_URL} from './../../constants';

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
        }) {
            var formData = new FormData();
            formData.append('username', 'user');
            formData.append('password', 'user');

            axios({
                method: 'post',
                url: state.baseUrl + "/login",
                data: formData,
                config: {
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    }
                }
            }).then(
                response => {
                    console.log("data", response.data);
                },
                error => {
                    console.log(error);
                }
            );
        },
        register({
            state
        }) {
            var datax = {
                username: "user",
                password: "user"
            };
            var formData = new FormData();
            formData.append('username', 'user');
            formData.append('password', 'user');

            axios({
                method: 'post',
                url: state.baseUrl + "/registration",
                data: datax
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