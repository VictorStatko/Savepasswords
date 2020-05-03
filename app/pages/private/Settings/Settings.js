import React from 'react';
import {Col, Row} from "react-bootstrap";
import styles from "./Settings.module.scss";
import {withTranslation} from "react-i18next";
import {connect} from "react-redux";
import {compose} from "redux";
import {withRouter} from "react-router-dom";
import SettingsMenu from "components/Private/Settings/SettingsMenu";
import SecuritySettings from "components/Private/Settings/SecuritySettings/SecuritySettings";

class Settings extends React.Component {
    state = {
        error: false,
        loading: true
    };
    renderSettings = () => {
        const {selectedSettings} = this.props;

        switch (selectedSettings) {
            case 'SECURITY':
                return <SecuritySettings/>;
        }
    };

    render() {
        const {t} = this.props;

        return (
            <React.Fragment>
                <Row className={styles.headerRow}>
                    <Col>
                        <h1>{t('settings.header')}</h1>
                    </Col>
                </Row>
                <hr className={styles.hr}/>
                <Row>
                    <Col>
                        <SettingsMenu/>
                    </Col>
                </Row>
                <Row>
                    <Col>
                        {this.renderSettings()}
                    </Col>
                </Row>
            </React.Fragment>
        );
    }

}

const mapStateToProps = (state) => {
    return {
        selectedSettings: state.settings.selectedSettings
    }
};

const withConnect = connect(mapStateToProps, {});

export default compose(withTranslation(), withRouter, withConnect)(Settings);