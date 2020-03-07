import React from 'react';
import {Col, Row} from "react-bootstrap";
import styles from "./PersonalAccounts.module.scss";
import PrimaryButton from "components/default/buttons/PrimaryButton";

class PersonalAccounts extends React.Component {

    render() {
        return (
            <React.Fragment>
                <Row className={styles.headerRow}>
                    <Col md={6}><h1 className={styles.header}>Accounts</h1></Col>
                    <Col md={6} className="d-flex justify-content-center">  <PrimaryButton text="qwerty"/></Col>
                </Row>
                <hr/>
            </React.Fragment>
        );
    }

}

export default PersonalAccounts;