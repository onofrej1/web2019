<template>
  <v-container fluid>
    <v-layout row>
      <v-flex v-if="showForm">
        <v-card class="elevation-8">
          <!--<v-toolbar>
            <v-toolbar-title>{{ activeResource }}</v-toolbar-title>
          </v-toolbar>-->
          <v-card-text>
            <data-form :data="formData" :fields="form" @submit="submit" @cancel="cancel"></data-form>
          </v-card-text>
        </v-card>
      </v-flex>
    </v-layout>

    <v-flex v-if="!showForm" md12>
      <v-card>
        <v-card-title>
          <h2>{{ capitalize(resource.title) }} list</h2>
          <v-layout row justify-end>
            <v-btn small color="primary" @click="createItem({})">
              <v-icon>add</v-icon>Create
            </v-btn>
            <v-menu offset-y>
              <template v-slot:activator="{ on }">
                <v-btn small color="primary" v-on="on" :disabled="filter.length == 0">
                  <v-icon>filter_list</v-icon>Add filter
                </v-btn>
              </template>
              <v-list>
                <v-list-tile v-for="(item, index) in filter" :key="index" @click="addFilter(item)">
                  <v-list-tile-title>{{ item.field }}</v-list-tile-title>
                </v-list-tile>
              </v-list>
            </v-menu>
            <v-btn small color="primary" @click="createItem({})">
              <v-icon>refresh</v-icon>Refresh
            </v-btn>
          </v-layout>

          <v-container>
            <v-layout row justify-end v-if="selectedFilters">
              <template v-for="(f, index) in selectedFilters">
                <div :key="f.field">
                  <v-layout justify-end align-end row :key="f.field">
                    <v-flex xs2 style="position:relative;bottom:15px">
                      <v-icon @click="removeFilter(index)">cancel</v-icon>
                    </v-flex>
                    <v-flex xs10>
                      <v-select
                        v-if="f.type==='select'"
                        :key="f.field"
                        dense
                        :items="getFilterOptions(f.field)"
                        v-model="search[f.field]"
                        append-icon="search"
                        :label="f.label"
                        :placeholder="f.field || f.label"
                        style="width:90%"
                      ></v-select>

                      <v-text-field
                        v-if="f.type==='text'"
                        :key="f.field"
                        v-model="search[f.field]"
                        append-icon="search"
                        :label="f.label"
                        :placeholder="f.field || f.label"
                        style="width:90%"
                      ></v-text-field>
                    </v-flex>
                  </v-layout>
                </div>
              </template>
            </v-layout>
          </v-container>
        </v-card-title>

        <template v-if="resource.listView">
          <component v-bind:is="resource.listView" :items="items" :actions="actions"></component>
        </template>

        <v-data-table
          v-if="list && status!='loading' && !resource.listView"
          d-block
          :pagination.sync="pagination"
          :headers="list"
          v-model="selected"
          :items="items"
          :rows-per-page-items="[10,25,{text: 'all', value:-1}]"
          class="elevation-1"
        >
          <template v-slot:headers="props" v-if="!resource.header">
            <tr>              
              <th v-if="resource.bulkActions">
                <v-checkbox
                  :input-value="props.all"
                  :indeterminate="props.indeterminate"
                  primary
                  hide-details
                  @click.stop="toggleAll"
                ></v-checkbox>
              </th>
              <th v-if="resource.expandRow">More</th>
              <th
                v-for="header in props.headers"
                :key="header.text"
                :class="['text-xs-right column sortable', pagination.descending ? 'desc' : 'asc', header.value === pagination.sortBy ? 'active' : '']"
                @click="changeSort(header.value)"
              >
                <v-icon small>arrow_upward</v-icon>
                {{ header.text }}
              </th>
              <th class="text-xs-right">
                Actions
              </th>              
            </tr>
          </template>

          <template v-slot:items="props" v-if="resource.header">
            <component v-bind:is="resource.header" :props="props"></component>
          </template>

          <template v-slot:items="props" v-if="resource.body">
            <component v-bind:is="resource.body" :props="props"></component>
          </template>

          <template v-if="resource.footer">
            <component v-bind:is="resource.footer"></component>
          </template>

          <template v-slot:pageText="props" v-if="pageText">
            <component v-bind:is="pageText"></component>
          </template>

          <template v-slot:items="props" v-else>
            <tr>
              <td @click="props.selected = !props.selected" v-if="resource.bulkActions">
                <v-checkbox :input-value="props.selected" primary hide-details></v-checkbox>
              </td>
              <th v-if="resource.expandRow" @click="props.expanded = !props.expanded">
                <v-icon>info</v-icon>
              </th>              
              <td class="text-xs-right" :key="field.value" v-for="field in list">
                <span v-html="field.render ? field.render(props) : props.item[field.value]"></span>
              </td>
              <td class="text-xs-right">
                <v-icon @click="editItem(props.item)">edit</v-icon>
                <v-icon @click="deleteItem(props.item)">delete</v-icon>
              </td>
            </tr>
          </template>

          <template v-slot:expand="props" v-if="resource.expandRow">
            <component v-bind:is="resource.expandRow" :row="props.item"></component>
          </template>
        </v-data-table>
      </v-card>
    </v-flex>
  </v-container>
