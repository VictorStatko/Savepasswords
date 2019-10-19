import React from 'react';
import {Route, Switch} from 'react-router-dom';
import SignUpPage from 'containers/SignUpPage';

const App = () => (
    <React.Fragment>
        <Switch>
            <Route exact path="/" component={SignUpPage}/>
            <Route path=""/>
        </Switch>
    </React.Fragment>
);

export default App;