import React from 'react';
import {withTranslation} from "react-i18next";
import {Modal} from "react-bootstrap";
import Icon from "components/default/icons";
import styles from "./AccountModal.module.scss";
import AccountForm from "../AccountForm";
import {Button, DeclineButton, PrimaryButton} from "components/default/buttons/Button/Button";
import {setStateAsync} from "utils/stateUtils";
import {connect} from "react-redux";
import {personalAccountsOperations} from "ducks/personalAccounts";
import {compose} from "redux";
import {isEmpty, isNotEmpty} from "utils/stringUtils";
import {isStringMaxLengthValid} from "utils/validationUtils";
import {toast} from "react-toastify";
import {IndexedDBService} from "indexedDB";
import {rsaDecrypt} from "utils/encryptionUtils";
import {personalAccountFoldersOperations} from "ducks/personalAccountFolders";
import history from "utils/history";
import {isObjectModified} from "utils/objectUtils";

const MAX_LENGTH_URL = 2047;
const MAX_LENGTH_NAME = 254;
const MAX_LENGTH_FOLDER_NAME = 255;

const indexedDBService = IndexedDBService.getService();

class AccountModal extends React.Component {
    state = {
        loading: false,
        newAccount: !this.props.account,
        infoAccount: !!this.props.account,
        editAccount: false,
        account: {}
    };

    componentDidMount = async () => {
        let account = {...this.props.account};

        if (this.props.account) {
            const privateKey = await indexedDBService.loadPrivateKey();
            if (isNotEmpty(account.username)) {
                account.username = await rsaDecrypt(privateKey, account.username);
            }
            if (isNotEmpty(account.password)) {
                account.password = await rsaDecrypt(privateKey, account.password);
            }
        } else {
            account = {};
        }

        this.setState({account: account});
    };

    handleFolderCreate = async (folderName) => {
        const {t} = this.props;

        let valid = true;

        if (isEmpty(folderName)) {
            this.setState({
                folderUuidError: t('global.validation.notEmpty')
            });
            valid = false;
        } else if (!isStringMaxLengthValid(folderName, MAX_LENGTH_FOLDER_NAME)) {
            this.setState({
                folderUuidError: t('global.validation.maxLength', {maxLength: MAX_LENGTH_FOLDER_NAME})
            });
            valid = false;
        }

        if (!valid) {
            throw new Error('Validation failed');
        }

        try {
            return await this.props.createFolder({name: folderName}, false);
        } catch (error) {
            this.setState({
                account: {
                    ...this.state.account, ...{folderUuid: null}
                }
            });
            throw error;
        }
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

        if (this.state.newAccount) {
            await this.onCreate();
        } else if (this.state.editAccount) {
            await this.onUpdate();
        }
    };

    onCreate = async () => {
        const {t} = this.props;
        const {account} = this.state;

        await setStateAsync(this, {loading: true});

        try {
            await this.props.createPersonalAccount({...account});
            toast.success(t('personalAccounts.creationSuccess'));
            this.props.close();
            let url;
            if (account.folderUuid) {
                url = `/accounts?folderUuid=${account.folderUuid}`;
            } else {
                url = `/accounts`;
            }
            history.push(url);
        } catch (error) {
            await setStateAsync(this, {loading: false});

            if (error && error.message && error.message === 'showOnForm') {
                this.setState({
                    serverError: error.messageTranslation
                });
            } else {
                console.error(error);
            }
        }
    };

    onUpdate = async () => {
        const {t} = this.props;
        const {account} = this.state;

        if (!isObjectModified(account, this.props.account)) {
            toast.success(t('personalAccounts.updateSuccess'));
            this.props.close();
        }

        await setStateAsync(this, {loading: true});

        try {
            await this.props.updatePersonalAccount({...account}, this.props.account);
            toast.success(t('personalAccounts.updateSuccess'));
            this.props.close();
            let url;
            if (account.folderUuid) {
                url = `/accounts?folderUuid=${account.folderUuid}`;
            } else {
                url = `/accounts`;
            }
            history.push(url);
        } catch (error) {
            await setStateAsync(this, {loading: false});

            if (error && error.message && error.message === 'showOnForm') {
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
        return validUrlName && validUrl && validName && isEmpty(this.state.folderUuidError);
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

    renderTitle = () => {
        const {t} = this.props;

        if (this.state.newAccount) {
            return t('personalAccounts.modal.header.new');
        } else if (this.state.editAccount) {
            return t('personalAccounts.modal.header.edit');
        } else {
            return t('personalAccounts.modal.header.info');
        }
    };

    onEditButtonClick = () => {
        this.setState({
            infoAccount: false,
            editAccount: true,
        });
    };

    render() {
        const {t, close} = this.props;
        const {account, urlError, nameError, serverError, folderUuidError, infoAccount, newAccount, editAccount, loading} = this.state;

        return (
            <Modal show centered size="lg" backdrop='static'>
                <form onSubmit={this.onSubmit}>
                    <Modal.Header>
                        <Modal.Title>
                            {this.renderTitle()}
                        </Modal.Title>
                        <Button content={<Icon name='close' styles={styles.modalIcon}/>} onClick={close}/>
                    </Modal.Header>
                    <Modal.Body>
                        <div>
                            <AccountForm account={account} urlError={urlError}
                                         nameError={nameError} serverError={serverError}
                                         folderError={folderUuidError}
                                         handleFolderCreate={this.handleFolderCreate}
                                         handleChange={this.handleChange} disabled={infoAccount}/>
                        </div>
                    </Modal.Body>
                    <Modal.Footer>
                        {newAccount || editAccount ?
                            <PrimaryButton type="submit" disabled={loading} content={t('global.save')}
                                           loading={loading}/>
                            : null}
                        {infoAccount ?
                            <PrimaryButton content={t('global.edit')} onClick={this.onEditButtonClick}/> : null}
                        <DeclineButton content={infoAccount ? t('global.close') : t('global.cancel')} onClick={close}/>
                    </Modal.Footer>
                </form>
            </Modal>
        );
    }

}

const withConnect = connect(null, {
    createPersonalAccount: personalAccountsOperations.createPersonalAccount,
    updatePersonalAccount: personalAccountsOperations.updatePersonalAccount,
    createFolder: personalAccountFoldersOperations.createFolder
});

export default compose(withTranslation(), withConnect)(AccountModal);
