<template>
  <v-container>
    <v-layout justify-space-around>
      <v-flex mt-1 mb-1>
        <v-card>
          <v-card-text>
            <p>
              Choose file
              <input type="file" id="file" ref="file" @change="handleFileUpload()">
              <v-btn color="secondary" @click="upload()">Submit file</v-btn>
            </p>

            <v-layout row wrap>
              <v-flex sm6 xs12 lg4 v-for="file in files" :key="file.path" pr-3 pb-3>
                <v-card>
                  <v-img :src="'http://localhost:8082/images/'+file.path" aspect-ratio="2.1"></v-img>

                  <v-card-title primary-title>
                    <div>
                      <h3 class="headline mb-0">{{ file.path }}</h3>
                    </div>
                  </v-card-title>

                  <v-card-actions>
                    <v-btn flat color="orange">
                      <v-icon>delete</v-icon>Delete
                    </v-btn>
                    <v-btn flat color="primary">
                      <v-icon>save_alt</v-icon>Download
                    </v-btn>
                  </v-card-actions>
                </v-card>
              </v-flex>
            </v-layout>
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
