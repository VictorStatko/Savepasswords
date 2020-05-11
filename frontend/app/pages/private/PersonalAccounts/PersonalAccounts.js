import React from 'react';
import {Col, Row} from "react-bootstrap";
import styles from "./PersonalAccounts.module.scss";
import ButtonRow from "components/Private/PersonalAccounts/AccountButtonRow";
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
import FolderButtonRow from "components/Private/PersonalAccounts/FolderButtonRow";
import AccountSearch from "components/Private/PersonalAccounts/AccountSearch";
import {personalAccountSharingsOperations} from "ducks/personalAccountsSharings";

class PersonalAccounts extends React.Component {

    state = {
        error: false,
        loading: true
    };

    componentDidMount = async () => {
        try {
            await this.props.fetchFolders();
            await this.props.fetchSharings();
            await this.props.fetchPersonalAccounts(this.props.selectedFolderUuid, this.props.selectedSharingFromAccountEntityUuid);
        } catch (e) {
            console.error(e);
            await setStateAsync(this, {error: true});
        } finally {
            await setStateAsync(this, {loading: false});
        }
    };

    componentDidUpdate = async (prevProps) => {
        if (this.props.selectedFolderUuid != prevProps.selectedFolderUuid || this.props.selectedSharingFromAccountEntityUuid != prevProps.selectedSharingFromAccountEntityUuid) {
            await setStateAsync(this, {error: false, loading: true});
            try {
                await this.props.fetchPersonalAccounts(this.props.selectedFolderUuid, this.props.selectedSharingFromAccountEntityUuid);
            } catch (e) {
                console.error(e);
                await setStateAsync(this, {error: true});
            } finally {
                await setStateAsync(this, {loading: false});
            }
        }
    };

    renderDataOrLoader = () => {
        if (this.state.error) {
            return <div className={styles.blockWithoutData}><ServerError/></div>;
        }

        if (this.state.loading) {
            return <PageSpinner delay={150} className={styles.spinner}/>;
        }

        const {t} = this.props;
        const activeFolder = this.props.folders.find(folder => folder.uuid === this.props.selectedFolderUuid);
        const activeSharing = this.props.sharings.find(sharing => sharing.sharingFromAccountEntityUuid === this.props.selectedSharingFromAccountEntityUuid);

        return <Row>
            <Col xl={3} lg={4} md={4} className={styles.menuColumn}>
                <FolderMenu/>
            </Col>
            <Col xl={9} lg={8} md={8} className={styles.listColumn}>
                {
                    <Row>
                        <Col>
                            <h2>{activeSharing ? `${t('personalAccounts.sharing.sharedFrom')} ${activeSharing.sharingFromEmail}` : activeFolder.name}</h2>
                        </Col>
                        <Col md={12} lg="auto" className={`d-flex ${styles.actionsRow}`}>
                            <Col
                                className={`d-flex ${activeFolder.uuid === null ? 'justify-content-center' : 'justify-content-start'} justify-content-lg-end align-items-center ${styles.searchColumn}`}>
                                <AccountSearch/>
                            </Col>
                            {
                                activeFolder.uuid === null
                                    ? null :
                                    <Col xs={3}
                                         className={`d-flex justify-content-end align-items-center ${styles.buttonColumn}`}>
                                        <FolderButtonRow folder={activeFolder}/>
                                    </Col>
                            }
                        </Col>
                    </Row>
                }
                <Col className={styles.accountList}>
                    <AccountList/>
                </Col>
            </Col>
        </Row>

    };


    render() {
        const {t} = this.props;

        return (
            <React.Fragment>
                <Row className={styles.headerRow}>
                    <Col>
                        <h1>{t('personalAccounts.header')}</h1>
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
        folders: state.personalAccountFolders.folders,
        sharings: state.personalAccountSharings.sharings,
        selectedFolderUuid: state.personalAccountFolders.selectedFolderUuid,
        selectedSharingFromAccountEntityUuid: state.personalAccountSharings.selectedSharingFromAccountEntityUuid
    }
};

const withConnect = connect(mapStateToProps, {
    fetchPersonalAccounts: personalAccountsOperations.fetchPersonalAccounts,
    fetchFolders: personalAccountFoldersOperations.fetchPersonalAccountFolders,
    fetchSharings: personalAccountSharingsOperations.fetchPersonalAccountSharings
});

export default compose(withTranslation(), withRouter, withConnect)(PersonalAccounts);