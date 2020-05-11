import * as types from "./types";
import fetch from "utils/fetch";
import {DELETE, GET, POST, PUT} from "utils/appConstants";
import {processErrorAsFormOrNotification, processErrorAsNotification} from "utils/errorHandlingUtils";
import {isNotEmpty} from "utils/stringUtils";
import {pemStringToPublicCryptoKey, rsaEncrypt} from "utils/encryptionUtils";
import {IndexedDBService} from "indexedDB";
import {folderAccountsCountDecreased, folderAccountsCountIncreased} from "../personalAccountFolders/actions";
import {progressFinished, progressStarted} from "ducks/progressBar/actions";
import {sharingAccountsCountIncreased, sharingAccountsCountUpdated} from "../personalAccountsSharings/actions";

const indexedDBService = IndexedDBService.getService();

const personalAccountRemoved = accountUuid => ({
    type: types.PERSONAL_ACCOUNT_REMOVED,
    accountUuid
});

const personalAccountsFetched = accounts => ({
    type: types.PERSONAL_ACCOUNTS_FETCH_SUCCESS,
    accounts
});

const personalAccountUpdated = account => ({
    type: types.PERSONAL_ACCOUNT_UPDATED,
    account
});

const personalAccountsPaginationPageChanged = newPage => ({
    type: types.PERSONAL_ACCOUNT_PAGINATION_PAGE_CHANGED,
    newPage
});

const personalAccountsSearchChanged = search => ({
    type: types.PERSONAL_ACCOUNT_SEARCH_CHANGED,
    search
});

const personalAccountShared = (sharedAccount, parentAccountUuid) => ({
    type: types.PERSONAL_ACCOUNT_SHARED,
    sharedAccount,
    parentAccountUuid
});

const personalAccountSharingRemoved = (parentAccountUuid, sharedAccountUuid) => ({
    type: types.PERSONAL_ACCOUNT_SHARING_REMOVED,
    parentAccountUuid,
    sharedAccountUuid
});

export const createPersonalAccount = (account, inSelectedFolder) => async dispatch => {
    try {
        dispatch(progressStarted());
        const publicKey = await indexedDBService.loadPublicKey();

        if (isNotEmpty(account.password)) {
            account.password = await rsaEncrypt(publicKey, account.password);
        }

        if (isNotEmpty(account.username)) {
            account.username = await rsaEncrypt(publicKey, account.username);
        }

        const response = await fetch(POST, "personal-accounts-management/accounts", account);
        const newAccount = response.data;
        dispatch(folderAccountsCountIncreased(newAccount.folderUuid, 1));

        if (inSelectedFolder) {
            dispatch(personalAccountUpdated(newAccount));
        }
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
        const newPasswordDecrypted = newAccount.password;
        const newUsernameDecrypted = newAccount.username;

        if (isNotEmpty(newAccount.password)) {
            newAccount.password = await rsaEncrypt(publicKey, newPasswordDecrypted);
        }

        if (isNotEmpty(newAccount.username)) {
            newAccount.username = await rsaEncrypt(publicKey, newUsernameDecrypted);
        }

        if (newAccount.sharedAccounts && newAccount.sharedAccounts.length > 0) {

            newAccount.sharedAccounts = JSON.parse(JSON.stringify(newAccount.sharedAccounts));

            for (const sharedAccount of newAccount.sharedAccounts) {
                sharedAccount.url = newAccount.url;
                sharedAccount.name = newAccount.name;
                sharedAccount.description = newAccount.description;

                const accountPublicKey = await pemStringToPublicCryptoKey(
                    sharedAccount.encryptionPublicKey,
                    {
                        isExtractable: false,
                        name: 'RSA-OAEP',
                        hash: 'SHA-512',
                        usage: '[encrypt, wrapKey]'
                    }
                );

                sharedAccount.password = await rsaEncrypt(accountPublicKey, newPasswordDecrypted);
                sharedAccount.username = await rsaEncrypt(accountPublicKey, newUsernameDecrypted);
            }
        }

        await fetch(PUT, `personal-accounts-management/accounts/${newAccount.uuid}`, newAccount);

        if (newAccount.folderUuid != oldAccount.folderUuid) {
            dispatch(folderAccountsCountIncreased(newAccount.folderUuid, 1));
            dispatch(folderAccountsCountDecreased(oldAccount.folderUuid, 1));
            dispatch(personalAccountRemoved(newAccount.uuid));
        } else {
            dispatch(personalAccountUpdated(newAccount));
        }

    } catch (error) {
        throw processErrorAsFormOrNotification(error);
    } finally {
        dispatch(progressFinished());
    }
};

