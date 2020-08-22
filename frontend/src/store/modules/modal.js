export default {
    namespaced: true,
    state: {
        settings: null
    },
    mutations: {
        setsettings(state, payload) {
            state.settings = payload;
        },
    },
    actions: {
        show({
            commit
        }, settings) {
            commit('setsettings', settings);
        },
        close({
            commit
        }) {
            commit('setsettings', null);
        }
    },
};