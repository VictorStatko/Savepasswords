import React from 'react';
import {Redirect, Switch} from 'react-router-dom';
import GuestPage from 'containers/GuestPage';
import PrivateRoute from "utils/routes/PrivateRoute";
import GuestRoute from "utils/routes/GuestRoute";
import PersonalAccounts from "pages/private/PersonalAccounts";
import PrivatePage from "containers/PrivatePage";
import NotFoundPage from "pages/private/404";

const App = () => (
    <Switch>
        <GuestRoute path="/sign-up" component={props => (<GuestPage {...props} process="sign-up"/>)}/>
        <GuestRoute path="/sign-in" component={props => (<GuestPage {...props} process="sign-in"/>)}/>
        <PrivateRoute exact path="/" component={() => ( <Redirect to="/accounts"/>)}/>
        <PrivateRoute exact path="/accounts" component={props => (<PrivatePage {...props} component={<PersonalAccounts/>}/>)}/>
        <PrivateRoute component={props => (<PrivatePage {...props} component={<NotFoundPage/>}/>)}/>
    </Switch>
);

export default App;