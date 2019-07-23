import SelectImageModal from './../components/SelectImageModal.vue';
import store from './../store'

export default class SelectImage {
    constructor(quill, options) {
        this.quill = quill;
        this.options = options;
        //this.container = document.querySelector(options.container);
        //quill.on("text-change", this.update.bind(this));
        this.modal();
    }

    modal() {
        this.quill.getModule('toolbar').addHandler('selImage', () => {
            console.log('shoh modal');
        });
    }
}