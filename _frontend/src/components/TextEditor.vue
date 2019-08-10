<template>
  <div class="editor-wrapper">
    <ckeditor :editor="editor" v-model="editorData" :config="editorConfig"></ckeditor>
  </div>
</template>

<script>
import ClassicEditor from "@ckeditor/ckeditor5-editor-classic/src/classiceditor";

import Essentials from "@ckeditor/ckeditor5-essentials/src/essentials";
import UploadAdapter from "@ckeditor/ckeditor5-adapter-ckfinder/src/uploadadapter";
import Autoformat from "@ckeditor/ckeditor5-autoformat/src/autoformat";
import Bold from "@ckeditor/ckeditor5-basic-styles/src/bold";
import Italic from "@ckeditor/ckeditor5-basic-styles/src/italic";
import BlockQuote from "@ckeditor/ckeditor5-block-quote/src/blockquote";
import CKFinder from "@ckeditor/ckeditor5-ckfinder/src/ckfinder";
import EasyImage from "@ckeditor/ckeditor5-easy-image/src/easyimage";
import Heading from "@ckeditor/ckeditor5-heading/src/heading";
import Image from "@ckeditor/ckeditor5-image/src/image";
import ImageCaption from "@ckeditor/ckeditor5-image/src/imagecaption";
import ImageStyle from "@ckeditor/ckeditor5-image/src/imagestyle";
import ImageToolbar from "@ckeditor/ckeditor5-image/src/imagetoolbar";
import ImageUpload from "@ckeditor/ckeditor5-image/src/imageupload";
import Link from "@ckeditor/ckeditor5-link/src/link";
import List from "@ckeditor/ckeditor5-list/src/list";
import MediaEmbed from "@ckeditor/ckeditor5-media-embed/src/mediaembed";
import Paragraph from "@ckeditor/ckeditor5-paragraph/src/paragraph";
import PasteFromOffice from "@ckeditor/ckeditor5-paste-from-office/src/pastefromoffice";
import Table from "@ckeditor/ckeditor5-table/src/table";
import TableToolbar from "@ckeditor/ckeditor5-table/src/tabletoolbar";

import imageIcon from '@ckeditor/ckeditor5-core/theme/icons/image.svg';

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
          Essentials,
          UploadAdapter,
          Autoformat,
          Bold,
          Italic,
          BlockQuote,
          CKFinder,
          EasyImage,
          Heading,
          Image,
          ImageCaption,
          ImageStyle,
          ImageToolbar,
          ImageUpload,
          Link,
          List,
          MediaEmbed,
          Paragraph,
          PasteFromOffice,
          Table,
          TableToolbar,
          InsertImage
          //CustomPlugin
        ],

        toolbar: {
          items: [
            "heading",
            "|",
            "bold",
            "italic",
            "link",
            "bulletedList",
            "numberedList",
            "imageUpload",
            "blockQuote",
            "insertTable",
            "mediaEmbed",
            "undo",
            "redo",
            "insertImage"
          ]
        },
        image: {
          toolbar: [ 'imageStyle:contact', 'imageTextAlternative', '|', 'imageStyle:alignLeft', 'imageStyle:full', 'imageStyle:alignRight' ],
            styles: [
                { name: 'full', icon: 'right', title: 'aaa' },
                { name: 'contact', icon: imageIcon, title: 'My contact style', className: 'my-contact' }
            ]
        },
        table: {
          contentToolbar: ["tableColumn", "tableRow", "mergeTableCells"]
        },
        // This value must be kept in sync with the language defined in webpack.config.js.
        language: "en"
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
<style>
.my-contact {
  border: 2px solid red;
  width: 100%;
}
</style>

