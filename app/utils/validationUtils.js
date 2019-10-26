import {isNotEmpty} from "./stringUtils";

export function isEmailValid(str) {
    return isNotEmpty(str) && str.match(/[A-z0-9._%+-]+@[A-z0-9.-]+\.[A-z]{2,}/);
}

export function isStringMaxLengthValid(str, maxLength) {
    return str && str.length <= maxLength;
}