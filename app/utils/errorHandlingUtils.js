import {toast} from 'react-toastify';
import i18n from 'i18n';

export function processErrorAsNotification(error) {
    console.error(error);
    if (error && error.data && error.data.error) {
        toast.error(i18n.t(error.data.error, i18n.t('global.requestError')));
    } else {
        toast.error(i18n.t('global.internalError'));
    }

    return error;
}

export function processErrorAsFormOrNotification(error) {
    console.error(error);
    if (error && error.status === 500) {
        return processErrorAsNotification(error);
    }

    let message = '';
    if (error && error.data && error.data.error) {
        message = i18n.t(error.data.error, '');
    }
    if (message !== '') {
        return Object.assign(new Error('showOnForm'), {messageTranslation: message});
    } else {
        return processErrorAsNotification(error);
    }
}