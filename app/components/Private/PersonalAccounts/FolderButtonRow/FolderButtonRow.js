import React from 'react';
import {withTranslation} from "react-i18next";
import Icon from "components/default/icons";
import styles from "./FolderButtonRow.module.scss";
import {Button} from "components/default/buttons/Button/Button";
import FolderModal from "../FolderModal";
import * as PropTypes from "prop-types";

class FolderButtonRow extends React.Component {
    state = {
        showEditFolder: false
    };

    toggleEditFolder = () => {
        this.setState({showEditFolder: !this.state.showEditFolder});
    };

    render() {

        return (
            <React.Fragment>
                <Button customStyle={styles.button} content={<Icon name='edit' styles={styles.buttonIcon}/>}
                        onClick={this.toggleEditFolder}/>
                <Button customStyle={styles.button} content={<Icon name='delete' styles={styles.buttonIcon}/>}
                        onClick={this.toggleAddFolder}/>
                {this.state.showEditFolder ? <FolderModal close={this.toggleEditFolder} folder={this.props.folder}/> : null}
            </React.Fragment>
        );
    }

}

export default withTranslation()(FolderButtonRow);

FolderButtonRow.propTypes = {
    folder: PropTypes.object
};