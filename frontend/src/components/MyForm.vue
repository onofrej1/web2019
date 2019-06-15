<template>
  <div class="hello">
    <v-form>
      <template v-for="field in fields">
        <v-text-field
          :key="field.name"
          v-if="field.type==='text'"
          v-model="data[field.name]"
          :label="field.label || field.name"
          required
        ></v-text-field>

        <v-date-picker v-if="field.type==='datex'" :key="field.name" v-model="data[field.name]"></v-date-picker>

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
  </div>
</template>

<script>
import InlineInput from "./InlineInput";
import { mapState } from "vuex";

export default {
  name: "my-form",
  props: {
    data: Object,
    fields: Array
  },
  components: {
    InlineInput
  },
  computed: {
    ...mapState('resources', {resourceData: 'data'})
  },
  methods: {
    getOptions: function(resource, field) {
      return (this.resourceData[resource] || []).map(data => ({ value: data.id, text: data[field] }));
    },
    submit: function(e) {      
      let data = this.data;
      console.log(data);
      for (let key in data) {
        const field = this.fields.find(f => f.name == key);
        if (field && field.type == 'pivotRelation') {
          data[key] = data[key].map(v =>
            this.resourceData[field.resourceTable].find(r => r.id == v.value)
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
