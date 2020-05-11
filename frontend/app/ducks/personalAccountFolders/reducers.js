import createReducer from "utils/createReducerUtils";
import {SIGN_OUT} from "ducks/account/types";
import * as types from "./types";
import {SHARING_SELECTED} from "../personalAccountsSharings/types";
import {isEmpty} from "utils/stringUtils";

const INITIAL_STATE = {folders: [], selectedFolderUuid: null};

const personalAccountsFoldersReducer = createReducer(INITIAL_STATE)({
    [SIGN_OUT]: () => (INITIAL_STATE),

    [types.FOLDER_UPDATED]: (state, {folder}) => {
        const folders = state.folders.slice();

        const index = folders.findIndex(listFolder => listFolder.uuid === folder.uuid);
        if (index === -1) {
            folders.push(folder);
        } else {
            folders[index] = folder;
        }
        return {...state, ...{folders: folders}};
    },

    [types.FOLDER_REMOVED]: (state, {folderUuid}) => {
        const folders = state.folders.filter(folder => folder.uuid !== folderUuid);
        return {...state, ...{folders: folders, selectedFolderUuid: null}};
    },

    [types.FOLDER_SELECTED]: (state, {folderUuid}) => {
        const folder = state.folders.find(folder => folder.uuid === folderUuid);
        return {...state, ...{selectedFolderUuid: folder.uuid}};
    },

    [SHARING_SELECTED]: (state, {selectedSharingFromAccountEntityUuid}) => {
        let resultFolderUuid = null;
        if (isEmpty(selectedSharingFromAccountEntityUuid)) {
            resultFolderUuid = state.selectedFolderUuid;
        }
        return {...state, ...{selectedFolderUuid: resultFolderUuid}};
    },

    [types.FOLDERS_FETCH_SUCCESS]: (state, {folders}) => {
        return {...state, ...{folders: folders}};
    },

    [types.FOLDER_ACCOUNT_SIZE_DECREASED]: (state, {folderUuid, count}) => {
        if (!folderUuid) {
            folderUuid = null;
        }

        const folders = state.folders.slice();
        const index = folders.findIndex(listFolder => listFolder.uuid === folderUuid);

        folders[index].accountsCount = folders[index].accountsCount - count;

        return {...state, ...{folders: folders}};
    },

    [types.FOLDER_ACCOUNT_SIZE_INCREASED]: (state, {folderUuid, count}) => {
        if (!folderUuid) {
            folderUuid = null;
        }

        const folders = state.folders.slice();
        const index = folders.findIndex(listFolder => listFolder.uuid === folderUuid);

        folders[index].accountsCount = folders[index].accountsCount + count;

        return {...state, ...{folders: folders}};
    },
});

export default personalAccountsFoldersReducer;