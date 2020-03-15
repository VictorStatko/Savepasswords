import React from 'react'
import {ReactComponent as LogoutIcon} from 'images/icons/logout.svg';
import {ReactComponent as MenuIcon} from 'images/icons/menu.svg';
import {ReactComponent as MenuPointIcon} from 'images/icons/menu-points.svg';
import {ReactComponent as AddIcon} from 'images/icons/add.svg';
import {ReactComponent as CloseIcon} from 'images/icons/close.svg';
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