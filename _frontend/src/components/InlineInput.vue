
<template>
  <v-sheet class="pa-3 shadow">
    <!--<h3>{{ field.label || field.name }}</h3>-->
    <span v-if="!items.length">No items to edit.</span>
    <template v-for="(f, index) in items">
      <div :key="field.name+'-'+index" class="inline-wrapper pa-2 mb-4">
        <component v-bind:is="field.form" :model="items[index]"></component>
        <div class="text-right mt-1 mb-2">
          <v-btn small @click="deleteItem(index)">
            <v-icon :key="'remove'+index" small>delete</v-icon>Remove
          </v-btn>
        </div>
      </div>
    </template>

    <div class="text-right">
      <v-btn small color="primary" @click="addItem()">
        <v-icon>add</v-icon>Add new
      </v-btn>
    </div>
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
      items: this.data
    };
  },
  methods: {
    addItem: function() {
      this.items.push({});
    },
    deleteItem: function(index) {
      this.items.splice(index, 1);
    }
  },
  computed: {
    /*model: {
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
    items: function(val) {
      console.log("emit change");
      this.$emit("change", val);
    }
  }
};
</script>
<style>
.inline-wrapper {
  border: 1px solid lightgray;
  border-top: 4px solid gray !important;
  background-color: #f8f8f8;
}
</style>