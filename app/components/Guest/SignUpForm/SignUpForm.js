import React, {Component} from 'react';
import FormUserDetails from "./FormUserDetails";
import FormPassword from "./FormPassword";
import styles from "./SignUpForm.module.scss";
import {connect} from "react-redux";
import {trySignUp} from "ducks/account/actions";
import {compose} from "redux";
import {withTranslation} from "react-i18next";
import {Link} from "react-router-dom";

class SignUpForm extends Component {
    state = {
        step: 1,
        name: "",
        email: "",
        password: ""
    };

    nextStep = () => {
        const {step} = this.state;
        this.setState({
            step: step + 1
        });
    };

    previousStep = () => {
        const {step} = this.state;
        this.setState({
            step: step - 1
        });
    };

    handleChange = (input, value) => {
        this.setState({[input]: value});
    };

    onSubmit = async e => {
        e.preventDefault();
        await this.props.trySignUp({
            email: this.state.email,
            name: this.state.name,
            password: this.state.password
        });
    };

    renderMultiStepForm = (step) => {
        switch (step) {
            case 1:
                return (
                    <FormUserDetails
                        nextStep={this.nextStep}
                        handleChange={this.handleChange}
                        name={this.state.name}
                        email={this.state.email}
                    />
                );
            case 2:
                return (
                    <FormPassword
                        previousStep={this.previousStep}
                        handleChange={(this.handleChange)}
                        password={this.state.password}
                    />
                );
            default:
                return null;
        }
    };


    render() {
        const {t} = this.props;
        const {step} = this.state;

        return (
            <React.Fragment>
                <form onSubmit={this.onSubmit}>
                    {this.renderMultiStepForm(step)}
                </form>
                <div className={styles.changePageLink}>
                    <Link to={'/sign-in'}>{t('signUp.alreadyRegisteredLink')}</Link>
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
        trySignUp: trySignUp
    }
);

export default compose(withTranslation(), withConnect)(SignUpForm);
