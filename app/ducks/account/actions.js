import {GET, POST} from "utils/appConstants";
import fetch from "utils/fetch";
import {processResponseErrorAsFormOrNotification, processResponseErrorAsNotification} from "utils/httpUtils";
import queryString from "query-string";
import * as types from "./types";
import {LocalStorageService} from "localStorage";

const localStorageService = LocalStorageService.getService();

export const userLoggedIn = () => ({
    type: types.SIGN_IN
});

export const userLoggedOut = () => ({
    type: types.SIGN_OUT
});

export const userLogInFail = () => ({
    type: types.SIGN_IN_FAIL
});

export const userDataFetched = data => ({
    type: types.DATA_FETCH,
    data
});

export const trySignUp = (payload) => async dispatch => {
    try {
        await fetch(POST, "auth/accounts", payload);
    } catch (error) {
        throw processResponseErrorAsNotification(error);
    }

};

export const signOut = () => async dispatch => {
    try {
        await fetch(POST, "auth/logout");
        localStorageService.clearToken();
        dispatch(userLoggedOut());
    } catch (error) {
        throw processResponseErrorAsNotification(error);
    }
};

export const fetchClientEncryptionSalt = (email) => async dispatch => {
    try {
        const response = await fetch(GET, `auth/accounts/client-encryption-salt?email=${email}`);
        return response.data.value;
    } catch (error) {
        throw processResponseErrorAsFormOrNotification(error);
    }
};


export const trySignIn = (payload) => async dispatch => {
    try {
        payload.grant_type = "password";
        payload.client_id = "webclient";
        const loginResponse = await fetch(POST, "auth/token", queryString.stringify(payload), {'Content-Type': 'application/x-www-form-urlencoded'});
        localStorageService.setToken(
            {
                access_token: loginResponse.data.access_token,
                refresh_token: loginResponse.data.refresh_token
            }
        );
        dispatch(userLoggedIn());
        const userDataResponse = await fetch(GET, "auth/accounts/current");
        dispatch(userDataFetched(userDataResponse.data));
    } catch (error) {
        localStorageService.clearToken();
        dispatch(userLogInFail());
        throw processResponseErrorAsFormOrNotification(error);
    }
};