import React, {Component} from 'react';
import {Link} from "react-router-dom";
import {withTranslation} from "react-i18next";
import TextInput from "components/default/inputs/TextInput";
import styles from "./SignInForm.module.scss";
import PrimaryButton from "../../default/buttons/PrimaryButton";

class SignInForm extends Component {
    state = {
        email: '',
        password: ''
    };

    handleChange = (input, value) => {
        this.setState({[input]: value});
    };

    render() {
        const {t} = this.props;
        const {email, password} = this.state;

        return (
            <div>
                <TextInput id="email" label={t('signIn.emailLabel')} className={styles.textInput} value={email}
                           onChange={e => this.handleChange('email', e.target.value)}/>
                <TextInput id="password" label={t('signIn.passwordLabel')} className={styles.textInput} value={password}
                           onChange={e => this.handleChange('password', e.target.value)}/>
                <div className={styles.buttonContainer}>
                    <PrimaryButton onClick={this.continue} text={t('global.submit')}/>
                </div>
                <div className={styles.changePageLink}>
                    <Link to={'/sign-up'}>{t('signIn.notRegisteredLink')}</Link>
                </div>
            </div>
        );
    }
}


export default withTranslation()(SignInForm);
