import {isNotEmpty} from "./stringUtils";

export function isEmailValid(str) {
    return isNotEmpty(str) && str.match(/\S+@\S+\.\S+/);
}

export function isStringMaxLengthValid(str, maxLength) {
    return str && str.length <= maxLength;
}