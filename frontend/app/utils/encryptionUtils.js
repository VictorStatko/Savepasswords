//this util is rewrited version of https://github.com/safebash/opencrypto

const cryptoLib = window.crypto || window.msCrypto;
const cryptoApi = cryptoLib.subtle || cryptoLib.webkitSubtle;
const chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/';
const lookup = new Uint8Array(256);

const PBES2_OID = '06092a864886f70d01050d';
const PBKDF2_OID = '06092a864886f70d01050c';

const AES256GCM_OID = '060960864801650304012e';
const AES192GCM_OID = '060960864801650304011a';
const AES128GCM_OID = '0609608648016503040106';

const AES256CBC_OID = '060960864801650304012a';
const AES192CBC_OID = '0609608648016503040116';
const AES128CBC_OID = '0609608648016503040102';

const AES256CFB_OID = '060960864801650304012c';
const AES192CFB_OID = '0609608648016503040118';
const AES128CFB_OID = '06086086480165030404';

const SHA512_OID = '06082a864886f70d020b0500';
const SHA384_OID = '06082a864886f70d020a0500';
const SHA256_OID = '06082a864886f70d02090500';
const SHA1_OID = '06082a864886f70d02070500';

const RSA_OID = '06092a864886f70d010101';
const EC_OID = '06072a8648ce3d0201';
const P256_OID = '06082a8648ce3d030107';
const P384_OID = '06052b81040022';
const P521_OID = '06052b81040023';

for (let i = 0; i < chars.length; i++) {
    lookup[chars.charCodeAt(i)] = i;
}

function encodeAb(arrayBuffer) {
    const bytes = new Uint8Array(arrayBuffer);
    const len = bytes.length;
    let base64 = '';

    for (let i = 0; i < len; i += 3) {
        base64 += chars[bytes[i] >> 2];
        base64 += chars[((bytes[i] & 3) << 4) | (bytes[i + 1] >> 4)];
        base64 += chars[((bytes[i + 1] & 15) << 2) | (bytes[i + 2] >> 6)];
        base64 += chars[bytes[i + 2] & 63];
    }

    if ((len % 3) === 2) {
        base64 = base64.substring(0, base64.length - 1) + '=';
    } else if (len % 3 === 1) {
        base64 = base64.substring(0, base64.length - 2) + '==';
    }

    return base64;
}

function decodeAb(base64) {
    const len = base64.length;
    let bufferLength = base64.length * 0.75;
    let p = 0;
    let encoded1;
    let encoded2;
    let encoded3;
    let encoded4;

    if (base64[base64.length - 1] === '=') {
        bufferLength--;
        if (base64[base64.length - 2] === '=') {
            bufferLength--;
        }
    }

    const arrayBuffer = new ArrayBuffer(bufferLength);
    let bytes = new Uint8Array(arrayBuffer);

    for (let i = 0; i < len; i += 4) {
        encoded1 = lookup[base64.charCodeAt(i)];
        encoded2 = lookup[base64.charCodeAt(i + 1)];
        encoded3 = lookup[base64.charCodeAt(i + 2)];
        encoded4 = lookup[base64.charCodeAt(i + 3)];

        bytes[p++] = (encoded1 << 2) | (encoded2 >> 4);
        bytes[p++] = ((encoded2 & 15) << 4) | (encoded3 >> 2);
        bytes[p++] = ((encoded3 & 3) << 6) | (encoded4 & 63);
    }

    return arrayBuffer;
}

function arrayBufferToString (arrayBuffer) {
    if (typeof arrayBuffer !== 'object') {
        throw new TypeError('Expected input to be an ArrayBuffer Object')
    }

    const decoder = new TextDecoder('utf-8');
    return decoder.decode(arrayBuffer)
}

function stringToArrayBuffer(str) {
    if (typeof str !== 'string') {
        throw new TypeError('Expected input to be a String');
    }

    const encoder = new TextEncoder('utf-8');
    const byteArray = encoder.encode(str);
    return byteArray.buffer;
}

