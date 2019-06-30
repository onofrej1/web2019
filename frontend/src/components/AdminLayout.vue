<template>
  <div>
    <v-toolbar dark color="primary" app>
      <!--<v-toolbar-side-icon></v-toolbar-side-icon>-->
      <img src="./../assets/images/furca-logo.png" width="50px" />
      <v-toolbar-title class="white--text" style="margin:5px">O5 Bežecký klub Furča</v-toolbar-title>

      <v-spacer></v-spacer>

      <v-btn icon>
        <v-icon>search</v-icon>
      </v-btn>

      <v-btn icon to="/login">
        <v-icon>account_circle</v-icon>
      </v-btn>
    </v-toolbar>

    <v-card id="menu">
      <v-list two-line dense class="pt-0">
        <v-list-tile>
          <!--<v-list-tile-avatar>
            <img src="https://picsum.photos/200">
          </v-list-tile-avatar>-->
          <v-list-tile-action>
            <v-icon medium>folder</v-icon>
          </v-list-tile-action>
          <v-list-tile-content>
            <v-list-tile-title>Profile</v-list-tile-title>
            <v-list-tile-sub-title>User profile settigns</v-list-tile-sub-title>
          </v-list-tile-content>
        </v-list-tile>

        <v-divider></v-divider>
        <v-list-group
          :prepend-icon="groupIcons[groupName]"
          :key="groupName"
          no-action
          v-for="(group, groupName) in modelNames"
        >
          <template v-slot:activator>
            <v-list-tile>
              <v-list-tile-content>
                <v-list-tile-title>{{ groupName }}</v-list-tile-title>
              </v-list-tile-content>
            </v-list-tile>
          </template>
          <template v-for="item in group">
            <v-list-tile :key="item.resource">
              <v-list-tile-content class="list-content-link">
                <v-list-tile-title class @click="setModel(item.resource)">{{ item.title }}</v-list-tile-title>
              </v-list-tile-content>
              <v-list-tile-action>
                <v-btn icon ripple @click="setModel(item.resource)">
                  <v-icon color="grey lighten-1" large>arrow_right</v-icon>
                </v-btn>
              </v-list-tile-action>
            </v-list-tile>
          </template>
        </v-list-group>
        <v-divider></v-divider>
        <v-list-tile>
          <v-list-tile-action>
            <v-icon medium>folder</v-icon>
          </v-list-tile-action>
          <v-list-tile-content>
            <v-list-tile-title>Media manager</v-list-tile-title>
            <v-list-tile-sub-title>Upload files and images</v-list-tile-sub-title>
          </v-list-tile-content>
        </v-list-tile>
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
import Error from './Error';

//import logo from "./../assets/images/furca-logo.png"

export default {
  name: "admin-layout",
  data: () => ({
    drawer: true,
    groupIcons: {
      Permissions: "account_circle",
      Admin: "list_alt"
    }
  }),
  props: {
    source: String,
    models: Object
  },
  methods: {
    setModel: function(modelName) {
      this.$router.push({
        name: "crud",
        params: { resource: modelName }
      });
    }
  },
  components: {
    Error
  },
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
