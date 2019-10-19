import {toast} from 'react-toastify';
import i18n from '../i18n';

export function processResponseError(response) {
    const t = i18n.t;
    toast.error(t('global.requestError'));
}