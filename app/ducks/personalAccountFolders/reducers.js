import createReducer from "utils/createReducerUtils";
import {SIGN_OUT} from "ducks/account/types";
import * as types from "./types";

const INITIAL_STATE = {folders: []};

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
});

export default personalAccountsFoldersReducer;