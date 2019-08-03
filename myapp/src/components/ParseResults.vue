<template>
  <v-container>
    <v-layout justify-space-around>
      <v-flex mt-1 mb-1>
        <v-card>
          <v-card-text>
            <p>
              Choose CSV file:
              <input id="dealCsv" type="file">
            </p>

            <v-card-text>
              <div v-if="step == 'check-names'">
                <v-layout mb-3 border-b-2>
                  <v-flex xs4 class="font-weight-bold">Status</v-flex>
                  <v-flex xs8 class="font-weight-bold">Similar names - choose</v-flex>
                </v-layout>
                <hr>

                <v-layout
                  align-content-space-around
                  fill-height
                  :key="item.id"
                  v-for="item in items"
                  mb-3
                  mt-3
                >
                  <v-flex xs2>
                    <template v-if="item.runner.runnerId">
                      <v-chip small color="green" text-color="white" class="mr-2">
                        <v-icon left>mdi-account-check</v-icon>OK
                      </v-chip>
                    </template>

                    <v-chip v-else small color="orange" text-color="white" class="mr-2">
                      <v-icon left>mdi-account-plus</v-icon>NEW
                    </v-chip>
                  </v-flex>

                  <v-flex xs4 mr-3 class="text-no-wrap">
                    <template v-if="edit[item.guid]" >
                      <v-text-field class="d-inline-block" v-model="item.name" style="width: 75%; margin-top: -7px; padding-top: 0px"></v-text-field>
                       <v-icon left>check</v-icon>
                       <v-icon left pr-2>clear</v-icon>
                    </template>
                    <span ml-3 v-else >
                      {{ item.name }} <v-icon left @click="editItem(item.guid)">create</v-icon>
                    </span>
                  </v-flex>

                  <v-flex xs6 v-if="item.runner">
                    <v-chip small mr-2 outlined :key="r.id" v-for="r in item.runner.names">
                      <v-icon left>mdi-account</v-icon>
                      {{ r.lastName }} {{ r.firstName }}, {{ moment(r.birthday).format('YYYY') }}
                    </v-chip>
                  </v-flex>
                  <br>
                </v-layout>
              </div>

              <v-data-table
                :headers="headers"
                :items="items"
                :items-per-page="10"
                class="elevation-1"
                v-if="step == 'load-data'"
              >
                <template v-slot:item.name="{ item, props, headers }">{{ item.name }}</template>
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
import { VIcon } from "vuetify/lib";
const moment = require("moment");

var levenshtein = require("underscore.string/levenshtein");

export default {
  name: "ParseResults",
  data() {
    return {
      //file: "",
      headers: [],
      items: [],
      results: [],
      edit: {},
      step: "",
      moment: moment
    };
  },
  computed: {
    ...mapState("files", ["files"])
  },
  components: { VIcon },
  methods: {
    ...mapActions("files", ["uploadFile", "fetchFiles"]),
    editItem(itemGuid) {
      this.edit = {...this.edit, [itemGuid]: true};
      console.log(this.edit);
    },
    upload() {
      let formData = new FormData();
      formData.append("file", this.file);
      formData.append("path", "images");

      this.uploadFile(formData);
    },
    getRunnerOptions(names) {
      return names.map(n => {
        let option = {};
        option.text = n.lastName + " " + n.firstName + " ," + n.birthday;
        option.value = n.id;
        return option;
      });
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
        var p = (Math.random().toString(16) + "000000000").substr(2, 8);
        return s ? "-" + p.substr(0, 4) + "-" + p.substr(4, 4) : p;
      }
      return _p8() + _p8(true) + _p8(true) + _p8();
    },
    sendData() {
      let me = this;
      console.log(this.items);

      let data = this.items.map((item, i) => {
        let obj = {};
        let [lastName, firstName] = item.name
          .split(/(\s+)/)
          .filter(e => e.trim().length > 0);
        obj.guid = item.guid;
        obj.firstName = firstName.trim();
        obj.lastName = lastName.trim();
        obj.birthday = item.birthday + "-01-01";
        return obj;
      });

      console.log(data);
      axios
        .post(BASE_URL + "/checkNames", { runners: data })
        .then(function(res) {
          me.results = res.data;
          //console.log(me.results);
          me.items = me.items.map(i => {
            i.runner = me.results.find(r => r.guid == i.guid);
            return i;
          });

          console.log(me.items);
          me.step = "check-names";

          console.log("SUCCESS!!");
        })
        .catch(function(e) {
          console.log(e);
          console.log("FAILURE!!");
        });
    },
    getParseData(data) {
      let me = this;
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
        let obj = { guid: this.guid(), runner: null };
        me.edit[obj.guid] = false;
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
  }
};
</script>
