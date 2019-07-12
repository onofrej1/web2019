<template>
  <v-app>
    <admin-layout v-if="layout === 'admin'" :models="this.models">
      <modal></modal>
    </admin-layout>
    <blank-layout v-if="layout == 'none'">
      <modal></modal>
    </blank-layout>
    <page-layout v-if="layout == null">
      <modal></modal>
    </page-layout>
  </v-app>
</template>

<script>
import ResourceSettings from "./ResourceSettings";
import AdminLayout from "./components/AdminLayout.vue";
import BlankLayout from "./components/BlankLayout.vue";
import PageLayout from "./components/PageLayout.vue";
import Modal from "./components/Modal";

export default {
  name: "app",
  data: () => ({
    models: ResourceSettings,
    isAdminPage: false,
    layout: null
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
      this.layout = this.$route.meta.layout;
      if (this.isAdminPage) {
        this.layout = "admin";
      }
    }
  },
  components: {
    AdminLayout,
    BlankLayout,
    PageLayout,
    Modal
  }
};
</script>

<style>
.box-heading {
  color: #fff;
  background-color: #222;
  font-weight: bold;
  font-size: 1.2em;
  height: 40px;
  padding: 16px 30px 30px 16px !important;
}

</style>
