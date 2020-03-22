import React, {Component} from 'react';
import styles from './NoData.module.scss';
import Icon from "components/default/icons";
import {withTranslation} from "react-i18next";

class NoData extends Component {

    render() {

        const {t} = this.props;

        return (
            <div className={styles.container}>
                <Icon name='info' styles={styles.icon}/>
                <hr/>
                <span className={styles.nothing}>{t('noData.nothing')}</span>
                <hr/>
                <span className={styles.description}>{t('noData.description')}</span>
            </div>
        );
    }
}


export default withTranslation()(NoData);