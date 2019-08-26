import jwt_decode from 'jwt-decode';
import axios from "axios";
import Vue from 'vue'

import { fail } from 'assert';
const moment = require("moment");

export const capitalize = (s) => {
    if (typeof s !== 'string') return ''
    return s.charAt(0).toUpperCase() + s.slice(1)
}

export const truncate = (source, size = 150) => {
    return source.length > size ? source.slice(0, size - 1) + "…" : source;
}

export const formatDate = (date) => {
    return moment(date).format("DD/MM/YYYY");
}

export const getToken = function () {
    let token = localStorage.getItem('token');
    return token ? jwt_decode(token) : null;
}

export const axiosSetInterceptors = function () {
    axios.interceptors.request.use(
        (config) => {
            let token = localStorage.getItem('token');
            if (token) {
                config.headers['Authorization'] = `Bearer ${ token }`;
            }

            return config;
        },

        (error) => {
            //console.log('error occured.');
            //return Promise.reject(error);
        }
    );
    /*axios.interceptors.response.use(undefined, function (error) {
        return Promise.reject(error);
    });*/
};

export const handleError = function(err, msg = null) {
    //console.error(err);
    console.error(msg);

    throw new Error("Stop script");
}

export const catchUnhandledRejection = function() {
   /* window.addEventListener('unhandledrejection', function(event) {
        console.error('Unhandled rejection (promise: ', event.promise, ', reason: ', event.reason, ').');
    });*/
    console.log('add listener');
    

    
}

export const guid = function () {
    function _p8(s) {
        var p = (Math.random().toString(16) + "000000000").substr(2, 8);
        return s ? "-" + p.substr(0, 4) + "-" + p.substr(4, 4) : p;
    }
    return _p8() + _p8(true) + _p8(true) + _p8();
}

/*
    zmaz
    export const getHeader = function () {
    const token = JSON.parse(localStorage.getItem('token'))
    return {
        'Accept': 'application/json',
        'Authorization': 'Bearer' + token
    }
}*/