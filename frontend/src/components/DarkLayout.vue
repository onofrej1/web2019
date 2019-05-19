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
      <v-list dense class="pt-0">
        <template v-for="(item) in modelNames">
          <v-list-tile :key="item">
            <v-list-tile-action>
              <v-icon></v-icon>
            </v-list-tile-action>

            <v-list-tile-content>
              <v-list-tile-title @click="setModel(item)">{{ item }}</v-list-tile-title>
            </v-list-tile-content>
          </v-list-tile>
        </template>
      </v-list>
    </v-card>

    <div class="content">
      <router-view></router-view>

      <router-link to="/foo">Go to Foo</router-link>
      <router-link to="/bar">Go to Bar</router-link>
    </div>

    <v-footer style="border:1px solid lightgray" app></v-footer>
  </div>
</template>

<script>
export default {
  name: "dark-layout",
  data: () => ({
    drawer: true
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
      return Object.keys(this.models);
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
