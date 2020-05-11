import * as PropTypes from 'prop-types';
import React, {Component} from 'react';
import styles from './TextInput.module.scss';
import {isEmpty} from "utils/stringUtils";
import Icon from "components/default/icons";

class TextInput extends Component {

    state = {
        showSecret: true
    };

    toggleSecret = () => {
        this.setState({
            showSecret: !this.state.showSecret
        });
    };

    render() {
        const {readOnly, label, placeholder, id, focused, className, value, error, onChange, secret} = this.props;
        const {showSecret} = this.state;
        return (
            <div className={className}>
                {isEmpty(label) ? null : <span className={styles.labelText}>{label}</span>}
                {secret && !isEmpty(value)? <Icon name='eye' styles={styles.eyeIcon} onClick={this.toggleSecret}/> : null}
                <input
                    id={id}
                    type={secret && showSecret ? 'password' : 'text'}
                    value={value}
                    placeholder={placeholder}
                    className={isEmpty(error) ? styles.textInput : `${styles.textInput} ${styles.error}`}
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
    onChange: PropTypes.func.isRequired,
    secret: PropTypes.bool,
};

TextInput.defaultProps = {
    readOnly: false,
    focused: false,
    error: "",
    value: "",
    label: "",
    placeholder: "",
    className: null,
    secret: false
};