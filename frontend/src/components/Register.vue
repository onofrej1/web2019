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
  name: "actions",
  props: ["submit", "cancel"],
  template: `<div>
    <v-btn color="primary" @click="submit">Submit</v-btn>
    <v-btn>Cancel</v-btn>
  </div>`
};

export default {
  name: "Register",
  props: {
    msg: String
  },
  components: {
    DataForm
  },
  data: function() {
    return {
      refs: this.$refs,
      actions: actions,
      form: [
        {
          name: "username",
          label: "Username",
          type: "text",
          flex: "xs12",
          rules: [
            v => !!v || "E-mail is required",
            // test unique user name
            //v =>  || "user name is allready taken"
          ]
        },
        {
          name: "password",
          label: "Password",
          type: "password",
          rules: [v => !!v || 'Password is required'],
          flex: "xs6 md3"
        },
        {
          name: "password_repeat",
          label: "Password confirm",
          type: "password",
          rules: [v => {
            console.log(refs);
            //v == this.$refs.password.nodeValue || 'Passwords dont match'
            return true;
            }
            ],
          flex: "xs6 md3"
        },
        {
          name: "name",
          label: "Name",
          type: "text",
          rules: [v => !!v || 'Name is required'],
          flex: "xs12"
        },
        {
          name: "email",
          label: "Email",
          type: "text",
          flex: "xs12",
          rules: [
            v => !!v || "E-mail is required",
            v => /.+@.+/.test(v) || "E-mail must be valid"
          ]
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
