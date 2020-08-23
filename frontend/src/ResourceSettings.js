const moment = require('moment');
// import ResultList from './components/ResultList.vue';
import { VIcon /* ,VTextField */} from 'vuetify/lib'


/* var footer = {
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
  components: {VTextField},
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
*/

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

const articles = {
  title: 'Article',
  group: "Blog",
  filter: [{
    field: 'content',
    type: 'text',
    'op': 'like',
    label: 'Content'
  }],
  form: [
    {
      name: 'title',
      type: 'text'
    },
    {
      name: 'category',
      type: 'relation',
      resource: 'categories',
      //show: 'category',
      render: (item) => `${item.category}`
    },
    {
      name: 'source',
      type: 'text'
    },
    {
      name: 'published_by',
      type: 'text'
    },
    {
      name: 'published_at',
      type: 'date'
    },
    {
      name: 'content',
      type: 'editor'
    },
    { name: 'tags', type: 'auto', url: 'api/tags/search/findByTag', resource: 'tags', show: 'tag', render: (item) => `${item.tag}`, label: 'Tags' },
  ],
  list: [{
      field: 'title'
    },
    {
      field: 'published_by'
    },
    {
      field: 'published_at'
    }
  ]
}

const users = {
  title: 'User',
  group: 'Users',
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
    { name: 'roles', type: 'pivotRelation', resource: 'roles', show: 'name', label: 'Roles' },
  ],
  list: [{
      field: 'username'
    },
    {
      field: 'email'
    },
  ]
}

const permissions = {
  title: 'Permission',
  group: 'Users',
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

const roles = {
  title: 'Role',
  group: 'Users',
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
    { label: 'Menu', name: 'menu', validate: 'required', type: 'relation', resource: 'menus', show: 'name' },
    { label: 'Page', name: 'page', validate: 'required', type: 'relation', resource: 'pages', show: 'title' },
    { label: 'Parent', name: 'parent', type: 'relation', resource: 'menuItems', show: 'title' },
  ],
  list: [
    { field: 'title', label: 'Title' },
    { field: 'link', label: 'Link' }
  ],
}

const results = {
  title: 'Results',
  filter: [],
  actions: ['create'],
  apiUrl: 'api/results?with=runner',
  group: 'Runs',
  form: [
    { label: 'Place', name: 'place', type: 'text' },
    { label: 'Category', name: 'category', type: 'text' },
    { label: 'Runner', name: 'runner', validate: 'required', type: 'relation', resource: 'runners', render: (item) => `${item.last_name} ${item.first_name}`},
  ],
  list: [
    { field: 'place', label: 'Place' },
    { field: 'last_name', label: 'Name', render: (item) => `<div>${item.runner.last_name} ${item.runner.first_name}</div>`},
    { field: 'category', label: 'Category' },
    { field: 'start_number', label: 'Running number' },
    { field: 'finish_time', label: 'Finish time' },
  ],
  // listView: ResultList,
  fetch: false,
};

/* const hamburg = {
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
}; */

const tags = {
  title: "Tag",
  group: "Blog",
  filter: [],
  form: [
    { label: 'Tag', name: 'tag', type: 'text' },
  ],
  list: [
    { field: 'tag', label: 'Tag' }
  ]
};

const categories = {
  title: "Category",
  group: "Blog",
  filter: [],
  form: [
    { label: 'Category', name: 'category', type: 'text' },
  ],
  list: [
    { field: 'category', label: 'Category' }
  ]
};

const pages = {
  title: "Page",
  group: "Content",
  filter: [],
  form: [
    { label: 'Title', name: 'title', type: 'text' },
    { label: 'Content', name: 'content', type: 'text'}
    /* { type: "editor", label: "Content", name: 'content' }, */
  ],
  list: [
    { field: 'title', label: 'Title', render: (item) => `<div>${item.title}</div>` }
  ],
};

const news = {
  title: "News",
  group: "Content",
  filter: [],
  form: [{
      label: 'Content',
      name: 'content',
      // type: 'editor'
      type: 'text',
    },
    {
      type: "date",
      label: "Published on",
      name: 'published'
    },
  ],
  customForm: bookForm,
  list: [{
      field: 'content',
      label: 'Content'
    },
    {
      field: 'published',
      label: 'Published'
    }
  ],
};

const events = {
  title: 'Event',
  group: "Runs",
  apiUrl: 'api/events?with=Run',
  filter: [{
    field: 'run',
    type: 'select',
    op: 'eq'
  },
  {
    field: 'event',
    type: 'text',
    op: 'like'
  }
],
  form: [{
      name: 'event',
      type: 'text'
    },
    {
      name: 'edition',
      type: 'text'
    },
    {
      name: 'run',
      type: 'relation',
      resource: 'runs',
      show: 'run',
    },
    {
      name: 'event_date',
      type: 'date'
    },
  ],
  list: [{
      field: 'event'
    },
    {
      field: 'event_date',
      label: 'Date',
      render: (item) => `${moment(item.event_date, 'YYYY-MM-DD').format('DD/MM/YYYY')}`
    },
    {
      field: 'run',
      label: 'Run',
      render: (item) => item.event ? item.event.name : ''
    },
    {
      field: 'edition'
    },
  ],
  //footer: footer
};

const expandRun = () => {
  return {
    name: 'my-footer',
    props: ['row'],
    components: {VIcon},
    template: `
    <td :colspan="4">
      <p v-for="run in row.runs">
        <v-icon :key="row.id" small>event</v-icon> {{ run.edition }}. rocnik 
        {{ run.runDate }}
      </p>
    </td>
    `
  };
}

const runs = {
  title: 'Run',
  group: "Runs",
  apiUrl: '',
  filter: [{
    field: 'name',
    type: 'select',
    'op': 'eq'
  }],
  form: [{
      name: 'run',
      type: 'text'
    },
    {
      name: 'locality',
      type: 'text'
    },
    /* {
      name: 'runs',
      type: 'inline',
      editLink: 'Edit runs',
      form: runInline
    }, */
  ],
  list: [
    {
      field: 'run'
    },
    {
      field: 'locality'
    },
  ],
  bulkActions: [{}],
  expandRow: expandRun(),
  /*footer: {
    template: '<span>fffoter</span>'
  },*/
  /*header: {
    template: '<span>fffoter</span>'
  }*/
};

const runners = {
  title: 'Runner',
  group: "Runs",
  filter: [{
    field: 'lastName',
    type: 'text',
    'op': 'like'
  }],
  form: [{
      name: 'first_name',
      type: 'text'
    },
    {
      name: 'last_name',
      type: 'text'
    },
    {
      name: 'born',
      type: 'date'
    },
  ],
  list: [{
      field: 'last_name'
    },
    {
      field: 'first_name'
    },
    {
      field: 'born',
      label: 'Date',
      render: (item) => `${moment(item.born, 'YYYY-MM-DD').format('YYYY')}`
    },
  ],
  //listView: RunnerList,
  //header: header,
  //items: items
};

export default {
  users,
  articles,
  categories,
  tags,
  runs,
  events,
  runners,
  roles,
  permissions,
  pages,
  menus,
  menuItems,
  results,
  news,
};