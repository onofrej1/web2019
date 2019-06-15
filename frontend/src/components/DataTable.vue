
<template>
  <div>
    <v-container>
      <v-layout row wrap justify-start v-if="true">
        <template v-for="f in filter">
          <v-flex :key="f.field" md4>
            <v-select
              v-if="f.type==='select'"
              :key="f.field"
              :items="getFilterOptions(f.field)"
              v-model="search[f.field]"
              append-icon="search"
              :label="filter.label"
              style="width:90%"
            ></v-select>

            <v-text-field
              v-if="f.type==='text'"
              :key="f.field"
              v-model="search[f.field]"
              append-icon="search"
              :label="f.label"
              style="width:90%"
            ></v-text-field>
          </v-flex>
        </template>
      </v-layout>
    </v-container>

    <v-data-table
      v-if="fields"
      d-block
      :headers="fields"
      :items="items"
      hide-actions
      class="elevation-1"
    >
      <template v-slot:headers="props" v-if="header">
        <component v-bind:is="header" :props="props"></component>
      </template>

      <template v-slot:items="props" v-if="body">
        <component v-bind:is="body" :props="props"></component>
      </template>

      <template v-if="footer" #footer>
        <component v-bind:is="footer"></component>
      </template>

      <template v-slot:pageText="props" v-if="pageText">
        <component v-bind:is="pageText"></component>
      </template>

      <template v-slot:items="props" v-else>
        <tr @click="props.expanded = !props.expanded">
          <td class="text-xs-right" :key="field.value" v-for="field in fields">
            <span v-html="field.render ? field.render(props) : props.item[field.value]"></span>
          </td>
          <td class="text-xs-right">
            <span
              class="action"
              :key="JSON.stringify(action)"
              @click="action.action(props.item)"
              v-for="action in actions"
            >
              <v-icon v-if="action.icon" color="primary">{{ action.icon }}</v-icon>
              {{ action.label || '' }}
            </span>
          </td>
        </tr>
      </template>

      <template v-slot:expand="props" v-if="expandRow">
        <component v-bind:is="expandRow" :row="props.item"></component>
      </template>
    </v-data-table>
  </div>
</template>

<script>
export default {
  name: "data-table",
  data: function() {
    return {
      search: {}
    };
  },
  props: {
    data: Array,
    fields: Array,
    filter: Array,
    actions: Array,
    footer: Object,
    header: Object,
    body: Object,
    expandRow: Object,
    pageText: Object
  },
  components: {},
  computed: {
    items: function() {
      var data = this.data;
      var search = this.search;
      if (!data) {
        return [];
      }
      this.filter.forEach(function(filter) {
        data = data.filter(item => {
          var searchValue = search[filter.field];
          if (!searchValue) {
            return true;
          }
          var value = item[filter.field];
          if (filter.op === "contains") {
            return value.includes(searchValue);
          }

          return value === searchValue;
        });
      });
      return data;
    }
  },
  methods: {
    getFilterOptions: function(field) {
      var filterOptions = this.data.map(row => {
        return {
          value: row[field],
          text: row[field]
        };
      });
      filterOptions.unshift({ value: null, text: "" });
      return filterOptions;
    }
  }
};
</script>
<style>
.action {
  cursor: pointer;
}
</style>