function arrayBufferToHexString(arrayBuffer) {
    if (typeof arrayBuffer !== 'object') {
        throw new TypeError('Expected input to be an ArrayBuffer Object');
    }

    const byteArray = new Uint8Array(arrayBuffer);
    let hexString = '';
    let nextHexByte;

    for (let i = 0; i < byteArray.byteLength; i++) {
        nextHexByte = byteArray[i].toString(16);

        if (nextHexByte.length < 2) {
            nextHexByte = '0' + nextHexByte;
        }

        hexString += nextHexByte;
    }

    return hexString;
}

function hexStringToArrayBuffer(hexString) {
    if (typeof hexString !== 'string') {
        throw new TypeError('Expected input of hexString to be a String');
    }

    if ((hexString.length % 2) !== 0) {
        throw new RangeError('Expected string to be an even number of characters');
    }

    const byteArray = new Uint8Array(hexString.length / 2);
    for (let i = 0; i < hexString.length; i += 2) {
        byteArray[i / 2] = parseInt(hexString.substring(i, i + 2), 16);
    }

    return byteArray.buffer;
}

function arrayBufferToBase64(arrayBuffer) {
    if (typeof arrayBuffer !== 'object') {
        throw new TypeError('Expected input to be an ArrayBuffer Object');
    }

    return encodeAb(arrayBuffer);
}

function base64ToArrayBuffer(b64) {
    if (typeof b64 !== 'string') {
        throw new TypeError('Expected input to be a base64 String');
    }

    return decodeAb(b64);
}

function decimalToHex(d, unsigned) {
    unsigned = (typeof unsigned !== 'undefined') ? unsigned : false;

    let h = null;
    if (typeof d === 'number') {
        if (unsigned) {
            h = (d).toString(16);
            return h.length % 2 ? '000' + h : '00' + h;
        } else {
            h = (d).toString(16);
            return h.length % 2 ? '0' + h : h;
        }
    } else if (typeof d === 'string') {
        h = (d.length / 2).toString(16);
        return h.length % 2 ? '0' + h : h;
    }
}

function addNewLines(str) {
    let finalString = '';
    while (str.length > 0) {
        finalString += str.substring(0, 64) + '\r\n';
        str = str.substring(64);
    }

    return finalString;
}

function removeLines(str) {
    return str.replace(/\r?\n|\r/g, '');
}

