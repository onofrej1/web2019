<template>
  <page-sidebar-layout>
    <template slot="content-header" v-if="page">{{ page.title }}</template>
    <template slot="content" v-if="page">
      <div v-html="page.content"></div>
    </template>
    <template slot="sidebar-header">Aktuality</template>
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
  data: () => ({
      //page: null,
  }),
  computed: {
    ...mapState("resources", { resourceData: "data" }),
    page: function() {
      return this.resourceData.pages.find(page => page.id == this.$route.params.id);
    }
  },
  mounted() {
    this.fetchData("pages");
  },
  methods: {
    ...mapActions("resources", ["fetchData"])
  }
};
</script>