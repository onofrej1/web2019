import Plugin from '@ckeditor/ckeditor5-core/src/plugin';
import imageIcon from '@ckeditor/ckeditor5-core/theme/icons/image.svg';
import ButtonView from '@ckeditor/ckeditor5-ui/src/button/buttonview';

import SelectImageModal from './../components/SelectImageModal.vue';
import store from './../store'

class InsertImage extends Plugin {
    init() {
        const editor = this.editor;

        editor.ui.componentFactory.add('insertImage', locale => {
            const view = new ButtonView(locale);

            view.set({
                label: 'Insert image',
                icon: imageIcon,
                tooltip: true
            });

            view.on('execute', () => {
                store.dispatch('modal/show', {
                    title: "Choose file",
                    component: SelectImageModal,
                    close: (imageUrl) => imageUrl ? this.insert(imageUrl): false
                });
            });

            return view;
        });
    }

    insert(imageUrl) {
        const editor = this.editor;

        editor.model.change(writer => {
            const imageElement = writer.createElement('image', {
                src: imageUrl,
            });
            editor.model.insertContent(imageElement, editor.model.document.selection);
        });
    }
}

export default InsertImage;