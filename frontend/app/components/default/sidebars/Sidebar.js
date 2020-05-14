import React from 'react';
import Icon from "../icons";
import styles from "./Sidebar.module.scss";
import logo from "images/logo-without-text.png";
import history from "utils/history";
import {withTranslation} from "react-i18next";

class Sidebar extends React.Component {
    state = {
        showSidebar: false,
    };

    componentDidMount() {
        document.onclick = function (event) {
            if (event.pageX > 250){
                document.getElementById('nav').classList.remove(styles.opened);
            }
        }
    }

    componentWillUnmount() {
        document.onclick = null;
    }

    toggleSidebar = () => {
        document.getElementById('nav').classList.toggle(styles.opened);
        this.setState({showSidebar: !this.state.showSidebar});
    };

    render() {
        const {t} = this.props;

        return (
            <React.Fragment>
                <Icon name='menu' styles={styles.toggleButton} onClick={this.toggleSidebar}/>
                <div id='nav' className={styles.sidebar}>
                    <img src={logo} className={styles.logo} onClick={() => history.push('/accounts')}/>
                    <span className={styles.closebtn} onClick={this.toggleSidebar}>&times;</span>
                    <a onClick={() => history.push('/accounts')}>  <Icon name='password' styles={styles.icon}/> {t('sidebar.item.accounts')}</a>
                    <a onClick={() => history.push('/settings')}>  <Icon name='settings' styles={styles.icon}/> {t('sidebar.item.settings')}</a>
                </div>
            </React.Fragment>
        );
    }

}

export default withTranslation()(Sidebar);