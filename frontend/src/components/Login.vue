<template>
  <div>
    <br>
    <v-card style="width:50%; margin:auto;">
      <v-toolbar>
        <v-toolbar-title>Login</v-toolbar-title>
      </v-toolbar>
      <v-card-text>
        <data-form :data="{}" :fields="form" :actions="actions" @submit="submit"></data-form>
        <v-btn color="primary" @click="logout()">Logout</v-btn>
      </v-card-text>
    </v-card>

    <router-link to="/crud/users">Go to Foo</router-link>
  </div>
</template>

<script>
import { mapActions } from "vuex";

import DataForm from "./DataForm";

let actions = {
  name: 'actions',
  props: ['submit', 'cancel'],
  template: `<div>
    <v-btn color="primary" @click="submit">Login</v-btn>
    <v-btn>Cancel</v-btn>
  </div>`
};

export default {
  name: "Login",
  props: {
    msg: String
  },
  components: {
    DataForm
  },
  data: function() {
    return {
      actions: actions,
      form: [
        {
          name: "username",
          type: "text"
        },
        {
          name: "password",
          type: "text"
        }
      ]
    };
  },
  methods: {
    ...mapActions('auth', ['login', 'logout']),
    submit: function(event) {
      console.log('login');
      let data = event.data;
      console.log(data);
      this.login(data);
    }
  }
};
</script>

<style scoped>
h3 {
  margin: 40px 0 0;
}
</style>
