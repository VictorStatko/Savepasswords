import axios from "axios";
import {BACKEND_URL} from "./appConstants";
import {LocalStorageService} from "localStorage";
import queryString from "query-string";

const localStorageService = LocalStorageService.getService();

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
            Promise.reject(error)
        });

    transport.interceptors.response.use((response) => {
            return response
        },
        function (error) {
            const originalRequest = error.config;
            if (error.response.status === 401) {

                return axios.post(`${BACKEND_URL}${'auth/token'}`,
                    queryString.stringify({
                        "refresh_token": localStorageService.getRefreshToken(),
                        "grant_type": "refresh_token",
                        "client_id": "webclient"
                    }), {
                        headers: {"Content-Type": "application/x-www-form-urlencoded"}
                    })
                    .then(res => {
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
                    })
            }
        });

    return transport({
        method,
        url: `${BACKEND_URL}${path}`,
        data,
        headers
    });
};