import React, {Component} from 'react';
import FormUserDetails from "./FormUserDetails";
import styles from "./SignUpForm.module.scss";
import {connect} from "react-redux";
import {accountOperations} from "ducks/account";
import {compose} from "redux";
import {withTranslation} from "react-i18next";
import {Link} from "react-router-dom";
import {toast} from 'react-toastify';
import i18n from "i18n";
import history from "utils/history";
import {setStateAsync} from "utils/stateUtils";

class SignUpForm extends Component {
    state = {
        email: "",
        password: "",
        loading: false
    };

    handleChange = (input, value) => {
        this.setState({[input]: value});
    };

    onSubmit = async e => {
        e.preventDefault();

        await setStateAsync(this, {loading: true});
        try {
            await this.props.trySignUp({
                email: this.state.email,
                password: this.state.password,
            });
        } finally {
            await setStateAsync(this, {loading: false});
        }

        toast.success(i18n.t('signUp.success'));
        history.push('/sign-in');
    };

    render() {
        const {t} = this.props;

        return (
            <React.Fragment>
                <form onSubmit={this.onSubmit}>
                    <FormUserDetails
                        handleChange={this.handleChange}
                        password={this.state.password}
                        email={this.state.email}
                        loading={this.state.loading}
                    />
                </form>
                <div className={styles.changePageLink}>
                    <Link to={'/sign-in'}>{t('signUp.alreadyRegisteredLink.part1')}<br/>{t('signUp.alreadyRegisteredLink.part2')}</Link>
                </div>
            </React.Fragment>
        )
    }
}

SignUpForm.propTypes = {};

const mapStateToProps = (state) => {
    return {
        account: state.account
    }
};

const withConnect = connect(
    mapStateToProps,
    {
        trySignUp: accountOperations.trySignUp
    }
);

export default compose(withTranslation(), withConnect)(SignUpForm);
