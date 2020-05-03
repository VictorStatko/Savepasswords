import {SETTING_TYPES} from "utils/appConstants";
import createReducer from "utils/createReducerUtils";
import {SIGN_IN, SIGN_OUT} from "../account/types";
import * as types from "./types";

const INITIAL_STATE = {activityHistory: [], selectedSettings: SETTING_TYPES[0]};

const settingsReducer = createReducer(INITIAL_STATE)({
    [SIGN_OUT]: () => null,
    [SIGN_IN]: () => (INITIAL_STATE),

    [types.SETTING_SELECTED]: (state, {setting}) => {
        return {...state, ...{selectedSettings: setting}};
    },

    [types.ACTIVITY_HISTORY_FETCHED]: (state, {activityHistory}) => {
        return {...state, ...{activityHistory: activityHistory}};
    },

    [types.SESSIONS_FINISHED]: (state) => {
        const newActivityHistory = JSON.parse(JSON.stringify(state.activityHistory)).filter(session => session.current);
        return {...state, ...{activityHistory: newActivityHistory}};
    },
});

export default settingsReducer;