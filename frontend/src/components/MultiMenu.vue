<template>
  <div class="dropdown-submenu">
    <div @click="toggleChildren">{{ node.title }}</div>
    <div v-if="node.items && node.items.length && showChildren" class="dropdown-menu">      
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
        "background-color": "yellow",
        cursor: "pointer",

        padding: "10px"
      };
    }
  },
  indent2() {
    return {
      transform: `translate(${this.depth * 50}px)`,
      width: "200px"
    };
  },
  methods: {
    toggleChildren() {
      this.showChildren = !this.showChildren;
    }
  }
};
</script>
<style>
.dropdown-submenu {
  position: relative;
}

.dropdown-menu {
  position: absolute;
  top: 100%;
  left: 0;
  z-index: 1000;
  float: left;
  min-width: 160px;
  padding: 0 0;  
  font-size: 14px;
  text-align: left;
  list-style: none;
  background-color: #fff;
  background-clip: padding-box;
  border: 1px solid #ccc;
  border: 1px solid rgba(0, 0, 0, 0.15);
  border-radius: 4px;
  -webkit-box-shadow: 0 6px 12px rgba(0, 0, 0, 0.175);
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.175);
}

.dropdown-submenu .dropdown-menu {
  top: 0;
  left: 100%;  
}
</style>