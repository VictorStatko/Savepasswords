import React from 'react';
import styles from './GuestPage.module.scss';
import logo from 'images/logo.png';
import {withTranslation} from 'react-i18next';
import SignUpForm from "components/Guest/SignUpForm";
import SignInForm from "components/Guest/SignInForm";

class GuestPage extends React.Component {

    state = {
        nameError: '',
        emailError: ''
    };

    render() {
        const {t, process} = this.props;

        return (
            <div className={styles.page}>
                <img src={logo} className={styles.logo} alt="logo"/>
                <div className={styles.formContainer}>
                    <h1 className={styles.title}>
                        {process === 'sign-up' ? t('signUp.title') : t('signIn.title')}
                    </h1>
                    {process === 'sign-up' ? <SignUpForm/> : <SignInForm/>}
                </div>
            </div>
        );
    }

}

export default withTranslation()(GuestPage);