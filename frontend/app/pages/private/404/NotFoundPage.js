import React, {Component} from 'react';
import NoFound from "components/default/noFound";
import styles from "./NotFoundPage.module.scss";

class NotFoundPage extends Component {

    render() {

        return (
            <div className={styles.container}>
                <NoFound/>
            </div>
        );
    }
}


export default NotFoundPage;