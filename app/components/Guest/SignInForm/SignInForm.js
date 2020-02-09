import React, {Component} from 'react';
import {Link} from "react-router-dom";
import {withTranslation} from "react-i18next";
import TextInput from "components/default/inputs/TextInput";
import styles from "./SignInForm.module.scss";
import PrimaryButton from "components/default/buttons/PrimaryButton";
import {isEmpty} from "utils/stringUtils";
import {connect} from "react-redux";
import {trySignIn} from "ducks/account/actions";
import {compose} from "redux";
import {isEmailValid, isStringMaxLengthValid} from "utils/validationUtils";
import {MAX_LENGTH_EMAIL, MAX_LENGTH_PASSWORD} from "utils/validationUtils";
import history from 'utils/history';

class SignInForm extends Component {
    state = {
        email: '',
        password: '',
        emailError: '',
        passwordError: '',
        serverError: ''
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

        if (this.validate()) {
            try {
                await this.props.trySignIn({
                    username: email,
                    password: password
                });
                history.push('/');
            } catch (error) {
                if (error.message && error.message === 'showOnForm') {
                    this.setState({
                        serverError: error.messageTranslation
                    });
                }
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
                emailError: t('signIn.validation.email')
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
            <form onSubmit={this.onSubmit}>
                <TextInput id="email" label={t('signIn.emailLabel')} className={styles.textInput} value={email}
                           onChange={e => this.handleChange('email', e.target.value)} error={emailError}/>
                <TextInput id="password" label={t('signIn.passwordLabel')} className={styles.textInput} value={password}
                           onChange={e => this.handleChange('password', e.target.value)} error={passwordError}/>
                <div className={styles.serverError}>{serverError}</div>
                <div className={styles.buttonContainer}>
                    <PrimaryButton onClick={this.continue} text={t('global.submit')}/>
                </div>
                <div className={styles.changePageLink}>
                    <Link to={'/sign-up'}>{t('signIn.notRegisteredLink')}</Link>
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
        trySignIn: trySignIn
    }
);

export default compose(withTranslation(), withConnect)(SignInForm);

