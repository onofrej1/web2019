<template>
  <div>
    <div ref="editor"></div>
    <div id="counter"></div>
  </div>
</template>

<script>
import Quill from "quill";
import "quill/dist/quill.core.css";
import "quill/dist/quill.snow.css";
import SelectImage from "./../plugins/SelectImage.js";

Quill.register("modules/selectImage", SelectImage);

var counter = class Counter {
  constructor(quill, options) {
    this.quill = quill;
    this.options = options;
    this.container = document.querySelector(options.container);
    quill.on("text-change", this.update.bind(this));
    this.update(); // Account for initial contents
  }

  calculate() {
    let text = this.quill.getText();
    if (this.options.unit === "word") {
      text = text.trim();
      // Splitting empty text returns a non-empty array
      return text.length > 0 ? text.split(/\s+/).length : 0;
    } else {
      return text.length;
    }
  }

  update() {
    var length = this.calculate();
    var label = this.options.unit;
    if (length !== 1) {
      label += "s";
    }
    this.container.innerText = length + " " + label;
  }
};

export default {
  name: "QuillEditor",
  props: {
    value: {
      type: String,
      default: ""
    }
  },

  data() {
    return {
      editor: null
    };
  },
  mounted() {
    this.editor = new Quill(this.$refs.editor, {
      modules: {
        toolbar: {
          container: [
            [{ header: [1, 2, 3, 4, false] }],
            [
              "link",
              "bold",
              "italic",
              "underline",
              "list",
              "align",
              "image",
              "selImage"
            ]
          ],
          handlers: {
            selImage: () => console.log("gggege")
          }
        },
        /*counter: {
          container: "#counter",
          unit: "word"
        },*/
        selectImage: true
      },
      theme: "snow",
      formats: [
        "link",
        "list",
        "align",
        "bold",
        "image",
        "underline",
        "header",
        "italic"
        //"selImage"
      ]
    });

    this.editor.root.innerHTML = this.value;

    var toolbar = this.editor.getModule("toolbar");
    //toolbar.addHandler("selImage", () => console.log("xxxx"));

    this.editor.on("text-change", () => this.update());
  },

  methods: {
    update() {
      this.$emit(
        "input",
        this.editor.getText() ? this.editor.root.innerHTML : ""
      );
    }
  }
};
</script>
<style>
.ql-selImage:after {
  content: "Ω";
}
</style>

