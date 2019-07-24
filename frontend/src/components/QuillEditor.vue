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

import SelectImageModal from "./../components/SelectImageModal.vue";
import store from "./../store";

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
      editor: null,
      selectionIndex: null
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
            selImage: this.ChooseImage
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

    //var toolbar = this.editor.getModule("toolbar");
    //toolbar.addHandler("selImage", () => console.log("xxxx"));

    this.editor.on("text-change", () => this.update());
    let me = this;

    this.editor.on("selection-change", function(range, oldRange, source) {
      if (range) {
        me.selectionIndex = range.index;
        if (range.length == 0) {
          console.log("User cursor is on", range.index);
        } else {
          var text = me.editor.getText(range.index, range.length);
          console.log("User has highlighted", text);
        }
      } else {
        console.log("Cursor not in the editor");
      }
    });
  },

  methods: {
    update() {
      this.$emit(
        "input",
        this.editor.getText() ? this.editor.root.innerHTML : ""
      );
    },

    ChooseImage() {
      store.dispatch("modal/show", {
        title: "Choose file",
        component: SelectImageModal,
        close: imageUrl => {
          imageUrl
            ? this.editor.insertEmbed(this.selectionIndex, "image", imageUrl)
            : false;
        }
      });
    }
  }
};
</script>
<style>
.ql-selImage:after {
  content: "Ω";
}
</style>

