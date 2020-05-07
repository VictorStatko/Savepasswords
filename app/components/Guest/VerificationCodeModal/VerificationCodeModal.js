import React from 'react';
import {withTranslation} from "react-i18next";
import {Modal} from "react-bootstrap";
import {Button, DeclineButton, PrimaryButton} from "components/default/buttons/Button/Button";
import styles from "./VerificationCodeModal.module.scss";
import {compose} from "redux";
import Radio from "components/default/radio";
import TextInput from "components/default/inputs/TextInput";
import Icon from "components/default/icons";
import {connect} from "react-redux";
import {accountOperations} from "ducks/account";
import {toast} from "react-toastify";
import history from "utils/history";
import {isEmpty} from "utils/stringUtils";
import {isEmailValid, isStringMaxLengthValid, MAX_LENGTH_EMAIL} from "utils/validationUtils";

const LENGTH_VERIFICATION_CODE = 8;

const OPTIONS = {
    RESEND_CODE: 'RESEND_CODE',
    ENTER_CODE: 'ENTER_CODE'
}

const INITIAL_STATE = {
    selectedOption: null,
    selectedOptionForRenderForm: null,
    verificationCode: "",
    verificationCodeError: "",
    email: "",
    emailError: "",
    serverError: "",
    loading: false
};

//TODO refactoring
//Not nice design
class VerificationCodeModal extends React.Component {

    state = Object.assign({}, INITIAL_STATE);

    handleRadioChange = e => {
        const {value} = e.target;

        this.setState({selectedOption: value});

        //for better display radio to user - first make radio active, and only after that page change
        setTimeout(() => {
            this.setState({selectedOptionForRenderForm: value});
        }, 150);
    };

    handleVerificationCodeChange = e => {
        this.setState({
            verificationCode: e.target.value,
            verificationCodeError: ""
        });
    };

    handleEmailChange = e => {
        this.setState({
            email: e.target.value,
            emailError: ""
        });
    };

    onVerificationCodeSubmit = async () => {
        if (!this.validateVerificationCode()) {
            return;
        }

        const {t} = this.props;

        try {
            this.setState({loading: true});

            await this.props.confirmVerification(this.state.verificationCode, true);

            history.push('/sign-in');
            toast.success(t('signUp.confirmationSuccess'));
        } catch (error) {
            this.setState({
                loading: false
            });

            if (error && error.message && error.message === 'showOnForm') {
                this.setState({
                    serverError: error.messageTranslation
                });
            }
        }
    };

    onResendSubmit = async () => {
        if (!this.validateEmail()) {
            return;
        }

        const {t} = this.props;

        try {
            this.setState({
                loading: true
            });

            await this.props.resendCode(this.state.email);

            this.props.close();
            toast.success(t('signUp.resendCodeSuccess'));
        } catch (error) {
            this.setState({
                loading: false
            });

            if (error && error.message && error.message === 'showOnForm') {
                this.setState({
                    serverError: error.messageTranslation
                });
            }
        }
    };

    validateVerificationCode = () => {
        const {t} = this.props;
        const {verificationCode} = this.state;

        let valid = true;

        if (!verificationCode || verificationCode.length !== LENGTH_VERIFICATION_CODE) {
            this.setState({
                verificationCodeError: t('global.validation.exactLength', {exactLength: LENGTH_VERIFICATION_CODE})
            });
            valid = false;
        }

        return valid;
    };

    validateEmail = () => {
        const {t} = this.props;
        const {email} = this.state;

        let valid = true;

        if (isEmpty(email)) {
            this.setState({
                emailError: t('global.validation.notEmpty')
            });
            valid = false;
        } else if (!isEmailValid(email)) {
            this.setState({
                emailError: t('global.validation.email')
            });
            valid = false;
        } else if (!isStringMaxLengthValid(email, MAX_LENGTH_EMAIL)) {
            this.setState({
                emailError: t('global.validation.maxLength', {maxLength: MAX_LENGTH_EMAIL})
            });
            valid = false;
        }

        return valid;
    };

