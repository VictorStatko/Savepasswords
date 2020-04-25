import createReducer from "utils/createReducerUtils";
import * as types from "./types";
import {SIGN_IN, SIGN_OUT} from "ducks/account/types";
import {recreatePagination, slicePage} from "utils/paginationUtils";
import {FOLDER_REMOVED, FOLDER_SELECTED} from "ducks/personalAccountFolders/types";
import {isBlank, isEmpty} from "utils/stringUtils";

const INITIAL_STATE_PAGINATION = {page: 1, size: 9, total: 0, search: ""};
const INITIAL_STATE = {accounts: [], pagination: {...INITIAL_STATE_PAGINATION}};

const personalAccountsReducer = createReducer(INITIAL_STATE)({
    [SIGN_OUT]: () => null,
    [SIGN_IN]: () => (INITIAL_STATE),

    [types.PERSONAL_ACCOUNT_UPDATED]: (state, {account}) => {
        const accounts = state.accounts.slice();

        const index = accounts.findIndex(listAccount => listAccount.uuid === account.uuid);
        if (index === -1) {
            accounts.push(account);
        } else {
            accounts[index] = account;
        }

        const filteredAccounts = filterAccounts(accounts, state.pagination.search);
        const newPagination = recreatePagination(filteredAccounts, state.pagination);
        const pagedAccounts = slicePage(filteredAccounts, newPagination);

        return {...state, ...{accounts: accounts, pagination: newPagination, pagedAccounts: pagedAccounts}};
    },

    [types.PERSONAL_ACCOUNT_REMOVED]: (state, {accountUuid}) => {
        const accounts = state.accounts.slice().filter(account => account.uuid !== accountUuid);

        const filteredAccounts = filterAccounts(accounts, state.pagination.search);
        const newPagination = recreatePagination(filteredAccounts, state.pagination);
        const pagedAccounts = slicePage(filteredAccounts, newPagination);

        return {...state, ...{accounts: accounts, pagination: newPagination, pagedAccounts: pagedAccounts}};
    },

    [types.PERSONAL_ACCOUNTS_FETCH_SUCCESS]: (state, {accounts}) => {
        const filteredAccounts = filterAccounts(accounts, state.pagination.search);
        const newPagination = recreatePagination(filteredAccounts, state.pagination);
        const pagedAccounts = slicePage(filteredAccounts, newPagination);

        return {...state, ...{accounts: accounts, pagination: newPagination, pagedAccounts: pagedAccounts}};
    },

    [types.PERSONAL_ACCOUNT_PAGINATION_PAGE_CHANGED]: (state, {newPage}) => {
        const pagination = {...state.pagination};
        pagination.page = newPage;

        const filteredAccounts = filterAccounts(state.accounts, pagination.search);
        const newPagination = recreatePagination(filteredAccounts, pagination);
        const pagedAccounts = slicePage(filteredAccounts, newPagination);

        return {...state, ...{pagination: newPagination, pagedAccounts: pagedAccounts}};
    },

    [types.PERSONAL_ACCOUNT_SEARCH_CHANGED]: (state, {search}) => {
        const pagination = {...state.pagination};
        pagination.search = search;

        const filteredAccounts = filterAccounts(state.accounts, pagination.search);
        const newPagination = recreatePagination(filteredAccounts, pagination);
        const pagedAccounts = slicePage(filteredAccounts, newPagination);

        return {...state, ...{pagination: newPagination, pagedAccounts: pagedAccounts}};
    },

    [FOLDER_REMOVED]: (state) => {
        return {...state, ...{pagination: INITIAL_STATE_PAGINATION}};
    },

    [FOLDER_SELECTED]: (state) => {
        return {...state, ...{pagination: INITIAL_STATE_PAGINATION}};
    },
});

function filterAccounts(accounts, search) {
    let newAccounts = accounts.slice();
    if (isBlank(search)) {
        return newAccounts;
    }

    const trimSearch = search.trim();
    return newAccounts.filter(account => {
        return (!isEmpty(account.name) && account.name.includes(trimSearch))
            || (!isEmpty(account.url) && account.url.includes(trimSearch))
    });
}


export default personalAccountsReducer;