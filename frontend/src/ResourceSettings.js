const moment = require('moment');
import RunnerList from './components/RunnerList.vue';

var footer = {
  name: 'my-footer',
  template: '<b>footer vv</b>'
};

var header = {
  name: 'my-header',
  props: ['props'],
  template: `
    <tr>
        <th
          v-for="header in props.headers"
          :key="header.text"
      
        >
          <v-icon small>arrow_upward</v-icon>
          {{ header.text }}
        </th>
      </tr>
  `
};

var items = {
  name: 'my-items',
  props: ['props'],
  template: `
    <tr>
      <td>
        <v-checkbox
          v-model="props.selected"
          primary
          hide-details
        ></v-checkbox>
      </td>
      <td>{{ props.item.lastName }}</td>
    </tr>
  `
}

var runInline = {
  name: 'inline',
  props: ['model'],
  template: `
    <div>
      <v-text-field
        v-model="model.id"
        label="Id"
        v-show="false"
        required>
      </v-text-field>
      <v-text-field
        v-model="model.name"
        label="Name"
        required>
      </v-text-field>
      <v-text-field
        v-model="model.edition"
        label="Edition"
        required>
      </v-text-field>      
    </div>
  `
};

var bookForm = {
  name: 'bookForm',
  props: ['model'],
  template: `
    <div>
      <v-select
      :items="[{value: 'aaa', text: 'AAA'}, {value: 'bbb', text: 'BBB}]"
      v-model="model.name"
      label="AAA"
    ></v-select>
    </div>
  `
};

/*const user = {
  title: 'Users',
  form: [
    { name: 'name', type: 'text' },
    { name: 'email', type: 'text' },
    { name: 'roles', type: 'pivotRelation', resourceTable: 'role', show: 'title', label: 'Roles' },
  ],
  list: [
    { field: 'name', label: 'Name' },
    { field: 'email', label: 'Email' },
  ]
}*/

const authors = {
  title: 'Author',
  group: "Runs",
  //apiUrl: '',
  filter: [{
    field: 'name',
    type: 'text',
    'op': 'contains',
    label: 'Text'
  }],
  form: [{
      name: 'name',
      type: 'text'
    },
    {
      name: 'age',
      type: 'text'
    },
   /* {
      name: 'book_list',
      label: 'Books',
      type: 'inline',
      form: inline
    }*/
    //{ name: 'content', type: 'editor' },
    { name: 'books', type: 'pivotRelation', resourceTable: 'books', show: 'title', label: 'Books' },
  ],
  list: [{
      field: 'name'
    },
    {
      field: 'age'
    },

    /* { render: props => {
       console.log(props);
       return 'aaa'+ props.item.text;
     } },*/
  ]
}

/*const posts = {
  title: 'Posts',
  filter: [{
      field: 'title',
      type: 'select',
      'op': 'eq',
      label: 'Title'
    },
    {
      field: 'text',
      type: 'text',
      'op': 'contains',
      label: 'Text'
    }
  ],
  form: [{
      name: 'title',
      type: 'text'
    },
    {
      name: 'author',
      type: 'text'
    },
    {
      name: 'text',
      type: 'text'
    },
    //{ name: 'content', type: 'editor' },
    //{ name: 'tags', type: 'pivotRelation', resourceTable: 'tag', show: 'title', label: 'Tags' },
  ],
  list: [{
      field: 'title'
    },
    {
      field: 'author'
    },
    {
      field: 'text'
    },
     { render: props => {
       console.log(props);
       return 'aaa'+ props.item.text;
     } },
  ],
  footer: footer2,
}*/

const users = {
  title: 'User',
  group: 'Permissions',
  filter: [{
    field: 'username',
    type: 'text',
    'op': 'eq'
  }],
  form: [{
      name: 'username',
      type: 'text'
    },
    {
      name: 'email',
      type: 'text'
    },    
    { name: 'roles', type: 'pivotRelation', resourceTable: 'roles', show: 'name', label: 'Roles' },
  ],
  list: [{
      field: 'username'
    },
    {
      field: 'email'
    },
  ]
}

const roles = {
  title: 'Role',
  group: 'Permissions',
  filter: [{
    field: 'name',
    type: 'text',
    'op': 'eq'
  }],
  form: [{
      name: 'name',
      type: 'text'
    },        
  ],
  list: [{
      field: 'name'
    },    
  ]
}

const menus = {
  title: 'Menu',
  filter: [],
  group: 'Content',
  form: [
    { label: 'Name', name: 'name', type: 'text' },
  ],
  list: [
    { field: 'name', label: 'Title'}
  ],
}

