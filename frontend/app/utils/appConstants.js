export const GET = "GET";
export const POST = "POST";
export const PUT = "PUT";
export const DELETE = "DELETE";

export const FOLDER_REMOVAL_OPTIONS = Object.freeze({
    FOLDER_ONLY: 'FOLDER_ONLY',
    WITH_ACCOUNTS: 'WITH_ACCOUNTS'
});

const SETTING_TYPES_INTERNAL = ['SECURITY', 'ACCOUNTS'];
SETTING_TYPES_INTERNAL.forEach(Object.freeze);

export const SETTING_TYPES = SETTING_TYPES_INTERNAL;