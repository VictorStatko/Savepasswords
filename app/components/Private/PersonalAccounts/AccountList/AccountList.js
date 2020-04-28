import React from 'react';
import {withTranslation} from "react-i18next";
import {Col, Row} from "react-bootstrap";
import AccountCard from "../AccountCard";
import {connect} from "react-redux";
import {compose} from "redux";
import NoData from "components/default/noData";
import styles from "./AccountList.module.scss";
import {personalAccountsOperations} from "ducks/personalAccounts";
import {withRouter} from "react-router-dom";
import Pagination from "components/default/pagination";

class AccountList extends React.Component {

    state = {};

    onPageChange = page => {
        this.props.changePaginationPage(page);
    };

    render() {
        const {accounts, pagination,  selectedSharingFromAccountEntityUuid} = this.props;

        const listItems = accounts.map((account) =>
            <Col xl={4} lg={6} md={12} key={account.uuid} className={styles.column}>
                <AccountCard account={account} sharedFromUuid={selectedSharingFromAccountEntityUuid}/>
            </Col>
        );
        return (
            <React.Fragment>
                {listItems.length > 0 ?
                    <React.Fragment>
                        <Row className={styles.paginationRow}>
                            <Pagination
                                total={pagination.total} current={pagination.page}
                                pageSize={pagination.size} onChange={this.onPageChange}
                            />
                        </Row>
                        <Row>{listItems}</Row>
                    </React.Fragment> :
                    <div className={styles.noData}><NoData/></div>}
            </React.Fragment>
        );
    }

}

const mapStateToProps = (state) => {
    return {
        accounts: state.personalAccounts.pagedAccounts,
        pagination: state.personalAccounts.pagination,
        selectedSharingFromAccountEntityUuid: state.personalAccountSharings.selectedSharingFromAccountEntityUuid
    }
};

const withConnect = connect(mapStateToProps, {
    changePaginationPage: personalAccountsOperations.changePaginationPage
});

export default compose(withTranslation(), withRouter, withConnect)(AccountList);
