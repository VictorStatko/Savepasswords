
import { createStore, applyMiddleware, compose } from 'redux';
import { routerMiddleware } from 'connected-react-router';
import createReducer from './reducers';


export default function configureStore(initialState = {}, history) {
    const middlewares = [routerMiddleware(history)];

    const enhancers = [applyMiddleware(...middlewares)];

    const composeEnhancers = process.env.NODE_ENV !== 'production' && typeof window === 'object' && window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__
        ? window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__({shouldHotReload: false}) : compose;

    const store = createStore(createReducer(), initialState, composeEnhancers(...enhancers));

    store.injectedReducers = {}; // Reducer registry

    if (module.hot) {
        module.hot.accept('./reducers', () => {
            store.replaceReducer(createReducer(store.injectedReducers));
            store.dispatch({ type: '@@REDUCER_INJECTED' });
        });
    }

    return store;
}