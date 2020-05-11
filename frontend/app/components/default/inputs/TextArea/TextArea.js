import * as PropTypes from 'prop-types';
import React, {Component} from 'react';
import styles from './TextArea.module.scss';
import {isEmpty} from "utils/stringUtils";

class TextArea extends Component {

    render() {
        const {readOnly, label, placeholder, id, focused, className, value, error, onChange} = this.props;

        return (
            <div className={className}>
                {isEmpty(label) ? null : <span className={styles.labelText}>{label}</span>}
                <textarea
                    id={id}
                    value={value}
                    placeholder={placeholder}
                    className={isEmpty(error) ? null : styles.error}
                    onChange={onChange}
                    readOnly={readOnly}
                    autoFocus={focused}
                    autoComplete="off"
                />
                {isEmpty(error) ? null : <div className={styles.errorText}>{error}</div>}
            </div>
        );
    }
}


export default TextArea;

TextArea.propTypes = {
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

TextArea.defaultProps = {
    readOnly: false,
    focused: false,
    error: "",
    value: "",
    label: "",
    placeholder: "",
    className: null
};