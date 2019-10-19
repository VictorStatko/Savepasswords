import * as PropTypes from 'prop-types';
import React from 'react';
import styles from './PrimaryButton.module.scss';

const PrimaryButton = ({text, onClick, type, disabled}) => (
    <button
        type={type}
        disabled={disabled}
        onClick={onClick}
        className={styles.button}>
        {text}
    </button>

);

export default PrimaryButton;

PrimaryButton.propTypes = {
    disabled: PropTypes.bool,
    onClick: PropTypes.func,
    text: PropTypes.string.isRequired,
    type: PropTypes.string
};

PrimaryButton.defaultProps = {
    disabled: false,
    onClick: () => {},
    type: "submit"
};