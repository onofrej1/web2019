<template>
  <div>
    <v-dialog v-model="dialog" min-width="290" :width="modal.width || '100%'">
      <v-card elevation="2">
        <v-toolbar dense flat color="modal.color || primary">
          <span style="color:white">{{ modal.title }}</span>
        </v-toolbar>
        <v-card-text>
          {{ modal.message }}
          <template v-if="modal.component">
            <component v-bind:is="modal.component" v-bind="getProps()" @close="close"></component>
          </template>
        </v-card-text>
        <v-card-actions>
          <v-btn v-if="modal.closeBtn" depressed color="primary" @click="closeModal()">Close</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
import { mapState, mapActions } from "vuex";

export default {
  name: "Modal",
  props: {
    component: Object
  },
  computed: {
    ...mapState("modal", { modalProps: "settings" }),
    dialog: {
      get: function() {
        return this.modalProps ? true : false;
      },
      set: function() {
        this.closeModal();
      }
    },
    modal: function() {
      return this.modalProps || {};
    }
  },
  methods: {
    ...mapActions("modal", { closeModal: "close" }),
    getProps() {
      return this.modal.bind;
    },
    close(data = null) {
      //console.log('confirm modal');
      if(this.modalProps.close) {
        this.modalProps.close(data);
      }
      this.closeModal();
    }
  }
};
</script>
