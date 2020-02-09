import {POST} from "utils/appConstants";
import fetch from "utils/fetch";
import {processResponseErrorAsFormOrNotification, processResponseErrorAsNotification} from "utils/httpUtils";
import queryString from "query-string";
import * as types from "./types";
import {LocalStorageService} from "localStorage";

const localStorageService = LocalStorageService.getService();

export const userLoggedIn = () => ({
    type: types.SIGN_IN
});

export const trySignUp = (payload) => async dispatch => {
    try {
        await fetch(POST, "accounts", payload, {});
    } catch (error) {
        throw processResponseErrorAsNotification(error);
    }

};

export const trySignIn = (payload) => async dispatch => {
    try {
        payload.grant_type = "password";
        payload.client_id = "webclient";
        const response = await fetch(POST, "auth/token", queryString.stringify(payload), {'Content-Type': 'application/x-www-form-urlencoded'});
        localStorageService.setToken(
            {
                access_token: response.data.access_token,
                refresh_token: response.data.refresh_token
            }
        );
        dispatch(userLoggedIn());

    } catch (error) {
        throw processResponseErrorAsFormOrNotification(error);
    }
};