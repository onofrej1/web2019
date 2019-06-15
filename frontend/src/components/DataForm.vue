
<template>
  <v-form>
    <template v-for="field in fields">
      <v-text-field
        :key="field.name"
        v-if="field.type==='text'"
        v-model="data[field.name]"
        :label="field.label || field.name"
        required
      ></v-text-field>

      <v-date-picker
        v-if="field.type==='date'"
        locale="en-EN"
        :key="field.name"
        v-model="data[field.name]"
      ></v-date-picker>

      <v-select
        v-if="field.type==='relation'"
        :key="field.name"
        :items="getOptions(field.resourceTable, field.show)"
        :value="data[field.name].id"
        v-model="data[field.name]"
        :label="field.label || field.name"
      ></v-select>

      <v-combobox
        :key="field.name"
        v-if="field.type==='pivotRelation'"
        v-model="data[field.name]"
        :items="getOptions(field.resourceTable, field.show)"
        item-text="text"
        item-value="text"
        :label="field.name || field.label"
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
    </template>
    <v-spacer></v-spacer>
    <v-btn color="primary" @click="submit">Save</v-btn>
    <v-btn @click="cancel">Cancel</v-btn>
  </v-form>
</template>

<script>
import InlineInput from "./InlineInput";
import { mapState } from "vuex";
const moment = require("moment");

export default {
  name: "data-form",
  props: {
    data: Object,
    fields: Array
  },
  components: {
    InlineInput
  },
  computed: {
    ...mapState("resources", { resources: "data" }),
    formatDate: value => moment(value, "YYYY-MM-DD")
  },
  methods: {
    getOptions: function(resource, field) {
      return (this.resources[resource] || []).map(data => ({
        value: data.id,
        text: data[field]
      }));
    },
    submit: function(e) {
      let data = this.data;
      console.log(data);
      for (let key in data) {
        const field = this.fields.find(f => f.name == key);
        if (field && field.type == "pivotRelation") {
          data[key] = data[key].map(v =>
            this.resources[field.resourceTable].find(r => r.id == v.value)
          );
        }
      }
      this.$emit("submit", { data: data, originalEvent: e });
    },
    cancel: function() {
      this.$emit("cancel");
    }
  }
};
</script>
