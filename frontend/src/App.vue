<template>
  <v-app> 
    <dark-layout v-if="layout === 'dark'" :models="this.models"></dark-layout>
    <blank-layout v-if="layout == null"></blank-layout>
  </v-app>
</template>

<script>
import CrudModels from "./CrudModels";
//import Admin from "./components/Admin.vue";
//import CrudAdmin from "./components/CrudAdmin.vue";
import DarkLayout from "./components/DarkLayout.vue";
import BlankLayout from "./components/BlankLayout.vue";


export default {
  name: "app",
  data: () => ({
    models: CrudModels,
    isAdminPage: false,
    layout: null,
  }),
  watch: {
    //$route(to, from) {
    $route() {
      this.isAdminPage = this.$route.meta.adminPage;
      if(this.isAdminPage) {
        this.layout = 'dark';
      } else {
        this.layout = null;
      }
      //console.log(this.isAdminPage);
    }
  },
  components: {
    DarkLayout,
    BlankLayout,
  }
};
</script>

<style>
</style>
