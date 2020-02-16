export function isEmpty(str) {
    return (!str || 0 === str.length);
}

export function isNotEmpty(str) {
    return (str && 0 !== str.length);
}

export function ellipsisByCharactersCount(str, charactersCount) {
    if (!str || !charactersCount) {
        return "";
    }


    return str.length > charactersCount ? str.substring(0, charactersCount) + '...' :
        str;

}