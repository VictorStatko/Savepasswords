import * as PropTypes from 'prop-types';
import React from 'react';
import styles from './SecondaryButton.module.scss';

const SecondaryButton = ({content, onClick, type, disabled, customStyle}) => (
    <button
        type={type}
        disabled={disabled}
        onClick={onClick}
        className={`${styles.button} ${customStyle}`}>
        {content}
    </button>

);

export default SecondaryButton;

SecondaryButton.propTypes = {
    disabled: PropTypes.bool,
    onClick: PropTypes.func,
    content: PropTypes.object.isRequired,
    type: PropTypes.string
};

SecondaryButton.defaultProps = {
    disabled: false,
    onClick: () => {},
    type: "submit"
};