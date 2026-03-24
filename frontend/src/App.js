import React from 'react';
import { Admin, Resource } from 'react-admin';
import dataProvider from './dataProvider';
import { IntegrationList } from './resources/integrations/IntegrationList';

function App() {
    return (
        <Admin dataProvider={dataProvider}>
            <Resource
                name="integrations"
                list={IntegrationList}
            />
        </Admin>
    );
}

export default App;