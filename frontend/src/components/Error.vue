<template>
  <div>
    <v-dialog v-model="dialog" max-width="290">
      <v-card>
        <v-toolbar>Message</v-toolbar>
        <v-card-text>{{ message }}</v-card-text>
        <v-card-actions>
          <v-btn color="primary" @click="clearMessage()">Close</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
import { mapState, mapActions } from "vuex";

export default {
  name: "Error",
  props: {
    error: String
  },
  computed: {
    ...mapState(["message"]),
    dialog: {
      get: function() {
        return this.message ? true : false;
      },
      set: function(newValue) {
        this.clearMessage();
      }
    }
  },
  methods: {
    ...mapActions(["clearMessage"])
  }
};
</script>
