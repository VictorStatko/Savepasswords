import React from 'react';
import {withTranslation} from "react-i18next";
import {Col, Modal, Row} from "react-bootstrap";
import {Button, DeclineButton} from "components/default/buttons/Button/Button";
import Icon from "components/default/icons";
import styles from "./AccountSharingModal.module.scss";
import {connect} from "react-redux";
import {compose} from "redux";
import * as PropTypes from "prop-types";
import TextInput from "components/default/inputs/TextInput";
import {isEmpty, isNotEmpty} from "utils/stringUtils";
import {isEmailValid, isStringMaxLengthValid, MAX_LENGTH_EMAIL} from "utils/validationUtils";
import {personalAccountsOperations} from "ducks/personalAccounts";
import {rsaDecrypt} from "utils/encryptionUtils";
import {IndexedDBService} from "indexedDB";
import {toast} from "react-toastify";
import {Spinner} from "components/default/spinner/Spinner";
import AlreadySharedAccount from "./AlreadySharedAccount";

const indexedDBService = IndexedDBService.getService();

class AccountSharingModal extends React.Component {
    state = {
        addLoading: false,
        email: "",
        emailError: "",
        globalError: "",
    };

    componentDidMount = async () => {
        let account = {...this.props.account};
        const privateKey = await indexedDBService.loadPrivateKey();
        if (isNotEmpty(account.username)) {
            account.username = await rsaDecrypt(privateKey, account.username);
        }
        if (isNotEmpty(account.password)) {
            account.password = await rsaDecrypt(privateKey, account.password);
        }

        this.setState({account: account});
    };

    handleEmailChange = e => {
        this.setState({
            emailError: "",
            globalError: "",
            email: e.target.value
        });
    };

    validateEmail = () => {
        const {t} = this.props;
        const {email} = this.state;

        let valid = true;

        if (isEmpty(email)) {
            this.setState({
                emailError: t('global.validation.notEmpty')
            });
            valid = false;
        } else if (!isEmailValid(email)) {
            this.setState({
                emailError: t('global.validation.email')
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

    onAddButtonClick = async () => {
        if (!this.validateEmail()) {
            return;
        }

        const {t} = this.props;

        if (!this.state.account) {
            this.setState({
                globalError: t('exceptions.clientEncryptionError')
            });
            return;
        }

        try {
            this.setState({addLoading: true});
            await this.props.shareAccount({...this.state.account}, this.state.email);
            this.setState({
                email: ""
            });
            toast.success(t('personalAccounts.sharingSuccess'));
        } catch (error) {
            if (error && error.message && error.message === 'showOnForm') {
                this.setState({
                    globalError: error.messageTranslation
                });
            } else {
                console.error(error);
            }
        } finally {
            this.setState({addLoading: false});
        }
    }

    render() {
        const {t, close, account} = this.props;
        const {addLoading, email, emailError, globalError} = this.state;

        const listItems = account.sharedAccounts && account.sharedAccounts.length > 0 ? account.sharedAccounts.map((sharedAccount, index) =>
            <AlreadySharedAccount sharedAccount={sharedAccount} parentAccount={account}
                                  key={sharedAccount.uuid} index={index}/>
        ) : null;

        return (
            <Modal show centered backdrop='static'>
                <form onSubmit={this.onSubmit}>
                    <Modal.Header>
                        <Modal.Title>
                            {t('personalAccounts.sharingModal.header')}
                        </Modal.Title>
                        <Button content={<Icon name='close' styles={styles.modalIcon}/>} onClick={close}/>
                    </Modal.Header>
                    <Modal.Body className={styles.modalBody}>
                        <div className={styles.accountDataContainer}>
                            {isEmpty(account.name) ? null :
                                <React.Fragment>
                                    <span> {t('personalAccounts.sharingModal.accountName')} </span><span
                                    className={styles.accountData}>{account.name}</span><br/>
                                </React.Fragment>
                            }
                            {isEmpty(account.url) ? null :
                                <React.Fragment>
                                    <span>{t('personalAccounts.sharingModal.accountUrl')} </span><span
                                    className={styles.accountData}>{account.url}</span><br/>
                                </React.Fragment>
                            }
                        </div>
                        <Row>
                            <Col xs={9} lg={10} className={styles.leftColumn}>
                                <TextInput id="email" placeholder={t('personalAccounts.sharingModal.emailPlaceholder')}
                                           error={!isEmpty(globalError) ? globalError : emailError} value={email}
                                           onChange={this.handleEmailChange}/>
                                <input type="text" style={{display: 'none'}}/>
                            </Col>
                            <Col xs={3} lg={2} className={`d-flex justify-content-end ${styles.rightColumn}`}>
                                <Button content={addLoading ? <Spinner className={styles.spinner}/> :
                                    <Icon name='add' styles={styles.buttonIcon}/>}
                                        customStyle={styles.addButton} onClick={this.onAddButtonClick}/>
                            </Col>
                        </Row>
                        {listItems ? <hr/> : null}
                        {listItems}
                    </Modal.Body>
                    <Modal.Footer>
                        <DeclineButton content={t('global.cancel')} onClick={close}/>
                    </Modal.Footer>
                </form>
            </Modal>
        );
    }

}

const withConnect = connect(null, {
    shareAccount: personalAccountsOperations.sharePersonalAccount
});

export default compose(withTranslation(), withConnect)(AccountSharingModal);

AccountSharingModal.propTypes = {
    close: PropTypes.func.isRequired,
    account: PropTypes.object.isRequired,
};