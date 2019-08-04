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
                <v-data-table
                  :headers="runnersHeader"
                  :items="editRunners"
                  :disable-pagination="true"
                  class="elevation-1"
                >
                  <template v-slot:item.names="{ item }">
                    <v-chip
                      @click="replaceItem(item, r)"
                      small
                      mr-2
                      outlined
                      :key="r.id"
                      v-for="r in item.names"
                    >
                      <v-icon left>mdi-account</v-icon>
                      {{ r.lastName }} {{ r.firstName }}, {{ moment(r.birthdate).format('YYYY') }}
                    </v-chip>
                  </template>
                  <template
                    v-for="field in runnersHeader.filter(i => i.editable)"
                    v-slot:[getSlotItemName(field.value)]="{ item, props, headers }"
                  >
                    <template v-if="editItem[item.guid]">
                      <v-text-field
                        :key="field.name"
                        class="d-inline-block"
                        v-model="item[field.value]"
                        style="width: 75%; margin-top: -7px; padding-top: 0px"
                      ></v-text-field>
                    </template>

                    <span
                      :key="field.name"
                      ml-3
                      v-else
                    >{{ item[field.value] }}</span>
                  </template>

                  <template v-slot:item.status="{ item }">
                    <v-flex xs2>
                      <template v-if="item.runnerId">
                        <v-chip small color="green" text-color="white" class="mr-2">
                          <v-icon left>mdi-account-check</v-icon>OK
                        </v-chip>
                      </template>

                      <v-chip v-else small color="orange" text-color="white" class="mr-2">
                        <v-icon left>mdi-account-plus</v-icon>NEW
                      </v-chip>
                    </v-flex>
                  </template>

                  <template v-slot:item.actions="{ item }">
                    <span v-if="editItem[item.guid]" class="text-no-wrap">
                      <v-icon left @click="confirmEdit(item)">check</v-icon>
                      <v-icon left pr-2 @click="cancelEdit(item)">clear</v-icon>
                    </span>
                    <v-icon v-else left @click="edit(item)">create</v-icon>
                  </template>
                </v-data-table>

                <v-btn @click="createRunners()">Next</v-btn>
              </div>

              <v-data-table
                :headers="headers"
                :items="items"
                :items-per-page="10"
                class="elevation-1"
                v-if="step == 'check-results'"
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
      editItem: {},
      editRunners: [],
      runners: [],
      runnersHeader: [
        { text: "status", value: "status" },
        { text: "lastName", value: "lastName", editable: true },
        { text: "firstName", value: "firstName", editable: true },
        { text: "birthdate", value: "birthdate", editable: true },
        { text: "names", value: "names" },
        { text: "actions", value: "actions" }
      ],
      step: "",
      moment: moment
    };
  },
  computed: {
    ...mapState("files", ["files"])
    //sortedEditItems: function() {
    //return this.editItems; //.sort((a, b) => a.result.place - b.result.place);
    //}
  },
  components: { VIcon },
  methods: {
    ...mapActions("files", ["uploadFile", "fetchFiles"]),
    getSlotItemName(field) {
      return "item." + field;
    },
    edit(item) {
      this.editItem = { ...this.editItem, [item.guid]: true };
    },
    confirmEdit(item) {
      this.runners = this.runners.map(i =>
        i.guid == item.guid ? { ...item } : i
      );
      this.editItem = { ...this.editItem, [item.guid]: false };
    },
    cancelEdit(item) {
      this.editItem = { ...this.editItem, [item.guid]: false };

      let origItem = this.runners.find(i => i.guid == item.guid);
      this.editRunners = this.editRunners.map(i =>
        i.guid == item.guid ? { ...origItem } : i
      );
    },
    replaceItem(item, replace) {
      console.log(replace);
      this.runners = this.runners.map(i =>
        i.guid == item.guid ? { ...replace } : i
      );
      this.editRunners = this.editRunners.map(i =>
        i.guid == item.guid ? { ...replace } : i
      );
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
            me.getParseData(csvdata);
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

      let data = this.items.map((item, i) => {
        let obj = {};
        let [lastName, firstName] = item.name
          .split(/(\s+)/)
          .filter(e => e.trim().length > 0);

        obj.guid = item.guid;
        obj.firstName = firstName.trim();
        obj.lastName = lastName.trim();
        obj.birthdate = item.birthdate + "-01-01";
        return obj;
      });

      axios
        .post(BASE_URL + "/checkNames", { runners: data })
        .then(function(res) {
          me.parseNamesData(res.data);
          me.step = "check-names";
        })
        .catch(function(e) {
          console.log(e);
          console.log("FAILURE!!");
        });
    },
    parseNamesData(data) {
        this.runners = JSON.parse(JSON.stringify(data));
        this.editRunners = data.map(d => {
          d.birthdate = moment(d.birthdate).format("YYYY-MM-DD");
          return d;
        });
    },
    createRunners() {
      let data = this.runners.filter(r => !r.runnerId);
      let me = this;
      console.log(data);
      axios
        .post(BASE_URL + "/createRunners", { runners: data })
        .then(function(res) {
          me.runners = data.map((item, i) => {
            item.runnerId = res.data[i];
            return item;
          });
          console.log(me.runners);
          me.step = "check-results";
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
        let obj = { guid: this.guid() };
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
    //this.check();
    this.parseCsv();
  }
};
</script>
