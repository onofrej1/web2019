<template>
  <v-container>
    <v-layout justify-space-around>
      <v-flex mt-1 mb-1>
        <v-card>
          <v-card-text>
            <p>
              Choose file
              <input type="file" ref="file" @change="setFile()">
              <v-btn small color="secondary" @click="upload()">Submit file</v-btn>
            </p>

            <v-layout row wrap>
              <v-flex sm6 xs12 lg4 v-for="file in files" :key="file.name" pr-3 pb-3>
                <v-card @click="selectFile(file)">
                  <v-img :src="file.path" aspect-ratio="2.1"></v-img>
                  <v-card-title primary-title>
                    <div>
                      <h3 class="headline mb-0">{{ file.name }}</h3>
                    </div>
                  </v-card-title>
                </v-card>
              </v-flex>
            </v-layout>

            <v-btn @click="$emit('close')" color="secondary">Close</v-btn>
          </v-card-text>
        </v-card>
      </v-flex>
    </v-layout>
  </v-container>
</template>

<script>
import { mapActions, mapState } from "vuex";

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
      formData.append("myfile", this.file);

      this.uploadFile(formData);
    },
    selectFile(file) {
        this.$emit('close', file.path);
    },
    setFile() {
      this.file = this.$refs.file.files[0];
    }
  },
  mounted() {
    this.fetchFiles();
  }
};
</script>
