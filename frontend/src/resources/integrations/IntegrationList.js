import React, { useEffect, useState } from 'react';
import {
    Datagrid,
    FunctionField,
    ListContextProvider,
    TextField,
    useList,
} from 'react-admin';

// Flattens { category: { name: config } } → [{ id, category, name, ...config }]
const flattenRoutes = (data) =>
    Object.entries(data).flatMap(([category, segments]) =>
        Object.entries(segments).map(([name, config]) => ({
            id: `${category}/${name}`,
            category,
            name,
            ...config,
        }))
    );

export const IntegrationList = () => {
    const [records, setRecords] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        fetch('http://localhost:8080/api/config/paths')
            .then((res) => res.json())
            .then((data) => {
                setRecords(flattenRoutes(data));
                setLoading(false);
            })
            .catch((err) => {
                console.error('Failed to load paths config:', err);
                setLoading(false);
            });
    }, []);

    const listContext = useList({ data: records, isLoading: loading });

    return (
        <ListContextProvider value={listContext}>
            <Datagrid bulkActionButtons={false}>
                <TextField source="category" label="Category" />
                <TextField source="name"     label="Name" />
                <TextField source="message"  label="Message" />
                <TextField source="httpStatus" label="HTTP Status" />
                <FunctionField
                    label="Allowed Methods"
                    render={(record) =>
                        record?.allowedMethods?.length
                            ? record.allowedMethods.join(', ')
                            : '—'
                    }
                />
                <FunctionField
                    label="Target URL"
                    render={(record) => record?.targetUrl ?? '—'}
                />
            </Datagrid>
        </ListContextProvider>
    );
};
