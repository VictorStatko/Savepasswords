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
import {setStateAsync} from "utils/stateUtils";
import VerificationCodeModal from "../VerificationCodeModal";

class SignUpForm extends Component {
    state = {
        email: "",
        password: "",
        repeatPassword: "",
        serverError: "",
        verificationCodeModal: false,
        loading: false,
        signUpAttempt: 1
    };

    handleChange = (input, value) => {
        this.setState({
                [input]: value,
                serverError: ''
            }
        );
    };

    handleVerificationCodeModalToggle = () => {
        this.setState({
            verificationCodeModal: !this.state.verificationCodeModal
        });
    };


    onSubmit = async e => {
        e.preventDefault();

        await setStateAsync(this, {loading: true});
        try {
            await this.props.trySignUp({
                email: this.state.email,
                password: this.state.password,
            });

            this.setState({
                email: "",
                password: "",
                repeatPassword: "",
                serverError: ""
            });
            toast.success(i18n.t('signUp.confirmationNeeded'));
        } catch (error) {
            this.setState({
                signUpAttempt: this.state.signUpAttempt + 1
            });

            if (error && error.message && error.message === 'showOnForm') {
                this.setState({
                    serverError: error.messageTranslation
                });
            } else {
                console.error(error);
            }
        } finally {
            await setStateAsync(this, {loading: false});
        }
    };

    render() {
        const {t} = this.props;
        const {verificationCodeModal} = this.state;

        return (
            <React.Fragment>
                <form onSubmit={this.onSubmit}>
                    <FormUserDetails
                        handleChange={this.handleChange}
                        password={this.state.password}
                        repeatPassword={this.state.repeatPassword}
                        email={this.state.email}
                        loading={this.state.loading}
                        serverError={this.state.serverError}
                        signUpAttempt={this.state.signUpAttempt}
                    />
                </form>
                <div className={styles.changePageLink}>
                    <Link to={'/sign-in'}><span
                        className={styles.existingAccount}>{t('signUp.useExistingAccount')}</span>
                    </Link>
                    <span className={styles.verificationCodeIssues}
                          onClick={this.handleVerificationCodeModalToggle}>{t('signUp.verificationCodeIssues')}</span>

                </div>
                {verificationCodeModal ?
                    <VerificationCodeModal close={this.handleVerificationCodeModalToggle}/>
                    : null
                }
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
