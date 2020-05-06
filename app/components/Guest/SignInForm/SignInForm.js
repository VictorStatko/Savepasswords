import React, {Component} from 'react';
import {Link} from "react-router-dom";
import {withTranslation} from "react-i18next";
import TextInput from "components/default/inputs/TextInput";
import styles from "./SignInForm.module.scss";
import {isEmpty} from "utils/stringUtils";
import {connect} from "react-redux";
import {accountOperations} from "ducks/account";
import {compose} from "redux";
import {isEmailValid, isStringMaxLengthValid, MAX_LENGTH_EMAIL, MAX_LENGTH_PASSWORD} from "utils/validationUtils";
import history from 'utils/history';
import {PrimaryButton} from "components/default/buttons/Button/Button";
import {setStateAsync} from "utils/stateUtils";

class SignInForm extends Component {
    state = {
        email: '',
        password: '',
        emailError: '',
        passwordError: '',
        serverError: '',
        loading: false
    };

    handleChange = (input, value) => {
        this.setState({
            [input]: value,
            [input + 'Error']: '',
            serverError: ''
        });
    };

    onSubmit = async e => {
        e.preventDefault();

        const {email, password} = this.state;

        if (!this.validate()) {
            return;
        }

        try {
            await setStateAsync(this, {loading: true});

            await this.props.trySignIn({
                username: email,
                password: password
            });

            history.push('/accounts');
        } catch (error) {
            await setStateAsync(this, {loading: false});

            if (error && error.message && error.message === 'showOnForm') {
                this.setState({
                    serverError: error.messageTranslation
                });
            } else {
                console.error(error);
            }
        }
    };

    validate = () => {
        const validEmail = this.validateEmail();
        const validPassword = this.validatePassword();
        return validEmail && validPassword;
    };

    validateEmail = () => {
        const {t} = this.props;
        const {email} = this.state;

        let valid = true;

        if (isEmpty(email)) {
            this.setState({
                emailError: t('global.validation.notEmpty')
            });
            valid = false;
        } else if (!isEmailValid(email)) {
            this.setState({
                emailError: t('global.validation.email')
            });
            valid = false;
        } else if (!isStringMaxLengthValid(email, MAX_LENGTH_EMAIL)) {
            this.setState({
                emailError: t('global.validation.maxLength', {maxLength: MAX_LENGTH_EMAIL})
            });
            valid = false;
        }

        return valid;
    };

    validatePassword = () => {
        const {t} = this.props;
        const {password} = this.state;

        let valid = true;

        if (isEmpty(password)) {
            this.setState({
                passwordError: t('global.validation.notEmpty')
            });
            valid = false;
        } else if (!isStringMaxLengthValid(password, MAX_LENGTH_PASSWORD)) {
            this.setState({
                passwordError: t('global.validation.maxLength', {maxLength: MAX_LENGTH_PASSWORD})
            });
            valid = false;
        }

        return valid;
    };

    render() {
        const {t} = this.props;
        const {email, password, emailError, passwordError, serverError} = this.state;

        return (
            <form onSubmit={this.onSubmit} className={styles.formContainerSignIn}>
                <TextInput id="email" label={t('signIn.emailLabel')} className={styles.textInput} value={email}
                           onChange={e => this.handleChange('email', e.target.value)} error={emailError}/>
                <TextInput id="password" secret label={t('signIn.passwordLabel')} className={styles.textInput} value={password}
                           onChange={e => this.handleChange('password', e.target.value)} error={passwordError}/>
                <div className={styles.serverError}>{serverError}</div>
                <div className={`${styles.buttonContainer} ${styles.buttonSignIn}`}>
                    <PrimaryButton type="submit" disabled={this.state.loading} content={t('global.submit')} loading={this.state.loading}/>
                </div>
                <div className={styles.changePageLink}>
                    <Link to={'/sign-up'}><span className={styles.newAccount}>{(t('signIn.createAccount'))}</span></Link>
                </div>
            </form>
        );
    }
}

const mapStateToProps = (state) => {
    return {
        account: state.account
    }
};

const withConnect = connect(
    mapStateToProps,
    {
        trySignIn: accountOperations.trySignIn
    }
);

export default compose(withTranslation(), withConnect)(SignInForm);

