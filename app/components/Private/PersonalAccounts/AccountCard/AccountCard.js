import React from 'react';
import {withTranslation} from "react-i18next";
import styles from "./AccountCard.module.scss";
import noImage from 'images/no-image-available.png';
import {Col, Row} from "react-bootstrap";
import {Button} from "components/default/buttons/Button/Button";
import Icon from "components/default/icons";
import {isEmpty, removeProtocols} from "utils/stringUtils";

class AccountCard extends React.Component {

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

        const {url, name} = this.props.account;

        const imageSrc = isEmpty(url) ? noImage : `//logo.clearbit.com/${url}`;
        const nameDiv = isEmpty(name) ? null : <div className={styles.urlName}>{name}</div>;
        const urlDiv = isEmpty(url) ? null : <div className={styles.urlName}>{removeProtocols(url)}</div>;

        const hrefClass = isEmpty(url) ? styles.imageHrefNotActive : styles.imageHrefActive;
        return (
            <div className={styles.card}>
                <Row>
                    <Col xs={4}>
                        <a href={`//${url}`} target="_blank" className={hrefClass} onClick={this.onHrefClick}>
                            <img className={styles.image} onDragStart={this.preventDragHandler}
                                 src={imageSrc} onError={e => this.onImageNotFound(e)}/>
                        </a>
                    </Col>
                    <Col xs={8} className="d-flex justify-content-end">
                        <Button customStyle={styles.button} content={<Icon name='key' styles={styles.buttonIcon}/>}/>
                        <Button customStyle={styles.button} content={<Icon name='delete' styles={styles.buttonIcon}/>}/>
                    </Col>
                </Row>
                <hr/>
                {nameDiv}
                {urlDiv}
            </div>
        );
    }

}

export default withTranslation()(AccountCard);