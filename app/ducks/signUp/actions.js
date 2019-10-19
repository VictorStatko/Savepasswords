import {POST} from "utils/appConstants";
import fetch from "utils/fetch";
import {processResponseError} from "utils/httpUtils";

export const checkAccountAlreadyExists = (payload) => async dispatch => {
    try {
        const response = await fetch(POST, "accounts/exists", payload);
        return response.data;
    } catch (response) {
        processResponseError(response);
    }

};