function toAsn1(wrappedKey, salt, iv, iterations, hash, cipher, keyLength) {
    wrappedKey = arrayBufferToHexString(wrappedKey);
    salt = arrayBufferToHexString(salt);
    iv = arrayBufferToHexString(iv);
    iterations = decimalToHex(iterations, true);
    const opt = {};

    switch (hash) {
        case 'SHA-512' :
            opt.HASH_OID = SHA512_OID;
            break;
        case 'SHA-384' :
            opt.HASH_OID = SHA384_OID;
            break;
        case 'SHA-256' :
            opt.HASH_OID = SHA256_OID;
            break;
        case 'SHA-1' :
            opt.HASH_OID = SHA1_OID;
    }

    switch (cipher) {
        case 'AES-GCM' :
            if (keyLength === 256) {
                opt.CIPHER_OID = AES256GCM_OID;
            } else if (keyLength === 192) {
                opt.CIPHER_OID = AES192GCM_OID;
            } else if (keyLength === 128) {
                opt.CIPHER_OID = AES128GCM_OID;
            }
            break;
        case 'AES-CBC' :
            if (keyLength === 256) {
                opt.CIPHER_OID = AES256CBC_OID;
            } else if (keyLength === 192) {
                opt.CIPHER_OID = AES192CBC_OID;
            } else if (keyLength === 128) {
                opt.CIPHER_OID = AES128CBC_OID;
            }
            break;
        case 'AES-CFB' :
            if (keyLength === 256) {
                opt.CIPHER_OID = AES256CFB_OID;
            } else if (keyLength === 192) {
                opt.CIPHER_OID = AES192CFB_OID;
            } else if (keyLength === 128) {
                opt.CIPHER_OID = AES128CFB_OID;
            }
    }

    const ITER_INTEGER = '02' + decimalToHex(iterations.length / 2) + iterations;
    const SALT_OCTET = '04' + decimalToHex(salt) + salt;
    const IV_OCTET = '04' + decimalToHex(iv) + iv;
    const KEY_OCTET_PADDING = decimalToHex(wrappedKey).length / 2 === 2 ? '82' : '81';
    const KEY_OCTET = '04' + KEY_OCTET_PADDING + decimalToHex(wrappedKey) + wrappedKey;

    opt.SEQUENCE_AES_CONTAINER = '30' + decimalToHex(opt.CIPHER_OID + IV_OCTET);
    opt.SEQUENCE_HASH_CONTAINER = '30' + decimalToHex(opt.HASH_OID);
    opt.SEQUENCE_PBKDF2_INNER_CONTAINER = '30' + decimalToHex(SALT_OCTET + ITER_INTEGER + opt.SEQUENCE_HASH_CONTAINER + opt.HASH_OID);
    opt.SEQUENCE_PBKDF2_CONTAINER = '30' + decimalToHex(PBKDF2_OID + opt.SEQUENCE_PBKDF2_INNER_CONTAINER + SALT_OCTET + ITER_INTEGER + opt.SEQUENCE_HASH_CONTAINER + opt.HASH_OID);
    opt.SEQUENCE_PBES2_INNER_CONTAINER = '30' + decimalToHex(opt.SEQUENCE_PBKDF2_CONTAINER + PBKDF2_OID + opt.SEQUENCE_PBKDF2_INNER_CONTAINER + SALT_OCTET + ITER_INTEGER + opt.SEQUENCE_HASH_CONTAINER + opt.HASH_OID + opt.SEQUENCE_AES_CONTAINER + opt.CIPHER_OID + IV_OCTET);
    opt.SEQUENCE_PBES2_CONTAINER = '30' + decimalToHex(PBES2_OID + opt.SEQUENCE_PBES2_INNER_CONTAINER + opt.SEQUENCE_PBKDF2_CONTAINER + PBKDF2_OID + opt.SEQUENCE_PBKDF2_INNER_CONTAINER + SALT_OCTET + ITER_INTEGER + opt.SEQUENCE_HASH_CONTAINER + opt.HASH_OID + opt.SEQUENCE_AES_CONTAINER + opt.CIPHER_OID + IV_OCTET);

    const SEQUENCE_PARAMETERS = opt.SEQUENCE_PBES2_CONTAINER + PBES2_OID + opt.SEQUENCE_PBES2_INNER_CONTAINER + opt.SEQUENCE_PBKDF2_CONTAINER + PBKDF2_OID + opt.SEQUENCE_PBKDF2_INNER_CONTAINER + SALT_OCTET + ITER_INTEGER + opt.SEQUENCE_HASH_CONTAINER + opt.HASH_OID + opt.SEQUENCE_AES_CONTAINER + opt.CIPHER_OID + IV_OCTET;
    const SEQUENCE_LENGTH = decimalToHex(SEQUENCE_PARAMETERS + KEY_OCTET);
    const SEQUENCE_PADDING = SEQUENCE_LENGTH.length / 2 === 2 ? '82' : '81';
    const SEQUENCE = '30' + SEQUENCE_PADDING + SEQUENCE_LENGTH + SEQUENCE_PARAMETERS + KEY_OCTET;

    const asnKey = hexStringToArrayBuffer(SEQUENCE);
    let pemKey = arrayBufferToBase64(asnKey);
    pemKey = addNewLines(pemKey);
    pemKey = '-----BEGIN ENCRYPTED PRIVATE KEY-----\r\n' + pemKey + '-----END ENCRYPTED PRIVATE KEY-----';

    return pemKey;
}

