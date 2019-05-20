<template>
  <v-container fluid>
    <!--<v-btn color="primary" @click="login()">Login</v-btn>
    <v-btn color="primary" @click="register()">Register</v-btn>-->
    <v-layout row>
      <v-flex v-if="activeForm">
        <v-card class="elevation-8">
          <!--<v-toolbar>
            <v-toolbar-title>{{ activeResource }}</v-toolbar-title>
          </v-toolbar>-->
          <v-card-text>
            <v-form>
              <template v-for="field in form">
                <v-text-field
                  :key="field.name"
                  v-if="field.type==='text'"
                  v-model="model[field.name]"
                  :label="field.label || field.name"
                  required
                ></v-text-field>

                <v-date-picker
                  v-if="field.type==='date'"
                  :key="field.name"
                  v-model="model[field.name]"
                  :landscape="landscape"
                  :reactive="reactive"
                ></v-date-picker>

                <v-select
                  v-if="field.type==='relation'"
                  :key="field.name"
                  :items="getOptions(field.resourceTable, field.show)"
                  v-model="model[field.name]"
                  :label="field.label || field.name"
                ></v-select>

                <template v-if="field.type==='pivotRelation'">
                  <template v-for="f in getOptions(field.resourceTable, field.show)">
                    <v-checkbox
                      :key="f.value"
                      v-model="model[field.name]"
                      :label="f.text"
                      :value="f.value"
                    ></v-checkbox>
                  </template>
                </template>

                <template v-if="field.type==='inline'">
                  <v-expansion-panel :key="field.name">
                    <v-expansion-panel-content :key="field.name" style="padding:10px">
                      <template v-slot:header>
                        <div>{{ field.label || field.name }}</div>
                      </template>

                      <template v-for="(f, index) in model[field.name]">
                        <div
                          class="grey lighten-4"
                          :key="field.name+'-'+index"
                          style="padding: 10px; border-top:4px solid grey !important"
                        >
                          <component
                            :key="field.name+'-'+index"
                            v-bind:is="field.form"
                            :model="model[field.name][index]"
                          ></component>
                        </div>
                        <div :key="field.name+'-d'+index" class="text-xs-right">
                          <v-btn flat @click="onDelete(model[field.name], index)">
                            <v-icon :key="'remove'+index" small>delete</v-icon>Remove
                          </v-btn>
                        </div>
                      </template>
                      <v-btn color="primary" @click="onAdd(field.name)">Add new</v-btn>
                    </v-expansion-panel-content>
                  </v-expansion-panel>
                </template>
              </template>
            </v-form>
          </v-card-text>

          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="primary" @click="onSubmit">Save</v-btn>
            <v-btn>Cancel</v-btn>
          </v-card-actions>
        </v-card>
      </v-flex>
    </v-layout>

    <v-flex v-if="!activeForm" md12>
      <v-card>
        <v-card-title>
          <v-btn small color="primary" @click="createItem({})">
            <v-icon>add</v-icon>Add new
          </v-btn>
          <v-spacer></v-spacer>
        </v-card-title>

        <v-container>
          <v-layout row wrap justify-start>
            <template v-for="filter in resourceSettings.filter">
              <v-flex :key="filter.field" md4>
                <v-select
                  v-if="filter.type==='select'"
                  :key="filter.field"
                  :items="getFilterOptions(activeResourceData, filter.field)"
                  v-model="search[filter.field]"
                  append-icon="search"
                  :label="filter.label"
                  style="width:90%"
                ></v-select>

                <v-text-field
                  v-if="filter.type==='text'"
                  :key="filter.field"
                  v-model="search[filter.field]"
                  append-icon="search"
                  :label="filter.label"
                  style="width:90%"
                ></v-text-field>
              </v-flex>
            </template>
          </v-layout>
        </v-container>

        <!--<router-link to="/crud/posts">Go to Foo</router-link>
        <router-link to="/crud/article">Go to Bar</router-link>-->

        <v-data-table
          v-if="resourceSettings"
          d-block
          :headers="table"
          :items="filteredItems"
          hide-actions
          class="elevation-1"
          v-model="abc"
        >
          <template v-slot:headers="props" v-if="customHeader">
            <component v-bind:is="customHeader" :props="props"></component>
          </template>

          <template v-slot:items="props" v-if="customBody">
            <component v-bind:is="customBody" :props="props"></component>
          </template>

          <template v-slot:items="props" v-else>
            <tr @click="props.expanded = !props.expanded">
              <td
                class="text-xs-right"
                :key="field.value"
                v-for="field in table"
              >{{ field.render ? field.render(props) : props.item[field.value] }}</td>
              <td class="text-xs-right">
                <v-icon color="primary" @click="editItem(props.item)">edit</v-icon>
                <v-icon color="red">delete</v-icon>
              </td>
            </tr>
          </template>

          <template v-slot:expand="props" v-if="expandRow">
            <component v-bind:is="expandRow"></component>
          </template>

          <template v-slot:pageText="props" v-if="pageText">
            <component v-bind:is="pageText"></component>
          </template>

          <template v-if="customFooter" #footer>
            <component v-bind:is="customFooter"></component>
          </template>
        </v-data-table>
      </v-card>
    </v-flex>
  </v-container>
