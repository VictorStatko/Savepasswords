import createReducer from "utils/createReducerUtils";
import * as types from "./types";
import {SIGN_OUT} from "ducks/account/types";

const INITIAL_STATE = {accounts: []};

const personalAccountsReducer = createReducer(INITIAL_STATE)({
    [SIGN_OUT]: () => (INITIAL_STATE),

    [types.PERSONAL_ACCOUNT_UPDATED]: (state, {account}) => {
        const accounts = state.accounts.slice();

        const index = accounts.findIndex(listAccount => listAccount.uuid === account.uuid);
        if (index === -1) {
            accounts.push(account);
        } else {
            accounts[index] = account;
        }
        return {...state, ...{accounts: accounts}};
    },

    [types.PERSONAL_ACCOUNT_REMOVED]: (state, {accountUuid}) => {
        const accounts = state.accounts.filter(account => account.uuid !== accountUuid);
        return {...state, ...{accounts: accounts}};
    },

    [types.PERSONAL_ACCOUNTS_FETCH_SUCCESS]: (state, {accounts}) => {
        return {...state, ...{accounts: accounts}};
    },
});

export default personalAccountsReducer;