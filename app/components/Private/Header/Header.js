import React from 'react';
import styles from "./Header.module.scss";
import {connect} from "react-redux";
import {compose} from "redux";
import {signOut} from "ducks/account/actions";
import {withTranslation} from "react-i18next";
import DropdownMenu from "components/default/dropdowns/DropdownMenu";
import Icon from "components/default/icons";
import logo from 'images/logo-without-text.png';
import history from "utils/history";
import {ellipsisByCharactersCount} from "utils/stringUtils";

class Header extends React.Component {

    onDropdownValueSelected = (key) => {
        switch (key) {
            case 'logout':
                this.props.signOut();
                break;
        }
    };

    render() {
        const {account, t} = this.props;
        const dropdownOptions = [{
            key: 'logout',
            name:
                <React.Fragment>
                    <Icon name='logout' styles={styles.dropdownIcon}/>{t('profile.dropdownItem.signOut')}
                </React.Fragment>
        }];

        return (
            <header className={styles.header}>
                <img src={logo} className={styles.logo} alt="logo" onClick={history.push('/')}/>
                <div className={styles.right}>
                    {ellipsisByCharactersCount(account.email, 30)}
                    <span className={styles.dropdownIconWrapper}>
                        <DropdownMenu options={dropdownOptions} handleChange={this.onDropdownValueSelected}/>
                    </span>
                </div>
            </header>
        );
    }

}

const mapStateToProps = (state) => {
    return {
        account: state.account
    }
};

const withConnect = connect(mapStateToProps, {
    signOut: signOut
});

export default compose(withTranslation(), withConnect)(Header);


