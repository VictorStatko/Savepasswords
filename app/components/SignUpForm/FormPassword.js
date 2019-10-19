import PropTypes from 'prop-types'
import React, {Component} from 'react';
import TextInput from "components/default/inputs/TextInput";
import styles from "./SignUpForm.module.scss";
import PrimaryButton from "components/default/buttons/PrimaryButton";
import {withTranslation} from "react-i18next";

class FormPassword extends Component {

    handlePasswordChange = e => {
        const {handleChange} = this.props;

        this.setState({
            passwordError: ''
        });

        handleChange('password', e.target.value);
    };

    render() {
        const {t, password} = this.props;

        return (
            <React.Fragment>
                <TextInput id="password" label={t('signUp.passwordLabel')} className={styles.textInput} value={password}
                           onChange={this.handlePasswordChange}/>
                <div className={styles.buttonContainer}>
                    <PrimaryButton type="submit" text={t('global.submit')}/>
                </div>
            </React.Fragment>
        );
    }
}

FormPassword.propTypes = {
  handleChange: PropTypes.func.isRequired,
  password: PropTypes.string.isRequired
};

export default withTranslation()(FormPassword);
