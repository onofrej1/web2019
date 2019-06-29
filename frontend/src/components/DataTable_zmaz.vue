
<template>
  <div>
    
  </div>
</template>

<script>
export default {
  name: "data-table",
  data: function() {
    return {
      search: {},
      //showFilter: false,
    };
  },
  props: {
    data: Array,
    fields: Array,
    filter: Array,
    actions: Array,
    footer: Object,
    header: Object,
    body: Object,
    expandRow: Object,
    pageText: Object,
  },
  components: {},
  computed: {
    items: function() {
      var data = this.data;
      var search = this.search;
      if (!data) {
        return [];
      }
      this.filter.forEach(function(filter) {
        data = data.filter(item => {
          var searchValue = search[filter.field];
          if (!searchValue) {
            return true;
          }
          var value = item[filter.field];
          if (filter.op === "contains") {
            return value.includes(searchValue);
          }

          return value === searchValue;
        });
      });
      return data;
    }
  },
  methods: {
    getFilterOptions: function(field) {
      var filterOptions = this.data.map(row => {
        return {
          value: row[field],
          text: row[field]
        };
      });
      filterOptions.unshift({ value: null, text: "" });
      return filterOptions;
    }
  }
};
</script>
<style>
.action {
  cursor: pointer;
}
</style>
