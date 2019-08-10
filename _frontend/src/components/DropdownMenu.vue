<template>
  <div :class="submenu" @mouseleave="hideMenu">
    <div :class="link" @mouseover="showMenu">
      <v-btn text @click="goToPage" :dark="isRoot">
        {{ node.title }}
        <v-icon v-if="node.items.length">{{ linkIcon }}</v-icon>
      </v-btn>
    </div>
    <div v-if="node.items && node.items.length && showChildren" :class="menu">
      <dropdown-menu :key="item.title" v-for="item in node.items" :node="item">
      </dropdown-menu>
    </div>
  </div>
</template>

<script>
export default {
  name: "dropdown-menu",
  props: {
    node: Object,
    isRoot: Boolean
  },
  data() {
    return {
      showChildren: false
    };
  },
  computed: {
    menu: function() {
      return {
        "dropdown-menu": !this.isRoot,
        "dropdown-root-menu": this.isRoot
      };
    },
    link: function() {
      return {
        link: !this.isRoot,
        "root-link": this.isRoot
      };
    },
    linkIcon: function() {
      return this.showChildren && !this.isRoot
        ? "arrow_right"
        : "arrow_drop_down";
    },
    submenu: function() {
      return {
        "dropdown-submenu": !this.isRoot,
        "dropdown-root-submenu": this.isRoot
      };
    }
  },
  methods: {
    showMenu() {
      this.showChildren = true;
    },
    hideMenu() {
      this.showChildren = false;
    },
    goToPage() {
      if (this.node.items.length) {
        return;
      }
      if(this.node.isExternal) {
        console.log('redirect');
        window.location = this.node.link;
      } else {
        this.$router.push({path: this.node.link});
      }  
    }
  }
};
</script>
<style>
.dropdown-submenu {
  position: relative;
  cursor: pointer;
}

.dropdown-root-submenu {
  position: relative;
  cursor: pointer;
  display: inline-block;
  min-width: 160px;
}

.dropdown-menu,
.dropdown-root-menu {
  position: absolute;
  top: 100%;
  left: 0;
  text-align: left;
  z-index: 1000;
  width: 100%;
  font-size: 14px;
  background: #fff;
  color: rgba(0, 0, 0, 0.87);
  border-radius: 4px;
  -webkit-box-shadow: 0 6px 12px rgba(0, 0, 0, 0.175);
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.175);
}

.dropdown-submenu .dropdown-menu {
  top: 0;
  left: 100%;
}

.dropdown-submenu .dropdown-root-menu {
  top: 100;
  left: 0;
}

.dropdown-submenu .v-btn,
.dropdown-root-submenu .v-btn {
  padding: 5px !important;
  margin-left: 0px !important;
  margin-right: 0px !important;
}

.link {
  text-align: center;
}
</style>