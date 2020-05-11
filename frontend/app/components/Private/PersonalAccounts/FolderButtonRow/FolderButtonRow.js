import React from 'react';
import {withTranslation} from "react-i18next";
import Icon from "components/default/icons";
import styles from "./FolderButtonRow.module.scss";
import {Button} from "components/default/buttons/Button/Button";
import FolderModal from "../FolderModal";
import * as PropTypes from "prop-types";
import FolderRemovingConfirmation from "../FolderRemovingConfirmation";
import {connect} from "react-redux";
import {personalAccountFoldersOperations} from "ducks/personalAccountFolders";
import {compose} from "redux";

class FolderButtonRow extends React.Component {
    state = {
        showEditFolder: false,
        showDeleteFolder: false
    };

    toggleEditFolder = () => {
        this.setState({showEditFolder: !this.state.showEditFolder});
    };

    toggleDeleteFolder = () => {
        this.setState({showDeleteFolder: !this.state.showDeleteFolder});
    };

    onFolderDelete = async (removalOption) => {
        await this.props.removeFolder(this.props.folder, removalOption);
        await this.props.selectFolder(null);
    };

    render() {

        return (
            <React.Fragment>
                <Button customStyle={styles.button} content={<Icon name='edit' styles={styles.buttonIcon}/>}
                        onClick={this.toggleEditFolder}/>
                <Button customStyle={styles.button} content={<Icon name='delete' styles={styles.buttonIcon}/>}
                        onClick={this.toggleDeleteFolder}/>
                {this.state.showEditFolder ?
                    <FolderModal close={this.toggleEditFolder} folder={this.props.folder}/> : null}
                {this.state.showDeleteFolder ?
                    <FolderRemovingConfirmation close={this.toggleDeleteFolder} folder={this.props.folder}
                                                delete={this.onFolderDelete}/> : null}
            </React.Fragment>
        );
    }

}

const withConnect = connect(null, {
    removeFolder: personalAccountFoldersOperations.removeFolder,
    selectFolder: personalAccountFoldersOperations.selectFolder
});

export default compose(withTranslation(), withConnect)(FolderButtonRow);

FolderButtonRow.propTypes = {
    folder: PropTypes.object
};