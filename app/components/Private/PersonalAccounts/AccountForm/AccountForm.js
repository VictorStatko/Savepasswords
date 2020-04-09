import React from 'react';
import {withTranslation} from "react-i18next";
import TextInput from "components/default/inputs/TextInput";
import styles from "./AccountForm.module.scss";
import {Col, Row} from "react-bootstrap";
import TextArea from "components/default/inputs/TextArea";
import * as PropTypes from "prop-types";
import CustomCreatableSelect from "components/default/inputs/CustomCreatableSelect";
import {connect} from "react-redux";
import {compose} from "redux";
import {isNotEmpty} from "utils/stringUtils";

class AccountForm extends React.Component {

    handleFolderCreate = async (name) => {
        const folder = await this.props.handleFolderCreate(name);
        return {value: folder.uuid, label: folder.name};
    };

    renderFolderSelect = () => {
        const {folders, disabled, folderError} = this.props;
        const options = folders
            .filter(folder => isNotEmpty(folder.uuid))
            .map(folder => {
                    return {value: folder.uuid, label: folder.name}
                }
            );

        const value = this.props.account.folderUuid
            ? {
                value: this.props.account.folderUuid,
                label: this.props.folders.find(folder => folder.uuid === this.props.account.folderUuid).name
            }
            : null;

        return <CustomCreatableSelect options={options} value={value} onChange={this.onSelectChange}
                                      readOnly={disabled} error={folderError} onCreate={this.handleFolderCreate}/>
    };

    onSelectChange = (newOption) => {
        this.props.handleChange('folderUuid', newOption && newOption.value ? newOption.value : null);
    };

    render() {
        const {t} = this.props;
        const {account, urlError, nameError, serverError, handleChange, disabled} = this.props;
        return (
            <React.Fragment>
                <TextInput id="url" label={t('personalAccounts.modal.form.url')} error={urlError} readOnly={disabled}
                           value={account.url} onChange={e => handleChange('url', e.target.value)}/>
                <TextInput id="name" label={t('personalAccounts.modal.form.name')} readOnly={disabled}
                           value={account.name} className={styles.textInput} error={nameError}
                           onChange={e => handleChange('name', e.target.value)}/>
                <Row>
                    <Col md={6}>
                        <TextInput id="username" label={t('personalAccounts.modal.form.username')} readOnly={disabled}
                                   value={account.username} className={styles.textInput}
                                   onChange={e => handleChange('username', e.target.value)}/>
                    </Col>
                    <Col md={6}>
                        <TextInput id="password" secret label={t('personalAccounts.modal.form.password')} readOnly={disabled}
                                   value={account.password} className={styles.textInput}
                                   onChange={e => handleChange('password', e.target.value)}/>
                    </Col>
                </Row>
                {this.renderFolderSelect()}
                <TextArea id="description" label={t('personalAccounts.modal.form.description')} readOnly={disabled}
                          value={account.description} className={styles.textInput} textarea
                          onChange={e => handleChange('description', e.target.value)}/>

                <div className={styles.serverError}>{serverError}</div>
            </React.Fragment>
        );
    }

}


const mapStateToProps = (state) => {
    return {
        folders: state.personalAccountFolders.folders
    }
};

const withConnect = connect(mapStateToProps, null);

export default compose(withTranslation(), withConnect)(AccountForm);

AccountForm.propTypes = {
    handleChange: PropTypes.func.isRequired,
    handleFolderCreate: PropTypes.func.isRequired,
    account: PropTypes.object.isRequired,
    urlError: PropTypes.string,
    nameError: PropTypes.string,
    serverError: PropTypes.string,
    folderError: PropTypes.string,
    disabled: PropTypes.bool
};