import React from 'react';
import {
    List,
    Datagrid,
    TextField,
    FunctionField,
} from 'react-admin';

export const IntegrationList = () => (
    <List title="Integrations & Allowed Paths">
        <Datagrid bulkActionButtons={false}>
            <TextField source="id" label="Path" />
            <TextField source="message" label="Message" />
            <TextField source="httpStatus" label="HTTP Status" />
            <FunctionField
                source="allowedMethods"
                label="Allowed Methods"
                render={(record) =>
                    record?.allowedMethods ? record.allowedMethods.join(', ') : '-'
                }
            />
            <TextField source="targetUrl" label="Target URL" emptyText="-" />
        </Datagrid>
    </List>
);