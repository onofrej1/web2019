import jwt_decode from 'jwt-decode';

export const getHeader = function () {
    const token = JSON.parse(localStorage.getItem('token'))
    return {
        'Accept': 'application/json',
        'Authorization': 'Bearer' + token
    }
}

export const getToken = function () {
    return jwt_decode(localStorage.getItem('token'));
}