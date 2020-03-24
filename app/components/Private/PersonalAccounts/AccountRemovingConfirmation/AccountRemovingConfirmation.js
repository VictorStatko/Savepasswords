import React from 'react';
import {withTranslation} from "react-i18next";
import {Modal} from "react-bootstrap";
import styles from "./AccountRemovingConfirmation.module.scss";
import Icon from "components/default/icons";
import {Button, ConfirmButton, DeclineButton} from "components/default/buttons/Button/Button";
import {isEmpty} from "utils/stringUtils";

class AccountRemovingConfirmation extends React.Component {

    render() {
        const {t} = this.props;
        const {url, name} = this.props;

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
                    <ConfirmButton type="submit" disabled={false} content={t('global.submit')}
                                   loading={false}/>
                    <DeclineButton content={t('global.close')} onClick={this.props.close}/>
                </Modal.Footer>
            </Modal>
        );
    }

}

export default withTranslation()(AccountRemovingConfirmation);
