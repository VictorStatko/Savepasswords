import React from "react";
import {connect} from "react-redux";
import {Redirect, Route} from "react-router-dom";

const PrivateRoute = ({component: Component, isSignedIn, ...rest}) => (
    <Route
        {...rest}
        render={props =>
            isSignedIn ? (
                <Component {...props} />
            ) : (
                <Redirect
                    to={{
                        pathname: "/sign-in",
                        state: {from: props.location}
                    }}
                />
            )
        }
    />
);

const mapStateToProps = state => {
    return {
        isSignedIn: state.account.isLoggedIn
    };
};

export default connect(mapStateToProps)(PrivateRoute);