<template>
  <div>
    <v-dialog v-model="dialog" max-width="290">
      <v-card elevation="2">
        <v-toolbar dense flat color="modal.color || primary">
          <span style="color:white">{{ modal.title }}</span>
        </v-toolbar>
        <v-card-text>
          {{ modal.message }}
          <template v-if="modal.component">
            <component v-bind:is="modal.component"></component>
          </template>
        </v-card-text>

        <v-btn depressed small color="primary" @click="closeModal()">Close</v-btn>
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
        console.log(this.message);
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
    ...mapActions("modal", { closeModal: "close" })
  }
};
</script>
