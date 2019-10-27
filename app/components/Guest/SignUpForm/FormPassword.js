import PropTypes from 'prop-types'
import React, {Component} from 'react';
import TextInput from "components/default/inputs/TextInput";
import styles from "./SignUpForm.module.scss";
import PrimaryButton from "components/default/buttons/PrimaryButton";
import {withTranslation} from "react-i18next";
import {isEmpty} from "utils/stringUtils";
import {isStringMaxLengthValid} from "utils/validationUtils";
import {MAX_LENGTH_PASSWORD} from "utils/validationUtils";

class FormPassword extends Component {
    state = {
        passwordError: ''
    };

    handlePasswordChange = e => {

        this.setState({
            passwordError: ''
        });

        this.props.handleChange('password', e.target.value);
    };

    submit = e => {
        if (!this.validatePassword()) {
            e.preventDefault();
        }
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
        }

        return valid;
    };

    render() {
        const {t, password} = this.props;
        const {passwordError} = this.state;
        return (
            <React.Fragment>
                <TextInput id="password" label={t('signUp.passwordLabel')} className={styles.textInput} value={password}
                           onChange={this.handlePasswordChange} error={passwordError}/>
                <div className={styles.buttonContainer}>
                    <PrimaryButton onClick={this.props.previousStep} text={t('global.back')}/>
                    <PrimaryButton type="submit" onClick={this.submit} text={t('global.submit')}/>
                </div>
            </React.Fragment>
        );
    }
}

FormPassword.propTypes = {
    handleChange: PropTypes.func.isRequired,
    password: PropTypes.string.isRequired,
    previousStep: PropTypes.func.isRequired
};

export default withTranslation()(FormPassword);