function fromAsn1(pem) {
    const opt = {};
    pem = removeLines(pem);
    pem = pem.replace('-----BEGIN ENCRYPTED PRIVATE KEY-----', '');
    pem = pem.replace('-----END ENCRYPTED PRIVATE KEY-----', '');
    pem = base64ToArrayBuffer(pem);

    const hex = arrayBufferToHexString(pem);
    opt.data = hex;

    if (opt.data.includes(PBES2_OID) && opt.data.includes(PBKDF2_OID)) {
        opt.valid = true;
    }

    opt.saltBegin = opt.data.indexOf(PBKDF2_OID) + 28;

    if (opt.data.includes(AES256GCM_OID)) {
        opt.cipher = 'AES-GCM';
        opt.keyLength = 256;
        opt.ivBegin = opt.data.indexOf(AES256GCM_OID) + 24;
    } else if (opt.data.includes(AES192GCM_OID)) {
        opt.cipher = 'AES-GCM';
        opt.keyLength = 192;
        opt.ivBegin = opt.data.indexOf(AES192GCM_OID) + 24;
    } else if (opt.data.includes(AES128GCM_OID)) {
        opt.cipher = 'AES-GCM';
        opt.keyLength = 128;
        opt.ivBegin = opt.data.indexOf(AES128GCM_OID) + 24;
    } else if (opt.data.includes(AES256CBC_OID)) {
        opt.cipher = 'AES-CBC';
        opt.keyLength = 256;
        opt.ivBegin = opt.data.indexOf(AES256CBC_OID) + 24;
    } else if (opt.data.includes(AES192CBC_OID)) {
        opt.cipher = 'AES-CBC';
        opt.keyLength = 192;
        opt.ivBegin = opt.data.indexOf(AES192CBC_OID) + 24;
    } else if (opt.data.includes(AES128CBC_OID)) {
        opt.cipher = 'AES-CBC';
        opt.keyLength = 128;
        opt.ivBegin = opt.data.indexOf(AES128CBC_OID) + 24;
    } else if (opt.data.includes(AES256CFB_OID)) {
        opt.cipher = 'AES-CFB';
        opt.keyLength = 256;
        opt.ivBegin = opt.data.indexOf(AES256CFB_OID) + 24;
    } else if (opt.data.includes(AES192CFB_OID)) {
        opt.cipher = 'AES-CFB';
        opt.keyLength = 192;
        opt.ivBegin = opt.data.indexOf(AES192CFB_OID) + 24;
    } else if (opt.data.includes(AES128CFB_OID)) {
        opt.cipher = 'AES-CFB';
        opt.keyLength = 128;
        opt.ivBegin = opt.data.indexOf(AES128CFB_OID) + 22;
    }

    if (opt.data.includes(SHA512_OID)) {
        opt.hash = 'SHA-512';
    } else if (opt.data.includes(SHA384_OID)) {
        opt.hash = 'SHA-384';
    } else if (opt.data.includes(SHA256_OID)) {
        opt.hash = 'SHA-256';
    } else if (opt.data.includes(SHA1_OID)) {
        opt.hash = 'SHA-1';
    }

    opt.saltLength = parseInt(opt.data.substr(opt.saltBegin, 2), 16);
    opt.ivLength = parseInt(opt.data.substr(opt.ivBegin, 2), 16);

    opt.salt = opt.data.substr(opt.saltBegin + 2, opt.saltLength * 2);
    opt.iv = opt.data.substr(opt.ivBegin + 2, opt.ivLength * 2);

    opt.iterBegin = opt.saltBegin + 4 + (opt.saltLength * 2);
    opt.iterLength = parseInt(opt.data.substr(opt.iterBegin, 2), 16);
    opt.iter = parseInt(opt.data.substr(opt.iterBegin + 2, opt.iterLength * 2), 16);

    opt.sequencePadding = opt.data.substr(2, 2) === '81' ? 8 : 10;
    opt.parametersPadding = opt.data.substr(2, 2) === '81' ? 12 : 16;
    opt.sequenceLength = parseInt(opt.data.substr(opt.sequencePadding, 2), 16);
    opt.encryptedDataBegin = opt.parametersPadding + (opt.sequenceLength * 2);
    opt.encryptedDataPadding = opt.data.substr(opt.encryptedDataBegin - 2, 2) === '81' ? 2 : 4;
    opt.encryptedDataLength = parseInt(opt.data.substr(opt.encryptedDataBegin, 6), 16);
    opt.encryptedData = opt.data.substr(opt.encryptedDataBegin + opt.encryptedDataPadding, (opt.encryptedDataLength * 2));

    return {
        salt: hexStringToArrayBuffer(opt.salt),
        iv: hexStringToArrayBuffer(opt.iv),
        cipher: opt.cipher,
        keyLength: opt.keyLength,
        hash: opt.hash,
        iter: opt.iter,
        encryptedData: hexStringToArrayBuffer(opt.encryptedData)
    };
}

