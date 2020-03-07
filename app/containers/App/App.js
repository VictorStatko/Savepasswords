import React from 'react';
import {Redirect, Switch} from 'react-router-dom';
import GuestPage from 'containers/GuestPage';
import PrivateRoute from "utils/routes/PrivateRoute";
import GuestRoute from "utils/routes/GuestRoute";
import PersonalAccounts from "pages/private/PersonalAccounts";
import PrivatePage from "containers/PrivatePage";

const App = () => (
        <Switch>
            <GuestRoute path="/sign-up" component={props => (<GuestPage {...props} process="sign-up"/>)}/>
            <GuestRoute path="/sign-in" component={props => (<GuestPage {...props} process="sign-in"/>)}/>
            <PrivateRoute path="/accounts" component={props => (<PrivatePage {...props} component={<PersonalAccounts/>}/>)}/>
            <Redirect to="/accounts"/>
        </Switch>
);

export default App;