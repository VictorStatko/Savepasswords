import * as PropTypes from 'prop-types';
import React from 'react';
import styles from './Button.module.scss';

export const Button = ({content, onClick, type, disabled, customStyle}) => {
    return (
        <button
            type={type}
            disabled={disabled}
            onClick={onClick}
            className={`${styles.button} ${customStyle}`}>
            {content}
        </button>

    );
};

Button.propTypes = {
    disabled: PropTypes.bool,
    onClick: PropTypes.func,
    content: PropTypes.oneOfType(
        [PropTypes.string.isRequired, PropTypes.object.isRequired]
    ),
    type: PropTypes.string
};

Button.defaultProps = {
    disabled: false,
    type: "submit"
};


export const DeclineButton = ({content, onClick, type, disabled, customStyle}) => {
    return (
        <Button type={type}
                disabled={disabled}
                onClick={onClick}
                customStyle={`${styles.declineButton} ${customStyle}`}
                content={content}/>
    );
};

DeclineButton.propTypes = {
    disabled: PropTypes.bool,
    onClick: PropTypes.func,
    content: PropTypes.oneOfType(
        [PropTypes.string.isRequired, PropTypes.object.isRequired]
    ),
    type: PropTypes.string
};

DeclineButton.defaultProps = {
    disabled: false,
    type: "submit"
};

export const ConfirmButton = ({content, onClick, type, disabled, customStyle}) => {
    return (
        <Button type={type}
                disabled={disabled}
                onClick={onClick}
                customStyle={`${styles.confirmButton} ${customStyle}`}
                content={content}/>
    );
};

ConfirmButton.propTypes = {
    disabled: PropTypes.bool,
    onClick: PropTypes.func,
    content: PropTypes.oneOfType(
        [PropTypes.string.isRequired, PropTypes.object.isRequired]
    ),
    type: PropTypes.string
};

ConfirmButton.defaultProps = {
    disabled: false,
    type: "submit"
};