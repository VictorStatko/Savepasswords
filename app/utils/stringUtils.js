export function isEmpty(str) {
    return (!str || 0 === str.length);
}

export function isNotEmpty(str) {
    return (str && 0 !== str.length);
}