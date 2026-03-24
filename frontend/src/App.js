import React from 'react';
import { Admin, Resource } from 'react-admin';
import jsonServerProvider from 'ra-data-json-server';
import { PostList, PostEdit, PostCreate } from './posts';
const dataProvider = jsonServerProvider('https://jsonplaceholder.typicode.com');


function App() {
  return (
      <Admin dataProvider={dataProvider}>
        <Resource
            name="posts"
            list={PostList}
            edit={PostEdit}
            create={PostCreate}
        />
      </Admin>
  );
}

export default App;