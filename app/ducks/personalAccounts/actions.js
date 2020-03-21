import * as types from "./types";
import fetch from "utils/fetch";
import {POST} from "utils/appConstants";
import {processResponseErrorAsFormOrNotification} from "utils/httpUtils";

const personalAccountUpdated = account => ({
    type: types.PERSONAL_ACCOUNT_UPDATED,
    account
});

export const createPersonalAccount = (payload) => async dispatch => {
    try {
        const createResponse = await fetch(POST, "personal-accounts-management/accounts", payload);
        dispatch(personalAccountUpdated(createResponse.data));
    } catch (error) {
        throw processResponseErrorAsFormOrNotification(error);
    }
};