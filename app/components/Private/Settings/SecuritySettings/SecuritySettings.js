import React from 'react';
import {connect} from "react-redux";
import {compose} from "redux";
import {withTranslation} from "react-i18next";
import ActivityHistory from "../ActivityHistory";
import styles from "./SecuritySettings.module.scss";
import {setStateAsync} from "utils/stateUtils";
import {settingsOperations} from "ducks/settings";
import ServerError from "components/default/serverError/ServerError";
import {PageSpinner} from "components/default/spinner/Spinner";

class SecuritySettings extends React.Component {

    state = {
        error: false,
        loading: true
    };

    componentDidMount = async () => {
        try {
            await this.props.fetchHistory();
        } catch (e) {
            console.error(e);
            await setStateAsync(this, {error: true});
        } finally {
            await setStateAsync(this, {loading: false});
        }
    };

    render() {
        if (this.state.error) {
            return <div className={styles.errorBlock}><ServerError/></div>;
        }

        if (this.state.loading) {
            return <PageSpinner delay={150} className={styles.spinner}/>;
        }

        return (
            <div className={styles.container}>
                <ActivityHistory/>
            </div>
        );
    }

}

const withConnect = connect(null, {
    fetchHistory: settingsOperations.fetchActivityHistory
});

export default compose(withTranslation(), withConnect)(SecuritySettings);