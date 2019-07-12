<template>
  <div>
    <slot></slot>
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
