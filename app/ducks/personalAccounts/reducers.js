import createReducer from "utils/createReducerUtils";
import * as types from "./types";

const personalAccountsReducer = createReducer({accounts: []})({
    [types.PERSONAL_ACCOUNT_UPDATED]: (state, {account}) => {
        const accounts = state.accounts;
        const index = accounts.findIndex(listAccount => listAccount.uuid === account.uuid);
        if (index === -1) {
            accounts.push(account);
        } else {
            accounts[index] = account;
        }
        return {...state, ...{accounts: accounts}};
    },
});

export default personalAccountsReducer;