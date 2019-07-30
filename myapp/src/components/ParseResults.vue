<template>
  <v-container>
    <v-layout justify-space-around>
      <v-flex mt-1 mb-1>
        <v-card>
          <v-card-text>
            <p>
              Choose file
              <input type="file" id="file" ref="file" @change="handleFileUpload()" />
              <v-btn color="secondary" @click="upload()">Submit file</v-btn>
            </p>

            <input id="dealCsv" type="file" />
            <br />
            <br />
            <v-card-text>
              <v-data-table
                :headers="headers"
                :items="items"
                :items-per-page="10"
                class="elevation-1"
              >
              <template v-slot:item.name="{ item, props, headers }">
                {{ item.name }} 
                <span v-if="true">
                  {{ misspelled[45].join(',') }}
                </span>
               
              </template>
              </v-data-table>
            </v-card-text>
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
var levenshtein = require("underscore.string/levenshtein");

export default {
  name: "ParseResults",
  data() {
    return {
      //file: "",
      headers: [],
      items: [],
      found: [],
      notFound: [],
      misspelled: []
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
    check() {
      //console.log('checkxxxxyy');
      console.log(levenshtein("onofrej", "onoffrexe"));
    },
    handleFileUpload() {
      this.file = this.$refs.file.files[0];
    },
    parseCsv(e) {
      let input = document.getElementById("dealCsv");
      let me = this;

      input.addEventListener("change", function() {
        if (this.files && this.files[0]) {
          var myFile = this.files[0];
          var reader = new FileReader();

          reader.addEventListener("load", function(e) {
            let csvdata = e.target.result;
            me.getParseData(csvdata); // calling function for parse csv data
          });

          //reader.readAsBinaryString(myFile);
          reader.readAsText(myFile);
        }
      });
    },
    guid() {
        function _p8(s) {
            var p = (Math.random().toString(16)+"000000000").substr(2,8);
            return s ? "-" + p.substr(0,4) + "-" + p.substr(4,4) : p ;
        }
        return _p8() + _p8(true) + _p8(true) + _p8();
    },
    sendData() {
      let me = this;
      let data = this.items.map((item, i) => {
        let obj = {};
        let [lastName, firstName] = item.name
          .split(/(\s+)/)
          .filter(e => e.trim().length > 0);

        obj.id = i;
        obj.firstName = firstName.trim();
        obj.lastName = lastName.trim();
        obj.birthday = item.birthday + "-01-01";
        return obj;
      });
      console.log(data);
      axios
        .post(BASE_URL + "/checkNames", { runners: data, meno: "eee" })
        .then(function(res) {
          //dispatch('fetchFiles');
          me.misspelled = res.data[2];
          console.log(me.misspelled);
          console.log("SUCCESS!!");
        })
        .catch(function(e) {
          console.log(e);
          console.log("FAILURE!!");
        });
    },
    getParseData(data) {
      let parsedata = [];
      let newLinebrk = data.split("\n");

      for (let i = 0; i < newLinebrk.length; i++) {
        parsedata.push(newLinebrk[i].split(","));
      }
      
      let dataHeader = parsedata[0];      
      this.headers = parsedata
        .shift()
        .filter(d => d.trim().length > 0)
        .map(d => ({ text: d, value: d }));

      console.log(JSON.stringify(this.headers));
      this.items = parsedata.map(d => {        
        let obj = {};        
        dataHeader.forEach((h, i) => {
          if (h.trim().length > 0) {
            obj[h] = d[i];
          }
        });
        return obj;
      });
      this.sendData();      
    }
  },
  mounted() {
    //this.fetchFiles();
    this.check();
    this.parseCsv();
  },
  parse() {
    axios.get(state.apiUrl + "/" + resource).then(
      response => {
        var data = response.data._embedded[resource];
        commit("setData", {
          resource,
          data: data
        });
        commit("setStatus", "success");
        return { ...response, data: data };
      },
      error => {
        commit("setStatus", "error");
        console.log(error);
      }
    );
  }
};
</script>
