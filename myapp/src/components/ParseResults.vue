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
                  :items="runnersEditCopy"
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
                        style="width: 75%; padding-top: 0px"
                      ></v-text-field>
                    </template>

                    <span :key="field.name" ml-3 v-else>{{ item[field.value] }}</span>
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

                <div>
                  <br>
                  <v-btn color="primary" class="mr-2" :disabled="!isDirty" @click="checkNames()">
                    <v-icon left>refresh</v-icon> Refresh
                  </v-btn>
                  <v-btn color="primary" :disabled="isDirty" @click="createRunners()">
                    Next <v-icon >navigate_next</v-icon> 
                  </v-btn>
                </div>
              </div>

              <div v-if="step=='check-results'">
                <v-data-table
                  :headers="resultsHeader"
                  :items="results"
                  :items-per-page="10"
                  class="elevation-1"
                >
                  <template
                    v-slot:item.name="{ item, props, headers }"
                  >{{ item.runner.lastName }} {{ item.runner.firstName }}</template>
                  <template
                    v-slot:item.year="{ item, props, headers }"
                  >{{ moment(item.runner.birthdate).format('YYYY') }}</template>
                </v-data-table>
                <v-btn @click="createResults()">Next</v-btn>
              </div>

              <div v-if="step=='done'">Done.</div>
            </v-card-text>
          </v-card-text>
        </v-card>
      </v-flex>
    </v-layout>
  </v-container>
</template>

<script>
var _ = require("underscore");
import axios from "axios";
import { mapActions, mapState } from "vuex";

import { BASE_URL, API_URL } from "./../constants";
import { guid } from "./../functions";
import { VIcon } from "vuetify/lib";
const moment = require("moment");

//var levenshtein = require("underscore.string/levenshtein");

export default {
  name: "ParseResults",
  data() {
    return {
      results: [],
      resultsHeader: [
        { text: "Place", value: "place" },
        { text: "category", value: "category" },
        { text: "Name", value: "name" },
        { text: "Date/Year", value: "year" },
        { text: "team", value: "team" },
        { text: "finish_time", value: "finish_time" }
      ],
      editItem: {},
      runnersEditCopy: [],
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
      moment: moment,
      isDirty: false,
    };
  },
  computed: {
    //...mapState("files", ["files"])
  },
  components: { VIcon },
  methods: {
    //...mapActions("files", ["uploadFile", "fetchFiles"]),

    init() {
      let input = document.getElementById("dealCsv");
      let me = this;

      input.addEventListener("change", function() {
        if (this.files && this.files[0]) {
          var myFile = this.files[0];
          var reader = new FileReader();

          reader.addEventListener("load", function(e) {
            let csvData = e.target.result;
            me.parseCsvData(csvData);
          });
          //reader.readAsBinaryString(myFile);
          reader.readAsText(myFile);
        }
      });
    },

    parseCsvData(csvData) {
      let data = [];
      let newLinebrk = csvData.split("\n");

      for (let i = 0; i < newLinebrk.length; i++) {
        data.push(newLinebrk[i].split(","));
      }

      let header = data.shift();

      data.forEach(d => {
        let item = { guid: guid() };
        header.forEach((h, i) => {
          item[h] = h.trim().length > 0 ? d[i] : "";
        });

        let runner = { guid: item.guid };
        let [lastName, firstName] = item.name
          .split(/(\s+)/)
          .filter(e => e.trim().length > 0);

        runner.firstName = firstName.trim();
        runner.lastName = lastName.trim();
        runner.birthdate = item.birthdate + "-01-01";

        this.results.push(item);
        this.runners.push(runner);
      });

      this.checkNames();
    },

    checkNames() {
      axios
        .post(BASE_URL + "/checkNames", { runners: this.runners })
        .then(res => {
          this.runners =  res.data;
          this.runnersEditCopy = JSON.parse(JSON.stringify(res.data));
          this.step = "check-names";
          this.isDirty = false;
        });
    },
    createRunners() {
      let [hasId, hasNotId] = _.partition(this.runners, r => r.runnerId);
      axios
        .post(BASE_URL + "/createRunners", { runners: hasNotId })
        .then(res => {
          this.results = this.results.map(i => {
            i.runner = res.data.concat(hasId).find(d => d.guid == i.guid);
            i.runnerId = i.runner.runnerId;
            i = _.omit(i, "name", "birthdate");
            return i;
          });
          this.step = "check-results";
        });
    },
    createResults() {
      let results = this.results.map(r => _.omit(r, "runner", "guid"));
      axios
        .post(BASE_URL + "/createResults", { results: results })
        .then(res => {
          this.step = "done";
        });
    },
    getSlotItemName(field) {
      return "item." + field;
    },
    edit(item) {
      this.setEditItem(item, true);
    },
    confirmEdit(item) {
      this.update('runners', item);
      this.setEditItem(item, false);
    },
    cancelEdit(item) {
      let origItem = this.runners.find(i => i.guid == item.guid);
      this.update('runnersEditCopy', origItem);
      this.setEditItem(item, false);
    },
    replaceItem(item, newItem) {
      newItem.guid = item.guid;
      this.update('runners', newItem);
      this.update('runnersEditCopy', newItem);
    },
    update(key, item) {
      this.isDirty = true;
      this[key] = this[key].map(i => i.guid == item.guid ? {...item} : i);
    },
    setEditItem(item, edit = false) {
      this.editItem = { ...this.editItem, [item.guid]: edit };
    },
  },
  mounted() {
    this.init();
  }
};
</script>