</template>

<script>
import DataForm from "./DataForm";
import { capitalize } from "./../functions";
import { mapState, mapGetters, mapActions } from "vuex";

export default {
  name: "admin",
  data: function() {
    return {
      showForm: false,
      list: [],
      form: [],
      formData: {},
      selectedFilters: [],
      search: {},
      actions: {
        edit: this.editItem,
        delete: this.deleteItem
      },
      pagination: {
        sortBy: "name"
      },
      selected: []
    };
  },
  components: {
    DataForm
  },
  mounted() {
    this.init();
  },
  computed: {
    ...mapState("resources", ["status"]),
    ...mapGetters("resources", {
      resourceData: "getResourceData",
      resource: "getResourceSettings"
    }),
    items: function() {
      let data = this.resourceData;
      //console.log(data);
      let search = this.search;
      if (!data) {
        return [];
      }
      this.resource.filter.forEach(function(filter) {
        data = data.filter(item => {
          let searchValue = search[filter.field];
          if (!searchValue) {
            return true;
          }
          let value = item[filter.field];
          if (filter.op === "contains") {
            return value.includes(searchValue);
          }

          return value === searchValue;
        });
      });
      return data;
    },
    filter: function() {
      return this.resource.filter.filter(
        f => !this.selectedFilters.includes(f)
      );
    },
    // zmaz
    pageText: function() {
      return this.resource.pageText || false;
    }
  },
  watch: {
    //$route(to, from) {
    $route() {
      this.init();
    }
  },
  methods: {
    ...mapActions("resources", [
      "setResource",
      "fetchData",
      "saveResource",
      "deleteResource"
    ]),
    init: function() {
      let resource = this.$route.params.resource;
      this.showForm = false;
      this.setResource(resource);
      this.setList();
      this.fetchData(resource);
    },
    addFilter: function(filter) {
      this.selectedFilters.push(filter);
    },
    removeFilter: function(index) {
      this.selectedFilters.splice(index, 1);
    },
    capitalize: capitalize,
    getFilterOptions: function(field) {
      let filterOptions = this.resourceData.map(row => {
        return {
          value: row[field],
          text: row[field]
        };
      });
      filterOptions.unshift({ value: null, text: "" });
      return filterOptions;
    },
    createItem: function() {
      this.formData = {};
      this.setForm(null);
    },
    editItem: function(item) {
      this.formData = item;
      this.setForm(item);
    },
    deleteItem: function(item) {
      if (window.confirm("Realy delete ?")) {
        this.deleteResource(item.id);
      }
    },
    submit(e) {
      e.originalEvent.preventDefault();

      this.saveResource(e.data);
      this.formData = {};
      this.showForm = false;
    },
    cancel() {
      this.showForm = false;
    },    
    setList() {
      this.list = [
        {
          text: "id",
          value: "id",
          align: "right"
        }
      ];
      for (let prop of this.resource.list) {
        this.list.push({
          render: prop.render,
          text: prop.label || prop.field,
          value: prop.field,
          align: "right"
        });
      }
    },
    setForm(row) {
      this.form = [
        {
          name: "id",
          type: "hidden"
        }
      ];

      for (let prop of this.resource.form) {
        this.form.push({ ...prop });
      }
      this.showForm = true;
    },
    toggleAll() {
      if (this.selected.length) {
        this.selected = [];
      } else {
        this.selected = this.items.slice();
      } 
    },
    changeSort(column) {
      if (this.pagination.sortBy === column) {
        this.pagination.descending = !this.pagination.descending;
      } else {
        this.pagination.sortBy = column;
        this.pagination.descending = false;
      }
    }
  }
};
</script>

<style>
</style>
