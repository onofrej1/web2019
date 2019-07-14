<template>
  <v-container>
    <v-layout justify-space-around>
      <v-flex mt-1 mb-1>
        <v-card>
          <v-card-text>
            File
            <input type="file" id="file" ref="file" @change="handleFileUpload()">
            <v-btn @click="upload()">Submit</v-btn>

            <div v-for="file in files" :key="file">
              {{ file.size }} - {{ file.lastAccessTime}}
            </div>
          </v-card-text>
        </v-card>
      </v-flex>
    </v-layout>
  </v-container>
</template>

<script>
import axios from "axios";
import { mapActions, mapState } from "vuex";

import { BASE_URL, API_URL } from "./../constants";

export default {
  data() {
    return {
      file: ""
    };
  },
  computed: {
    ...mapState("files", ["files"])
  },
  methods: {
    ...mapActions("files", ["uploadFile", "fetchFiles"]),
    upload() {
      let formData = new FormData();
      formData.append("file", this.file);
      formData.append("path", "images");

      this.uploadFile(formData);
    },

    handleFileUpload() {
      this.file = this.$refs.file.files[0];
    }
  },
  mounted() {
    this.fetchFiles();
  }
};
</script>
