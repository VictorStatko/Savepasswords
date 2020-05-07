import React from 'react';
import {withTranslation} from 'react-i18next';
import {PageSpinner} from "components/default/spinner/Spinner";
import styles from "./Root.module.scss";
import queryString from 'query-string';
import {isNotEmpty} from "utils/stringUtils";
import {toast} from "react-toastify";
import history from 'utils/history';
import {connect} from "react-redux";
import {compose} from "redux";
import {accountOperations} from "ducks/account";

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

                await this.props.confirmVerification(verificationCode, false);
                toast.success(t('signUp.confirmationSuccess'));
            }
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

const withConnect = connect(null, {
    confirmVerification: accountOperations.confirmVerification
});

export default compose(withTranslation(), withConnect)(Root);