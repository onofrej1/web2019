<template>
  <v-container>
    <v-layout >
      <v-flex xs8 class="mr-3">
        <v-card>
          <v-card-title class="box-heading">
            <slot name="content-header"></slot>
          </v-card-title>
          <v-card-text class="pt-5">
            <slot name="content"></slot>
          </v-card-text>    
        </v-card>
      </v-flex>
      <v-flex xs4>
        <v-card>
          <v-card-title class="box-heading">
            <slot name="sidebar-header"></slot>
          </v-card-title>
          <v-card-text class="pt-5">
            <slot name="sidebar-content">
              <div :key="item.id" v-for="item in news">
                <v-icon>date_range</v-icon>
                <strong>{{ moment(item.publishedOn).format('DD/MM/YYYY') }}</strong>
                <p style="padding-left: 5px; margin-top: 3px">
                  <span v-html="item.content"></span>
                </p>              
              </div>
            </slot>
          </v-card-text> 
        </v-card>
      </v-flex>
    </v-layout>
  </v-container>
</template>

<script>
import { mapState, mapActions } from "vuex";
import moment from 'moment';

export default {
  name: "PageSidebarLayout",
  data: () => ({
    moment: moment
  }),
  components: {
    
  },
  computed: {
    ...mapState("resources", { resources: "data" }),
    news: function() {
      return this.resources.news;
    }
  },
  methods: {
    ...mapActions("resources", ["getResource"]),
  },
  mounted() {
    this.getResource({resource: 'news'});
  }
};
</script>