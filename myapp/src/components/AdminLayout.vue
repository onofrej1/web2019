<template>
  <div>
    <v-app-bar dark color="primary" app>
      <!--<v-toolbar-side-icon></v-toolbar-side-icon>-->
      <img src="./../assets/images/furca-logo.png" width="50px">
      <v-toolbar-title class="white--text" style="margin:5px">O5 Bežecký klub Furča</v-toolbar-title>

      <v-spacer></v-spacer>

      <v-btn icon>
        <v-icon>search</v-icon>
      </v-btn>

      <v-btn icon to="/login">
        <v-icon>account_circle</v-icon>
      </v-btn>
      <v-btn icon @click="logout()">
        <v-icon>input</v-icon>
      </v-btn>
    </v-app-bar>

    <v-card id="menu">
      <v-list class="pt-0">
        <v-list-item>
          <!--<v-list-item-avatar>
            <img src="https://picsum.photos/200">
          </v-list-item-avatar>-->
          <v-list-item-action>
            <v-icon medium>folder</v-icon>
          </v-list-item-action>
          <v-list-item-content>
            <v-list-item-title>Profile</v-list-item-title>
            <v-list-item-subtitle>User profile settigns</v-list-item-subtitle>
          </v-list-item-content>
        </v-list-item>

        <v-divider></v-divider>
        <v-list-group
          :prepend-icon="groupIcons[groupName]"
          :key="groupName"
          no-action
          v-for="(group, groupName) in modelNames"
          value="true"
        >
          <template v-slot:activator>
            <v-list-item-title>{{ groupName }}</v-list-item-title>
          </template>
          <template v-for="item in group">
            <v-list-item :key="item.resource">
              <v-list-item-content class="list-content-link">
                <v-list-item-title class @click="setModel(item.resource)">{{ item.title }}</v-list-item-title>
              </v-list-item-content>
              <v-list-item-action>
                <v-btn icon ripple @click="setModel(item.resource)">
                  <v-icon color="grey lighten-1" large>arrow_right</v-icon>
                </v-btn>
              </v-list-item-action>
            </v-list-item>
          </template>
        </v-list-group>
        <v-divider></v-divider>
        <v-list-item>
          <v-list-item-action>
            <v-icon medium @click="openModal">folder</v-icon>
          </v-list-item-action>
          <v-list-item-content>
            <v-list-item-title>Media manager</v-list-item-title>
            <v-list-item-subtitle>Upload files and images</v-list-item-subtitle>
          </v-list-item-content>
        </v-list-item>
      </v-list>
    </v-card>

    <div class="content">
      <slot></slot>
      <router-view></router-view>
    </div>

    <v-footer style="border:1px solid lightgray" app></v-footer>
  </div>
</template>

<script>
var _ = require("underscore");
import { mapActions } from "vuex";

//import logo from "./../assets/images/furca-logo.png"

export default {
  name: "admin-layout",
  data: () => ({
    drawer: true,
    groupIcons: {
      Permissions: "account_circle",
      Runs: "calendar_today",
      Content: "list_alt",
      Blog: "list_alt"
    }
  }),
  props: {
    source: String,
    models: Object
  },
  methods: {
    ...mapActions("modal", { showModal: "show" }),
    ...mapActions("auth", ["logout"]),
    setModel: function(modelName) {
      this.$router.push({
        name: "crud",
        params: { resource: modelName }
      });
    },
    openModal: function() {
      this.showModal({
        title: "Modal title",
        component: {
          name: "test",
          template: `
          <h1>modal</h1>
        `
        }
      });
    }
  },
  components: {},
  computed: {
    modelNames: function() {
      return _.chain(this.models)
        .map((m, key) => ({ ...m, resource: key }))
        .groupBy(m => m.group)
        .value();
    }
  }
};
</script>
<style>
#menu {
  width: 300px;
  height: 100%;
  top: 65px;
  left: 0px;
  z-index: 1;
  margin-right: 10px;
  position: fixed;
  overflow-x: hidden;
}

.content {
  margin-left: 300px;
  margin-top: 65px;
  padding: 10px;
}

.list-content-link {
  cursor: pointer;
}
</style>
