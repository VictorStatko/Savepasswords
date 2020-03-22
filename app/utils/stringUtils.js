export function isEmpty(str) {
    return (!str || 0 === str.length);
}

export function isNotEmpty(str) {
    return (str && 0 !== str.length);
}

export function reverseString(str) {
    return str.split("").reverse().join("");
}

export function ellipsisByCharactersCount(str, charactersCount) {
    if (!str || !charactersCount) {
        return "";
    }


    return str.length > charactersCount ? str.substring(0, charactersCount) + '...' :
        str;

}

export function removeProtocols(str) {
    return str.replace(/(^\w+:|^)\/\//, '');
}