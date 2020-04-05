import fetch from "utils/fetch";
import {POST} from "utils/appConstants";
import {processErrorAsFormOrNotification} from "utils/errorHandlingUtils";
import * as types from "./types";


const folderUpdated = folder => ({
    type: types.FOLDER_UPDATED,
    folder
});

export const createFolder = (folder) => async dispatch => {
    try {
        const createResponse = await fetch(POST, "personal-accounts-management/folders", folder);
        dispatch(folderUpdated(createResponse.data));
    } catch (error) {
        throw processErrorAsFormOrNotification(error);
    }
};