</template>

<script>
import CrudModels from "./../CrudModels";
import DarkLayout from "./DarkLayout";

var _ = require("lodash");

import { mapState, mapActions } from "vuex";
//console.log(this.abc);

export default {
  name: "admin",
  data: function() {
    return {
      activeForm: false,
      models: CrudModels,
      table: [],
      form: [],
      model: {},
      search: {},
      appComponents: [],
      abc: []
    };
  },
  components: {
    DarkLayout
    //editor: Editor,
    //quillEditor
  },
  computed: {
    ...mapState(["activeResource", "resourceData"]),
    activeResourceData: function() {
      return this.resourceData[this.activeResource] || [];
    },
    resourceSettings: function() {
      return this.models[this.activeResource];
    },
    customFooter: function() {
      return this.resourceSettings.footer;
    },
    customHeader: function() {
      return this.resourceSettings.header || false;
    },
    expandRow: function() {
      return this.resourceSettings.expandRow || false;
    },
    pageText: function() {
      return this.resourceSettings.pageText || false;
    },
    customBody: function() {
      return this.resourceSettings.items || false;
    },
    filteredItems: function() {
      var data = this.activeResourceData;
      var search = this.search;
      if (!data) {
        return [];
      }
      this.resourceSettings.filter.forEach(function(filter) {
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
  watch: {
    //$route(to, from) {
    $route() {
      let resource = this.$route.params.resource;
      this.activeForm = false;
      this.setActiveResource(resource);
      this.makeTable();
      this.fetchResourceData(resource);
    }
  },
  methods: {
    ...mapActions([
      "register",
      "login",
      "setActiveResource",
      "fetchResourceData",
      "saveResourceData"
    ]),

    getOptions: function(resourceName, field) {
      let resource = this.resourceData[resourceName];
      let options =
        resource &&
        resource.map(data => {
          return {
            value: data.id,
            text: data[field]
          };
        });
      return options;
    },
    getFilterOptions: function(data, field) {
      var filterOptions = data.map(row => {
        return {
          value: row[field],
          text: row[field]
        };
      });
      filterOptions.unshift({ value: null, text: "" });
      return filterOptions;
    },
    createItem: function() {
      this.model = {};
      this.buildForm(null);
      this.activeForm = true;
    },
    editItem: function(item) {
      this.model = item;
      this.buildForm(item);
      this.activeForm = true;
    },
    onAdd: function(field) {
      if (this.model[field] === undefined) {
        //this.model[field] = [];
        this.model = { ...this.model, [field]: [] };
      }
      this.model[field].push({});
      //console.log('length'+this.model[field].length)
      console.log(JSON.stringify(this.model));
    },
    onDelete: function(data, index) {
      console.log("delete");
      data.splice(index, 1);
    },
    onSubmit(evt) {
      evt.preventDefault();
      console.log(JSON.stringify(this.model));
      //console.log(this.model.book_list[1].name);
      this.saveResourceData(this.model);
      console.log("resource:" + this.activeResource);
      this.fetchResourceData(this.activeResource);
      this.model = {};
      this.activeForm = false;
    },
    onReset() {
      this.activeForm = false;
    },
    makeTable() {
      this.table = [
        {
          text: "id",
          value: "id",
          align: "right"
        }
      ];
      for (let prop of this.resourceSettings.list) {
        this.table.push({
          render: prop.render,
          text: prop.label || prop.field,
          value: prop.field,
          align: "right"
        });
      }
      /*this.table.push({
                text: 'action',
                class: 'text-xs-right'
              });*/
    },
    buildForm(row) {
      this.form = [
        {
          name: "id",
          type: "hidden"
        }
      ];
      let field;

      for (let prop of this.resourceSettings.form) {
        let name = prop.name;
        /*if(prop.type === 'inline') {
          row[name] = []
        }*/
        if (row) {
          let value =
            row[name] instanceof Array && prop.type !== "inline"
              ? row[name].map(v => v.id)
              : row[name];
          row[name] = value;
          console.log(value);
        }
        field = {
          ...prop
        };

        if (prop.type == "relation" || prop.type == "pivotRelation") {
          this.fetchResourceData(prop.resourceTable);
        }

        this.form.push(field);
      }
      //console.log(this.form);
    }
  }
};
</script>

<style>
</style>
