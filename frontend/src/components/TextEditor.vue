<template>
  <div class="editor-wrapper">
    <ckeditor
      :editor="editor"
      v-model="editorData"
      :config="editorConfig"
    ></ckeditor>
  </div>
</template>

<script>
import ClassicEditor from "@ckeditor/ckeditor5-editor-classic/src/classiceditor";

import EssentialsPlugin from "@ckeditor/ckeditor5-essentials/src/essentials";
import BoldPlugin from "@ckeditor/ckeditor5-basic-styles/src/bold";
import ItalicPlugin from "@ckeditor/ckeditor5-basic-styles/src/italic";
import LinkPlugin from "@ckeditor/ckeditor5-link/src/link";
import Image from "@ckeditor/ckeditor5-image/src/image";
import ParagraphPlugin from "@ckeditor/ckeditor5-paragraph/src/paragraph";
import InsertImage from "./../plugins/InsertImage";

export default {
  name: "TextEditor",
  props: {
    data: String
  },
  data() {
    return {
      editor: ClassicEditor,
      editorData: this.data,
      editorConfig: {
        plugins: [
          EssentialsPlugin,
          BoldPlugin,
          ItalicPlugin,
          LinkPlugin,
          ParagraphPlugin,
          Image,
          InsertImage
          //CustomPlugin
        ],

        toolbar: {
          items: ["bold", "italic", "link", "undo", "redo", "insertImage"]
        }
      }
    };
  },
  watch: {
    editorData: function(newVal, oldVal) {
      this.$emit("input", newVal);
      //console.log("value changed from " + oldVal + " to " + newVal);
    }
  },
  computed: {},
  methods: {}
};

function customPlugin(editor) {
  editor.model.document.on("change", () => {
    console.log("The Document has changed!");
  });
}
</script>

