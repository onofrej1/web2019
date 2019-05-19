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
        <template v-for="(item) in items">
          <v-list-tile :key="item.title">
            <v-list-tile-action>
              <v-icon>{{ item.icon }}</v-icon>
            </v-list-tile-action>

            <v-list-tile-content>
              <v-list-tile-title>{{ item.title }}</v-list-tile-title>
            </v-list-tile-content>
          </v-list-tile>
          <v-divider :key="item.id"></v-divider>
        </template>
      </v-list>
    </v-card>

    <div class="content">
      <div :key="item.id" v-for="item in data">{{ item.id }}</div>

      <router-link to="/foo">Go to Foo</router-link>
      <router-link to="/bar">Go to Bar</router-link>
      name{{ meno }}
      <router-view></router-view>
    </div>

    <v-footer style="border:1px solid lightgray" app></v-footer>
  </div>
</template>

<script>
import { mapState, mapActions } from "vuex";

export default {
  name: "Admin",
  data() {
    return {
      items: [
        { title: "Home", icon: "dashboard" },
        { title: "About", icon: "question_answer" }
      ],
      right: null,
      results: [],
    };
  },
  computed: {
    ...mapState('crud', ["data", "meno"])
  },
  methods: {
    //...mapActions('crud')
  },
  mounted() {
    this.$store.dispatch('crud/fetchData');
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
