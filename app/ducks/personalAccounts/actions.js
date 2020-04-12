import * as types from "./types";
import fetch from "utils/fetch";
import {DELETE, GET, POST, PUT} from "utils/appConstants";
import {processErrorAsFormOrNotification, processErrorAsNotification} from "utils/errorHandlingUtils";
import {isNotEmpty} from "utils/stringUtils";
import {rsaEncrypt} from "utils/encryptionUtils";
import {IndexedDBService} from "indexedDB";
import {folderAccountsCountDecreased, folderAccountsCountIncreased} from "../personalAccountFolders/actions";
import {progressFinished, progressStarted} from "ducks/progressBar/actions";

const indexedDBService = IndexedDBService.getService();

const personalAccountUpdated = account => ({
    type: types.PERSONAL_ACCOUNT_UPDATED,
    account
});

const personalAccountRemoved = accountUuid => ({
    type: types.PERSONAL_ACCOUNT_REMOVED,
    accountUuid
});

const personalAccountsFetched = accounts => ({
    type: types.PERSONAL_ACCOUNTS_FETCH_SUCCESS,
    accounts
});


export const createPersonalAccount = (account) => async dispatch => {
    try {
        dispatch(progressStarted());
        const publicKey = await indexedDBService.loadPublicKey();

        if (isNotEmpty(account.password)) {
            account.password = await rsaEncrypt(publicKey, account.password);
        }

        if (isNotEmpty(account.username)) {
            account.username = await rsaEncrypt(publicKey, account.username);
        }

        const createResponse = await fetch(POST, "personal-accounts-management/accounts", account);
        dispatch(personalAccountUpdated(createResponse.data));
        dispatch(folderAccountsCountIncreased(account.folderUuid));
    } catch (error) {
        throw processErrorAsFormOrNotification(error);
    } finally {
        dispatch(progressFinished());
    }
};

export const updatePersonalAccount = (newAccount, oldAccount) => async dispatch => {
    try {
        dispatch(progressStarted());
        const publicKey = await indexedDBService.loadPublicKey();

        if (isNotEmpty(newAccount.password)) {
            newAccount.password = await rsaEncrypt(publicKey, newAccount.password);
        }

        if (isNotEmpty(newAccount.username)) {
            newAccount.username = await rsaEncrypt(publicKey, newAccount.username);
        }

        const updateResponse = await fetch(PUT, `personal-accounts-management/accounts/${newAccount.uuid}`, newAccount);
        dispatch(personalAccountUpdated(updateResponse.data));

        if (newAccount.folderUuid !== oldAccount.folderUuid) {
            dispatch(folderAccountsCountIncreased(newAccount.folderUuid));
            dispatch(folderAccountsCountDecreased(oldAccount.folderUuid));
        }

    } catch (error) {
        throw processErrorAsFormOrNotification(error);
    } finally {
        dispatch(progressFinished());
    }
};

export const removePersonalAccount = (account) => async dispatch => {
    try {
        dispatch(progressStarted());
        await fetch(DELETE, `personal-accounts-management/accounts/${account.uuid}`);
        dispatch(personalAccountRemoved(account.uuid));
        dispatch(folderAccountsCountDecreased(account.folderUuid))
    } catch (error) {
        throw processErrorAsNotification(error);
    } finally {
        dispatch(progressFinished());
    }
};

export const fetchPersonalAccounts = (folderUuid) => async dispatch => {
    try {
        dispatch(progressStarted());
        let url;
        if (folderUuid){
            url = `personal-accounts-management/accounts?folderUuid=${folderUuid}`;
        } else {
            url = `personal-accounts-management/accounts?unfolderedOnly=true`;
        }
        const fetchResponse = await fetch(GET, url);
        dispatch(personalAccountsFetched(fetchResponse.data));
    }  finally {
     dispatch(progressFinished());
    }
};