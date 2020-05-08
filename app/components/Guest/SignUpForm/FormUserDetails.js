import * as PropTypes from 'prop-types'
import React, {Component} from 'react';
import TextInput from "components/default/inputs/TextInput";
import styles from "./SignUpForm.module.scss";
import {withTranslation} from "react-i18next";
import {isEmpty} from "utils/stringUtils";
import {
    isEmailValid,
    isStringMaxLengthValid,
    isStringMinLengthValid,
    MAX_LENGTH_EMAIL,
    MAX_LENGTH_PASSWORD,
    MIN_LENGTH_PASSWORD
} from "utils/validationUtils";
import {compose} from "redux";
import {connect} from "react-redux";
import {PrimaryButton} from "components/default/buttons/Button/Button";
import Recaptcha from "components/default/recaptcha";

class FormUserDetails extends Component {
    constructor(props) {
        super(props);

        this.state = {
            passwordError: '',
            repeatPasswordError: '',
            emailError: '',
            captchaReady: false,
            captchaVerified: false,
            captchaRendered: false
        };

        this.captcha = null;
    };

    submit = async e => {
        const clientValidation = this.validate();

        if (!clientValidation) {
            e.preventDefault();
        }

        if (this.props.signUpAttempt > 1 && !this.state.captchaVerified) {
            if (!this.state.captchaRendered) {
                await this.captcha.renderExplicitly();
            }

            e.preventDefault();
        }
    };

    validate = () => {
        const validPassword = this.validatePassword();
        const validEmail = this.validateEmail();
        const validRepeatPassword = this.validateRepeatPassword();

        return validPassword && validEmail && validRepeatPassword;
    };

    validateEmail = () => {
        const {t, email} = this.props;
        let valid = true;

        if (isEmpty(email)) {
            this.setState({
                emailError: t('global.validation.notEmpty')
            });
            valid = false;
        } else if (!isEmailValid(email)) {
            this.setState({
                emailError: t('signUp.validation.email')
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

    validateRepeatPassword = () => {
        const {t, repeatPassword, password} = this.props;
        let valid = true;
        if (isEmpty(repeatPassword)) {
            this.setState({
                repeatPasswordError: t('global.validation.notEmpty')
            });
            valid = false;
        } else if (repeatPassword !== password) {
            this.setState({
                repeatPasswordError: t('signUp.passwordNotEqual')
            });
            valid = false;
        }

        return valid;
    };

    validatePassword = () => {
        const {t, password} = this.props;
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
        } else if (!isStringMinLengthValid(password, MIN_LENGTH_PASSWORD)) {
            this.setState({
                passwordError: t('global.validation.minLength', {minLength: MIN_LENGTH_PASSWORD})
            });
            valid = false;
        }

        return valid;
    };

    handleEmailChange = e => {
        this.setState({
            emailError: ''
        });

        this.props.handleChange('email', e.target.value);
    };

    handlePasswordChange = e => {

        this.setState({
            passwordError: '',
            repeatPasswordError: ''
        });

        this.props.handleChange('password', e.target.value);
    };

    handleRepeatChange = e => {

        this.setState({
            repeatPasswordError: ''
        });

        this.props.handleChange('repeatPassword', e.target.value);
    };

    renderRecaptcha = () => {
        return <Recaptcha classRef={e => (this.captcha = e)}
                          onVerify={() => this.setState({captchaVerified: true})}
                          onLoad={() => this.setState({captchaReady: true})}
                          onRender={() => this.setState({captchaRendered: true})}/>
    };


    render() {
        const {t, password, repeatPassword, email, loading, serverError} = this.props;
        const {passwordError, emailError, repeatPasswordError, captchaReady, captchaVerified, captchaRendered} = this.state;

        return (
            <React.Fragment>
                <TextInput id="email" label={t('signUp.emailLabel')} className={styles.textInput} value={email}
                           onChange={this.handleEmailChange} error={emailError}/>
                <TextInput id="password" secret label={t('signUp.passwordLabel')} className={styles.textInput}
                           value={password}
                           onChange={this.handlePasswordChange} error={passwordError}/>
                <TextInput id="repeatPassword" secret label={t('signUp.passwordLabelRepeat')}
                           className={styles.textInput}
                           value={repeatPassword}
                           onChange={this.handleRepeatChange} error={repeatPasswordError}/>
                {isEmpty(serverError) ? null : <div className={styles.serverError}>{serverError}</div>}
                <div className={styles.recaptchaContainer}>
                    {this.renderRecaptcha()}
                </div>
                <div className={styles.buttonContainer}>
                    <PrimaryButton type="submit"
                                   disabled={loading || !captchaReady || (captchaRendered && !captchaVerified)}
                                   loading={loading} onClick={this.submit} content={t('global.submit')}/>
                </div>
            </React.Fragment>
        );
    }
}

const mapStateToProps = (state) => {
    return {
        account: state.account
    }
};

const withConnect = connect(
    mapStateToProps
);

FormUserDetails.propTypes = {
    email: PropTypes.string.isRequired,
    handleChange: PropTypes.func.isRequired,
    password: PropTypes.string.isRequired,
    repeatPassword: PropTypes.string.isRequired,
    loading: PropTypes.bool.isRequired,
    signUpAttempt: PropTypes.number.isRequired
};

export default compose(withTranslation(), withConnect)(FormUserDetails);
