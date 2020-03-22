import * as types from "./types";
import fetch from "utils/fetch";
import {GET, POST} from "utils/appConstants";
import {processResponseErrorAsFormOrNotification, processResponseErrorAsNotification} from "utils/httpUtils";
import {isNotEmpty} from "utils/stringUtils";
import {rsaEncrypt} from "utils/encryptionUtils";
import {IndexedDBService} from "indexedDB";

const indexedDBService = IndexedDBService.getService();

const personalAccountUpdated = account => ({
    type: types.PERSONAL_ACCOUNT_UPDATED,
    account
});

const personalAccountsFetched = accounts => ({
    type: types.PERSONAL_ACCOUNTS_FETCH_SUCCESS,
    accounts
});


export const createPersonalAccount = (account) => async dispatch => {
    try {
        const publicKey = await indexedDBService.loadPublicKey();

        if (isNotEmpty(account.password)) {
            account.password = await rsaEncrypt(publicKey, account.password);
        }

        if (isNotEmpty(account.username)) {
            account.username = await rsaEncrypt(publicKey, account.username);
        }

        const createResponse = await fetch(POST, "personal-accounts-management/accounts", account);
        dispatch(personalAccountUpdated(createResponse.data));
    } catch (error) {
        throw processResponseErrorAsFormOrNotification(error);
    }
};

export const fetchPersonalAccounts = () => async dispatch => {
    try {
        const fetchResponse = await fetch(GET, "personal-accounts-management/accounts");
        dispatch(personalAccountsFetched(fetchResponse.data));
    } catch (error) {
        throw processResponseErrorAsNotification(error);
    }
};