export const removePersonalAccount = (account, sharedFromUuid) => async dispatch => {
    try {
        dispatch(progressStarted());
        await fetch(DELETE, `personal-accounts-management/accounts/${account.uuid}`);
        dispatch(personalAccountRemoved(account.uuid));
        if (!sharedFromUuid) {
            dispatch(folderAccountsCountDecreased(account.folderUuid, 1))
        } else {
            dispatch(sharingAccountsCountIncreased(sharedFromUuid))
        }
    } catch (error) {
        throw processErrorAsNotification(error);
    } finally {
        dispatch(progressFinished());
    }
};

export const fetchPersonalAccounts = (folderUuid, selectedSharingFromAccountEntityUuid) => async dispatch => {
    try {
        dispatch(progressStarted());
        let url;
        if (folderUuid) {
            url = `personal-accounts-management/accounts?folderUuid=${folderUuid}`;
        } else if (selectedSharingFromAccountEntityUuid) {
            url = `personal-accounts-management/accounts?parentPersonalAccountAccountEntityUuid=${selectedSharingFromAccountEntityUuid}`;
        } else {
            url = `personal-accounts-management/accounts?unfolderedOnly=true`;
        }
        const fetchResponse = await fetch(GET, url);
        dispatch(personalAccountsFetched(fetchResponse.data));
        if (!folderUuid && selectedSharingFromAccountEntityUuid) {
            dispatch(sharingAccountsCountUpdated(selectedSharingFromAccountEntityUuid, fetchResponse.data.length));
        }
    } finally {
        dispatch(progressFinished());
    }
};

export const sharePersonalAccount = (parentAccount, emailToShare) => async dispatch => {
    try {
        dispatch(progressStarted());
        const accountDataResponse = await fetch(GET, `personal-accounts-management/accounts/sharing/account-data?accountEmail=${emailToShare}`);

        const parentUuid = parentAccount.uuid;

        parentAccount.folderUuid = null;
        parentAccount.encryptionPublicKey = accountDataResponse.data.publicKey;
        parentAccount.sharedAccounts = [];
        parentAccount.uuid = null;

        console.log(accountDataResponse.data);

        const publicKey = await pemStringToPublicCryptoKey(
            accountDataResponse.data.publicKey,
            {
                isExtractable: false,
                name: 'RSA-OAEP',
                hash: 'SHA-512',
                usage: '[encrypt, wrapKey]'
            }
        );

        if (isNotEmpty(parentAccount.password)) {
            parentAccount.password = await rsaEncrypt(publicKey, parentAccount.password);
        }

        if (isNotEmpty(parentAccount.username)) {
            parentAccount.username = await rsaEncrypt(publicKey, parentAccount.username);
        }

        const response = await fetch(POST, `personal-accounts-management/accounts?type=shared&fromPersonalAccountUuid=${parentUuid}&toUserAccountUuid=${accountDataResponse.data.entityUuid}`, parentAccount);
        const newSharedAccount = response.data;

        dispatch(personalAccountShared(newSharedAccount, parentUuid));
    } catch (error) {
        throw processErrorAsFormOrNotification(error);
    } finally {
        dispatch(progressFinished());
    }
};

export const removePersonalAccountSharing = (parentAccount, sharedAccount) => async dispatch => {
    try {
        dispatch(progressStarted());
        await fetch(DELETE, `personal-accounts-management/accounts/${sharedAccount.uuid}`);
        dispatch(personalAccountSharingRemoved(parentAccount.uuid, sharedAccount.uuid));
    } catch (error) {
        throw processErrorAsNotification(error);
    } finally {
        dispatch(progressFinished());
    }
}

export const changePaginationPage = (newPage) => dispatch => {
    dispatch(personalAccountsPaginationPageChanged(newPage));
};

export const changeSearch = (search) => dispatch => {
    dispatch(personalAccountsSearchChanged(search));
};