export function generateSalt(length) {
    let arr = new Uint8Array((length || 40) / 2);
    cryptoLib.getRandomValues(arr);
    return Array.from(arr, dec2hex).join('');
}

function dec2hex(dec) {
    return ('0' + dec.toString(16)).substr(-2);
}

export async function getRSAKeyPair(modulusLength, hash, paddingScheme, usages, isExtractable) {
    modulusLength = (typeof modulusLength !== 'undefined') ? modulusLength : 2048;
    hash = (typeof hash !== 'undefined') ? hash : 'SHA-512';
    paddingScheme = (typeof paddingScheme !== 'undefined') ? paddingScheme : 'RSA-OAEP';
    usages = (typeof usages !== 'undefined') ? usages : ['encrypt', 'decrypt'];
    isExtractable = (typeof isExtractable !== 'undefined') ? isExtractable : false;

    if (typeof modulusLength !== 'number') {
        throw new TypeError('Expected input of modulusLength to be a Number');
    }

    if (typeof hash !== 'string') {
        throw new TypeError('Expected input of hash expected to be a String');
    }

    if (typeof paddingScheme !== 'string') {
        throw new TypeError('Expected input of paddingScheme to be a String');
    }

    if (typeof usages !== 'object') {
        throw new TypeError('Expected input of usages to be an Array');
    }

    if (typeof isExtractable !== 'boolean') {
        throw new TypeError('Expected input of isExtractable to be a Boolean');
    }

    return await cryptoApi.generateKey(
        {
            name: paddingScheme,
            modulusLength: modulusLength,
            publicExponent: new Uint8Array([0x01, 0x00, 0x01]),
            hash: {name: hash}
        },
        isExtractable,
        usages
    );
}


export async function encryptPrivateKeyToPemString(privateKey, passphrase, iterations, hash, cipher, keyLength) {
    iterations = (typeof iterations !== 'undefined') ? iterations : 100000;
    hash = (typeof hash !== 'undefined') ? hash : 'SHA-512';
    cipher = (typeof cipher !== 'undefined') ? cipher : 'AES-GCM';
    keyLength = (typeof keyLength !== 'undefined') ? keyLength : 256;

    if (Object.prototype.toString.call(privateKey) !== '[object CryptoKey]' && privateKey.type !== 'private') {
        throw new TypeError('Expected input of privateKey to be a CryptoKey Object')
    }

    if (typeof passphrase !== 'string') {
        throw new TypeError('Expected input of passphrase to be a String')
    }

    if (typeof iterations !== 'number') {
        throw new TypeError('Expected input of iterations to be a Number')
    }

    if (typeof hash !== 'string') {
        throw new TypeError('Expected input of hash to be a String')
    }

    if (typeof cipher !== 'string') {
        throw new TypeError('Expected input of cipher to be a String')
    }

    if (typeof keyLength !== 'number') {
        throw new TypeError('Expected input of keyLength to be a Number')
    }

    let ivLength = null;
    switch (cipher) {
        case 'AES-GCM':
            ivLength = 12;
            break;
        case 'AES-CBC':
            ivLength = 16;
            break;
        case 'AES-CFB':
            ivLength = 16;
            break;
        default:
            throw new TypeError('Unsupported cipher');
    }

    const salt = stringToArrayBuffer(generateSalt(16));
    const iv = stringToArrayBuffer(generateSalt(ivLength));

    const baseKey = await cryptoApi.importKey(
        'raw',
        stringToArrayBuffer(passphrase),
        {
            name: 'PBKDF2'
        },
        false,
        ['deriveKey']
    );

    const derivedKey = await cryptoApi.deriveKey(
        {
            name: 'PBKDF2',
            salt: salt,
            iterations: iterations,
            hash: hash
        },
        baseKey,
        {
            name: cipher,
            length: keyLength
        },
        false,
        ['wrapKey']
    );

    const wrappedKey = await cryptoApi.wrapKey(
        'pkcs8',
        privateKey,
        derivedKey,
        {
            name: cipher,
            iv: iv,
            tagLength: 128
        }
    );

    return toAsn1(wrappedKey, salt, iv, iterations, hash, cipher, keyLength);
}

