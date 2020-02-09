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
        await fetch(POST, "accounts", payload, {});
    } catch (error) {
        throw processResponseErrorAsNotification(error);
    }

};

export const signOut = () => async dispatch => {
    localStorageService.clearToken();
    dispatch(userLoggedOut());
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
        const userDataResponse = await fetch(GET, "accounts/current");
        dispatch(userDataFetched(userDataResponse.data));
    } catch (error) {
        localStorageService.clearToken();
        dispatch(userLogInFail());
        throw processResponseErrorAsFormOrNotification(error);
    }
};