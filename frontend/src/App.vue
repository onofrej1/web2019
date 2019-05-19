<template>
  <v-app> 
    <dark-layout :models="this.models"></dark-layout>
  </v-app>
</template>

<script>
import CrudModels from "./CrudModels";
//import Admin from "./components/Admin.vue";
//import CrudAdmin from "./components/CrudAdmin.vue";
import DarkLayout from "./components/DarkLayout.vue";

export default {
  name: "app",
  data: () => ({
    models: CrudModels
  }),
  components: {
    DarkLayout
  }
};
</script>

<style>
</style>
