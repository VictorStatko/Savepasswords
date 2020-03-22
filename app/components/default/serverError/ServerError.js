import React, {Component} from 'react';
import styles from './ServerError.module.scss';
import {withTranslation} from "react-i18next";

class ServerError extends Component {

    render() {

        const {t} = this.props;

        return (
            <div className={styles.container}>
                <span className={styles.oops}>{t('serverError.oops')}</span>
                <hr/>
                <span className={styles.wrong}>{t('serverError.wrong')}</span>
                <hr/>
                <span className={styles.description}>{t('serverError.description')}</span>
            </div>
        );
    }
}


export default withTranslation()(ServerError);