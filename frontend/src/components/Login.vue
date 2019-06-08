<template>
  <div class="hello">
    <h1>{{ msg }}</h1>
    login

    <router-link to="/crud/users">Go to Foo</router-link>
  </div>
</template>

<script>
export default {
  name: 'HelloWorld',
  props: {
    msg: String
  }
}
</script>

<style scoped>
h3 {
  margin: 40px 0 0;
}

</style>
