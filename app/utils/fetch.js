import axios from "axios";
import {BACKEND_URL} from "./appConstants";
import {LocalStorageService} from "localStorage";
import queryString from "query-string";
import {toast} from "react-toastify";
import i18n from "../i18n";
import {store} from "app";
import {userLoggedOut} from "ducks/account/actions";
import {IndexedDBService} from "indexedDB";

const localStorageService = LocalStorageService.getService();
const indexedDBService = IndexedDBService.getService();

export default async (method, path, data, headers) => {
    const transport = axios.create({
        withCredentials: true
    });

    transport.interceptors.request.use(
        config => {
            const token = localStorageService.getAccessToken();
            if (token) {
                config.headers['Authorization'] = 'Bearer ' + token;
            }
            return config;
        },
        error => {
            return Promise.reject(error.response)
        });

    transport.interceptors.response.use((response) => {
            return response
        },
        async function (error) {
            const originalRequest = error.config;

            if (error.response && error.response.status === 401) {
                const refreshToken = localStorageService.getRefreshToken();
                const logoutTry = error.config.url === `${BACKEND_URL}${'auth/logout'}`;

                if (logoutTry || !refreshToken) {
                    localStorageService.clearToken();
                    await indexedDBService.clearKeys();
                    store.dispatch(userLoggedOut());

                    if (!logoutTry) {
                        toast.error(i18n.t('global.auth.sessionExpired'));
                    }

                    return;
                }

                try {
                    const res = await axios.post(`${BACKEND_URL}${'auth/token'}`,
                        queryString.stringify({
                            "refresh_token": refreshToken,
                            "grant_type": "refresh_token",
                            "client_id": "webclient"
                        }), {
                            headers: {"Content-Type": "application/x-www-form-urlencoded"}
                        });

                    if (res.status === 200) {
                        localStorageService.setToken(
                            {
                                access_token: res.data.access_token,
                                refresh_token: res.data.refresh_token
                            }
                        );

                        originalRequest.headers['Authorization'] = 'Bearer ' + localStorageService.getAccessToken();

                        return axios(originalRequest);
                    }
                } catch (e) {
                    toast.error(i18n.t('global.auth.sessionExpired'));
                    localStorageService.clearToken();
                    await indexedDBService.clearKeys();
                    store.dispatch(userLoggedOut());
                }

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