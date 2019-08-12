<template>
  <page-sidebar-layout>
    <template slot="content-header">{{ article ? article.title : "Clanky" }}</template>
    <template slot="content">
      <template v-if="article">
        <div v-html="article.content"></div>
        <div class="mt-3">
          <v-btn to="/articles" small outlined>
            <v-icon dark>chevron_left</v-icon>Späť
          </v-btn>
        </div>
      </template>
      <div v-else>
        <template v-for="article in articles">
          <v-layout :key="article.id">
            <v-flex xs4 ma-4>
              <v-img
                :aspect-ratio="16/9"
                style="border:1px solid gray"
                src="https://cdn.vuetifyjs.com/images/parallax/material.jpg"
              ></v-img>
            </v-flex>
            <v-flex xs8 mt-4>
              <h3 style="border-bottom: 0.5px solid lightgray">{{ article.title }}</h3>
              <div class="mt-1 mb-2">
                <small>
                  <v-icon small>date_range</v-icon>
                  {{ formatDate(article.publishedOn) }}
                  <v-icon small>person</v-icon>
                  {{ article.publishedBy }}
                  <v-icon small>label</v-icon>
                  {{ article.category.category }}
                </small>
              </div>
              <div v-html="truncate(article.content)"></div>
              <div class="text-right mt-2">
                <v-btn :to="'/articles/'+article.id" small outlined>
                  Celý článok
                  <v-icon right dark>chevron_right</v-icon>
                </v-btn>
              </div>
            </v-flex>
          </v-layout>
        </template>
      </div>
    </template>
    <template slot="sidebar-header">Aktuality</template>
  </page-sidebar-layout>
</template>

<script>
import { mapState, mapActions } from "vuex";
import PageSidebarLayout from "./PageSidebarLayout";
import { BASE_URL, IMAGES_URL } from "./../constants";
import { formatDate, truncate } from "./../functions";

export default {
  name: "Article",
  components: {
    PageSidebarLayout
  },
  data: () => ({
    IMAGES_URL: IMAGES_URL,
    article: null
  }),
  computed: {
    ...mapState("resources", { resourceData: "data" }),
    articles: function() {
      return this.resourceData.articles;
    }
    /*article: function() {
      return this.resourceData.articles.find(
        article => article.id == this.$route.params.id
      );
    }*/
  },
  watch: {
    $route() {
      let id = this.$route.params.id;
      this.article = id
        ? this.resourceData.articles.find(a => a.id == id)
        : null;
    }
  },
  mounted() {
    this.fetchData("articles");
  },
  methods: {
    ...mapActions("resources", ["fetchData"]),
    formatDate: formatDate,
    truncate: truncate
  }
};
</script>