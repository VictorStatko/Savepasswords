import React from 'react';
import styles from './GuestPage.module.scss';
import logo from 'images/logo.png';
import {withTranslation} from 'react-i18next';
import SignUpForm from "components/Guest/SignUpForm";
import SignInForm from "components/Guest/SignInForm";
import {Col, Row} from "react-bootstrap";

class GuestPage extends React.Component {

    render() {
        const {t, process} = this.props;
        return (
            <div className={styles.page}>
                <div className={styles.formContainer}>
                    <Row className={styles.row}>
                        <Col lg={5} className={styles.leftColumn}>
                            <h1>{t('global.appName')}</h1>
                            <hr/>
                            <img src={logo} className={styles.logo} alt="logo"/>
                        </Col>
                        <Col lg={7}>
                            <h1 className={styles.title}>
                                {process === 'sign-up' ? t('signUp.title') : t('signIn.title')}
                            </h1>
                            {process === 'sign-up' ? <SignUpForm/> : <SignInForm/>}</Col>
                    </Row>
                </div>
            </div>
        );
    }

}

export default withTranslation()(GuestPage);