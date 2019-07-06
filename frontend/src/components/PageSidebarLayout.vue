<template>
  <v-container>
    <v-layout >
      <v-flex xs8 class="mr-3">
        <v-card>
          <v-card-title class="box-heading">
            <slot name="content-header"></slot>
          </v-card-title>
          <v-card-text>
            <slot name="content">bbb</slot>
          </v-card-text>    
        </v-card>
      </v-flex>
      <v-flex xs4>
        <v-card>
          <v-card-title class="box-heading">
            <slot name="sidebar-header"></slot>
          </v-card-title>
          <v-card-text>
            <slot name="sidebar-content">bbb</slot>
          </v-card-text> 
        </v-card>
      </v-flex>
    </v-layout>
  </v-container>
</template>

<script>
export default {
  name: "PageSidebarLayout"
};
</script>