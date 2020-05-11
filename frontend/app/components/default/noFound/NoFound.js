import React, {Component} from 'react';
import {withTranslation} from "react-i18next";
import styles from './NoFound.module.scss';

class NoFound extends Component {

    render() {

        const {t} = this.props;

        return (
            <div className={styles.container}>
                <span className={styles.error}>{t('notFound.error')}</span>
                <hr/>
                <span className={styles.description}>{t('notFound.description')}</span>
            </div>
        );
    }
}


export default withTranslation()(NoFound);