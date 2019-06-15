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
            <data-form :data="formData" :fields="form" @submit="submit" @cancel="cancel"></data-form>
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
        <data-table
          v-if="resourceSettings"
          :data="resourceData || []"
          :fields="list"
          :actions="actions"
          :filter="resourceSettings.filter"
          :header="resourceSettings.header"
          :footer="resourceSettings.footer"
          :body="resourceSettings.body"
          :expandRow="resourceSettings.expandRow"
        ></data-table>
      </v-card>
    </v-flex>
  </v-container>
</template>

<script>
import DataForm from "./DataForm";
import DataTable from "./DataTable";

import { mapState, mapGetters, mapActions } from "vuex";

export default {
  name: "admin",
  data: function() {
    return {
      showForm: false,
      list: [],
      form: [],
      formData: {},
      actions: [
        { icon: "edit", action: this.editItem },
        { icon: "delete", action: this.deleteItem }
      ]
    };
  },
  components: {
    DataForm,
    DataTable
  },
  computed: {
    ...mapState("resources", ["resource", "settings", "data"]),
    ...mapGetters("resources", {
      resourceData: "getResourceData",
      resourceSettings: "getResourceSettings"
    }),
    // zmaz
    pageText: function() {
      return this.resourceSettings.pageText || false;
    }
  },
  watch: {
    //$route(to, from) {
    $route() {
      let resource = this.$route.params.resource;
      this.showForm = false;
      this.setResource(resource);
      this.setList();
      this.fetchData(resource);
    }
  },
  methods: {
    ...mapActions("resources", ["setResource", "fetchData", "saveData"]),
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
          let value =
            row[name] instanceof Array && prop.type === "pivotRelation"
              ? row[name].map(v => ({ value: v.id, text: v[prop.show] })) //.map(v => v.name)
              : row[name];
          row[name] = value;
        }

        if (prop.type == "relation" || prop.type == "pivotRelation") {
          this.fetchData(prop.resourceTable);
        }
        this.form.push({ ...prop });
      }
      this.showForm = true;
    }
  }
};
</script>

<style>
</style>
