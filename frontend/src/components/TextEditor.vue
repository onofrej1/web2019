<template>
  <div class="editor-wrapper">
    <v-btn @click="setContent()">Click</v-btn>
    <ckeditor
      @change="console.log('ccc')"
      :editor="editor"
      v-model="editorData"
      :config="editorConfig"
    ></ckeditor>
    {{ editorData }}
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
      //editorData: "<p>Content of the editor.</p>",
      editorConfig: {
        plugins: [
          EssentialsPlugin,
          BoldPlugin,
          ItalicPlugin,
          LinkPlugin,
          ParagraphPlugin,
          Image,
          InsertImage,
          this.ChangeContent
        ],

        toolbar: {
          items: ["bold", "italic", "link", "undo", "redo", "insertImage"]
        }
      }
    };
  },
  computed: {
    editorDataxxx: function() {
      return this.data;
    }
  },
  methods: {
    onReady(editor) {
      console.log("rredy");
      console.log(editor);
    },
    setContent() {
      console.log("set");
      //this.editor.setData('xxx');
    },
    ChangeContent(editor) {
      console.log(editor);
      console.log(this);
      let emit = this.$emit;
      editor.model.document.on("change", () => {
        emit("input", this.editorData);
        console.log("The Document has changed!");
      });
    }
  }
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
h3 {
  margin: 40px 0 0;
}
</style>
