<template>
  <div>
    <br>
    <v-card style="width:50%; margin:auto;">
      <v-toolbar>
        <v-toolbar-title>Create account</v-toolbar-title>
      </v-toolbar>
      <v-card-text>
        <data-form :data="{}" :fields="form" :actions="actions" @submit="submit"></data-form>
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
    <v-btn color="primary" @click="submit">Submit</v-btn>
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
          label: "Username",
          type: "text"
        },
        {
          name: "password",
          label: "Password",
          type: "text"
        },
        {
          name: "name",
          label: "Name",
          type: "text"
        },
        {
          name: "email",
          label: "Email",
          type: "text"
        }
      ]
    };
  },
  methods: {
    ...mapActions('auth', ['register']),
    submit: function(event) {
      console.log('login');
      let data = event.data;
      console.log(data);
      this.register(data);
    }
  }
};
</script>
