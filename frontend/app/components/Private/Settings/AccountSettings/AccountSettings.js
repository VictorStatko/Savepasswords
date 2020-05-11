import React from 'react';
import {connect} from "react-redux";
import {compose} from "redux";
import {withTranslation} from "react-i18next";
import {Button} from "components/default/buttons/Button/Button";
import styles from "./AccountSettings.module.scss";
import DeepRemoveConfirmation from "../DeepRemoveConfirmation";
import {settingsOperations} from "ducks/settings";

class AccountSettings extends React.Component {

    state = {
        removeModal: false
    };

    handleDeleteModalToggle = () => {
        this.setState({removeModal: !this.state.removeModal});
    };

    handleDelete = async () => {
        await this.props.removeAccount();
    };

    render() {
        const {t} = this.props;
        const {removeModal} = this.state;

        return (
            <div className={styles.container}>
                <Button customStyle={styles.removeButton} content={t('settings.account.deepRemove')}
                        onClick={this.handleDeleteModalToggle}/>
                {removeModal ?
                    <DeepRemoveConfirmation close={this.handleDeleteModalToggle} delete={this.handleDelete}/>
                    : null
                }
            </div>
        );
    }

}

const withConnect = connect(null, {
    removeAccount: settingsOperations.removeAccount
});

export default compose(withTranslation(), withConnect)(AccountSettings);