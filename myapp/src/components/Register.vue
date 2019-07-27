<template>
  <div>
    <br>
    <v-container fluid>
      <v-layout row>
        <v-flex md6 offset-md3 lg4 offset-lg4>
          <v-card>
            <v-toolbar>
              <v-toolbar-title>
                <v-icon>person_add</v-icon> Create account
                </v-toolbar-title>
            </v-toolbar>

            <v-card-text>
              <data-form :data="{}" :fields="form" :actions="actions" @submit="submit"></data-form>
            </v-card-text>
          </v-card>
        </v-flex>
      </v-layout>
    </v-container>

    <router-link to="/crud/users">Go to Foo</router-link>
  </div>
</template>

<script>
import { mapActions, mapState } from "vuex";

import DataForm from "./DataForm";

let actions = {
  name: "actions",
  props: ["submit", "cancel"],
  template: `<div class="text-xs-right">
    <v-btn color="primary" text-xs-right @click="submit">
      Submit
    </v-btn>
  </div>`
};

export default {
  name: "Register",
  components: {
    DataForm
  },
  data: function() {
    return {
      actions: actions,
      users: [],
      form: [
        {
          name: "username",
          label: "Username/Login",
          validate: "required|isUnique:users,username",
          type: "text",
          flex: "xs12",
          props: {
            "prepend-inner-icon": "person"
          }
        },
        {
          name: "email",
          label: "Email",
          type: "text",
          flex: "xs12",
          validate: "required|email",
          props: {
            "prepend-inner-icon": "mail"
          }
        },
        {
          name: "password",
          label: "Password",
          type: "text",
          inputType: "password",
          validate: "required|min:6",
          flex: "xs6 md6",
          props: {
            "prepend-inner-icon": "lock"
          }
        },
        {
          name: "password_confirm",
          label: "Password confirm",
          type: "text",
          inputType: "password",
          validate: "required|confirmed:password",
          flex: "xs6 md6",
          props: {
            "prepend-inner-icon": "lock",
            'data-vv-as': "password"
          }
        },
        {
          name: "name",
          label: "Name",
          type: "text",
          validate: "required",
          flex: "xs12",
          props: {
            "prepend-inner-icon": "person_outline"
          }
        }
      ]
    };
  },
  methods: {
    ...mapActions("auth", ["register"]),
    submit: function(event) {
      let data = event.data;
      console.log(data);
      this.register(data);
    }
  }
};
</script>
