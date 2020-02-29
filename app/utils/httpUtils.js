import {toast} from 'react-toastify';
import i18n from 'i18n';

export function processResponseErrorAsNotification(response) {

    if (response && response.data && response.data.error) {
        toast.error(i18n.t(response.data.error, i18n.t('global.requestError')));
    } else {
        toast.error(i18n.t('global.requestError'));
    }

    return response;
}

export function processResponseErrorAsFormOrNotification(response) {

    if (response && response.status === 500) {
        return processResponseErrorAsNotification(response);
    }

    let message = '';
    if (response.data && response.data.error) {
        message = i18n.t(response.data.error, '');
    }
    if (message !== '') {
        return Object.assign(new Error('showOnForm'), {messageTranslation: message});
    } else {
        return processResponseErrorAsNotification(response);
    }
}