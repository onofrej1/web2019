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
                    //router.push({name: 'home'});
                },
                error => {
                    console.log(error);
                }
            );
        },
        logout() {
            localStorage.removeItem('token');
            router.push({name: 'login'});
        },
        register({
            state
        }, data) {
            axios({
                method: 'post',
                url: state.baseUrl + "/registration",
                data: data
            }).then(
                response => {
                    console.log("data", response.data);
                },
                error => {
                    console.log(error);
                }
            );
        },
        /*checkUserName({
            state
        }, username) {
            axios({
                method: 'post',
                url: state.baseUrl + "/check-username",
                data: username
            }).then(
                response => {
                    return response.data;
                },
                error => {
                    console.log(error);
                }
            );
        },*/

    }
};