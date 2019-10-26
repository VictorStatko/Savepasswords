import {toast} from 'react-toastify';
import i18n from 'i18n';

export function processResponseError(response) {
    if (response.data && response.data.message) {
        toast.error(i18n.t(response.data.message, i18n.t('global.requestError')));
    } else {
        toast.error(i18n.t('global.requestError'));
    }
}