const menuItems = {
  title: 'MenuItem',
  filter: [],
  group: 'Content',
  form: [
    { label: 'Title', name: 'title', type: 'text' },
    { label: 'Link', name: 'link', type: 'text' },
    { label: 'Menu', name: 'menu', type: 'relation', resourceTable: 'menus', show: 'name' },
    { label: 'Page', name: 'page', type: 'relation', resourceTable: 'pages', show: 'title' },
    { label: 'Parent', name: 'parent', type: 'relation', resourceTable: 'menuItems', show: 'title' },
  ],
  list: [
    { field: 'title', label: 'Title' },
    { field: 'link', label: 'Link' }
  ],
}

/*



const hamburg = {
  title: "Pages",
  form: [
    { label: 'Title', name: 'title', type: 'text' },
    { label: 'Date', name: 'event_date', type: 'datepicker' },
    { label: "Notes", type: "textarea", rows: 8, name: 'notes' },
  ],
  list: [
    { field: 'title', label: 'Title' },
    { label: 'Date', render: (row) => `<div>${row.event_date}</div>` }
  ],
};

const tag = {
  title: "Tags",
  form: [
    { label: 'title', name: 'title', type: 'text' },
  ],
  list: [
    { field: 'title', label: 'Title' }
  ]
};*/

const pages = {
  title: "Page",
  group: "Content",
  filter: [],
  form: [
    { label: 'Title', name: 'title', type: 'text' },
    { type: "text", label: "Content", name: 'content' },
  ],
  list: [
    { field: 'title', label: 'Title', render: (row) => `<div>${row.item.title}</div>` }
  ],
};

const books = {
  title: "Book",
  group: "Runs",
  filter: [],
  form: [{
      label: 'Title',
      name: 'title',
      type: 'text'
    },
    {
      type: "text",
      label: "Description",
      name: 'description'
    },
  ],
  customForm: bookForm,
  list: [{
      field: 'title',
      label: 'Title'
    },
    {
      field: 'description',
      label: 'Description'
    }
  ],
};

const aaa = (props) => {
  //console.log(props);
  var xxx = {
    name: 'my-footer',
    props: ['row'],
    template: `
    <div style="padding:10px">
      <p v-for="run in row.runs">
        <v-icon :key="row.id" small>event</v-icon> {{ run.edition }}. rocnik 
        {{ run.runDate }}
      </p>
    </div>
    `
  };
  return xxx;
}

const runs = {
  title: 'Run',
  group: "Runs",
  filter: [{
    field: 'name',
    type: 'select',
    op: 'eq'
  },
  {
    field: 'event',
    type: 'text',
    op: 'contains'
  }
],
  form: [{
      name: 'name',
      type: 'text'
    },
    {
      name: 'edition',
      type: 'text'
    },
    {
      name: 'event',
      type: 'relation',
      resourceTable: 'events',
      show: 'name',
    },
    {
      name: 'runDate',
      type: 'date'
    },
  ],
  list: [{
      field: 'name'
    },
    {
      field: 'runDate',
      label: 'Date',
      render: (props) => `${moment(props.item.runDate, 'YYYY-MM-DD').format('DD/MM/YYYY')}`
    },
    {
      field: 'event',
      label: 'Event',
      render: (props) => props.item.event ? props.item.event.name : ''
    },
    {
      field: 'edition'
    },
  ],
  footer: footer
};

const events = {
  title: 'Event',
  group: "Runs",
  apiUrl: '',
  filter: [{
    field: 'name',
    type: 'select',
    'op': 'eq'
  }],
  form: [{
      name: 'name',
      type: 'text'
    },
    {
      name: 'locality',
      type: 'text'
    },
    {
      name: 'runs',
      type: 'inline',
      form: runInline
    },
  ],
  list: [
    /*{
      field: 'xx',
      render: (props) => `<v-icon>info</v-icon>`
    },*/
    {
      field: 'name'
    },
    {
      field: 'locality'
    },
  ],
  expandRow: aaa(),
};

const runners = {
  title: 'Runner',
  group: "Runs",
  filter: [{
    field: 'lastName',
    type: 'text',
    'op': 'contains'
  }],
  form: [{
      name: 'firstName',
      type: 'text'
    },
    {
      name: 'lastName',
      type: 'text'
    },
    {
      name: 'birthday',
      type: 'date'
    },
  ],
  list: [{
      field: 'lastName'
    },
    {
      field: 'firstName'
    },
    {
      field: 'birthday',
      label: 'Date',
      render: (props) => `${moment(props.item.birthday, 'YYYY-MM-DD').format('DD/MM/YYYY')}`
    },
  ],
  //listView: RunnerList,
  //header: header,
  items: items
};

export default {
  users,
  //posts,
  authors,
  books,
  runs,
  events,
  runners,
  roles,
  pages,
  menus,
  menuItems
};