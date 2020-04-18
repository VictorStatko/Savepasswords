import React from 'react';
import styles from "./FolderMenu.module.scss";
import Icon from "components/default/icons";
import {connect} from "react-redux";
import {compose} from "redux";
import {withTranslation} from "react-i18next";
import {withRouter} from "react-router-dom";
import {personalAccountFoldersOperations} from "ducks/personalAccountFolders";

class FolderMenu extends React.Component {

    onFolderClick = (uuid) => {
        if (uuid != this.props.selectedFolderUuid) {
            this.props.selectFolder(uuid);
        }
    };

    render() {
        const activeFolderUuid = this.props.selectedFolderUuid;

        const listItems = this.props.folders.map((folder) => {
                return <div key={folder.uuid} className={styles.navItem} onClick={() => this.onFolderClick(folder.uuid)}>
                    <Icon name='folder'
                          styles={activeFolderUuid === folder.uuid ? `${styles.icon} ${styles.iconActive}` : styles.icon}/>
                    <div className={styles.badge}>{folder.accountsCount}</div>
                    <span className={styles.text}>{folder.name}</span>
                </div>
            }
        );

        return (
            <div className={styles.menuContainer}>
                {listItems}
            </div>
        );
    }

}

const mapStateToProps = (state) => {
    return {
        folders: state.personalAccountFolders.folders,
        selectedFolderUuid: state.personalAccountFolders.selectedFolderUuid
    }
};

const withConnect = connect(mapStateToProps, {
    selectFolder: personalAccountFoldersOperations.selectFolder
});

export default compose(withTranslation(), withRouter, withConnect)(FolderMenu);