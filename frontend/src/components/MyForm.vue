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

        <v-date-picker
          v-if="field.type==='date'"
          :key="field.name"
          v-model="data[field.name]"
          :landscape="landscape"
          :reactive="reactive"
        ></v-date-picker>

        <v-select
          v-if="field.type==='relation'"
          :key="field.name"
          :items="getOptions(field.resourceTable, field.show)"
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
      <v-btn color="primary" @click="onSubmit">Save</v-btn>
      <v-btn>Cancel</v-btn>
    </v-form>
  </div>
</template>

<script>
import InlineInput from "./InlineInput";

export default {
  name: "my-form",
  props: {
    msg: String,
    data: Object,
    fields: Array,
    //submit: Function
  },
  components: {
    InlineInput
  },
  methods: {
    onSubmit: function() {
      console.log("submit");
      console.log(this.data);
      //this.submit(data);
    },
  }
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
h3 {
  margin: 40px 0 0;
}
</style>
