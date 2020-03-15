import React from 'react';
import {withTranslation} from "react-i18next";
import SecondaryButton from "components/default/buttons/SecondaryButton";
import Icon from "components/default/icons";
import styles from "./ButtonRow.module.scss";
import AccountModal from "../AccountModal";

class ButtonRow extends React.Component {
    state = {
        show: false
    };

    handleClose = () => {
        this.setState({show: false});
    };
   handleShow = () => {
       this.setState({show: true});
   };

    render() {

        return (
            <React.Fragment>
                <div>
                    <SecondaryButton content={<Icon name='add' styles={styles.buttonIcon}/>}
                                     customStyle={styles.button} onClick={this.handleShow}/>
                    <AccountModal show={this.state.show} onHide={this.handleClose} new/>
                </div>
            </React.Fragment>
        );
    }

}

export default withTranslation()(ButtonRow);