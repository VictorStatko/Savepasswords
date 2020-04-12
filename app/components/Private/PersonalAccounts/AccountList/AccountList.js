import React from 'react';
import {withTranslation} from "react-i18next";
import {Col, Row} from "react-bootstrap";
import AccountCard from "../AccountCard";
import {connect} from "react-redux";
import {compose} from "redux";
import NoData from "components/default/noData";
import styles from "./AccountList.module.scss";
import {setStateAsync} from "utils/stateUtils";
import {personalAccountsOperations} from "ducks/personalAccounts";
import {PageSpinner} from "components/default/spinner";
import ServerError from "components/default/serverError/ServerError";
import queryString from 'query-string';
import {withRouter} from "react-router-dom";

class AccountList extends React.Component {

    state = {};

    componentDidMount = async () => {
        await this.fetchAccounts();
    };

    fetchAccounts = async () => {
        await setStateAsync(this, {error: false, loading: true, folderExistenceError: false});

        let params = queryString.parse(this.props.location.search);
        try {
            await this.props.fetchPersonalAccounts(params.folderUuid);
        } catch (e) {
            console.error(e);
            if (e.data && e.data.error && e.data.error === 'exceptions.personalAccountFolderNotFoundByUuid') {
                await setStateAsync(this, {folderExistenceError: true});
            } else {
                await setStateAsync(this, {error: true});
            }
        } finally {
            await setStateAsync(this, {loading: false});
        }
    };

    componentDidUpdate = async (prevProps) => {
        if (this.props.location !== prevProps.location) {
            await this.fetchAccounts();
        }
    };

    renderDataOrLoader = () => {
        const {t} = this.props;

        if (this.state.loading || this.props.parentLoading) {
            return <PageSpinner className={styles.spinner}/>;
        }

        if (this.state.folderExistenceError) {
            return <ServerError wrongMessage={t('exceptions.personalAccountFolderNotFoundByUuid')}
                                descriptionMessage={t('global.recheckData')}/>
        }

        if (this.state.error) {
            return <div className={styles.blockWithoutData}><ServerError/></div>;
        } else {

            const listItems = this.props.accounts.map((account) =>
                <Col xl={4} lg={6} md={6} key={account.uuid} className={styles.column}>
                    <AccountCard account={account}/>
                </Col>
            );

            return (
                <React.Fragment>
                    {listItems.length > 0 ? <Row>{listItems}</Row> : <div className={styles.noData}><NoData/></div>}
                </React.Fragment>
            );
        }
    };

    render() {
        return (
            <React.Fragment>
                {this.renderDataOrLoader()}
            </React.Fragment>
        );
    }

}

const mapStateToProps = (state) => {
    return {
        accounts: state.personalAccounts.accounts
    }
};

const withConnect = connect(mapStateToProps, {
    fetchPersonalAccounts: personalAccountsOperations.fetchPersonalAccounts
});

export default compose(withTranslation(), withRouter, withConnect)(AccountList);
