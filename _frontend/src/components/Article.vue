<template>
  <page-sidebar-layout>
    <template slot="content-header" >{{ article ? article.title : "Clanky" }}</template>
    <template slot="content">
      <div v-if="article" v-html="article.content"></div>
      <div v-else>
          <div :key="article.id" v-for="article in articles">
              {{ article.title }}
          </div>
      </div>
    </template>
    <template slot="sidebar-header">Aktuality</template>
  </page-sidebar-layout>
</template>

<script>
import { mapState, mapActions } from "vuex";
import PageSidebarLayout from "./PageSidebarLayout";
import {BASE_URL, IMAGES_URL} from './../constants';

export default {
  name: "Article",
  components: {
    PageSidebarLayout
  },
  data: () => ({
    IMAGES_URL: IMAGES_URL
     
  }),
  computed: {
    ...mapState("resources", { resourceData: "data" }),
    articles: function() {
        return this.resourceData.articles;
    },
    article: function() {
        return this.resourceData.articles.find(article => article.id == this.$route.params.id);
    }
  },
  mounted() {
    this.fetchData("articles");
  },
  methods: {
    ...mapActions("resources", ["fetchData"])
  }
};
</script>