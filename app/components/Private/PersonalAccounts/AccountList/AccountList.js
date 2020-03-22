import React from 'react';
import {withTranslation} from "react-i18next";
import {Col, Row} from "react-bootstrap";
import AccountCard from "../AccountCard";
import {connect} from "react-redux";
import {compose} from "redux";
import NoData from "components/default/noData";
import styles from "./AccountList.module.scss";

class AccountList extends React.Component {

    render() {
        const listItems = this.props.accounts.map((account) =>
            <Col xl={3} lg={4} md={6} sm={12} key={account.uuid}>
                <AccountCard account={account}/>
            </Col>
        );

        return (
            <React.Fragment>
                {listItems.length > 0 ? <Row>{listItems}</Row> : <div className={styles.noData}><NoData/></div>}
            </React.Fragment>
        );
    }

}

const mapStateToProps = (state) => {
    return {
        accounts: state.personalAccounts.accounts
    }
};

const withConnect = connect(mapStateToProps, null);

export default compose(withTranslation(), withConnect)(AccountList);