export async function decryptPrivateKeyFromPemString(encryptedPrivateKey, passphrase, options) {
    const epki = fromAsn1(encryptedPrivateKey);

    if (typeof options === 'undefined') {
        options = {};
    }

    const keyOptions = {};

    options.name = (typeof options.name !== 'undefined') ? options.name : 'RSA-OAEP';
    options.isExtractable = (typeof options.isExtractable !== 'undefined') ? options.isExtractable : false;

    if (typeof options.keyUsages === 'undefined') {
        switch (options.name) {
            case 'ECDH':
                options.keyUsages = ['deriveKey', 'deriveBits'];
                break;
            case 'ECDSA':
            case 'RSA-PSS':
                options.keyUsages = ['sign'];
                break;
            case 'RSA-OAEP':
                options.keyUsages = ['decrypt'];
                break;
            default:
                throw new TypeError('Unsupported encryptedPrivateKey');
        }
    }

    keyOptions.name = options.name;

    if (options.name === 'ECDH' || options.name === 'ECDSA') {
        options.namedCurve = (typeof options.namedCurve !== 'undefined') ? options.namedCurve : 'P-256';

        keyOptions.namedCurve = options.namedCurve;

        if (typeof options.namedCurve !== 'string') {
            throw new TypeError('Expected input of options.namedCurve to be a base64 String');
        }
    } else if (options.name === 'RSA-OAEP' || options.name === 'RSA-PSS') {
        options.hash = (typeof options.hash !== 'undefined') ? options.hash : {};
        options.hash.name = (typeof options.hash.name !== 'undefined') ? options.hash.name : 'SHA-512';

        keyOptions.hash = {};
        keyOptions.hash.name = options.hash.name;

        if (typeof options.hash.name !== 'string') {
            throw new TypeError('Expected input of options.hash.name to be a base64 String');
        }
    } else {
        throw new TypeError('Unsupported encryptedPrivateKey');
    }

    if (typeof encryptedPrivateKey !== 'string') {
        throw new TypeError('Expected input of encryptedPrivateKey to be a base64 String');
    }

    if (typeof passphrase !== 'string') {
        throw new TypeError('Expected input of passphrase to be a String');
    }

    if (typeof options.keyUsages !== 'object') {
        throw new TypeError('Expected input of options.keyUsages to be a String');
    }

    if (typeof options.isExtractable !== 'boolean') {
        throw new TypeError('Expected input of options.isExtractable to be a Boolean');
    }

    const baseKey = await cryptoApi.importKey(
        'raw',
        stringToArrayBuffer(passphrase),
        {
            name: 'PBKDF2'
        },
        false,
        ['deriveKey']
    );

    const derivedKey = await cryptoApi.deriveKey(
        {
            name: 'PBKDF2',
            salt: epki.salt,
            iterations: epki.iter,
            hash: epki.hash
        },
        baseKey,
        {
            name: epki.cipher,
            length: epki.keyLength
        },
        false,
        ['unwrapKey']
    );

    return await cryptoApi.unwrapKey(
        'pkcs8',
        epki.encryptedData,
        derivedKey,
        {
            name: epki.cipher,
            iv: epki.iv,
            tagLength: 128
        },
        keyOptions,
        options.isExtractable,
        options.keyUsages
    );
}

export async function publicCryptoKeyToPemString(publicKey) {
    if (Object.prototype.toString.call(publicKey) !== '[object CryptoKey]' && publicKey.type !== 'public') {
        throw new TypeError('Expected input to be a CryptoKey Object');
    }

    const exportedPublicKey = await cryptoApi.exportKey(
        'spki',
        publicKey
    );

    const b64 = arrayBufferToBase64(exportedPublicKey);
    let pem = addNewLines(b64);
    return '-----BEGIN PUBLIC KEY-----\r\n' + pem + '-----END PUBLIC KEY-----';
}

