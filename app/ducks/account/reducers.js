import createReducer from "utils/createReducerUtils";
import * as types from "./types";

const accountReducer = createReducer({isLoggedIn: false})({
    [types.SIGN_IN]: () => {

        return {
            isLoggedIn: true
        };
    }
});

export default accountReducer;