import * as PropTypes from 'prop-types';
import React, {Component} from 'react';
import styles from './TextInput.module.scss';
import {isEmpty} from "utils/stringUtils";

class TextInput extends Component {

    render() {
        const {readOnly, label, placeholder, id, focused, className, value, error, onChange} = this.props;

        return (
            <div className={className}>
                {isEmpty(label) ? null : <span className={styles.labelText}>{label}</span>}
                <input
                    id={id}
                    type="text"
                    value={value}
                    placeholder={placeholder}
                    className={isEmpty(error) ? null : styles.error}
                    onChange={onChange}
                    readOnly={readOnly}
                    autoFocus={focused}
                    autoComplete="off"
                />
                {isEmpty(error) ? null : <span className={styles.errorText}>{error}</span>}
            </div>
        );
    }
}


export default TextInput;

TextInput.propTypes = {
    focused: PropTypes.bool,
    error: PropTypes.string,
    id: PropTypes.string.isRequired,
    label: PropTypes.string,
    placeholder: PropTypes.string,
    readOnly: PropTypes.bool,
    value: PropTypes.string,
    className: PropTypes.string,
    onChange:PropTypes.func.isRequired
};

TextInput.defaultProps = {
    readOnly: false,
    focused: false,
    error: "",
    value: "",
    label: "",
    placeholder: "",
    className: null
};