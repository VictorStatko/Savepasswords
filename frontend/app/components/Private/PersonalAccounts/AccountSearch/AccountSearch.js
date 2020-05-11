import React, {Component} from 'react';
import TextInput from "components/default/inputs/TextInput";
import styles from "./AccountSearch.module.scss";
import {connect} from "react-redux";
import {personalAccountsOperations} from "ducks/personalAccounts";
import {compose} from "redux";
import {withTranslation} from "react-i18next";

class AccountSearch extends Component {
    render() {
        const {t, changeSearch, search} = this.props;

        return (
            <TextInput id="accountsSearch" className={styles.container} placeholder={t('global.search')}
                       value={search} onChange={e => changeSearch(e.target.value)}/>
        );
    }
}

const mapStateToProps = (state) => {
    return {
        search: state.personalAccounts.pagination.search
    }
};

const withConnect = connect(mapStateToProps, {
    changeSearch: personalAccountsOperations.changeSearch
});

export default compose(withTranslation(), withConnect)(AccountSearch);
