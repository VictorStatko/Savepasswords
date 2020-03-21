import React, {Component} from 'react';
import FormUserDetails from "./FormUserDetails";
import styles from "./SignUpForm.module.scss";
import {connect} from "react-redux";
import {accountOperations} from "ducks/account";
import {compose} from "redux";
import {withTranslation} from "react-i18next";
import {Link} from "react-router-dom";
import {toast} from 'react-toastify';
import i18n from "i18n";
import history from "utils/history";

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
            password: this.state.password,
        });

        toast.success(i18n.t('signUp.success'));
        history.push('/sign-in');
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
        trySignUp: accountOperations.trySignUp
    }
);

export default compose(withTranslation(), withConnect)(SignUpForm);
