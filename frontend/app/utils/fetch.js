import axios from "axios";
import {toast} from "react-toastify";
import i18n from "../i18n";
import {store} from "app";
import {userLoggedOut} from "ducks/account/actions";
import {IndexedDBService} from "indexedDB";

const indexedDBService = IndexedDBService.getService();

export default async (method, path, data, headers) => {
    const transport = axios.create({
        withCredentials: true
    });

    transport.interceptors.response.use((response) => {
            return response
        },
        async function (error) {
            const logoutTry = error.config.url === `${process.env.REACT_APP_BACKEND_URL}${'auth/logout'}`;

            if (logoutTry) {
                await indexedDBService.clearKeys();
                store.dispatch(userLoggedOut());
                return Promise.resolve();
            } else if (error.response && error.response.status === 401) {
                await indexedDBService.clearKeys();
                store.dispatch(userLoggedOut());
                toast.error(i18n.t('global.auth.sessionExpired'));
                return Promise.reject(error.response);
            } else {
                return Promise.reject(error.response)
            }
        });

    return transport({
        method,
        url: `${process.env.REACT_APP_BACKEND_URL}${path}`,
        data,
        headers
    });
};