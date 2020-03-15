import React from 'react';
import {withTranslation} from "react-i18next";
import {Button, Modal} from "react-bootstrap";
import Icon from "components/default/icons";
import styles from "./AccountModal.module.scss";
import SecondaryButton from "components/default/buttons/SecondaryButton";
import AccountForm from "../AccountForm";

class AccountModal extends React.Component {

    render() {
        const {t} = this.props;

        return (
            <Modal show={this.props.show} onHide={this.props.onHide} centered size="lg" animation={false} backdrop='static'>
                <Modal.Header>
                    <Modal.Title className={styles.modalTitle}>
                        {this.props.new ? t('personalAccounts.modal.header.new') : t('personalAccounts.modal.header.edit')}
                    </Modal.Title>
                    <SecondaryButton content={<Icon name='close' styles={styles.closeIcon}/>} onClick={this.props.onHide}/>
                </Modal.Header>
                <Modal.Body>
                    <div className={styles.modalBody}>
                    <AccountForm/>
                    </div>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={this.props.onHide}>
                        Close
                    </Button>
                    <Button variant="primary" onClick={this.props.onHide}>
                        Save Changes
                    </Button>
                </Modal.Footer>
            </Modal>
        );
    }

}

export default withTranslation()(AccountModal);