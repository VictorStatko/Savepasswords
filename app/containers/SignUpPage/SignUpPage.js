import React from 'react';
import styles from './SignUpPage.module.scss';
import logo from 'images/logo.png';
import {withTranslation} from 'react-i18next';
import SignUpForm from "components/SignUpForm";

class SignUpPage extends React.Component {

    render() {
        const {t} = this.props;

        return (
            <div className={styles.page}>
                <img src={logo} className={styles.logo} alt="logo"/>
                <div className={styles.formContainer}>
                    <h1 className={styles.title}> {t('signUp.title')}</h1>
                    <SignUpForm/>
                </div>
            </div>
        );
    }

}

export default withTranslation()(SignUpPage);