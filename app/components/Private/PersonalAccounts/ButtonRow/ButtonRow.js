import React from 'react';
import {withTranslation} from "react-i18next";
import Icon from "components/default/icons";
import styles from "./ButtonRow.module.scss";
import AccountModal from "../AccountModal";
import {Button} from "components/default/buttons/Button/Button";
import FolderModal from "../FolderModal";

class ButtonRow extends React.Component {
    state = {
        showAddAccount: false,
        showAddFolder: false
    };

    toggleAddAccount = () => {
        this.setState({showAddAccount: !this.state.showAddAccount});
    };

    toggleAddFolder = () => {
        this.setState({showAddFolder: !this.state.showAddFolder});
    };

    render() {

        return (
            <React.Fragment>
                <Button content={<Icon name='add' styles={styles.buttonIcon}/>}
                        customStyle={styles.button} onClick={this.toggleAddAccount}/>
                <Button content={<Icon name='newFolder' styles={styles.buttonIcon}/>}
                        customStyle={styles.button} onClick={this.toggleAddFolder}/>
                {this.state.showAddAccount ? <AccountModal close={this.toggleAddAccount}/> : null}
                {this.state.showAddFolder ? <FolderModal close={this.toggleAddFolder}/> : null}
            </React.Fragment>
        );
    }

}

export default withTranslation()(ButtonRow);