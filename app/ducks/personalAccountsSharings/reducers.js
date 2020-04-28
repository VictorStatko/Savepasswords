import createReducer from "utils/createReducerUtils";
import {SIGN_OUT} from "ducks/account/types";
import * as types from "./types";
import {FOLDER_SELECTED} from "../personalAccountFolders/types";

const INITIAL_STATE = {sharings: [], selectedSharingFromAccountEntityUuid: null};

const personalAccountsSharingsReducer = createReducer(INITIAL_STATE)({
    [SIGN_OUT]: () => (INITIAL_STATE),

    [types.SHARINGS_FETCH_SUCCESS]: (state, {sharings}) => {
        return {...state, ...{sharings: sharings}};
    },

    [types.SHARING_SELECTED]: (state, {selectedSharingFromAccountEntityUuid}) => {
        const sharingFromAccountEntityUuid = selectedSharingFromAccountEntityUuid ?
            state.sharings.find(sharing => sharing.sharingFromAccountEntityUuid === selectedSharingFromAccountEntityUuid).sharingFromAccountEntityUuid
            : null;

        return {...state, ...{selectedSharingFromAccountEntityUuid: sharingFromAccountEntityUuid}};
    },

    [FOLDER_SELECTED]: (state) => {
        return {...state, ...{selectedSharingFromAccountEntityUuid: null}};
    },

    [types.SHARING_ACCOUNT_SIZE_DECREASED]: (state, {sharingFromAccountEntityUuid}) => {
        let sharings = JSON.parse(JSON.stringify(state.sharings));
        let newSelectedSharingFromAccountEntityUuid = state.selectedSharingFromAccountEntityUuid;

        const sharing = sharings.find(sharing => sharing.sharingFromAccountEntityUuid === sharingFromAccountEntityUuid);

        if (sharing.sharedAccountsCount > 1) {
            sharing.sharedAccountsCount -= 1;
        } else {
            sharings = sharings.filter(sharing => sharing.sharingFromAccountEntityUuid !== sharingFromAccountEntityUuid);
            if (state.selectedSharingFromAccountEntityUuid == sharingFromAccountEntityUuid){
                newSelectedSharingFromAccountEntityUuid = null;
            }
        }

        return {...state, ...{sharings: sharings}, ...{selectedSharingFromAccountEntityUuid: newSelectedSharingFromAccountEntityUuid}};
    },

    [types.SHARING_ACCOUNT_SIZE_UPDATED]: (state, {sharingFromAccountEntityUuid, newSize}) => {
        let sharings = JSON.parse(JSON.stringify(state.sharings));
        let newSelectedSharingFromAccountEntityUuid = state.selectedSharingFromAccountEntityUuid;

        const sharing = sharings.find(sharing => sharing.sharingFromAccountEntityUuid === sharingFromAccountEntityUuid);

        if (newSize === 0) {
            sharings = sharings.filter(sharing => sharing.sharingFromAccountEntityUuid !== sharingFromAccountEntityUuid);
            if (state.selectedSharingFromAccountEntityUuid == sharingFromAccountEntityUuid){
                newSelectedSharingFromAccountEntityUuid = null;
            }
        } else {
            sharing.sharedAccountsCount = newSize;
        }

        return {...state, ...{sharings: sharings}, ...{selectedSharingFromAccountEntityUuid: newSelectedSharingFromAccountEntityUuid}};
    },
});

export default personalAccountsSharingsReducer;