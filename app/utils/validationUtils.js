import {isNotEmpty} from "./stringUtils";

export const MAX_LENGTH_EMAIL = 254;
export const MAX_LENGTH_PASSWORD = 60;
export const MAX_LENGTH_USERNAME = 254;

export function isEmailValid(str) {
    return isNotEmpty(str) && str.match(/^[A-z0-9._%+-]+@[A-z0-9.-]+\.[A-z]{2,}$/);
}

export function isStringMaxLengthValid(str, maxLength) {
    return str && str.length <= maxLength;
}