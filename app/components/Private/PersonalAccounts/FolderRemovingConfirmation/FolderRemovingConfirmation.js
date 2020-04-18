import React from 'react';
import {withTranslation} from "react-i18next";
import {Modal} from "react-bootstrap";
import styles from "./FolderRemovingConfirmation.module.scss";
import Icon from "components/default/icons";
import {Button, DeclineButton, PrimaryButton} from "components/default/buttons/Button/Button";
import {toast} from "react-toastify";
import {FOLDER_REMOVAL_OPTIONS} from "utils/appConstants";
import Radio from "components/default/radio";
import history from "utils/history";

class FolderRemovingConfirmation extends React.Component {
    state = {
        loading: false,
        removalOption: FOLDER_REMOVAL_OPTIONS.FOLDER_ONLY
    };

    handleDeleteConfirm = async () => {
        const {t} = this.props;
        this.setState({loading: true});
        try {
            await this.props.delete(this.state.removalOption);
            toast.success(t('personalAccountFolders.removeSuccess'));
            history.push('/accounts');
        } catch (error) {
            this.setState({loading: false});
        }
    };

    handleRadioChange = e => {
        const {name, value} = e.target;

        this.setState({
            [name]: value
        });
    };


    renderRadio = () => {
        const {t} = this.props;

        return (
            <div>
                <div>{t('personalAccountsFolders.removeConfirmation.selectOption')}</div>
                <div className={styles.radioBox}>
                    <Radio value={FOLDER_REMOVAL_OPTIONS.FOLDER_ONLY}
                           label={t('personalAccountsFolders.removeConfirmation.option.folderOnly')}
                           checked={this.state.removalOption === FOLDER_REMOVAL_OPTIONS.FOLDER_ONLY}
                           name="removalOption"
                           onChange={this.handleRadioChange}
                    />
                    <Radio value={FOLDER_REMOVAL_OPTIONS.WITH_ACCOUNTS}
                           label={t('personalAccountsFolders.removeConfirmation.option.withAccounts')}
                           checked={this.state.removalOption === FOLDER_REMOVAL_OPTIONS.WITH_ACCOUNTS}
                           name="removalOption"
                           onChange={this.handleRadioChange}
                    />
                </div>
            </div>
        )
    };

    render() {
        const {t} = this.props;
        const {loading} = this.state;
        const {folder} = this.props;

        return (
            <Modal show centered size="lg" backdrop='static'>
                <Modal.Header>
                    <Modal.Title>
                        {t('personalAccountFolders.removeConfirmation.header')}
                    </Modal.Title>
                    <Button content={<Icon name='close' styles={styles.modalIcon}/>} onClick={this.props.close}/>
                </Modal.Header>
                <Modal.Body>
                    <div>
                        <div>{t('personalAccountFolders.removeConfirmation.question')}</div>
                        <div className={styles.warning}>{t('personalAccountsFolders.removeConfirmation.note')}</div>
                        {t('personalAccountsFolders.removeConfirmation.folderName')}
                        <div className={styles.removingInfo}> {folder.name}</div>
                        {folder.accountsCount > 0 ? this.renderRadio() : null}
                    </div>
                </Modal.Body>
                <Modal.Footer>
                    <PrimaryButton type="submit" disabled={loading} onClick={this.handleDeleteConfirm}
                                   content={t('global.submit')} loading={loading}/>
                    <DeclineButton content={t('global.cancel')} onClick={this.props.close}/>
                </Modal.Footer>
            </Modal>
        );
    }

}

export default withTranslation()(FolderRemovingConfirmation);