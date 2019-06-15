<template>
  <v-container fluid>
    
    <!--<v-btn color="primary" @click="login()">Login</v-btn>
    <v-btn color="primary" @click="register()">Register</v-btn>-->
    <v-layout row>
      <v-flex v-if="showForm">
        <v-card class="elevation-8">
          <!--<v-toolbar>
            <v-toolbar-title>{{ activeResource }}</v-toolbar-title>
          </v-toolbar>-->
          <v-card-text>
            <my-form :data="formData" :fields="form" @submit="submit" @cancel="cancel"></my-form>
          </v-card-text>
        </v-card>
      </v-flex>
    </v-layout>

    <v-flex v-if="!showForm" md12>
      <v-card>
        <v-card-title>
          <v-btn small color="primary" @click="createItem({})">
            <v-icon>add</v-icon>Add new
          </v-btn>
          <v-spacer></v-spacer>
        </v-card-title>

        <v-container>
          <v-layout row wrap justify-start v-if="true">
            <template v-for="filter in resourceSettings.filter">
              <v-flex :key="filter.field" md4>
                <v-select
                  v-if="filter.type==='select'"
                  :key="filter.field"
                  :items="getFilterOptions(filter.field)"
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
            <component v-bind:is="expandRow" :row="props.item"></component>
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
import DarkLayout from "./DarkLayout";
import MyForm from "./MyForm";

import { mapState, mapGetters, mapActions } from "vuex";

export default {
  name: "admin",
  data: function() {
    return {
      showForm: false,
      table: [],
      form: [],
      formData: {},
      search: {},
      appComponents: [],
      menu: false
    };
  },
  components: {
    DarkLayout,
    MyForm
  },
  computed: {
    ...mapState('resources', ['resource', 'settings', 'data']),
    ...mapGetters('resources', {
      resourceData: 'getResourceData',
      resourceSettings: 'getResourceSettings'
    }),
    customFooter: function() {
      return this.resourceSettings.footer;
    },
    customHeader: function() {
      return this.resourceSettings.header || false;
    },
    customBody: function() {
      return this.resourceSettings.items || false;
    },
    expandRow: function() {
      return this.resourceSettings.expandRow || false;
    },
    pageText: function() {
      return this.resourceSettings.pageText || false;
    },
    
    filteredItems: function() {
      var data = this.resourceData;
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
      this.showForm = false;
      this.setResource(resource);
      this.makeTable();
      this.fetchData(resource);
    }
  },
  methods: {
    ...mapActions('resources', [
      "setResource",
      "fetchData",
      "saveData"
    ]),    
    getFilterOptions: function(field) {
      console.log(this.resourceData);
      var filterOptions = this.resourceData.map(row => {
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
      this.buildForm(null);
    },
    editItem: function(item) {
      this.formData = item;
      this.buildForm(item);
    },

    submit(e) {
      e.originalEvent.preventDefault();
      let data = e.data;
     
      console.log(data);

      this.saveData(data);
      this.formData = {};
      this.showForm = false;
    },
    cancel() {
      this.showForm = false;
    },
    makeTable() {
      console.log(this.data);
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
        if (row) {
          let value =
            row[name] instanceof Array && prop.type === "pivotRelation"
              ? row[name].map(v => ({ value: v.id, text: v[prop.show] })) //.map(v => v.name)
              : row[name];
          row[name] = value;
        }
        field = {
          ...prop
        };

        if (prop.type == "relation" || prop.type == "pivotRelation") {
          this.fetchData(prop.resourceTable);
        }

        this.form.push(field);
      }
      this.showForm = true;
    }
  }
};
</script>

<style>
</style>
