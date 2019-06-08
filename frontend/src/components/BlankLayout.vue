<template>
  <div>
    blank
    <router-view></router-view>
  </div>
</template>

<script>
export default {
  name: "blank-layout",
  props: {
    msg: String
  }
};
</script>

<style scoped>
h3 {
  margin: 40px 0 0;
}
</style>
