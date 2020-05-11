import fetch from "utils/fetch";
import {DELETE, FOLDER_REMOVAL_OPTIONS, GET, POST, PUT} from "utils/appConstants";
import {processErrorAsFormOrNotification, processErrorAsNotification} from "utils/errorHandlingUtils";
import * as types from "./types";
import i18n from 'i18n';
import {progressFinished, progressStarted} from "ducks/progressBar/actions";

const folderUpdated = folder => ({
    type: types.FOLDER_UPDATED,
    folder
});

const folderRemoved = folderUuid => ({
    type: types.FOLDER_REMOVED,
    folderUuid
});

const folderSelected = folderUuid => ({
    type: types.FOLDER_SELECTED,
    folderUuid
});

const foldersFetched = folders => ({
    type: types.FOLDERS_FETCH_SUCCESS,
    folders
});

export const folderAccountsCountIncreased = (folderUuid, count) => ({
    type: types.FOLDER_ACCOUNT_SIZE_INCREASED,
    folderUuid,
    count
});

export const folderAccountsCountDecreased = (folderUuid, count) => ({
    type: types.FOLDER_ACCOUNT_SIZE_DECREASED,
    folderUuid,
    count
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

export const updateFolder = (folder) => async dispatch => {
    try {
        dispatch(progressStarted());
        const updateResponse = await fetch(PUT, `personal-accounts-management/folders/${folder.uuid}`, folder);
        dispatch(folderUpdated(updateResponse.data));
        return updateResponse.data;
    } catch (error) {
        throw processErrorAsFormOrNotification(error);
    } finally {
        dispatch(progressFinished());
    }
};

export const removeFolder = (folder, removalOption) => async dispatch => {
    try {
        dispatch(progressStarted());
        await fetch(DELETE, `personal-accounts-management/folders/${folder.uuid}?removalOption=${removalOption}`);
        dispatch(folderRemoved(folder.uuid));

        if (removalOption === FOLDER_REMOVAL_OPTIONS.FOLDER_ONLY && folder.accountsCount > 0) {
            dispatch(folderAccountsCountIncreased(null, folder.accountsCount))
        }
    } catch (error) {
        throw processErrorAsNotification(error);
    } finally {
        dispatch(progressFinished());
    }
};

export const selectFolder = (folderUuid) => dispatch => {
    const uuidToDispatch = folderUuid ? folderUuid : null;
    dispatch(folderSelected(uuidToDispatch));
};

export const fetchPersonalAccountFolders = () => async dispatch => {
    try {
        dispatch(progressStarted());
        const fetchFoldersResponse = await fetch(GET, "personal-accounts-management/folders");
        const unfolderedItem = {uuid: null, name: i18n.t('personalAccountFolders.unfolderedItems'), accountsCount: 0};
        dispatch(foldersFetched([unfolderedItem, ...fetchFoldersResponse.data]));

        const fetchUnfolderedCountResponse = await fetch(GET, "personal-accounts-management/accounts/count?unfolderedOnly=true");

        unfolderedItem.accountsCount = fetchUnfolderedCountResponse.data.value;
        dispatch(folderUpdated(unfolderedItem));
    } finally {
        dispatch(progressFinished());
    }
};