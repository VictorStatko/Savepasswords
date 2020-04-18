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

    state = {
        showSpinner: false,
    };

    componentDidMount = () => {
        this.timer = setTimeout(
            () => this.setState({showSpinner: true}),
            this.props.delay
        );
    };

    componentWillUnmount() {
        clearTimeout(this.timer);
    }

    render() {
        const {className} = this.props;

        return (
            this.state.showSpinner && <React.Fragment>
                <div className={styles.pageSpinnerContainer}>
                    <Spinner className={`${styles.pageLoading} ${className}`}/>
                </div>
            </React.Fragment>
        );
    }
}

PageSpinner.propTypes = {
    className: PropTypes.string,
    delay: PropTypes.number
};

PageSpinner.defaultProps = {
    className: null,
    delay: 1
};