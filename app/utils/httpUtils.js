import {toast} from 'react-toastify';
import i18n from 'i18n';

export function processResponseErrorAsNotification(error) {
    const response = error.response;

    if (response.data && response.data.message) {
        toast.error(i18n.t(response.data.message, i18n.t('global.requestError')));
    } else {
        toast.error(i18n.t('global.requestError'));
    }

    return error;
}

export function processResponseErrorAsFormOrNotification(error) {
    const response = error.response;
    let message = '';
    if (response.data && response.data.message) {
        message = i18n.t(response.data.message, '');
    }
    if (message !== '') {
        return Object.assign(new Error('showOnForm'), {messageTranslation: message});
    } else {
        return processResponseErrorAsNotification(error);
    }
}