import React from 'react';
import Icon from "../icons";
import styles from "./Sidebar.module.scss";
import logo from "images/logo-without-text.png";
import history from "utils/history";

class Sidebar extends React.Component {
    state = {
        showSidebar: false,
    };

    toggleSidebar = () => {

        this.setState({showSidebar: !this.state.showSidebar});
    };

    render() {

        return (
            <React.Fragment>
                <Icon name='menu' styles={styles.toggleButton} onClick={this.toggleSidebar}/>
                {this.state.showSidebar ?  <div className={styles.sidebar}>
                    <img src={logo} className={styles.logo} onClick={history.push('/')}/>
                    <span className={styles.closebtn} onClick={this.toggleSidebar}>&times;</span>
                    <a href="#">About</a>
                    <a href="#">Services</a>
                    <a href="#">Clients</a>
                    <a href="#">Contact</a>
                </div> : null }
            </React.Fragment>
        );
    }

}

export default Sidebar;