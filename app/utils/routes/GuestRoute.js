import React from "react";
import {connect} from "react-redux";
import {Redirect, Route} from "react-router-dom";

const GuestRoute = ({
                        component: Component,
                        isSignedIn,
                        location: {state},
                        ...rest
                    }) => (
    <Route
        {...rest}
        render={props =>
            isSignedIn ? (
                <Redirect
                    to={{
                        pathname: "/",
                        state
                    }}
                />
            ) : (
                <Component {...props} />
            )
        }
    />
);

const mapStateToProps = state => {
    return {
        isSignedIn: state.account.isLoggedIn
    };
};

export default connect(mapStateToProps)(GuestRoute);