import fetch from "utils/fetch";
import {GET, POST} from "utils/appConstants";
import {processErrorAsFormOrNotification, processErrorAsNotification} from "utils/errorHandlingUtils";
import * as types from "./types";
import i18n from 'i18n';

const folderUpdated = folder => ({
    type: types.FOLDER_UPDATED,
    folder
});

const foldersFetched = folders => ({
    type: types.FOLDERS_FETCH_SUCCESS,
    folders
});

export const createFolder = (folder) => async dispatch => {
    try {
        const createResponse = await fetch(POST, "personal-accounts-management/folders", folder);
        dispatch(folderUpdated(createResponse.data));
    } catch (error) {
        throw processErrorAsFormOrNotification(error);
    }
};

export const fetchPersonalAccountFolders = () => async dispatch => {
    try {
        const fetchResponse = await fetch(GET, "personal-accounts-management/folders");
        dispatch(foldersFetched([{uuid: null, name: i18n.t('personalAccountFolders.unfolderedItems')}, ... fetchResponse.data]));
    } catch (error) {
        throw processErrorAsNotification(error);
    }
};