import React from 'react';
import {Switch} from 'react-router-dom';
import GuestPage from 'containers/GuestPage';
import PrivateRoute from "utils/routes/PrivateRoute";
import GuestRoute from "utils/routes/GuestRoute";
import Dashboard from "pages/private/Dashboard/Dashboard";

const App = () => (
    <React.Fragment>
        <Switch>
            <GuestRoute path="/sign-up" component={props => (<GuestPage {...props} process="sign-up"/>)}/>
            <GuestRoute path="/sign-in" component={props => (<GuestPage {...props} process="sign-in"/>)}/>
            <PrivateRoute path="/" component={Dashboard}/>
        </Switch>
    </React.Fragment>
);

export default App;