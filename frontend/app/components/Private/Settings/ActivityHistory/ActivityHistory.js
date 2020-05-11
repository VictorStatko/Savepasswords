import React from 'react';
import {connect} from "react-redux";
import {compose} from "redux";
import {withTranslation} from "react-i18next";
import ActivityItem from "../ActivityItem";
import {settingsOperations} from "ducks/settings";
import styles from "./ActivityHistory.module.scss";
import {Spinner} from "components/default/spinner/Spinner";
import {toast} from "react-toastify";

class ActivityHistory extends React.Component {

    state = {
        loading: false
    }

    finishSessions = async () => {
        const {t} = this.props;

        this.setState({
            loading: true
        });

        try {
            await this.props.finishSessions();
            toast.success(t('settings.security.finishSuccess'));
        } finally {
            this.setState({
                loading: false
            });
        }
    };

    render() {
        const {t} = this.props;

        const listItems = this.props.activityHistory.map((item, i) => {
                return <ActivityItem item={item} key={i}/>;
            }
        );

        const canFinish = listItems.length > 1;

        return (
            <div className={styles.container}>
                <h3 className={styles.header}>{t('settings.security.activityHistory')}</h3>
                <table>
                    <thead>
                    <tr>
                        <th>{t('settings.security.date')}</th>
                        <th>{t('settings.security.systemInformation')}</th>
                        <th>{t('settings.security.geoInformation')}</th>
                    </tr>
                    </thead>
                    <tbody>
                    {listItems}
                    </tbody>
                </table>
                <div className={canFinish ? styles.finish : `${styles.finish} ${styles.finishDisabled}`}>
                    <span
                        onClick={canFinish ? this.finishSessions : null}>{t('settings.security.finishAllSessions')}</span>
                    {this.state.loading ? <Spinner className={styles.spinner}/> : null}
                </div>
            </div>
        );
    }

}

const mapStateToProps = (state) => {
    return {
        activityHistory: state.settings.activityHistory
    }
};

const withConnect = connect(mapStateToProps, {
    fetchHistory: settingsOperations.fetchActivityHistory,
    finishSessions: settingsOperations.finishSessions
});

export default compose(withTranslation(), withConnect)(ActivityHistory);