<template>
  <div>
    <img src="./../assets/images/header.jpg" width="100%" height="200px" style="display:block;">
    <div class="page-toolbar text-center">
      <template  v-for="item in menuItems">
        <v-btn text dark :key="item.title" v-if="item.items.length == 0">{{ item.title }}</v-btn>
        <dropdown-menu :key="item.title" v-else :node="item" :isRoot="true"></dropdown-menu>
      </template>
    </div>

    <div>
      <slot></slot>
      <router-view></router-view>
    </div>
    
    <div class="page-toolbar">
      <v-container class="page-toolbar">
        <v-layout  justify-space-around>
          <v-flex offset-xs1 mt-1 mb-1>
            <div>O5 bežecký klub FURČA</div>
            <div>Tokajícka 2, 040 22 Košice Mobil: 0904 246 060</div>
            <div>E-mail: bohunek.zdenek@maratonfurca.sk</div>
          </v-flex>
          <v-flex mt-1 mb-1>
            <a href="http://youtu.be/zJahlKPCL9g" style="color: white;">
              webstránka:
              <span class="glyphicon glyphicon-envelope"/>
              erikonofrej@maratonfurca.sk
            </a>&nbsp;
            <a href="http://www.toplist.sk/" target="_top">
              <img
                src="http://toplist.sk/count.asp?id=1254971&logo=mc"
                border="0"
                alt="TOPlist"
                width="88"
                height="60"
              >
            </a>
          </v-flex>
          <v-flex xs1></v-flex>
        </v-layout>
      </v-container>
    </div>
  </div>
</template>

<script>
import { mapState, mapActions } from "vuex";
import DropdownMenu from './DropdownMenu';

export default {
  name: "PageLayout",
  data: () => ({
    
  }),
  components: {
    DropdownMenu
  },
  computed: {
    ...mapState("resources", { resources: "data" }),
    menuItems: function() {
      let tree = [];
      this.resources.menuItems.rows
        .filter(i => !i.parent_id)
        .forEach(m => {
          let items = this.resources.menuItems.rows
            .filter(i => i.parent_id === m.id)
            .map(item => this.createMenuItem(item));

          tree.push({ ...this.createMenuItem(m), items: items });
        });
      return tree;
    }
  },
  methods: {
    ...mapActions("resources", ["getResource"]),
    createMenuItem: function(menuItem) {
      let item = { title: menuItem.title };
      if (menuItem.page_id) {
        item.link = "/pages/" + menuItem.page_id;
      } else {
        item.link = menuItem.link;
        item.isExternalLink = /^https?:\/\//.test(item.link);
      }

      let items = this.resources.menuItems.rows
            .filter(i => i.parent_id === menuItem.id)
            .map(item => this.createMenuItem(item));

      item.items = items;

      return item;
    },
    showPage(menuItem) {
      if(menuItem.isExternalLink) {
        window.location = menuItem.link;
      } else {
        this.$router.push(menuItem.link);
      }  
    }
  },

  mounted() {
    this.getResource({resource: "menuItems"});
  }
};
</script>
<style>
.page-toolbar {
  background: none !important;
  background-image: linear-gradient(to bottom, #3c3c3c 0, #222 100%) !important;
  color: #9d9d9d !important;
}

.menu {
  display: inline-block !important;
}
</style>

