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
import {withRouter} from "react-router-dom";
import queryString from "query-string";

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

    renderDataOrLoader = () => {
        const params = queryString.parse(this.props.location.search);
        const activeFolderUuid = params.folderUuid ? params.folderUuid : null;

        const activeFolder = this.props.folders.find(folder => folder.uuid === activeFolderUuid);

        if (this.state.error) {
            return <div className={styles.blockWithoutData}><ServerError/></div>;
        } else {
            return <Row>
                <Col xl={3} lg={4} md={4} className={styles.menuColumn}>
                    {this.state.loading ? <PageSpinner className={styles.spinner}/> :
                        <FolderMenu activeFolder={activeFolder}/>}
                </Col>
                <Col className={styles.listColumn}>
                    {activeFolder ?  <Col><h2>{activeFolder.name}</h2></Col> : null}
                    <Col className={styles.accountList}>
                        <AccountList parentLoading={this.state.loading}/>
                    </Col>
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

const mapStateToProps = (state) => {
    return {
        folders: state.personalAccountFolders.folders
    }
};

const withConnect = connect(mapStateToProps, {
    fetchPersonalAccounts: personalAccountsOperations.fetchPersonalAccounts,
    fetchFolders: personalAccountFoldersOperations.fetchPersonalAccountFolders
});

export default compose(withTranslation(), withRouter, withConnect)(PersonalAccounts);