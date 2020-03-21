import * as PropTypes from 'prop-types';
import React from 'react';
import styles from './Button.module.scss';
import Spinner from "components/default/spinner";

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
    type: "button"
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
    type: "button"
};

export const ConfirmButton = ({content, onClick, type, disabled, customStyle, loading}) => {
    return (
        <Button type={type}
                disabled={disabled}
                onClick={onClick}
                customStyle={`${styles.confirmButton} ${customStyle}`}
                content={<React.Fragment>{loading ? <Spinner/> : null} {content}</React.Fragment>}/>
    );
};

ConfirmButton.propTypes = {
    disabled: PropTypes.bool,
    onClick: PropTypes.func,
    content: PropTypes.oneOfType(
        [PropTypes.string.isRequired, PropTypes.object.isRequired]
    ),
    loading: PropTypes.bool,
    type: PropTypes.string
};

ConfirmButton.defaultProps = {
    disabled: false,
    type: "button",
    loading: false
};