<template>
  <v-card>
    <v-layout>
      <v-flex xs4>
        <v-select v-model="run" class="ma-2" label="run" :items="runOptions" @change="getResults()"></v-select>
      </v-flex>
    </v-layout>

    <v-data-table
      d-block
      :single-expand="false"
      :headers="header"
      :items="myItems"
      item-key="id"
      class="elevation-1"
    >
      <template v-slot:item.name="{ item, props }">
        <span>{{ item.runner.lastName }} {{ item.runner.firstName }}</span>
      </template>
      <template v-slot:item.actions="{ item }">
        <span class="text-no-wrap">
          <v-icon @click="actions.edit(item)">edit</v-icon>
          <v-icon @click="actions.delete(item)">delete</v-icon>
        </span>
      </template>
    </v-data-table>
  </v-card>
</template>

<script>
import { mapGetters, mapState } from "vuex";

export default {
  name: "ResultList",
  props: {
    items: Array,
    actions: Object,
    fetch: Function
  },
  data: function() {
    return {
      run: ""
    };
  },
  methods: {
    getResults: function() {
      this.fetch({
        resource: "results",
        url: "api/results/search/run?id=" + this.run
      });
    }
  },
  computed: {
    ...mapGetters("resources", {
      resourceData: "getResourceData",
      resource: "getResourceSettings"
    }),
    ...mapState("resources", ["data"]),
    myItems: function() {
      return this.resourceData;
    },
    runOptions: function() {
      let emptyOption = { value: null, text: "" };

      return [emptyOption].concat(
        this.data.runs.map(row => {
          return {
            value: row.id,
            text: row.name
          };
        })
      );
    },
    header() {
      let header = [];
      for (let prop of this.resource.list) {
        header.push({
          render: prop.render,
          text: prop.label || prop.field,
          value: prop.field,
          align: "right"
        });
      }
      header.push({ text: "Actions", value: "actions" });
      return header;
    }
  },
  mounted() {
    this.fetch({ resource: "runs", url: "api/runs" });
  }
};
</script>
