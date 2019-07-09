<template>
  <div class="dropdown-menu">
    <div @click="toggleChildren" >{{ node.title }}</div>

    <div v-if="node.items && node.items.length" class="dropdown-submenu">
      <multi-menu
        v-if="showChildren"
        :depth="depth + 1"
        :key="item.title"
        v-for="item in node.items"
        :node="item"
      ></multi-menu>
    </div>
  </div>
</template>

<script>
export default {
  name: "multi-menu",
  props: {
    node: Object,
    depth: Number
  },
  data() {
    return {
      showChildren: false
    };
  },
  computed: {
    iconClasses() {
      return {
        "fa-plus-square-o": !this.showChildren,
        "fa-minus-square-o": this.showChildren
      };
    },
    labelClasses() {
      return { "has-children": this.nodes };
    },
    indent() {
      return { 
        transform: `translate(${this.depth * 50}px)`,
        'background-color': 'yellow',
        cursor: 'pointer',
        
        padding: '10px'
        };
    }
  },
  indent2() {
      return {
        transform: `translate(${this.depth * 50}px)`,
        width: '200px'
      }
  },
  methods: {
    toggleChildren() {
      this.showChildren = !this.showChildren;
    }
  }
};
</script>
<style>
.dropdown-menux {
  mmposition: absolute;
  border: 1px solid black;
  background-color: red;
  xxtop: 100%;
  left: 0;
}

.dropdown-submenu {
  position: relative;
  background-color: red;
  top: 0;
  left: 100%;
  width: 200px;
}

.xxxdropdown-submenu .xxdropdown-menu {
  
  top: 0;
  left: 100%;
  margin-top: -1px;
}
</style>