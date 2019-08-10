<template>
  <div>
    <div :key="item.id" v-for="item in items">
        {{ item.lastName }} <span @click="actions.edit(item)">edit</span>
    </div>
    runners
  </div>
</template>

<script>
export default {
  name: 'RunnerList',
  props: {
    items: Array,
    actions: Object
  },
}
</script>
