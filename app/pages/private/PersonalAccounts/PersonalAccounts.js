import React from 'react';
import {Col, Row} from "react-bootstrap";
import styles from "./PersonalAccounts.module.scss";
import ButtonRow from "components/Private/PersonalAccounts/ButtonRow";
import {withTranslation} from "react-i18next";

class PersonalAccounts extends React.Component {

    render() {
        const {t} = this.props;

        return (
            <React.Fragment>
                <Row className={styles.headerRow}>
                    <Col><h1 className={styles.header}>{t('personalAccounts.header')}</h1></Col>
                </Row>
                <Row>
                    <Col className="d-flex justify-content-center justify-content-md-end">
                        <ButtonRow/>
                    </Col>
                </Row>

                <hr/>
            </React.Fragment>
        );
    }

}

export default withTranslation()(PersonalAccounts);