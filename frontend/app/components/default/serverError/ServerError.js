import React, {Component} from 'react';
import styles from './ServerError.module.scss';
import {withTranslation} from "react-i18next";
import * as PropTypes from "prop-types";

class ServerError extends Component {

    render() {

        const {t, oopsMessage, wrongMessage, descriptionMessage} = this.props;

        return (
            <div className={styles.container}>
                <span className={styles.oops}>{oopsMessage ? oopsMessage : t('serverError.oops')}</span>
                <hr/>
                <span className={styles.wrong}>{wrongMessage ? wrongMessage : t('serverError.wrong')}</span>
                <hr/>
                <span className={styles.description}>{descriptionMessage ? descriptionMessage : t('serverError.description')}</span>
            </div>
        );
    }
}


export default withTranslation()(ServerError);

ServerError.propTypes = {
    oopsMessage: PropTypes.string,
    wrongMessage: PropTypes.string,
    descriptionMessage: PropTypes.string,
};