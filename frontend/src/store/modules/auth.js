import axios from "axios";
import to from 'await-to-js';
import {router} from './../../main'
import {
    BASE_URL,
    API_URL
} from './../../constants';
import {
    handleError
} from './../../functions';

/* const axiosForm = axios.create({
    headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
    }
}); */

export default {
    namespaced: true,
    state: {
        baseUrl: BASE_URL,
        apiUrl: API_URL,
    },
    actions: {
        async login({
            state
        }, data) {
            let [err, response] = await to(axios.post(state.baseUrl + "/login", data));
            if (err) return handleError(err, 'Login error.');

            localStorage.setItem('token', response.data);
        },
        logout() {
            localStorage.removeItem('token');
            router.push({name: 'login'});
        },
        async register({
            state
        }, data) {
            let [err] = await to(axios.post(state.baseUrl + "/registration",data));
            if (err) return handleError(err, 'User registration error.');
        },
        /*checkUserName({
            state
        }, username) {
            axios({
                method: 'post',
                url: state.baseUrl + "/check-username",
                data: username
            });
        },*/
    }
};