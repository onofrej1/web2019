<template>
  <div>
    <div ref="editor"></div>
  </div>
</template>

<script>
import Quill from "quill";
import "quill/dist/quill.core.css";
import "quill/dist/quill.snow.css";
import SelectImage from "./../plugins/SelectImage.js";

import SelectImageModal from './../components/SelectImageModal.vue';
import store from './../store'

Quill.register("modules/selectImage", SelectImage);

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
            ["bold", "italic", "underline", "strike"], // toggled buttons
            ["blockquote", "code-block"],

            [{ header: 1 }, { header: 2 }], // custom button values
            [{ list: "ordered" }, { list: "bullet" }],
            [{ indent: "-1" }, { indent: "+1" }], // outdent/indent

            [{ size: ["small", false, "large", "huge"] }], // custom dropdown
            [{ header: [1, 2, 3, 4, 5, 6, false] }],

            [{ color: [] }, { background: [] }], // dropdown with defaults from theme
            [{ font: [] }],
            [{ align: [] }],

            ["clean", "selImage"], // remove formatting button

            ["link", "image", "video"] // link and image, video
          ],
          handlers: {
            selImage: ChooseImage.bind(this)
          }
        }
        
        //selectImage: true
      },
      theme: "snow",
      formats: [
        "link",
        "header",
        "color",
        "font",
        "code-block",
        "blockquote",
        "clean",
        "strike",
        "size",
        "background",
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

function ChooseImage() {
  insert = insert.bind(this);
  let selection = this.editor.getSelection();
  
  store.dispatch("modal/show", {
    title: "Choose file",
    component: SelectImageModal,
    close: imageUrl => (imageUrl ? insert(imageUrl, selection) : false)
  });
}

function insert(image, selection) {
  //console.log(selection);
  this.editor.insertEmbed(selection, 'image', image);
}
</script>
<style>
.ql-selImage:after {
  content: "Ω";
}
</style>

