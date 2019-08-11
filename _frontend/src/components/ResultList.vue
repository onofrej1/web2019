<template>
  <v-card>
    <v-layout>
      <v-flex xs4>
        <v-select
          v-model="event"
          class="ma-4"
          label="Event"
          :items="eventOptions"
          @change="getRuns()"
        ></v-select>
      </v-flex>
      <v-flex v-if="event" xs4>
        <v-select
          v-model="run"
          class="ma-4"
          label="Edition"
          :items="runOptions"
          @change="getResults()"
        ></v-select>
      </v-flex>
      <v-flex v-if="run">
        <p class="text-right ma-4">
          <v-chip class="ma-2">
            <v-icon left>sort</v-icon>
            {{ myItems.length }} items
          </v-chip>
        </p>
      </v-flex>
    </v-layout>

    <v-data-table
      v-if="event && run"
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
      event: null,
      run: null
    };
  },
  methods: {
    getResults: function() {
      localStorage.run = this.run;
      if (!this.run) return;

      this.fetch({
        resource: "results",
        url: "api/results/search/run?id=" + this.run
      });
    },
    getRuns: function() {
      localStorage.event = this.event;
      this.run = null;

      if (!this.event) return;

      this.fetch({
        resource: "runs",
        url: "api/runs/search/event?id=" + this.event
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
    eventOptions: function() {
      let emptyOption = { value: null, text: "" };

      return [emptyOption].concat(
        this.data.events.map(row => {
          return {
            value: row.id,
            text: row.name
          };
        })
      );
    },
    runOptions: function() {
      let emptyOption = { value: null, text: "" };

      return [emptyOption].concat(
        this.data.runs.map(row => {
          return {
            value: row.id,
            text: row.edition + ". rocnik"
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
    this.event = parseInt(localStorage.event);
    this.run = parseInt(localStorage.run);

    if (!this.data.events.length) {
      this.fetch({ resource: "events", url: "api/events" });
    }
  }
};
</script>
