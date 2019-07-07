<template>
  <page-sidebar-layout>
    <template slot="content-header">
      {{ page.title }}
    </template>
    <template slot="content">
     <div v-html="page.content"></div>
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
    ...mapState('resources', {resourceData: 'data'}),
    page: function() {
      return this.resourceData.pages.find(page => page.id == this.$route.params.id);
    }
  },
  mounted() {
    this.fetchData("pages");
  },
  methods: {
    ...mapActions('resources', ['fetchData'])
  }
};
</script>