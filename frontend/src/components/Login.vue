<template>
  <div>
    <br>
    <v-container fluid>
      <v-layout row>
        <v-flex xs6 offset-xs3 md6 offset-md3 lg4 offset-lg4 style="max-width: 450px;">
          <v-card>
            <v-toolbar>
              <v-toolbar-title>
                <v-icon>person</v-icon>Login
              </v-toolbar-title>
            </v-toolbar>
            <v-card-text>
              <data-form :data="{}" :fields="form" :actions="actions" @submit="submit"></data-form>
              <v-alert
                v-model="error"
                dismissible
                type="error"
              >{{ errorMessage }}</v-alert>
              <p class="text-xs-center">
                [
                <v-icon small>person_add</v-icon>Create new account ]
              </p>
            </v-card-text>
          </v-card>
        </v-flex>
      </v-layout>
    </v-container>

    <router-link to="/crud/users">Go to Foo</router-link>
  </div>
</template>

<script>
import { mapActions } from "vuex";

import DataForm from "./DataForm";
import { VBtn } from 'vuetify/lib';

let actions = {
  name: "actions",
  props: ["submit", "cancel"],
  components: {VBtn},
  template: `<div>
    <v-btn color="primary" block @click="submit">Login</v-btn>
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
      error: false,
      errorMessage: null,
      form: [
        {
          name: "email",
          label: "Email",
          type: "text",
          props: {
            "prepend-inner-icon": "person"
          }
        },
        {
          name: "password",
          label: "Password",
          type: "text",
          inputType: "password",
          props: {
            "prepend-inner-icon": "lock"
          }
        }
      ]
    };
  },
  methods: {
    ...mapActions("auth", ["login", "logout"]),
    submit: function(event) {
      let data = event.data;

      this.login(data).catch(e => {
        console.log(e);
        this.error = true;
        this.errorMessage = 'Bad credentials.';
      });
    }
  }
};
</script>

<style scoped>
h3 {
  margin: 40px 0 0;
}
</style>
