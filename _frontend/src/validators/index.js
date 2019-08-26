import {
    Validator
} from "vee-validate";
import store from '../store/index';

Validator.extend("isUnique", {
    validate: function(value, args) {
        return store.dispatch('resources/getResource', args[0]).then(res => {
            return !res.data.map(u => u[args[1]]).includes(value);
        });
    },
    getMessage: (field, params, data) => {
        return field+" is taken.";
    }
});