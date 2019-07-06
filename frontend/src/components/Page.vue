<template>
  <page-sidebar-layout>
    <template slot="content-header">
      Page
    </template>
    <template slot="content">
      <div v-html="page.body"></div>
    </template>
    <template slot="sidebar-header">
      Aktuality
    </template>
  </page-sidebar-layout>
</template>

<script>
import { mapState, mapActions } from "vuex";
import PageSidebarLayout from "./PageSidebarLayout";
export default {
  name: "Page",
  components: {
    PageSidebarLayout
  },
  computed: {
    ...mapState('resources', ["data"]),
    page: function() {
      if (!this.data.page) {
        return {};
      }
      let id = this.$route.params.id;
      return this.data.page.find(page => page.id == id);
    }
  },
  created() {
    //this.fetchResourceData("page");
  },
  methods: {
    //...mapActions(["fetchResourceData"])
  }
};
</script>