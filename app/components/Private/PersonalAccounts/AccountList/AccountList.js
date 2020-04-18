import React from 'react';
import {withTranslation} from "react-i18next";
import {Col, Row} from "react-bootstrap";
import AccountCard from "../AccountCard";
import {connect} from "react-redux";
import {compose} from "redux";
import NoData from "components/default/noData";
import styles from "./AccountList.module.scss";
import {personalAccountsOperations} from "ducks/personalAccounts";
import {PageSpinner} from "components/default/spinner";
import ServerError from "components/default/serverError/ServerError";
import queryString from 'query-string';
import {withRouter} from "react-router-dom";
import Pagination from "components/default/pagination";
import {getMaxValidPageNumber, sliceItemsByPage} from "utils/paginationUtils";
import history from "utils/history";

const ITEMS_PER_PAGE = 9;

class AccountList extends React.Component {

    state = {};

    componentDidMount = async () => {
        await this.fetchAccounts();
    };

    //TODO FETCH BY PAGE
    fetchAccounts = async () => {
        this.setState({error: false, loading: true, folderExistenceError: false});

        const query = queryString.parse(this.props.location.search);

        try {
            await this.props.fetchPersonalAccounts(query.folderUuid);
        } catch (e) {
            console.error(e);
            if (e.data && e.data.error && e.data.error === 'exceptions.personalAccountFolderNotFoundByUuid') {
                this.setState({folderExistenceError: true});
            } else {
                this.setState({error: true});
            }
        } finally {
            this.setState({loading: false});
        }
    };

    componentDidUpdate = async (prevProps) => {
        if (this.props.location !== prevProps.location) {
            await this.fetchAccounts();
        }
    };

    onPageChange = page => {
        const query = queryString.parse(this.props.location.search);
        query.page = page;

        history.push({
                pathname: this.props.location.pathname,
                search: queryString.stringify(query)
            }
        );
    };

    renderDataOrLoader = () => {
        const {t, accounts} = this.props;

        if (this.state.loading || this.props.parentLoading) {
            return <PageSpinner delay={150} className={styles.spinner}/>;
        }

        if (this.state.folderExistenceError) {
            return <ServerError wrongMessage={t('exceptions.personalAccountFolderNotFoundByUuid')}
                                descriptionMessage={t('global.recheckData')}/>
        }

        if (this.state.error) {
            return <div className={styles.blockWithoutData}><ServerError/></div>;
        } else {
            const pageFromQuery = queryString.parse(this.props.location.search).page;
            const pageInt = pageFromQuery ? Number.parseInt(pageFromQuery) : 1;
            const maxValidPageNumber = getMaxValidPageNumber(pageInt, accounts.length, ITEMS_PER_PAGE);

            if (maxValidPageNumber < pageInt) {
                this.onPageChange(maxValidPageNumber);
                return;
            }

            if (pageInt < 1) {
                this.onPageChange(1);
                return;
            }

            const listItems = sliceItemsByPage(accounts, pageInt, ITEMS_PER_PAGE).map((account) =>
                <Col xl={4} lg={6} md={6} key={account.uuid} className={styles.column}>
                    <AccountCard account={account}/>
                </Col>
            );

            return (
                <React.Fragment>
                    {listItems.length > 0 ?
                        <React.Fragment>
                            <Row className={styles.paginationRow}>
                                <Pagination
                                    total={accounts.length} current={pageInt}
                                    pageSize={ITEMS_PER_PAGE} onChange={this.onPageChange}
                                />
                            </Row>
                            <Row>{listItems}</Row>
                        </React.Fragment> :
                        <div className={styles.noData}><NoData/></div>}
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
