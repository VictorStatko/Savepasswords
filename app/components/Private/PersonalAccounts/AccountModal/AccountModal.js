import React from 'react';
import {withTranslation} from "react-i18next";
import {Modal} from "react-bootstrap";
import Icon from "components/default/icons";
import styles from "./AccountModal.module.scss";
import AccountForm from "../AccountForm";
import {Button, ConfirmButton, DeclineButton} from "components/default/buttons/Button/Button";
import {setStateAsync} from "utils/stateUtils";
import {connect} from "react-redux";
import {personalAccountsOperations} from "ducks/personalAccounts";
import {compose} from "redux";
import {isEmpty} from "utils/stringUtils";
import {isStringMaxLengthValid} from "utils/validationUtils";
import {toast} from "react-toastify";

const MAX_LENGTH_URL = 2047;
const MAX_LENGTH_NAME = 254;

class AccountModal extends React.Component {
    state = {
        account: {},
        loading: false
    };

    handleChange = (input, value) => {
        if (input === 'url' || input === 'name') {
            this.setState({
                urlError: null,
                nameError: null
            });
        }

        this.setState({
            account: {
                ...this.state.account, [input]: value
            },
            [input + 'Error']: null,
            serverError: null
        });
    };

    onSubmit = async (e) => {
        e.preventDefault();

        if (!this.validate()) {
            return;
        }

        const {t} = this.props;

        await setStateAsync(this, {loading: true});

        try {
            await this.props.createPersonalAccount(this.state.account);
            toast.success(t('personalAccounts.creationSuccess'));
            this.props.close();
        } catch (error) {
            await setStateAsync(this, {loading: false});

            if (error.message && error.message === 'showOnForm') {
                this.setState({
                    serverError: error.messageTranslation
                });
            } else {
                console.error(error);
            }
        }
    };

    validate = () => {
        const validUrlName = this.validateUrlName();
        const validUrl = this.validateUrl();
        const validName = this.validateName();
        return validUrlName && validUrl && validName;
    };

    validateUrl = () => {
        const {t} = this.props;
        const {url} = this.state.account;

        let valid = true;

        if (!isEmpty(url) && !isStringMaxLengthValid(url, MAX_LENGTH_URL)) {
            this.setState({
                urlError: t('global.validation.maxLength', {maxLength: MAX_LENGTH_URL})
            });
            valid = false;
        }

        return valid;
    };

    validateName = () => {
        const {t} = this.props;
        const {name} = this.state.account;

        let valid = true;

        if (!isEmpty(name) && !isStringMaxLengthValid(name, MAX_LENGTH_NAME)) {
            this.setState({
                nameError: t('global.validation.maxLength', {maxLength: MAX_LENGTH_NAME})
            });
            valid = false;
        }

        return valid;
    };

    validateUrlName = () => {
        const {t} = this.props;
        const {url, name} = this.state.account;

        let valid = true;

        if (isEmpty(url) && isEmpty(name)) {
            this.setState({
                urlError: t('personalAccounts.modal.form.validation.nameUrl'),
                nameError: t('personalAccounts.modal.form.validation.nameUrl'),
            });

            valid = false;
        }

        return valid;
    };

    render() {
        const {newAccount, t} = this.props;

        return (
            <Modal show centered size="lg" animation={false} backdrop='static'>
                <form onSubmit={this.onSubmit}>
                    <Modal.Header>
                        <Modal.Title className={styles.modalTitle}>
                            {newAccount ? t('personalAccounts.modal.header.new') : t('personalAccounts.modal.header.edit')}
                        </Modal.Title>
                        <Button content={<Icon name='close' styles={styles.closeIcon}/>} onClick={this.props.close}/>
                    </Modal.Header>
                    <Modal.Body>
                        <div className={styles.modalBody}>
                            <AccountForm account={this.state.account} urlError={this.state.urlError}
                                         nameError={this.state.nameError} serverError={this.state.serverError}
                                         handleChange={this.handleChange}/>
                        </div>
                    </Modal.Body>
                    <Modal.Footer>
                        <ConfirmButton type="submit" disabled={this.state.loading} content={t('global.submit')} loading={this.state.loading}/>
                        <DeclineButton content={t('global.close')} onClick={this.props.close}/>
                    </Modal.Footer>
                </form>
            </Modal>
        );
    }

}

const withConnect = connect(null, {
    createPersonalAccount: personalAccountsOperations.createPersonalAccount
});

export default compose(withTranslation(), withConnect)(AccountModal);
