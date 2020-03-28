import React from 'react';
import {withTranslation} from "react-i18next";
import {Modal} from "react-bootstrap";
import styles from "./AccountRemovingConfirmation.module.scss";
import Icon from "components/default/icons";
import {Button, DeclineButton, PrimaryButton} from "components/default/buttons/Button/Button";
import {isEmpty} from "utils/stringUtils";
import {connect} from "react-redux";
import {personalAccountsOperations} from "ducks/personalAccounts";
import {compose} from "redux";
import {setStateAsync} from "utils/stateUtils";
import {toast} from "react-toastify";

class AccountRemovingConfirmation extends React.Component {
    state = {
        loading: false
    };

    handleDeleteConfirm = async () => {
        const {t} = this.props;

        await setStateAsync(this, {loading: true});
        try {
            await this.props.delete();
            toast.success(t('personalAccounts.removeSuccess'));
        } catch (error) {
            await setStateAsync(this, {loading: false});
        }
    };

    render() {
        const {t} = this.props;
        const {url, name} = this.props;
        const {loading} = this.state;

        return (
            <Modal show centered size="lg" backdrop='static'>
                <Modal.Header>
                    <Modal.Title>
                        {t('personalAccounts.removeConfirmation.header')}
                    </Modal.Title>
                    <Button content={<Icon name='close' styles={styles.modalIcon}/>} onClick={this.props.close}/>
                </Modal.Header>
                <Modal.Body>
                    <div>
                        <div>{t('personalAccounts.removeConfirmation.question')}</div>
                        <div className={styles.warning}>{t('personalAccounts.removeConfirmation.note')}</div>
                        {isEmpty(name) ? null :
                            <React.Fragment>
                                {t('personalAccounts.removeConfirmation.accountName')}
                                <span className={styles.removingInfo}> {name}</span>
                                <br/>
                            </React.Fragment>
                        }
                        {isEmpty(url) ? null :
                            <React.Fragment>
                                {t('personalAccounts.removeConfirmation.accountUrl')}
                                <span className={styles.removingInfo}> {url}</span>
                                <br/>
                            </React.Fragment>
                        }
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

const withConnect = connect(null, {
    removePersonalAccount: personalAccountsOperations.removePersonalAccount
});

export default compose(withTranslation(), withConnect)(AccountRemovingConfirmation);

