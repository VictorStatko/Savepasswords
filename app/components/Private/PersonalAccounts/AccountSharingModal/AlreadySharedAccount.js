import React from 'react';
import {withTranslation} from "react-i18next";
import {Col, Row} from "react-bootstrap";
import {Button} from "components/default/buttons/Button/Button";
import Icon from "components/default/icons";
import styles from "./AccountSharingModal.module.scss";
import {connect} from "react-redux";
import {compose} from "redux";
import * as PropTypes from "prop-types";
import {personalAccountsOperations} from "ducks/personalAccounts";
import {Spinner} from "components/default/spinner/Spinner";
import {toast} from "react-toastify";


class AlreadySharedAccount extends React.Component {
    state = {
        loading: false
    };

    onRemoveButtonClick = async () => {
        const {t, parentAccount, sharedAccount, removeSharing} = this.props;
        try {
            this.setState({loading: true});
            await removeSharing(parentAccount, sharedAccount);
            toast.success(t('personalAccounts.sharingRemoveSuccess'));
        }  finally {
            this.setState({loading: false});
        }
    }

    render() {
        const {parentAccount, sharedAccount, index} = this.props;
        const {loading} = this.state;
        return (
            <React.Fragment>
                <Row>
                    <Col xs={9} lg={10}
                         className={`d-flex justify-content-start align-items-center ${styles.leftColumn}`}>
                        <div>{sharedAccount.ownerEmail}</div>
                    </Col>
                    <Col xs={3} lg={2}
                         className={`d-flex justify-content-end align-items-center ${styles.rightColumn}`}>
                        <Button content={
                            loading ? <Spinner className={styles.spinner}/> :
                                <Icon name='delete' styles={styles.buttonIcon}/>
                        }
                                onClick={this.onRemoveButtonClick}/>
                    </Col>
                </Row>
                {parentAccount.sharedAccounts[index + 1] ? <hr/> : null}
            </React.Fragment>
        );
    }

}

const withConnect = connect(null, {
    removeSharing: personalAccountsOperations.removePersonalAccountSharing
});

export default compose(withTranslation(), withConnect)(AlreadySharedAccount);

AlreadySharedAccount.propTypes = {
    sharedAccount: PropTypes.object.isRequired,
    parentAccount: PropTypes.object.isRequired,
    index: PropTypes.number.isRequired
};