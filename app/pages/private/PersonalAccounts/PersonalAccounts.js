import React from 'react';
import {Col, Row} from "react-bootstrap";
import styles from "./PersonalAccounts.module.scss";
import ButtonRow from "components/Private/PersonalAccounts/ButtonRow";
import {withTranslation} from "react-i18next";
import {connect} from "react-redux";
import {personalAccountsOperations} from "ducks/personalAccounts";
import {compose} from "redux";
import {setStateAsync} from "utils/stateUtils";
import {PageSpinner} from "components/default/spinner";
import ServerError from "components/default/serverError";
import AccountList from "components/Private/PersonalAccounts/AccountList";

class PersonalAccounts extends React.Component {

    state = {
        error: false,
        loading: true
    };

    componentDidMount = async () => {
        try {
            await this.props.fetchPersonalAccounts();
        } catch (e) {
            console.error(e);
            await setStateAsync(this, {error: true});
        } finally {
            await setStateAsync(this, {loading: false});
        }
    };

    renderDataOrLoader = () => {
        if (this.state.loading){
             return <div className={styles.blockWithoutData}><PageSpinner/></div>;
        }

        if (this.state.error) {
            return <div className={styles.blockWithoutData}><ServerError/></div>;
        } else {
            return <AccountList/>;
        }
    };


    render() {
        const {t} = this.props;

        return (
            <React.Fragment>
                <Row className={styles.headerRow}>
                    <Col>
                        <h1 className={styles.header}>{t('personalAccounts.header')}</h1>
                    </Col>
                    <Col className="d-flex justify-content-end">
                        <ButtonRow/>
                    </Col>
                </Row>
                <hr/>
                {this.renderDataOrLoader()}
            </React.Fragment>
        );
    }

}

const withConnect = connect(null, {
    fetchPersonalAccounts: personalAccountsOperations.fetchPersonalAccounts
});

export default compose(withTranslation(), withConnect)(PersonalAccounts);