import React from 'react';
import styles from "./Header.module.scss";
import {connect} from "react-redux";
import {compose} from "redux";
import {accountOperations} from "ducks/account";
import {withTranslation} from "react-i18next";
import DropdownMenu from "components/default/dropdowns";
import Icon from "components/default/icons";
import {ellipsisByCharactersCount} from "utils/stringUtils";
import Sidebar from "components/default/sidebars";
import LoadingBar from "react-redux-loading-bar";

class Header extends React.Component {

    onDropdownValueSelected = (key) => {
        switch (key) {
            case 'logout':
                this.props.signOut();
                break;
        }
    };

    render() {
        const {account, t, i18n} = this.props;

        const dropdownOptions = [{
            key: 'logout',
            name:
                <React.Fragment>
                    <Icon name='logout' styles={styles.dropdownIcon}/>{t('profile.dropdownItem.signOut')}
                </React.Fragment>
        }];

        return (
            <React.Fragment>
                <LoadingBar style={{ backgroundColor: 'rgb(8, 106, 255)', height: '2px' }} updateTime={150}/>
                <header className={styles.header}>
                    <div className={styles.menu}><Sidebar/></div>
                    <div className={styles.right}>
                        <div className={styles.language}>
                            <Icon name='uk' styles={styles.languageIcon} onClick={async () => {await i18n.changeLanguage('en')}}/>
                            <Icon name='rus' styles={styles.languageIcon} onClick={async () => {await i18n.changeLanguage('ru')}}/>
                        </div>
                        <span className="d-sm-none">
                            {ellipsisByCharactersCount(account.email, 15)}
                        </span>
                        <span className="d-none d-sm-inline">
                            {account.email}
                        </span>
                        <span className={styles.dropdownIconWrapper}>
                        <DropdownMenu options={dropdownOptions} handleChange={this.onDropdownValueSelected}/>
                    </span>
                    </div>
                </header>
            </React.Fragment>
        );
    }

}

const mapStateToProps = (state) => {
    return {
        account: state.account
    }
};

const withConnect = connect(mapStateToProps, {
    signOut: accountOperations.signOut
});

export default compose(withTranslation(), withConnect)(Header);


