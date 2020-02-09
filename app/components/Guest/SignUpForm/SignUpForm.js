import React, {Component} from 'react';
import FormUserDetails from "./FormUserDetails";
import styles from "./SignUpForm.module.scss";
import {connect} from "react-redux";
import {trySignUp} from "ducks/account/actions";
import {compose} from "redux";
import {withTranslation} from "react-i18next";
import {Link} from "react-router-dom";

class SignUpForm extends Component {
    state = {
        email: "",
        password: ""
    };

    handleChange = (input, value) => {
        this.setState({[input]: value});
    };

    onSubmit = async e => {
        e.preventDefault();
        await this.props.trySignUp({
            email: this.state.email,
            password: this.state.password
        });
    };

    render() {
        const {t} = this.props;

        return (
            <React.Fragment>
                <form onSubmit={this.onSubmit}>
                    <FormUserDetails
                        handleChange={this.handleChange}
                        password={this.state.password}
                        email={this.state.email}
                    />
                </form>
                <div className={styles.changePageLink}>
                    <Link to={'/sign-in'}>{t('signUp.alreadyRegisteredLink')}</Link>
                </div>
            </React.Fragment>
        )
    }
}

SignUpForm.propTypes = {};

const mapStateToProps = (state) => {
    return {
        account: state.account
    }
};

const withConnect = connect(
    mapStateToProps,
    {
        trySignUp: trySignUp
    }
);

export default compose(withTranslation(), withConnect)(SignUpForm);