export async function pemStringToPublicCryptoKey(pem, options) {
    if (typeof options === 'undefined') {
        options = {};
    }

    options.isExtractable = (typeof options.isExtractable !== 'undefined') ? options.isExtractable : true;

    if (typeof pem !== 'string') {
        throw new TypeError('Expected input of pem to be a String');
    }

    if (typeof options.isExtractable !== 'boolean') {
        throw new TypeError('Expected input of options.isExtractable to be a String');
    }

    pem = pem.replace('-----BEGIN PUBLIC KEY-----', '');
    pem = pem.replace('-----END PUBLIC KEY-----', '');
    const b64 = removeLines(pem);
    const arrayBuffer = base64ToArrayBuffer(b64);
    const hex = arrayBufferToHexString(arrayBuffer);
    let opt = null;

    if (hex.includes(EC_OID)) {
        options.name = (typeof options.name !== 'undefined') ? options.name : 'ECDH';

        if (typeof options.name !== 'string') {
            throw new TypeError('Expected input of options.name to be a String');
        }

        let curve = null;
        if (hex.includes(P256_OID)) {
            curve = 'P-256';
        } else if (hex.includes(P384_OID)) {
            curve = 'P-384';
        } else if (hex.includes(P521_OID)) {
            curve = 'P-521';
        } else {
            throw new TypeError('Invalid curve');
        }

        if (options.name === 'ECDH') {
            options.usages = (typeof options.usages !== 'undefined') ? options.usages : [];
        } else if (options.name === 'ECDSA') {
            options.usages = (typeof options.usages !== 'undefined') ? options.usages : ['verify'];
        } else {
            throw new TypeError('Invalid algorithm name')
        }

        if (typeof options.usages !== 'object') {
            throw new TypeError('Expected input of options.usages to be a String')
        }

        opt = {
            name: options.name,
            namedCurve: curve
        }
    } else if (hex.includes(RSA_OID)) {
        options.name = (typeof options.name !== 'undefined') ? options.name : 'RSA-OAEP';
        options.hash = (typeof options.hash !== 'undefined') ? options.hash : 'SHA-512';

        if (typeof options.name !== 'string') {
            throw new TypeError('Expected input of options.name to be a String');
        }

        if (typeof options.hash !== 'string') {
            throw new TypeError('Expected input of options.hash to be a String');
        }

        if (options.name === 'RSA-OAEP') {
            options.usages = (typeof options.usages !== 'undefined') ? options.usages : ['encrypt'];
        } else if (options.name === 'RSA-PSS') {
            options.usages = (typeof options.usages !== 'undefined') ? options.usages : ['verify'];
        } else {
            throw new TypeError('Invalid algorithm name');
        }

        if (typeof options.usages !== 'object') {
            throw new TypeError('Expected input of options.usages to be an Array');
        }

        opt = {
            name: options.name,
            hash: {
                name: options.hash
            }
        };
    } else {
        throw new TypeError('Invalid public key')
    }

    return await cryptoApi.importKey(
        'spki',
        arrayBuffer,
        opt,
        options.isExtractable,
        options.usages
    );
}

export async function rsaEncrypt(publicKey, data) {
    if (Object.prototype.toString.call(publicKey) !== '[object CryptoKey]' && publicKey.type !== 'public') {
        throw new TypeError('Expected input of publicKey to be a CryptoKey of type public')
    }

    if (typeof data !== 'string') {
        throw new TypeError('Expected input of data to be an String')
    }

    const arrayBuffer = stringToArrayBuffer(data);

    const encrypted = await cryptoApi.encrypt(
        {
            name: 'RSA-OAEP'
        },
        publicKey,
        arrayBuffer
    );

    return arrayBufferToBase64(encrypted);
}

export async function rsaDecrypt (privateKey, encryptedData) {
    if (Object.prototype.toString.call(privateKey) !== '[object CryptoKey]' && privateKey.type !== 'private') {
        throw new TypeError('Expected input of privateKey to be a CryptoKey of type private')
    }

    if (typeof encryptedData !== 'string') {
        throw new TypeError('Expected input of encryptedData to be a String')
    }

    const decrypted = await cryptoApi.decrypt(
        {
            name: 'RSA-OAEP'
        },
        privateKey,
        base64ToArrayBuffer(encryptedData)
    );

    return arrayBufferToString(decrypted);
}
