import React from 'react';
import * as PropTypes from "prop-types";
import styles from "./FolderMenu.module.scss";
import Icon from "components/default/icons";
import {connect} from "react-redux";
import {compose} from "redux";
import {withTranslation} from "react-i18next";
import {Badge} from "react-bootstrap";

class FolderMenu extends React.Component {

    render() {
        const listItems = this.props.folders.map((folder) => {
                return <div key={folder.uuid} className={styles.navItem} onClick={() => this.props.click(folder.uuid)}>
                    <Icon name='folder' styles={styles.icon}/> <Badge variant="secondary" className={styles.badge}>{folder.accountsCount}</Badge>
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

export default compose(withTranslation(), withConnect)(FolderMenu);

FolderMenu.propTypes = {
    click: PropTypes.func.isRequired
};