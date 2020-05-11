import * as types from "./types";
import {progressFinished, progressStarted} from "../progressBar/actions";
import fetch from "utils/fetch";
import {DELETE, GET, POST} from "utils/appConstants";
import {processErrorAsNotification} from "utils/errorHandlingUtils";
import {userLoggedOut} from "../account/actions";
import {IndexedDBService} from "indexedDB";

const indexedDBService = IndexedDBService.getService();

const settingSelected = setting => ({
    type: types.SETTING_SELECTED,
    setting
});

const activityHistoryFetched = activityHistory => ({
    type: types.ACTIVITY_HISTORY_FETCHED,
    activityHistory
});

const sessionsFinished = () => ({
    type: types.SESSIONS_FINISHED
});

export const selectSetting = (setting) => dispatch => {
    dispatch(settingSelected(setting));
};

export const fetchActivityHistory = () => async dispatch => {
    try {
        dispatch(progressStarted());
        const fetchResponse = await fetch(GET, 'auth/sessions');
        dispatch(activityHistoryFetched(fetchResponse.data));
    } finally {
        dispatch(progressFinished());
    }
};

export const finishSessions = () => async dispatch => {
    try {
        dispatch(progressStarted());
        await fetch(POST, 'auth/sessions?action=clear');
        dispatch(sessionsFinished());
    } catch (error) {
        throw processErrorAsNotification(error);
    } finally {
        dispatch(progressFinished());
    }
};

export const removeAccount = () => async dispatch => {
    try {
        dispatch(progressStarted());
        await fetch(DELETE, 'auth/accounts/current');
        await indexedDBService.clearKeys();
        dispatch(userLoggedOut());
    } catch (error) {
        throw processErrorAsNotification(error);
    } finally {
        dispatch(progressFinished());
    }
};