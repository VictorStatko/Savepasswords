import React from 'react'
import {ReactComponent as LogoutIcon} from 'images/icons/logout.svg';
import {ReactComponent as MenuIcon} from 'images/icons/menu.svg';
import {ReactComponent as MenuPointIcon} from 'images/icons/menu-points.svg';
import {ReactComponent as AddIcon} from 'images/icons/add.svg';
import {ReactComponent as CloseIcon} from 'images/icons/close.svg';
import {ReactComponent as DeleteIcon} from 'images/icons/delete.svg';
import {ReactComponent as KeyIcon} from 'images/icons/key.svg';
import {ReactComponent as InfoIcon} from 'images/icons/info.svg';
import {ReactComponent as PasswordIcon} from 'images/icons/password.svg';
import {ReactComponent as EyeIcon} from 'images/icons/eye.svg';
import {ReactComponent as NewFolderIcon} from 'images/icons/new-folder.svg';
import {ReactComponent as FolderIcon} from 'images/icons/folder.svg';
import {ReactComponent as EditIcon} from 'images/icons/edit.svg';
import {ReactComponent as ShareIcon} from 'images/icons/share.svg';
import {ReactComponent as SettingIcon} from 'images/icons/settings.svg';
import {ReactComponent as UKIcon} from 'images/icons/uk.svg';
import {ReactComponent as RussiaIcon} from 'images/icons/russia.svg';
import * as PropTypes from "prop-types";

const Icon = ({name, styles, onClick}) => {
    switch (name) {
        case 'logout':
            return (<LogoutIcon className={styles} onClick={onClick}/>);
        case 'menu':
            return (<MenuIcon className={styles} onClick={onClick}/>);
        case 'menu-points':
            return (<MenuPointIcon className={styles} onClick={onClick}/>);
        case 'add':
            return (<AddIcon className={styles} onClick={onClick}/>);
        case 'close':
            return (<CloseIcon className={styles} onClick={onClick}/>);
        case 'delete':
            return (<DeleteIcon className={styles} onClick={onClick}/>);
        case 'key':
            return (<KeyIcon className={styles} onClick={onClick}/>);
        case 'info':
            return (<InfoIcon className={styles} onClick={onClick}/>);
        case 'password':
            return (<PasswordIcon className={styles} onClick={onClick}/>);
        case 'eye':
            return (<EyeIcon className={styles} onClick={onClick}/>);
        case 'newFolder':
            return (<NewFolderIcon className={styles} onClick={onClick}/>);
        case 'folder':
            return (<FolderIcon className={styles} onClick={onClick}/>);
        case 'edit':
            return (<EditIcon className={styles} onClick={onClick}/>);
        case 'share':
            return (<ShareIcon className={styles} onClick={onClick}/>);
        case 'settings':
            return (<SettingIcon className={styles} onClick={onClick}/>);
        case 'uk':
            return (<UKIcon className={styles} onClick={onClick}/>);
        case 'rus':
            return (<RussiaIcon className={styles} onClick={onClick}/>);
        default:
            return null;
    }
};

export default Icon;

Icon.propTypes = {
    styles: PropTypes.string,
    onClick: PropTypes.func,
    name: PropTypes.string.isRequired
};