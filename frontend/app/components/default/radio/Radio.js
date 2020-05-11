import React, {Component} from 'react';
import styles from './Radio.module.scss';
import * as PropTypes from "prop-types";

class Radio extends Component {

    render() {

        const {checked, value, onChange, name, label} = this.props;

        return (
            <label className={styles.container}> {label}
                <input type="radio" checked={checked} onChange={onChange} value={value} name={name}/>
                    <span className={styles.checkmark}/>
            </label>
        );
    }
}


export default Radio;

Radio.propTypes = {
    checked: PropTypes.bool.isRequired,
    value: PropTypes.string.isRequired,
    onChange: PropTypes.func.isRequired,
    name: PropTypes.string.isRequired,
    label: PropTypes.string
};