
<template>
  <v-sheet class="pa-12 shadow">
    <div>{{ field.label || field.name }}</div>

    <template v-for="(f, index) in model">
      <div :key="field.name+'-'+index">
        <div class="greyxx lighten-5xx inline-wrapper">
          <component v-bind:is="field.form" class="text-xs-right" :model="model[index]"></component>
        </div>
        <div class="text-right">
          <v-btn text @click="deleteItem(model, index)">
            <v-icon :key="'remove'+index" small>delete</v-icon>Remove
          </v-btn>
        </div>
      </div>
    </template>
    <v-btn color="primary" @click="addItem(field.name)">Add new</v-btn>
  </v-sheet>
</template>

<script>
export default {
  name: "inline-input",
  props: {
    data: Array,
    field: Object
  },
  data: function() {
    return {
      model: this.data
    };
  },
  methods: {
    addItem: function(field) {
      this.model.push({});
    },
    deleteItem: function(index) {
      this.model.splice(index, 1);
    }
  },
  computed: {
    /*modelxxx: {
      // getter
      get: function() {
        return this.model;
      },
      // setter
      set: function(newValue) {
        this.model = newValue;
      }
    }*/
  },
  watch: {
    model: function(val) {
      console.log("emit change");
      this.$emit("change", val);
    }
  }
};
</script>
<style>
.inline-wrapper {
  padding: 10px;
  border-top: 4px solid black !important;
  background-color: #F8F8F8;
}
.shadow {
   
   xxxborder-left: 2px solid gray;
}
</style>