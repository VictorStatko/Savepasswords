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
    MAX_LENGTH_PASSWORD, MIN_LENGTH_PASSWORD
} from "utils/validationUtils";
import {compose} from "redux";
import {connect} from "react-redux";
import {PrimaryButton} from "components/default/buttons/Button/Button";

class FormUserDetails extends Component {
    state = {
        passwordError: '',
        emailError: ''
    };

    submit = async e => {
        const {t} = this.props;

        const clientValidation = this.validate();

        if (!clientValidation) {
            e.preventDefault();
        }
    };

    validate = () => {
        const validPassword = this.validatePassword();
        const validEmail = this.validateEmail();

        return validPassword && validEmail;
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
            passwordError: ''
        });

        this.props.handleChange('password', e.target.value);
    };


    render() {
        const {t, password, email, loading, serverError} = this.props;
        const {passwordError, emailError} = this.state;

        return (
            <React.Fragment>
                <TextInput id="email" label={t('signUp.emailLabel')} className={styles.textInput} value={email}
                           onChange={this.handleEmailChange} error={emailError}/>
                <TextInput id="password" secret label={t('signUp.passwordLabel')} className={styles.textInput}
                           value={password}
                           onChange={this.handlePasswordChange} error={passwordError}/>
                {isEmpty(serverError) ? null : <div className={styles.serverError}>{serverError}</div>}
                <div className={styles.buttonContainer}>
                    <PrimaryButton type="submit" disabled={loading} loading={loading} onClick={this.submit}
                                   content={t('global.submit')}/>
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
    loading: PropTypes.bool.isRequired
};

export default compose(withTranslation(), withConnect)(FormUserDetails);
