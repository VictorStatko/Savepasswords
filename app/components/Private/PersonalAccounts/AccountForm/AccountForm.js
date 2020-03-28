import React from 'react';
import {withTranslation} from "react-i18next";
import TextInput from "components/default/inputs/TextInput";
import styles from "./AccountForm.module.scss";
import {Col, Row} from "react-bootstrap";
import TextArea from "components/default/inputs/TextArea";
import * as PropTypes from "prop-types";

class AccountForm extends React.Component {

    render() {
        const {t} = this.props;
        const {account, urlError, nameError, serverError, handleChange, disabled} = this.props;
        return (
            <React.Fragment>
                <TextInput id="url" label={t('personalAccounts.modal.form.url')} error={urlError} readOnly={disabled}
                           value={account.url} onChange={e => handleChange('url', e.target.value)}/>
                <TextInput id="name" label={t('personalAccounts.modal.form.name')} readOnly={disabled}
                           value={account.name} className={styles.textInput} error={nameError}
                           onChange={e => handleChange('name', e.target.value)}/>
                <Row>
                    <Col md={6}>
                        <TextInput id="username" label={t('personalAccounts.modal.form.username')} readOnly={disabled}
                                   value={account.username} className={styles.textInput}
                                   onChange={e => handleChange('username', e.target.value)}/>
                    </Col>
                    <Col md={6}>
                        <TextInput id="password" secret label={t('personalAccounts.modal.form.password')} readOnly={disabled}
                                   value={account.password} className={styles.textInput}
                                   onChange={e => handleChange('password', e.target.value)}/>
                    </Col>
                </Row>
                <TextArea id="description" label={t('personalAccounts.modal.form.description')} readOnly={disabled}
                          value={account.description} className={styles.textInput} textarea
                          onChange={e => handleChange('description', e.target.value)}/>

                <div className={styles.serverError}>{serverError}</div>
            </React.Fragment>
        );
    }

}

export default withTranslation()(AccountForm);

AccountForm.propTypes = {
    handleChange: PropTypes.func.isRequired,
    account: PropTypes.object.isRequired,
    urlError: PropTypes.string,
    nameError: PropTypes.string,
    serverError: PropTypes.string,
    disabled: PropTypes.bool
};