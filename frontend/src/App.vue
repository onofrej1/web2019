<template>
  <v-app> 
    <admin-layout v-if="layout === 'admin'" :models="this.models"></admin-layout>
    <blank-layout v-if="layout == null"></blank-layout>
  </v-app>
</template>

<script>
import ResourceSettings from "./ResourceSettings";
import AdminLayout from "./components/AdminLayout.vue";
import BlankLayout from "./components/BlankLayout.vue";


export default {
  name: "app",
  data: () => ({
    models: ResourceSettings,
    isAdminPage: false,
    layout: null,
  }),
  mounted() {
    this.init();
  },
  watch: {
    $route() {
      this.init();
    }
  },
  methods: {
    init: function() {
      this.isAdminPage = this.$route.meta.adminPage;
      if(this.isAdminPage) {
        this.layout = 'admin';
      } else {
        this.layout = null;
      }
    }
  },
  components: {
    AdminLayout,
    BlankLayout,
  }
};
</script>

<style>
</style>
