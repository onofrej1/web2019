
<template>
  <v-expansion-panel :key="field.name">
    <v-expansion-panel-content :key="field.name" style="padding:10px">
      <template v-slot:header>
        <div>{{ field.label || field.name }}</div>
      </template>

      <template v-for="(f, index) in data">
        <div
          class="grey lighten-4"
          :key="field.name+'-'+index"
          style="padding: 10px; border-top:4px solid grey !important;"
        >
          <component
            :key="field.name+'-'+index"
            v-bind:is="field.form"
            class="text-xs-right"
            :model="data[index]"
          ></component>
        </div>
        <div :key="field.name+'-d'+index" class="text-xs-right">
          <v-btn flat @click="onDelete(data, index)">
            <v-icon :key="'remove'+index" small>delete</v-icon>Remove
          </v-btn>
        </div>
      </template>
      <v-btn color="primary" @click="onAdd(field.name)">Add new</v-btn>
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
      onAdd: function(field) {
      if (this.data === undefined) {
        this.data = [];
      }
      this.data.push({});
      console.log(JSON.stringify(this.data));
    },
    onDelete: function(data, index) {
      console.log("delete");
      data.splice(index, 1);
    },
  }
};
</script>