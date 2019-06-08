<template>
  <div>
    <v-toolbar dark color="primary" app>
      <v-toolbar-side-icon></v-toolbar-side-icon>
      <v-toolbar-title class="white--text">O5 Bežecký klub Furča</v-toolbar-title>

      <v-spacer></v-spacer>
      <v-btn icon>
        <v-icon>search</v-icon>
      </v-btn>
    </v-toolbar>

    <v-card id="menu">
      <v-list two-line dense class="pt-0">
        <v-list-tile :key="item">
          <v-list-tile-content>
            <v-list-tile-title>Permissions</v-list-tile-title>
            <v-list-tile-sub-title>Set roles and permissions</v-list-tile-sub-title>
          </v-list-tile-content>
        </v-list-tile>
        <v-divider></v-divider>
        <v-list-group
          :prepend-icon="groupIcons[groupName]"
          :key="group"
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
          <template v-for="(item) in group">
            <v-list-tile :key="item.title">
              <v-list-tile-content>
                <v-list-tile-title @click="setModel(item.resource)">{{ item.title }}</v-list-tile-title>
              </v-list-tile-content>
              <v-list-tile-action>
                <v-btn icon ripple>
                  <v-icon color="grey lighten-1">info</v-icon>
                </v-btn>
              </v-list-tile-action>
            </v-list-tile>
          </template>
        </v-list-group>
        <v-divider></v-divider>
        <v-list-tile :key="item">
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
      aaa
      <router-view></router-view>
      {{ modelGroups }}
      <router-link to="/foo">Go to Foo</router-link>
      <router-link to="/bar">Go to Bar</router-link>
    </div>

    <v-footer style="border:1px solid lightgray" app></v-footer>
  </div>
</template>

<script>
var _ = require("underscore");
export default {
  name: "dark-layout",
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
      console.log(modelName);
      this.$router.push({
        name: "crud",
        params: { resource: modelName }
      });
    }
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
</style>
