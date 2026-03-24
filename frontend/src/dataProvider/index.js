import { fetchUtils } from 'react-admin';

const API_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080/api';

const httpClient = (url, options = {}) => {
    return fetchUtils.fetchJson(url, options);
};

const dataProvider = {
    getList: async (resource, params) => {
        if (resource !== 'integrations') {
            throw new Error(`Unknown resource: ${resource}`);
        }

        const { json } = await httpClient(`${API_URL}/config/paths`);

        const data = Object.entries(json).map(([key, value]) => ({
            id: key,
            ...value
        }));

        return {
            data: data,
            total: data.length,
        };
    },

    // Required stubs (we don't use them yet)
    getOne: () => Promise.reject(new Error('Not implemented')),
    getMany: () => Promise.reject(new Error('Not implemented')),
    getManyReference: () => Promise.reject(new Error('Not implemented')),
    create: () => Promise.reject(new Error('Not implemented')),
    update: () => Promise.reject(new Error('Not implemented')),
    updateMany: () => Promise.reject(new Error('Not implemented')),
    delete: () => Promise.reject(new Error('Not implemented')),
    deleteMany: () => Promise.reject(new Error('Not implemented')),
};

export default dataProvider;