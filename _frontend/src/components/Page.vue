<template>
  <page-sidebar-layout>
    <template slot="content-header" v-if="page">{{ page.title }}</template>
    <template slot="content" v-if="page">
      <div v-html="page.content"></div>
      <img :src="IMAGES_URL+'m3.jpg'" width="100px" />
    </template>
    <template slot="sidebar-header">Aktuality</template>
  </page-sidebar-layout>
</template>

<script>
import { mapState, mapActions } from "vuex";
import PageSidebarLayout from "./PageSidebarLayout";
import {BASE_URL, IMAGES_URL} from './../constants';

export default {
  name: "Page",
  components: {
    PageSidebarLayout
  },
  data: () => ({
    IMAGES_URL: IMAGES_URL
     
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