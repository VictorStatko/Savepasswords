import React from 'react';
import styles from "./Header.module.scss";
import {connect} from "react-redux";
import {compose} from "redux";
import {signOut} from "ducks/account/actions";

class Header extends React.Component {

    logout = () => {
        this.props.signOut();
    };

    render() {
        const {account} = this.props;
        return (
            <header className={styles.header}>
                <div className={styles.right}>
                    <span>{account.email}</span>
                    <a href="#" onClick={this.logout}>Logout</a>
                </div>
            </header>
        );
    }

}

const mapStateToProps = (state) => {
    return {
        account: state.account
    }
};

const withConnect = connect(mapStateToProps, {
    signOut: signOut
});

export default compose(withConnect)(Header);


