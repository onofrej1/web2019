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
          <h4>{{ capitalize(resource.title) }} list</h4>
          <v-layout row justify-end>
            <v-btn v-if="resource.actions.includes('create')" small color="primary" @click="createItem({})">
              <v-icon>add</v-icon>Create
            </v-btn>
            <v-menu v-if="resource.actions.includes('filter')" offset-y>
              <template v-slot:activator="{ on }">
                <v-btn small color="primary" v-on="on" :disabled="filter.length == 0">
                  <v-icon>filter_list</v-icon>Add filter
                </v-btn>
              </template>
              <v-list>
                <v-list-item v-for="(item, index) in filter" :key="index" @click="addFilter(item)">
                  <v-list-item-title>{{ item.field }}</v-list-item-title>
                </v-list-item>
              </v-list>
            </v-menu>
            <v-btn v-if="resource.actions.includes('refresh')" small color="primary" @click="createItem({})">
              <v-icon>refresh</v-icon>Refresh
            </v-btn>
          </v-layout>

          <v-container>
            <v-layout row justify-end v-if="activeFilters">
              <template v-for="(f, index) in activeFilters">
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
          <component v-bind:is="resource.listView" :fetch="fetchCustomData" :items="items" :actions="actions"></component>
        </template>

        <v-data-table
          v-if="list && status!='loading' && !resource.listView"
          d-block
          :sort-by.sync="sortBy"
          :sort-desc.sync="descending"
          :single-expand="false"
          :expanded.sync="expanded"
          :headers="list"
          v-model="selected"
          :items="items"
          item-key="id"
          class="elevation-1"
          :show-expand="resource.expandRow ? true : false"
          :show-select="resource.bulkActions ? true : false"
        >
          <template v-slot:footer v-if="resource.footer">
            <component v-bind:is="resource.footer"></component>
          </template>

          <template v-slot:top v-if="pageText">
            <component v-bind:is="pageText"></component>
          </template>

          <template
            v-for="field in list"
            v-slot:[getSlotItemName(field)]="{ item, props, headers }"
          >
            <span
              :key="field.name"
              v-html="field.render ? field.render(item, props) : item[field.value]"
            ></span>
          </template>

          <template v-slot:item.actions="{ item }">
            <span class="text-no-wrap">
              <v-icon @click="editItem(item)">edit</v-icon>
              <v-icon @click="deleteItem(item)">delete</v-icon>
            </span>
          </template>

          <template v-slot:body="props" v-if="resource.body">
            <component v-bind:is="resource.body" :props="props"></component>
          </template>

          <template v-slot:expanded-item="props">
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
      activeFilters: [],
      search: {},
      actions: {
        edit: this.editItem,
        delete: this.deleteItem
      },
      sortBy: "name",
      descending: false,
      expanded: [],
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
      return this.resource.filter.filter(f => !this.activeFilters.includes(f));
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
    capitalize: capitalize,
    ...mapActions("resources", [
      "setResource",
      "fetchData",
      "fetchCustomData",
      "saveResource",
      "deleteResource"
    ]),
    init: function() {
      let resource = this.$route.params.resource;
      this.showForm = false;
      this.setResource(resource);
      this.setList();
      if(this.resource.fetch) {
        this.fetchData(resource);
      }
    },
    getSlotItemName(field) {
      return "item." + field.value;
    },
    addFilter: function(filter) {
      this.activeFilters.push(filter);
    },
    removeFilter: function(index) {
      this.activeFilters.splice(index, 1);
    },
    getFilterOptions: function(field) {
      let emptyOption = { value: null, text: "" };

      return [emptyOption].concat(
        this.resourceData.map(row => {
          return {
            value: row[field],
            text: row[field]
          };
        })
      );
    },
    createItem: function() {
      this.formData = {};
      this.setForm();
    },
    editItem: function(item) {
      this.setForm();

      let data = {};
      //console.log(item);
      this.form.forEach(field => {
        //console.log(item[field.name]);
        //data[field.name] = item[field.name];
      });
      //console.log(data);
      //return;
      this.formData = item;
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
      this.list.push({ text: "Actions", value: "actions" });
    },
    setForm() {
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
    
  }
};
</script>

<style>
</style>