    renderRadio = () => {
        const {t} = this.props;

        return (
            <div>
                <Radio value={OPTIONS.ENTER_CODE}
                       label={t('verificationCodeModal.option.enter')}
                       checked={this.state.selectedOption === OPTIONS.ENTER_CODE}
                       name="selectedOption"
                       onChange={this.handleRadioChange}
                />
                <Radio value={OPTIONS.RESEND_CODE}
                       label={t('verificationCodeModal.option.text')}
                       checked={this.state.selectedOption === OPTIONS.RESEND_CODE}
                       name="selectedOption"
                       onChange={this.handleRadioChange}
                />
            </div>
        )
    };

    renderSelectedForm = () => {
        if (this.state.selectedOptionForRenderForm === OPTIONS.ENTER_CODE) {
            return this.renderFormForManualSendVerificationCode();
        } else if (this.state.selectedOptionForRenderForm === OPTIONS.RESEND_CODE) {
            return this.renderFormForResendVerificationCode();
        }
    };

    onSubmitButtonClick = async () => {
        if (this.state.selectedOptionForRenderForm === OPTIONS.ENTER_CODE) {
            await this.onVerificationCodeSubmit();
        } else if (this.state.selectedOptionForRenderForm === OPTIONS.RESEND_CODE) {
            await this.onResendSubmit();
        }
    };

    onBackButtonClick = () => {
        this.setState(Object.assign({}, INITIAL_STATE));
    };

    renderFormForManualSendVerificationCode = () => {
        const {t} = this.props;

        return <div className={styles.body}>
            <div className={styles.text}>{t('verificationCodeModal.enter.text')}</div>
            <TextInput id="verificationCode" placeholder={t('verificationCodeModal.enter.placeholder')}
                       value={this.state.verificationCode} error={this.state.verificationCodeError}
                       onChange={this.handleVerificationCodeChange}/>
            <input type="text" style={{display: 'none'}}/>
            <div className={styles.serverError}>{this.state.serverError}</div>
        </div>
    };

    renderFormForResendVerificationCode = () => {
        const {t} = this.props;

        return <div className={styles.body}>
            <div className={styles.text}>{t('verificationCodeModal.resend.text')}</div>
            <TextInput id="email" placeholder={t('verificationCodeModal.resend.placeholder')} value={this.state.email}
                       error={this.state.emailError} onChange={this.handleEmailChange}/>
            <input type="text" style={{display: 'none'}}/>
            <div className={styles.serverError}>{this.state.serverError}</div>
        </div>
    };

    render() {
        const {t, close} = this.props;
        const {loading} = this.state;

        return (
            <Modal size={"lg"} show centered backdrop='static'>
                <form onSubmit={this.onSubmit}>
                    <Modal.Header className={styles.header}>
                        <Modal.Title>
                            {t('verificationCodeModal.header')}
                        </Modal.Title>
                        <Button content={<Icon name='close' styles={styles.modalIcon}/>} onClick={close}/>
                    </Modal.Header>
                    <Modal.Body>
                        {this.state.selectedOptionForRenderForm ? this.renderSelectedForm() : this.renderRadio()}
                    </Modal.Body>
                    <Modal.Footer className={styles.footer}>
                        {this.state.selectedOptionForRenderForm ?
                            <PrimaryButton disabled={loading} loading={loading} onClick={this.onSubmitButtonClick}
                                           content={t('global.submit')}/>
                            : null}
                        {this.state.selectedOptionForRenderForm ?
                            <DeclineButton content={t('global.back')} onClick={this.onBackButtonClick}/> : null}
                    </Modal.Footer>
                </form>
            </Modal>
        );
    }

}


const withConnect = connect(null, {
    confirmVerification: accountOperations.confirmVerification,
    resendCode: accountOperations.resendVerificationCode
});

export default compose(withTranslation(), withConnect)(VerificationCodeModal);