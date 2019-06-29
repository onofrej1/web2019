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
          <h2>{{ capitalize(resource) }} list</h2>
          <v-layout row justify-end>
            <v-btn small color="primary" @click="createItem({})">
              <v-icon>add</v-icon> Create
            </v-btn>
            <v-menu offset-y>
              <template v-slot:activator="{ on }">
                <v-btn small color="primary" dark v-on="on">
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
        <v-data-table
          v-if="list"
          d-block
          :headers="list"
          :items="items"
          hide-actions
          class="elevation-1"
        >
          <template v-slot:headers="props" v-if="resourceSettings.header">
            <component v-bind:is="resourceSettings.header" :props="props"></component>
          </template>

          <template v-slot:items="props" v-if="resourceSettings.body">
            <component v-bind:is="resourceSettings.body" :props="props"></component>
          </template>

          <template v-if="resourceSettings.footer" #footer>
            <component v-bind:is="resourceSettings.footer"></component>
          </template>

          <template v-slot:pageText="props" v-if="pageText">
            <component v-bind:is="pageText"></component>
          </template>

          <template v-slot:items="props" v-else>
            <tr @click="props.expanded = !props.expanded">
              <td class="text-xs-right" :key="field.value" v-for="field in list">
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

          <template v-slot:expand="props" v-if="resourceSettings.expandRow">
            <component v-bind:is="resourceSettings.expandRow" :row="props.item"></component>
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
      actions: [
        { icon: "edit", action: this.editItem },
        { icon: "delete", action: this.deleteItem }
      ]
    };
  },
  components: {
    DataForm
  },
  mounted() {
    this.init();
  },
  computed: {
    ...mapState("resources", ["resource"]),
    ...mapGetters("resources", {
      resourceData: "getResourceData",
      resourceSettings: "getResourceSettings"
    }),
    items: function() {
      let data = this.resourceData;
      console.log(data);
      let search = this.search;
      if (!data) {
        return [];
      }
      this.resourceSettings.filter.forEach(function(filter) {
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
      return this.resourceSettings.filter;
    }, 
    // zmaz
    pageText: function() {
      return this.resourceSettings.pageText || false;
    }
  },
  watch: {
    //$route(to, from) {
    $route() {
      this.init();
    }
  },
  methods: {
    ...mapActions("resources", ["setResource", "fetchData", "saveData"]),
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
      console.log("delete"); //todo
    },
    submit(e) {
      e.originalEvent.preventDefault();
      console.log(e.data);

      this.saveData(e.data);
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
      for (let prop of this.resourceSettings.list) {
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

      for (let prop of this.resourceSettings.form) {
        let name = prop.name;
        if (row) {
          let value = row[name];
          if (row[name] instanceof Array && prop.type === "pivotRelation") {
            row[name] = row[name].map(v => ({
              value: v.id,
              text: v[prop.show]
            }));
          } else if (prop.type === "relation") {
            row[name] = { value: value.id, text: value[prop.show] };
          }
        }

        if (prop.type == "relation" || prop.type == "pivotRelation") {
          this.fetchData(prop.resourceTable);
        }
        this.form.push({ ...prop });
      }
      //console.log(this.form);
      this.showForm = true;
    }
  }
};
</script>

<style>
</style>
