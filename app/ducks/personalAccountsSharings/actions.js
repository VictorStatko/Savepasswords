import {progressFinished, progressStarted} from "ducks/progressBar/actions";
import fetch from "utils/fetch";
import {GET} from "utils/appConstants";
import * as types from "./types";

const sharingsFetched = sharings => ({
    type: types.SHARINGS_FETCH_SUCCESS,
    sharings
});

const sharingSelected = selectedSharingFromAccountEntityUuid => ({
    type: types.SHARING_SELECTED,
    selectedSharingFromAccountEntityUuid
});

export const sharingAccountsCountIncreased = (sharingFromAccountEntityUuid) => ({
    type: types.SHARING_ACCOUNT_SIZE_DECREASED,
    sharingFromAccountEntityUuid
});

export const sharingAccountsCountUpdated = (sharingFromAccountEntityUuid, newSize) => ({
    type: types.SHARING_ACCOUNT_SIZE_UPDATED,
    sharingFromAccountEntityUuid,
    newSize
});

export const fetchPersonalAccountSharings = () => async dispatch => {
    try {
        dispatch(progressStarted());
        const fetchSharingsResponse = await fetch(GET, "personal-accounts-management/accounts/sharing");

        if (!fetchSharingsResponse.data || fetchSharingsResponse.data.length === 0) {
            dispatch(sharingSelected(null));
        }

        dispatch(sharingsFetched(fetchSharingsResponse.data));
    } finally {
        dispatch(progressFinished());
    }
};

export const selectSharing = (selectedSharingFromAccountEntityUuid) => dispatch => {
    dispatch(sharingSelected(selectedSharingFromAccountEntityUuid));
};