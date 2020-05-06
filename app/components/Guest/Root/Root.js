import React from 'react';
import {withTranslation} from 'react-i18next';
import {PageSpinner} from "components/default/spinner/Spinner";
import styles from "./Root.module.scss";
import queryString from 'query-string';
import {isNotEmpty} from "utils/stringUtils";
import {processErrorAsNotification} from "utils/errorHandlingUtils";
import {toast} from "react-toastify";
import fetch from "utils/fetch";
import {POST} from "utils/appConstants";
import history from 'utils/history';

class Root extends React.Component {

    state = {
        loading: false
    };

    componentDidMount = async () => {
        try {
            const params = queryString.parse(this.props.location.search);
            const action = params.action;
            const verificationCode = params.verificationCode;

            const {t} = this.props;

            if (action === 'confirmRegistration' && isNotEmpty(verificationCode)) {
                this.setState({
                    loading: true
                });

                await fetch(POST, `auth/accounts?action=confirm-registration&verificationCode=${verificationCode}`);
                toast.success(t('signUp.confirmationSuccess'));
            }
        } catch (e) {
            processErrorAsNotification(e);
        } finally {
            this.setState({
                loading: false
            });
            history.push('/sign-in');
        }
    }

    render() {
        const {loading} = this.state;
        return (
            <div className={styles.container}>
                {loading ? <PageSpinner/> : null}
            </div>
        );
    }

}

export default withTranslation()(Root);