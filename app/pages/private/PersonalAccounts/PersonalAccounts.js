import React from 'react';
import {Col, Row} from "react-bootstrap";
import styles from "./PersonalAccounts.module.scss";
import ButtonRow from "components/Private/PersonalAccounts/ButtonRow";
import {withTranslation} from "react-i18next";
import {connect} from "react-redux";
import {personalAccountsOperations} from "ducks/personalAccounts";
import {compose} from "redux";
import {setStateAsync} from "utils/stateUtils";
import ServerError from "components/default/serverError";
import AccountList from "components/Private/PersonalAccounts/AccountList";
import FolderMenu from "components/Private/PersonalAccounts/FolderMenu";
import {personalAccountFoldersOperations} from "ducks/personalAccountFolders";
import {PageSpinner} from "components/default/spinner/Spinner";
import history from 'utils/history';

class PersonalAccounts extends React.Component {

    state = {
        error: false,
        loading: true
    };

    componentDidMount = async () => {
        try {
            await this.props.fetchFolders();
        } catch (e) {
            console.error(e);
            await setStateAsync(this, {error: true});
        } finally {
            await setStateAsync(this, {loading: false});
        }
    };

    onFolderClick = (uuid) => {
        let url;
        if (uuid){
            url = `/accounts?folderUuid=${uuid}`;
        } else {
            url = `/accounts`;
        }
       history.push(url);
    };

    renderDataOrLoader = () => {


        if (this.state.error) {
            return <div className={styles.blockWithoutData}><ServerError/></div>;
        } else {
            return <Row>
                <Col xl={3} lg={4} md={4} className={styles.menuColumn}>
                    {this.state.loading ? <PageSpinner className={styles.spinner}/> :
                        <FolderMenu click={this.onFolderClick}/>}
                </Col>
                <Col className={styles.listColumn}>
                    <AccountList parentLoading={this.state.loading}/>
                </Col>
            </Row>
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
                <hr className={styles.hr}/>
                {this.renderDataOrLoader()}
            </React.Fragment>
        );
    }

}

const withConnect = connect(null, {
    fetchPersonalAccounts: personalAccountsOperations.fetchPersonalAccounts,
    fetchFolders: personalAccountFoldersOperations.fetchPersonalAccountFolders
});

export default compose(withTranslation(), withConnect)(PersonalAccounts);