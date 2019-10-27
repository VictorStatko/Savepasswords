import * as PropTypes from 'prop-types'
import React, {Component} from 'react';
import TextInput from "components/default/inputs/TextInput";
import styles from "./SignUpForm.module.scss";
import PrimaryButton from "components/default/buttons/PrimaryButton";
import {withTranslation} from "react-i18next";
import {isEmpty} from "utils/stringUtils";
import {isEmailValid, isStringMaxLengthValid} from "utils/validationUtils";
import {compose} from "redux";
import {connect} from "react-redux";
import {checkAccountAlreadyExists} from "ducks/account/actions";
import {MAX_LENGTH_EMAIL} from "utils/validationUtils";
import {MAX_LENGTH_USERNAME} from "utils/validationUtils";

class FormUserDetails extends Component {
    state = {
        nameError: '',
        emailError: ''
    };

    continue = async e => {
        e.preventDefault();
        const {t} = this.props;

        const clientValidation = this.validate();

        if (clientValidation) {
            const accountExists = await this.props.checkAccountAlreadyExists({email: this.props.email});
            if (accountExists.value) {
                this.setState({
                    emailError: t('signUp.validation.emailAlreadyExists')
                });
            } else {
                this.props.nextStep();
            }
        }
    };

    validate = () => {
        const validName = this.validateName();
        const validEmail = this.validateEmail();

        return validName && validEmail;
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

    validateName = () => {
        const {t, name} = this.props;
        let valid = true;

        if (isEmpty(name)) {
            this.setState({
                nameError: t('global.validation.notEmpty')
            });
            valid = false;
        } else if (!isStringMaxLengthValid(name, MAX_LENGTH_USERNAME)) {
            this.setState({
                nameError: t('global.validation.maxLength', {maxLength: MAX_LENGTH_USERNAME})
            });
            valid = false;
        }

        return valid;
    };

    handleNameChange = e => {
        this.setState({
            nameError: ''
        });

        this.props.handleChange('name', e.target.value);
    };

    handleEmailChange = e => {
        this.setState({
            emailError: ''
        });

        this.props.handleChange('email', e.target.value);
    };


    render() {
        const {t, name, email} = this.props;
        const {nameError, emailError} = this.state;

        return (
            <React.Fragment>
                <TextInput id="name" label={t('signUp.nameLabel')} className={styles.textInput} value={name}
                           onChange={this.handleNameChange} error={nameError}/>
                <TextInput id="email" label={t('signUp.emailLabel')} className={styles.textInput} value={email}
                           onChange={this.handleEmailChange} error={emailError}/>
                <div className={styles.buttonContainer}>
                    <PrimaryButton onClick={this.continue} text={t('global.next')}/>
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
    mapStateToProps,
    {
        checkAccountAlreadyExists
    }
);

FormUserDetails.propTypes = {
    email: PropTypes.string.isRequired,
    handleChange: PropTypes.func.isRequired,
    name: PropTypes.string.isRequired,
    nextStep: PropTypes.func.isRequired
};

export default compose(withTranslation(), withConnect)(FormUserDetails);
