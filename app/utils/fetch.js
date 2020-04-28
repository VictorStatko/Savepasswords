import axios from "axios";
import {BACKEND_URL} from "./appConstants";
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
            if (error.response && error.response.status === 401) {
                const logoutTry = error.config.url === `${BACKEND_URL}${'auth/logout'}`;
                await indexedDBService.clearKeys();
                store.dispatch(userLoggedOut());

                if (!logoutTry) {
                    toast.error(i18n.t('global.auth.sessionExpired'));
                    return Promise.reject(error.response);
                }

                return Promise.resolve();

            } else {
                return Promise.reject(error.response)
            }
        });

    return transport({
        method,
        url: `${BACKEND_URL}${path}`,
        data,
        headers
    });
};