import React from 'react'
import {ReactComponent as LogoutIcon} from 'images/icons/logout.svg';
import {ReactComponent as MenuIcon} from 'images/icons/menu.svg';
import * as PropTypes from "prop-types";

const Icon = ({name, styles, onClick}) => {
    switch (name) {
        case 'logout':
            return (<LogoutIcon className={styles} onClick={onClick}/>);
        case 'menu':
            return (<MenuIcon className={styles} onClick={onClick}/>);
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