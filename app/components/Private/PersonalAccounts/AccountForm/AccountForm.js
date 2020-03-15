import React from 'react';
import {withTranslation} from "react-i18next";
import TextInput from "components/default/inputs/TextInput";
import styles from "./AccountForm.module.scss";
import {Col, Row} from "react-bootstrap";
import TextArea from "components/default/inputs/TextArea";

class AccountForm extends React.Component {
    render() {
        const {t} = this.props;

        return (
            <form>
                <TextInput id="email" label={t('personalAccounts.modal.form.url')} value=''/>
                <TextInput id="name" label={t('personalAccounts.modal.form.name')} value='' className={styles.textInput}/>
                <Row>
                <Col md={6}>
                    <TextInput id="name" label={t('personalAccounts.modal.form.username')} value='' className={styles.textInput}/>
                </Col>
                <Col md={6}>
                    <TextInput id="name" label={t('personalAccounts.modal.form.password')} value='' className={styles.textInput}/>
                </Col>
                </Row>
                <TextArea id="name" label={t('personalAccounts.modal.form.description')} value='' className={styles.textInput} textarea/>
            </form>
        );
    }

}

export default withTranslation()(AccountForm);