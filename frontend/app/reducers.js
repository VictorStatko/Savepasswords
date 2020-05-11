import {combineReducers} from 'redux';
import {connectRouter} from 'connected-react-router';
import { loadingBarReducer } from 'react-redux-loading-bar';

import history from 'utils/history';

export default function createReducer(injectedReducers = {}) {
    return combineReducers({
        router: connectRouter(history),
        ...injectedReducers,
        loadingBar: loadingBarReducer
    });
}