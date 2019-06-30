import jwt_decode from 'jwt-decode';
import axios from "axios";

export const capitalize = (s) => {
    if (typeof s !== 'string') return ''
    return s.charAt(0).toUpperCase() + s.slice(1)
}


/*export const getHeader = function () {
    const token = JSON.parse(localStorage.getItem('token'))
    return {
        'Accept': 'application/json',
        'Authorization': 'Bearer' + token
    }
}*/

export const getToken = function () {
    return jwt_decode(localStorage.getItem('token'));
}

export const axiosSetToken = function() {
    axios.interceptors.request.use(
        (config) => {
          let token = localStorage.getItem('token');
          console.log(getToken());
          if (token) {
            config.headers['Authorization'] = `Bearer ${ token }`;
          }
      
          return config;
        }, 
      
        (error) => {
          return Promise.reject(error);
        }
      );
};