
<template>
  <v-expansion-panels>
    <v-expansion-panel :key="field.name">
      <v-expansion-panel-header>
        <div>{{ field.label || field.name }}</div>
      </v-expansion-panel-header>
      <v-expansion-panel-content :key="field.name" class="pa-3">
        <template v-for="(f, index) in data">
          {{ index }}
          <div class="grey lighten-4 inline-wrapper" :key="field.name+'-'+index">
            <component
              :key="field.name+'-'+index"
              v-bind:is="field.form"
              class="text-xs-right"
              :model="data[index]"
            ></component>
          </div>
          <div :key="field.name+'-d'+index" class="text-xs-right">
            <v-btn text @click="deleteItem(data, index)">
              <v-icon :key="'remove'+index" small>delete</v-icon>Remove
            </v-btn>
          </div>
        </template>
        <v-btn color="primary" @click="addItem(field.name)">Add new</v-btn>
      </v-expansion-panel-content>
    </v-expansion-panel>
  </v-expansion-panels>
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
      this.data = [...this.data, {}];
    },
    deleteItem: function(data, index) {
      // test
      data.splice(index, 1);
    }
  }
};
</script>
<style>
.inline-wrapper {
  padding: 10px;
  border-top: 4px solid grey !important;
}
</style>