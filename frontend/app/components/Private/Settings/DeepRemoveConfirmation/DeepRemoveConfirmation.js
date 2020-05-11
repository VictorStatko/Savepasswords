import React from 'react';
import {withTranslation} from "react-i18next";
import {Modal} from "react-bootstrap";
import styles from "./DeepRemoveConfirmation.module.scss";
import Icon from "components/default/icons";
import {Button, DeclineButton, PrimaryButton} from "components/default/buttons/Button/Button";
import {setStateAsync} from "utils/stateUtils";
import {toast} from "react-toastify";

class DeepRemoveConfirmation extends React.Component {
    state = {
        loading: false
    };

    handleDeleteConfirm = async () => {
        const {t} = this.props;

        await setStateAsync(this, {loading: true});
        try {
            await this.props.delete();
            toast.success(t('settings.account.deepRemove.info'));
        } catch (error) {
            await setStateAsync(this, {loading: false});
        }
    };

    render() {
        const {t} = this.props;
        const {loading} = this.state;

        return (
            <Modal show centered size="lg" backdrop='static'>
                <Modal.Header>
                    <Modal.Title>
                        {t('settings.account.deepRemoveConfirmation.header')}
                    </Modal.Title>
                    <Button content={<Icon name='close' styles={styles.modalIcon}/>} onClick={this.props.close}/>
                </Modal.Header>
                <Modal.Body>
                    <div>
                        {t('settings.account.deepRemoveConfirmation.question')}
                        <br/>
                        {t('settings.account.deepRemoveConfirmation.info')}
                    </div>
                </Modal.Body>
                <Modal.Footer>
                    <PrimaryButton type="submit" disabled={loading} customStyle={styles.submitButton} onClick={this.handleDeleteConfirm}
                                   content={t('global.submit')} loading={loading}/>
                    <DeclineButton content={t('global.cancel')} onClick={this.props.close}/>
                </Modal.Footer>
            </Modal>
        );
    }

}

export default withTranslation()(DeepRemoveConfirmation);

