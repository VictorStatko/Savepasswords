import {toast} from 'react-toastify';
import i18n from 'i18n';

export function processResponseErrorAsNotification(error) {
    const response = error.response;

    if (response.data && response.data.error) {
        toast.error(i18n.t(response.data.error, i18n.t('global.requestError')));
    } else {
        toast.error(i18n.t('global.requestError'));
    }

    return error;
}

export function processResponseErrorAsFormOrNotification(error) {
    console.log(error);
    if (error.response.status === 500) {
        return processResponseErrorAsNotification(error);
    }
    const response = error.response;
    let message = '';
    if (response.data && response.data.error) {
        message = i18n.t(response.data.error, '');
    }
    if (message !== '') {
        return Object.assign(new Error('showOnForm'), {messageTranslation: message});
    } else {
        return processResponseErrorAsNotification(error);
    }
}