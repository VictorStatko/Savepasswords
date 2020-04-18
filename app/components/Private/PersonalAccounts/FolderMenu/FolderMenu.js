import React from 'react';
import styles from "./FolderMenu.module.scss";
import Icon from "components/default/icons";
import {connect} from "react-redux";
import {compose} from "redux";
import {withTranslation} from "react-i18next";
import history from "utils/history";
import {withRouter} from "react-router-dom";

class FolderMenu extends React.Component {

    onFolderClick = (uuid) => {
        let url;
        if (uuid) {
            url = `/accounts?folderUuid=${uuid}`;
        } else {
            url = `/accounts`;
        }
        history.push(url);
    };

    render() {
        const activeFolderUuid = this.props.activeFolder ?
            this.props.activeFolder.uuid ? this.props.activeFolder.uuid : null
            : undefined;

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
        folders: state.personalAccountFolders.folders
    }
};

const withConnect = connect(mapStateToProps, null);

export default compose(withTranslation(), withRouter, withConnect)(FolderMenu);