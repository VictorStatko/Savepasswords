import React from 'react';
import Header from "components/Private/Header";
import {Container} from "react-bootstrap";
import styles from "./PrivatePage.module.scss";
class PrivatePage extends React.Component {

    render() {
        const {component} = this.props;
        return (
            <div>
                <Header/>
                <Container className={styles.container}>
                    {component}
                </Container>
            </div>
        );
    }

}

export default PrivatePage;


