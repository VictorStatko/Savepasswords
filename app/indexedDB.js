import Dexie from 'dexie';
import {store} from "app";
import {userLoggedOut} from "./ducks/account/actions";
import {LocalStorageService} from "./localStorage";
import {toast} from "react-toastify";
import i18n from "./i18n";

const localStorageService = LocalStorageService.getService();
const indexedDB = new Dexie('syp');
indexedDB.version(1).stores({privateKeys: '++keyId', publicKeys: '++keyId'});

export const IndexedDBService = (function () {
    let _service;

    function _getService() {
        if (!_service) {
            _service = this;
            return _service
        }
        return _service
    }

    async function _insertKeys(privateKey, publicKey) {
        return indexedDB.transaction('rw', indexedDB.privateKeys, indexedDB.publicKeys, () => {
            indexedDB.privateKeys.put({keyId: 0, key: privateKey});
            indexedDB.publicKeys.put({keyId: 0, key: publicKey});
        });
    }

    async function _clearKeys() {
        return indexedDB.transaction('rw', indexedDB.privateKeys, indexedDB.publicKeys, () => {
            indexedDB.privateKeys.clear();
            indexedDB.publicKeys.clear();
        });
    }

    async function _loadPublicKey() {
        const keyRecord = await indexedDB.publicKeys.get(0);

        if (!keyRecord || !keyRecord.key) {
            localStorageService.clearToken();
            await _clearKeys();
            store.dispatch(userLoggedOut());
            toast.error(i18n.t('global.auth.sessionExpired'));
            throw new Error('Public key not found');
        }

        return keyRecord.key;
    }

    async function _loadPrivateKey() {
        const keyRecord = await indexedDB.privateKeys.get(0);

        if (!keyRecord || !keyRecord.key) {
            localStorageService.clearToken();
            await _clearKeys();
            store.dispatch(userLoggedOut());
            toast.error(i18n.t('global.auth.sessionExpired'));
            throw new Error('Private key not found');
        }
        return keyRecord.key;
    }

    return {
        getService: _getService,
        insertKeys: _insertKeys,
        clearKeys: _clearKeys,
        loadPublicKey: _loadPublicKey,
        loadPrivateKey: _loadPrivateKey
    }
})();