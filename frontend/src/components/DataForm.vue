
<template>
  <v-form>
    <v-container>
      <v-layout row wrap>
        <template v-for="field in fields">
          <v-flex :key="field.name" :[getFlexAttribute(field)]="true">
            <v-text-field
              v-if="field.type=='text'"
              v-model="data[field.name]"
              v-bind="getProps(field)"
              v-validate="field.validate"
              :type="field.inputType || 'text'"
            ></v-text-field>

            <v-textarea
              v-if="field.type=='textarea'"
              v-model="data[field.name]"
              v-bind="getProps(field)"
              v-validate="field.validate"
            ></v-textarea>

            <div v-if="field.type=='editor'">
              <label>{{ field.label || field.name }}</label>
              <text-editor
                :data="data[field.name]"
                @input="data[field.name] = $event"
                v-bind="getProps(field)"
              />
            </div>

            <v-menu
              v-if="field.type==='date'"
              :key="field.name"
              v-model="menu[field.name]"
              :close-on-content-click="false"
              :nudge-right="40"
              lazy
              transition="scale-transition"
              offset-y
              full-width
              min-width="290px"
            >
              <template v-slot:activator="{ on }">
                <v-text-field
                  v-if="field.type=='date'"
                  v-model="data[field.name]"
                  v-bind="getProps(field)"
                  v-validate="field.validate"
                  prepend-icon="event"
                  readonly
                  v-on="on"
                ></v-text-field>
              </template>
              <v-date-picker
                locale="en-EN"
                :key="field.name"
                v-model="data[field.name]"
                @input="menu[field.name] = false"
              ></v-date-picker>
            </v-menu>

            <v-select
              v-if="field.type=='relation'"
              v-model="data[field.name]"
              v-bind="getProps(field)"
              v-validate="field.validate"
              :items="getOptions(field.resourceTable, field.show)"
            ></v-select>

            <v-combobox
              v-if="field.type=='pivotRelation'"
              v-model="data[field.name]"
              v-bind="getProps(field)"
              v-validate="field.validate"
              :items="getOptions(field.resourceTable, field.show)"
              item-text="text"
              item-value="text"
              multiple
            >
              <template v-slot:selection="data">
                <v-chip
                  :key="JSON.stringify(data.item)"
                  :selected="data.selected"
                  :disabled="data.disabled"
                  class="v-chip--select-multi"
                  @input="data.parent.selectItem(data.item)"
                >{{ data.item && data.item.text }}</v-chip>
              </template>
            </v-combobox>

            <template v-if="field.type==='inline'">
              <inline-input :key="field.name" :data="data[field.name]" :field="field"></inline-input>
            </template>
          </v-flex>
        </template>
      </v-layout>
    </v-container>
    <v-spacer></v-spacer>
    <template v-if="actions">
      <component v-bind:is="actions" :submit="submit" :cancel="cancel"></component>
    </template>
    <template v-else>
      <v-btn color="primary" @click="submit">Save</v-btn>
      <v-btn @click="cancel">Cancel</v-btn>
    </template>
  </v-form>
</template>

<script>
import InlineInput from "./InlineInput";
//import ClassicEditor from '@ckeditor/ckeditor5-build-classic';
import TextEditor from "./TextEditor";
import { mapState, mapActions } from "vuex";
const moment = require("moment");

export default {
  name: "data-form",
  props: {
    data: Object,
    fields: Array,
    actions: Object
  },
  data: function() {
    return {
      menu: {}
      //ClassicEditor: ClassicEditor
    };
  },
  mounted() {
    this.pivotRelations.forEach(relation => {
      this.fetchData(relation.resourceTable);
      if (!this.data[relation.name]) return;

      this.data[relation.name] = this.data[relation.name].map(v => ({
        value: v.id,
        text: v[relation.show]
      }));
    });
    this.relations.forEach(relation => {
      this.fetchData(relation.resourceTable);
      if (!this.data[relation.name]) return;

      this.data[relation.name] = {
        value: this.data[relation.name].id,
        text: this.data[relation.name][relation.show]
      };
    });
  },
  components: {
    InlineInput,
    TextEditor
  },
  computed: {
    ...mapState("resources", { resources: "data" }),
    formatDate: value => moment(value, "YYYY-MM-DD"),
    pivotRelations: function() {
      return this.fields.filter(field => field.type == "pivotRelation");
    },
    relations: function() {
      return this.fields.filter(field => field.type == "relation");
    }
  },
  methods: {
    ...mapActions("resources", ["fetchData"]),
    getOptions: function(resource, field) {
      let emptyOption = { value: null, text: "" };

      return [emptyOption].concat(
        this.resources[resource].map(data => ({
          value: data.id,
          text: data[field]
        }))
      );
    },
    getProps: function(field) {
      let customProps = field.props;
      let commonProps = {
        ref: field.name,
        key: field.name,
        name: field.name,
        label: field.label || field.name,
        "data-vv-name": field.name,
        "error-messages": this.errors.collect(field.name)
      };
      return { ...commonProps, ...customProps };
    },
    getFlexAttribute(field) {
      return field.flex || "xs12 sm12 md12 lg12";
    },
    submit: function(e) {
      let data = this.data;
      console.log(data);
      delete data["_links"];

      this.$validator.validateAll().then(valid => {
        if (valid) {
          this.pivotRelations.forEach(relation => {
            data[relation.name] = data[relation.name].map(v => v.value);
          });
          this.relations.forEach(relation => {
            let value = data[relation.name];
            data[relation.name] =
              isNaN(value) && value !== undefined ? value.value : value;
          });
          this.$emit("submit", { data: data, originalEvent: e });
        }
      });
    },
    cancel: function() {
      this.$emit("cancel");
    }
  }
};
</script>
