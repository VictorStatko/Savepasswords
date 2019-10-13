import React from 'react';
import {Route, Switch} from 'react-router-dom';

import './style.scss';

const App = () => (
    <div className="app-wrapper">
        <Switch>
            <Route exact path="/"/>
            <Route path=""/>
        </Switch>
    </div>
);

export default App;