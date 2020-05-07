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
import Recaptcha from "components/default/recaptcha";

class SignInForm extends Component {
    constructor(props) {
        super(props);
        this.state = {
            email: '',
            password: '',
            emailError: '',
            passwordError: '',
            serverError: '',
            loading: false,
            captchaReady: false,
            captchaVerified: false,
            captchaRendered: false,
            loginAttempt: 1
        };
        this.captcha = null;
    }

    onSubmit = async (e) => {
        e.preventDefault();

        if (!this.validate()) {
            return;
        }

        if (this.state.loginAttempt > 1 && !this.state.captchaVerified) {
            if (!this.state.captchaRendered) {
                await this.captcha.renderExplicitly();
            }
            return;
        }

        try {
            this.setState({loading: true});

            const {email, password} = this.state;

            await this.props.trySignIn({
                username: email,
                password: password
            });

            this.setState({loading: false});

            history.push('/accounts');
        } catch (error) {

            this.setState({
                loading: false,
                loginAttempt: this.state.loginAttempt + 1
            });

            if (error && error.message && error.message === 'showOnForm') {
                this.setState({
                    serverError: error.messageTranslation
                });
            } else {
                console.error(error);
            }
        }
    };

    handleChange = (input, value) => {
        this.setState({
            [input]: value,
            [input + 'Error']: '',
            serverError: ''
        });
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

    renderRecaptcha = () => {
        return <Recaptcha classRef={e => (this.captcha = e)}
                          onVerify={() => this.setState({captchaVerified: true})}
                          onLoad={() => this.setState({captchaReady: true})}
                          onRender={() => this.setState({captchaRendered: true})}/>
    };

    render() {
        const {t} = this.props;
        const {email, password, emailError, passwordError, serverError, captchaReady, loading, captchaVerified, captchaRendered} = this.state;

        return (
            <form onSubmit={this.onSubmit} className={styles.formContainerSignIn}>
                <TextInput id="email" label={t('signIn.emailLabel')} className={styles.textInput} value={email}
                           onChange={e => this.handleChange('email', e.target.value)} error={emailError}/>
                <TextInput id="password" secret label={t('signIn.passwordLabel')} className={styles.textInput}
                           value={password}
                           onChange={e => this.handleChange('password', e.target.value)} error={passwordError}/>
                <div className={styles.serverError}>{serverError}</div>
                <div className={styles.recaptchaContainer}>
                    {this.renderRecaptcha()}
                </div>
                <div className={`${styles.buttonContainer} ${styles.buttonSignIn}`}>
                    <PrimaryButton type="submit"
                                   disabled={loading || !captchaReady || (captchaRendered && !captchaVerified)}
                                   content={t('global.submit')} loading={loading}/>
                </div>
                <div className={styles.changePageLink}>
                    <Link to={'/sign-up'}><span
                        className={styles.newAccount}>{(t('signIn.createAccount'))}</span></Link>
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

