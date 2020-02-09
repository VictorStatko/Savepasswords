import {POST} from "utils/appConstants";
import fetch from "utils/fetch";
import {processResponseErrorAsFormOrNotification, processResponseErrorAsNotification} from "utils/httpUtils";

export const trySignUp = (payload) => async dispatch => {
    try {
        await fetch(POST, "accounts", payload);
    } catch (error) {
        throw processResponseErrorAsNotification(error);
    }

};

export const trySignIn = (payload) => async dispatch => {
    try {
        const response = await fetch(POST, "auth/sign-in", payload);
        localStorage.setItem('refresh-token', response.headers['refresh-token']);
    } catch (error) {
        throw processResponseErrorAsFormOrNotification(error);
    }
};