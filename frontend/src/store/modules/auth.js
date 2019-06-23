import axios from "axios";
import {BASE_URL, API_URL} from './../../constants';
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
            //var formData = new FormData();
            //formData.append('username', data.username);
            //formData.append('password', data.password);
            let formData = {
                username: data.username,
                password: data.password,
            };

            axios({
                method: 'post',
                url: state.baseUrl + "/login",
                data: formData,
            }).then(
                response => {
                    //localStorage.setItem('user', JSON.stringify(response.user));
                    var decoded = jwt_decode(response.data);
                    let token = response.data;
                    axios.defaults.headers.common['Authorization'] = `Bearer ${token}`; 
                   
                    localStorage.setItem('token', token);
                    console.log("data", response.data);
                    console.log(decoded);
                },
                error => {
                    console.log(error);
                }
            );
        },
        logout({state}) {
            localStorage.removeItem('token');
            delete axios.defaults.headers.common['Authorization'];
            /*axios({
                method: 'get',
                url: state.baseUrl + "/logout",
            }).then(
                response => {
                    console.log("data", response.data);
                },
                error => {
                    console.log(error);
                }
            );*/
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