import fetch from "utils/fetch";
import {GET, POST} from "utils/appConstants";
import {processErrorAsFormOrNotification, processErrorAsNotification} from "utils/errorHandlingUtils";
import * as types from "./types";
import i18n from 'i18n';
import {progressFinished, progressStarted} from "ducks/progressBar/actions";

const folderUpdated = folder => ({
    type: types.FOLDER_UPDATED,
    folder
});

const foldersFetched = folders => ({
    type: types.FOLDERS_FETCH_SUCCESS,
    folders
});

export const folderAccountsCountIncreased = folderUuid => ({
    type: types.FOLDER_ACCOUNT_SIZE_INCREASED,
    folderUuid
});

export const folderAccountsCountDecreased = folderUuid => ({
    type: types.FOLDER_ACCOUNT_SIZE_DECREASED,
    folderUuid
});

export const createFolder = (folder, errorsAsForm) => async dispatch => {
    try {
        dispatch(progressStarted());
        const createResponse = await fetch(POST, "personal-accounts-management/folders", folder);
        dispatch(folderUpdated(createResponse.data));
        return createResponse.data;
    } catch (error) {
        if (errorsAsForm) {
            throw processErrorAsFormOrNotification(error);
        } else {
            throw processErrorAsNotification(error);
        }
    } finally {
        dispatch(progressFinished());
    }
};

export const fetchPersonalAccountFolders = () => async dispatch => {
    try {
        dispatch(progressStarted());
        const fetchResponse = await fetch(GET, "personal-accounts-management/folders");
        dispatch(foldersFetched([{uuid: null, name: i18n.t('personalAccountFolders.unfolderedItems'), accountsCount: 999}, ... fetchResponse.data]));
    }  finally {
        dispatch(progressFinished());
    }
};