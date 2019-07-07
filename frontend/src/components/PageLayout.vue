<template>
  <div>
    <img src="./../assets/images/header.jpg" width="100%" height="200px" style="display:block;">
    <div class="page-toolbar text-xs-center">
      <!--<v-toolbar-side-icon></v-toolbar-side-icon>-->
      <v-menu :key="item.title" offset-y class="menu" v-for="item in menuItems">
        <template v-slot:activator="{ on }">
          <v-btn flat dark v-on="on">{{ item.title }}</v-btn>
        </template>
        <v-list v-if="item.items.length > 0">
          <v-list-tile v-for="(subItem, index) in item.items" :key="index">
            <v-list-tile-title @click="showPage(subItem)">{{ subItem.title }}</v-list-tile-title>
          </v-list-tile>
        </v-list>
      </v-menu>
    </div>
    
    <div>
      <slot></slot>
      <router-view></router-view>
    </div>

    <div class="page-toolbar">
      <v-container class="page-toolbar">
        <v-layout justify-space-around>
          <v-flex mt-1 mb-1>
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
        </v-layout>
      </v-container>
    </div>
  </div>
</template>

<script>
import { mapState, mapActions } from "vuex";

export default {
  name: "PageLayout",
  data: () => ({
    
  }),
  computed: {
    ...mapState("resources", { resources: "data" }),
    menuItems: function() {
      let tree = [];
      this.resources.menuItems
        .filter(i => !i.parent)
        .forEach(m => {
          let items = this.resources.menuItems
            .filter(i => i.parent && i.parent.id === m.id)
            .map(item => this.createMenuItem(item));

          tree.push({ ...this.createMenuItem(m), items: items });
        });
      return tree;
    }
  },
  methods: {
    ...mapActions("resources", ["fetchData"]),
    createMenuItem: function(menuItem) {
      let item = { title: menuItem.title };
      if (menuItem.page) {
        item.link = "/page/" + menuItem.page.id;
      } else {
        item.link = menuItem.link;
        item.isExternal = /^https?:\/\//.test(item.link);
      }
      return item;
    },
    showPage(menuItem) {
      
      this.$router.push(menuItem.link);
    }
  },

  mounted() {
    this.fetchData("menuItems");
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

