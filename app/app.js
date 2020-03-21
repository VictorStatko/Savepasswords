import React from 'react';
import ReactDOM from 'react-dom';
import {Provider} from 'react-redux';
import {ConnectedRouter} from 'connected-react-router';
import history from 'utils/history';
import App from 'containers/App';
import {ToastContainer} from 'react-toastify';
import 'react-toastify/dist/ReactToastify.min.css';
import throttle from 'lodash.throttle';
import 'bootstrap/dist/css/bootstrap.min.css';

// eslint-disable-next-line no-unused-vars
import i18n from "./i18n";

import configureStore from './configureStore';

import 'styles/theme.scss';
import {loadState, saveState} from "./localStorage";

// Create redux store with history
const initialState = loadState();
export const store = configureStore(initialState, history);

store.subscribe(throttle(() => {
    saveState({
        account: store.getState().account
    });
}, 1000));

const MOUNT_NODE = document.getElementById('app');

const render = () => {
    ReactDOM.render(
        <Provider store={store}>
            <ConnectedRouter history={history}>
                <App/>
                <ToastContainer
                    position="bottom-right"
                    autoClose={5000}
                    hideProgressBar
                    newestOnTop
                    closeOnClick
                    rtl={false}
                    pauseOnVisibilityChange
                    draggable
                    pauseOnHover
                />
            </ConnectedRouter>
        </Provider>,
        MOUNT_NODE
    );
};

if (module.hot) {
    // Hot reloadable React components and translation json files
    // modules.hot.accept does not accept dynamic dependencies,
    // have to be constants at compile-time
    module.hot.accept(['containers/App'], () => {
        ReactDOM.unmountComponentAtNode(MOUNT_NODE);
        render();
    });
}

render();