import React, {Component} from 'react';
import styles from './Spinner.module.scss';
import * as PropTypes from "prop-types";

export class Spinner extends Component {

    render() {
        const {className} = this.props;

        return (
            <div className={`${styles.loading} ${className}`}/>
        );
    }
}

Spinner.propTypes = {
    className: PropTypes.string
};

Spinner.defaultProps = {
    className: null
};

export class PageSpinner extends Component {

    render() {
        const {className} = this.props;

        return (
            <div className={styles.pageSpinnerContainer}>
            <Spinner className={`${styles.pageLoading} ${className}`}/>
            </div>
        );
    }
}

PageSpinner.propTypes = {
    className: PropTypes.string
};

PageSpinner.defaultProps = {
    className: null
};