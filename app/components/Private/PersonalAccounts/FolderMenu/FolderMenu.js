import React from 'react';
import styles from "./FolderMenu.module.scss";
import Icon from "components/default/icons";
import {connect} from "react-redux";
import {compose} from "redux";
import {withTranslation} from "react-i18next";
import {withRouter} from "react-router-dom";
import {personalAccountFoldersOperations} from "ducks/personalAccountFolders";
import {personalAccountSharingsOperations} from "ducks/personalAccountsSharings";

class FolderMenu extends React.Component {

    onFolderClick = (uuid) => {
        this.props.selectFolder(uuid);
    };

    onSharingClick = (selectedSharingFromAccountEntityUuid) => {
        this.props.selectSharing(selectedSharingFromAccountEntityUuid);
    };

    render() {
        const {t} = this.props;

        const activeSharingFromAccountEntityUuid = this.props.selectedSharingFromAccountEntityUuid;
        const activeFolderUuid = this.props.selectedSharingFromAccountEntityUuid ? 'None' : this.props.selectedFolderUuid;

        const folderItems = this.props.folders.map((folder) => {
                return <div key={folder.uuid} className={styles.navItem} onClick={() => this.onFolderClick(folder.uuid)}>
                    <Icon name='folder'
                          styles={activeFolderUuid === folder.uuid ? `${styles.icon} ${styles.iconActive}` : styles.icon}/>
                    <div className={styles.badge}>{folder.accountsCount}</div>
                    <span className={styles.text}>{folder.name}</span>
                </div>
            }
        );

        const sharingItems = this.props.sharings.map((sharing) => {
                return <div key={sharing.sharingFromAccountEntityUuid} className={styles.navItem}
                            onClick={() => this.onSharingClick(sharing.sharingFromAccountEntityUuid)}>
                    <Icon name='folder'
                          styles={activeSharingFromAccountEntityUuid === sharing.sharingFromAccountEntityUuid ? `${styles.icon} ${styles.iconActive}` : styles.icon}/>
                    <div className={styles.badge}>{sharing.sharedAccountsCount}</div>
                    <span
                        className={styles.text}>{t('personalAccounts.sharing.sharedFrom')} {sharing.sharingFromEmail}</span>
                </div>
            }
        );

        const resultList = folderItems.concat(sharingItems);

        return (
            <div className={styles.menuContainer}>
                {resultList}
            </div>
        );
    }

}

const mapStateToProps = (state) => {
    return {
        folders: state.personalAccountFolders.folders,
        sharings: state.personalAccountSharings.sharings,
        selectedFolderUuid: state.personalAccountFolders.selectedFolderUuid,
        selectedSharingFromAccountEntityUuid: state.personalAccountSharings.selectedSharingFromAccountEntityUuid
    }
};

const withConnect = connect(mapStateToProps, {
    selectFolder: personalAccountFoldersOperations.selectFolder,
    selectSharing: personalAccountSharingsOperations.selectSharing
});

export default compose(withTranslation(), withRouter, withConnect)(FolderMenu);