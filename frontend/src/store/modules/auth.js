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
axios.defaults.withCredentials = true;

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
            await axios.get('http://localhost:8000/sanctum/csrf-cookie');
            let [err, response] = await to(axios.post(state.apiUrl+'/login', data));
            if (err) throw err;

            console.log(response);
        },
        logout({state}) {
            axios.get(state.apiUrl+'/logout').then((response) => {
                console.log(response);
                router.push({name: 'login'});
            });
        },
        async register({
            state
        }, data) {
            let [err] = await to(axios.post(state.apiUrl + "/register", data));
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