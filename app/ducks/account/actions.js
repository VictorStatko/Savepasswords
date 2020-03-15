import {GET, POST} from "utils/appConstants";
import fetch from "utils/fetch";
import {processResponseErrorAsFormOrNotification, processResponseErrorAsNotification} from "utils/httpUtils";
import queryString from "query-string";
import * as types from "./types";
import {LocalStorageService} from "localStorage";
import {IndexedDBService} from 'indexedDB';
import {
    decryptPrivateKeyFromPemString,
    encryptPrivateKeyToPemString,
    generateSalt,
    getRSAKeyPair,
    pemStringToPublicCryptoKey,
    publicCryptoKeyToPemString
} from "utils/encryptionUtils";
import {reverseString} from "utils/stringUtils";
import argon2 from "argon2-browser";
import {toast} from "react-toastify";
import i18n from "i18n";

const localStorageService = LocalStorageService.getService();
const indexedDBService = IndexedDBService.getService();

export const userLoggedIn = () => ({
    type: types.SIGN_IN
});

export const userLoggedOut = () => ({
    type: types.SIGN_OUT
});

export const userLogInFail = () => ({
    type: types.SIGN_IN_FAIL
});

export const userDataFetched = data => ({
    type: types.DATA_FETCH,
    data
});

export const trySignUp = (payload) => async dispatch => {
    const passwordSalt = generateSalt(16);
    payload.clientPasswordSalt = passwordSalt;
    const plainPassword = payload.password;

    try {
        const passwordHash = await argon2.hash(
            {
                pass: plainPassword,
                salt: passwordSalt
            }
        );

        payload.password = passwordHash.encoded;

        const RSAKeyPair = await getRSAKeyPair(2048, 'SHA-512', 'RSA-OAEP', ['encrypt', 'decrypt'], true);

        payload.privateKey = await encryptPrivateKeyToPemString(
            RSAKeyPair.privateKey,
            reverseString(plainPassword).trim().normalize(),
            100000,
            'SHA-512',
            'AES-GCM',
            256
        );

        payload.publicKey = await publicCryptoKeyToPemString(RSAKeyPair.publicKey);

    } catch (e) {
        toast.error(i18n.t('exceptions.clientEncryptionError'));
        throw e;
    }

    try {
        await fetch(POST, "auth/accounts", payload);
    } catch (error) {
        throw processResponseErrorAsNotification(error);
    }

};

export const signOut = () => async dispatch => {
    try {
        await fetch(POST, "auth/logout");
        localStorageService.clearToken();
        await indexedDBService.clearKeys();
        dispatch(userLoggedOut());
    } catch (error) {
        throw processResponseErrorAsNotification(error);
    }
};

export const trySignIn = (payload) => async dispatch => {
    try {
        const clientSaltResponse = await fetch(GET, `auth/accounts/client-encryption-salt?email=${payload.username}`);
        const clientSalt = clientSaltResponse.data.value;

        const plainPassword = payload.password;
        const passwordHash = await argon2.hash(
            {
                pass: plainPassword,
                salt: clientSalt
            }
        );
        payload.password = passwordHash.encoded;

        payload.grant_type = "password";
        payload.client_id = "webclient";

        const loginResponse = await fetch(POST, "auth/token", queryString.stringify(payload), {'Content-Type': 'application/x-www-form-urlencoded'});

        localStorageService.setToken(
            {
                access_token: loginResponse.data.access_token,
                refresh_token: loginResponse.data.refresh_token
            }
        );

        const userDataResponse = await fetch(GET, "auth/accounts/current");

        const keyPair = await fetch(GET, 'auth/accounts/current/keypair');

        const cryptoPublicKey = await pemStringToPublicCryptoKey(
            keyPair.data.publicKey,
            {
                isExtractable: false,
                name: 'RSA-OAEP',
                hash: 'SHA-512',
                usage: '[encrypt, wrapKey]'
            }
        );

        const hash = {};
        hash.name = 'SHA-512';

        const decryptedPrivateKey = await decryptPrivateKeyFromPemString(
            keyPair.data.privateKey,
            reverseString(plainPassword).trim().normalize(),
            {name: 'RSA-OAEP', isExtractable: false, hash: hash}
        );

        await indexedDBService.insertKeys(decryptedPrivateKey, cryptoPublicKey);

        dispatch(userLoggedIn());
        dispatch(userDataFetched(userDataResponse.data));
    } catch (error) {
        localStorageService.clearToken();
        await indexedDBService.clearKeys();
        dispatch(userLogInFail());
        throw processResponseErrorAsFormOrNotification(error);
    }
};