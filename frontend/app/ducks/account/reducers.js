import createReducer from "utils/createReducerUtils";
import * as types from "./types";

const accountReducer = createReducer({isLoggedIn: false})({
    [types.SIGN_IN]: () => ({
        isLoggedIn: true
    }),
    [types.SIGN_OUT]: () => ({
        isLoggedIn: false
    }),

    [types.SIGN_IN_FAIL]: () => ({
        isLoggedIn: false
    }),

    [types.DATA_FETCH]: (state, {data}) => {
        return {...state, ...{uuid: data.uuid, email: data.email}};
    },
});

export default accountReducer;