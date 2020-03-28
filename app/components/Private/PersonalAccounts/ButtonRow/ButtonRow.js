import React from 'react';
import {withTranslation} from "react-i18next";
import Icon from "components/default/icons";
import styles from "./ButtonRow.module.scss";
import AccountModal from "../AccountModal";
import {Button} from "components/default/buttons/Button/Button";

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
                    <Button content={<Icon name='add' styles={styles.buttonIcon}/>}
                            customStyle={styles.button} onClick={this.handleShow}/>
                    {this.state.show ? <AccountModal close={this.handleClose}/> : null}
                </div>
            </React.Fragment>
        );
    }

}

export default withTranslation()(ButtonRow);