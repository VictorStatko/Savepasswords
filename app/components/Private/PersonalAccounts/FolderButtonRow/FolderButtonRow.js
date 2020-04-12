import React from 'react';
import {withTranslation} from "react-i18next";
import Icon from "components/default/icons";
import styles from "./FolderButtonRow.module.scss";
import {Button} from "components/default/buttons/Button/Button";

class FolderButtonRow extends React.Component {

    render() {

        return (
            <React.Fragment>
                <Button customStyle={styles.button} content={<Icon name='edit' styles={styles.buttonIcon}/>}
                        onClick={this.toggleAddAccount}/>
                <Button customStyle={styles.button} content={<Icon name='delete' styles={styles.buttonIcon}/>}
                        onClick={this.toggleAddFolder}/>
            </React.Fragment>
        );
    }

}

export default withTranslation()(FolderButtonRow);