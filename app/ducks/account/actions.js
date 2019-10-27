import {POST} from "utils/appConstants";
import fetch from "utils/fetch";
import {processResponseErrorAsFormOrNotification, processResponseErrorAsNotification} from "utils/httpUtils";

export const checkAccountAlreadyExists = (payload) => async dispatch => {
    try {
        const response = await fetch(POST, "accounts/exists", payload);
        return response.data;
    } catch (error) {
        throw processResponseErrorAsNotification(error);
    }

};

export const trySignUp = (payload) => async dispatch => {
    try {
        await fetch(POST, "accounts/sign-up", payload);
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