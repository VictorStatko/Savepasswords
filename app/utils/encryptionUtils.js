export function generateSalt(length) {
    let arr = new Uint8Array((length || 40) / 2);
    window.crypto.getRandomValues(arr);
    return Array.from(arr, dec2hex).join('');
}

function dec2hex (dec) {
    return ('0' + dec.toString(16)).substr(-2);
}