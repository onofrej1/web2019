
<template>
  <v-expansion-panel :key="field.name">
    <v-expansion-panel-content :key="field.name" style="padding:10px">
      <template v-slot:header>
        <div>{{ field.label || field.name }}</div>
      </template>

      <template v-for="(f, index) in data">
        <div
          class="grey lighten-4 inline-wrapper"
          :key="field.name+'-'+index"
        >
          <component
            :key="field.name+'-'+index"
            v-bind:is="field.form"
            class="text-xs-right"
            :model="data[index]"
          ></component>
        </div>
        <div :key="field.name+'-d'+index" class="text-xs-right">
          <v-btn flat @click="deleteItem(data, index)">
            <v-icon :key="'remove'+index" small>delete</v-icon>Remove
          </v-btn>
        </div>
      </template>
      <v-btn color="primary" @click="addItem(field.name)">Add new</v-btn>
    </v-expansion-panel-content>
  </v-expansion-panel>
</template>

<script>
export default {
  name: "inline-input",
  props: {
    data: Array,
    field: Object
  },
  methods: {
    addItem: function(field) {
      if (this.data === undefined) {
        this.data = [];
      }
      this.data.push({});
      console.log(JSON.stringify(this.data));
    },
    deleteItem: function(data, index) {
      data.splice(index, 1);
    }
  }
};
</script>
<style>
.inline-wrapper {
  padding: 10px; border-top:4px solid grey !important;
}
</style>