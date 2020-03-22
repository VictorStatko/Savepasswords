import * as types from "./types";
import fetch from "utils/fetch";
import {POST} from "utils/appConstants";
import {processResponseErrorAsFormOrNotification} from "utils/httpUtils";
import {isNotEmpty} from "utils/stringUtils";
import {rsaEncrypt} from "utils/encryptionUtils";
import {IndexedDBService} from "indexedDB";

const indexedDBService = IndexedDBService.getService();

const personalAccountUpdated = account => ({
    type: types.PERSONAL_ACCOUNT_UPDATED,
    account
});

export const createPersonalAccount = (account) => async dispatch => {
    try {
        const publicKey = await indexedDBService.loadPublicKey();

        if (isNotEmpty(account.password)) {
            account.password = await rsaEncrypt(publicKey, account.password);
        }

        if (isNotEmpty(account.url)) {
            account.url = await rsaEncrypt(publicKey, account.url);
        }

        if (isNotEmpty(account.name)) {
            account.name = await rsaEncrypt(publicKey, account.name);
        }

        if (isNotEmpty(account.username)) {
            account.username = await rsaEncrypt(publicKey, account.username);
        }

        if (isNotEmpty(account.description)) {
            account.description = await rsaEncrypt(publicKey, account.description);
        }

        const createResponse = await fetch(POST, "personal-accounts-management/accounts", account);
        dispatch(personalAccountUpdated(createResponse.data));
    } catch (error) {
        throw processResponseErrorAsFormOrNotification(error);
    }
};