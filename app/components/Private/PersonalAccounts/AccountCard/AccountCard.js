import React from 'react';
import {withTranslation} from "react-i18next";
import styles from "./AccountCard.module.scss";
import noImage from 'images/no-image-available.png';
import {Col, Row} from "react-bootstrap";
import {Button} from "components/default/buttons/Button/Button";
import Icon from "components/default/icons";
import {isEmpty, removeProtocols} from "utils/stringUtils";
import AccountRemovingConfirmation from "components/Private/PersonalAccounts/AccountRemovingConfirmation";
import {connect} from "react-redux";
import {personalAccountsOperations} from "ducks/personalAccounts";
import {compose} from "redux";
import AccountModal from "../AccountModal";

class AccountCard extends React.Component {
    state = {
    };

    handleDataModalToggle = () => {
        this.setState({dataModal: !this.state.dataModal});
    };

    handleDeleteModalToggle = () => {
        this.setState({deleteModal: !this.state.deleteModal});
    };

    handleDeleteConfirm = async () => {
        await this.props.removePersonalAccount(this.props.account);
    };

    preventDragHandler = (e) => {
        e.preventDefault();
    };

    onImageNotFound = (e) => {
        e.target.onerror = null;
        e.target.src = noImage;
    };

    onHrefClick = (e) => {
        if (isEmpty(this.props.account.url)) {
            e.preventDefault();
        }
    };

    render() {
        const {deleteModal, dataModal} = this.state;

        const {name} = this.props.account;
        let {url} = this.props.account;
        url = isEmpty(url) ? null : removeProtocols(url);

        const imageSrc = isEmpty(url) ? noImage : `//logo.clearbit.com/${url}`;
        const nameDiv = isEmpty(name) ? null : <div className={styles.urlName}>{name}</div>;
        const urlDiv = isEmpty(url) ? null : <div className={styles.urlName}>{url}</div>;

        const hrefClass = isEmpty(url) ? styles.imageHrefNotActive : styles.imageHrefActive;
        return (
            <React.Fragment>
                <div className={styles.card}>
                    <Row className={styles.imageRow}>
                        <Col xs={4}>
                            <a href={`//${url}`} target="_blank" className={hrefClass} onClick={this.onHrefClick}>
                                <img className={styles.image} onDragStart={this.preventDragHandler}
                                     src={imageSrc} onError={e => this.onImageNotFound(e)}/>
                            </a>
                        </Col>
                        <Col xs={8} className="d-flex justify-content-end align-items-center">
                            <Button customStyle={styles.button}
                                    content={<Icon name='key' styles={styles.buttonIcon}/>}
                                    onClick={this.handleDataModalToggle}/>
                            <Button customStyle={styles.button}
                                    content={<Icon name='delete' styles={styles.buttonIcon}/>}
                                    onClick={this.handleDeleteModalToggle}/>
                        </Col>
                    </Row>
                    <hr/>
                    {nameDiv}
                    {urlDiv}
                </div>
                {deleteModal ?
                    <AccountRemovingConfirmation close={this.handleDeleteModalToggle}
                                                 delete={this.handleDeleteConfirm}
                                                 url={url}
                                                 name={name}/>
                    : null
                }
                {dataModal ?
                    <AccountModal close={this.handleDataModalToggle} account={this.props.account}/>
                    : null
                }
            </React.Fragment>
        );
    }

}

const withConnect = connect(null, {
    removePersonalAccount: personalAccountsOperations.removePersonalAccount
});

export default compose(withTranslation(), withConnect)(AccountCard);
