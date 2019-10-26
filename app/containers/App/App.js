import React from 'react';
import {Redirect, Route, Switch} from 'react-router-dom';
import GuestPage from 'containers/GuestPage';

const App = () => (
    <React.Fragment>
        <Switch>
            <Route path="/sign-up" render={props => (<GuestPage {...props} process="sign-up"/>)}/>
            <Route path="/sign-in" render={props => (<GuestPage {...props} process="sign-in"/>)}/>
            <Route path="" render={() => (<Redirect to="/sign-up"/>)}/>
        </Switch>
    </React.Fragment>
);

export default App;