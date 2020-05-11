import React from 'react';
import {withTranslation} from "react-i18next";
import TextInput from "components/default/inputs/TextInput";
import styles from "./FolderForm.module.scss";
import * as PropTypes from "prop-types";

class FolderForm extends React.Component {

    render() {
        const {t} = this.props;
        const {folder, nameError, serverError, handleChange} = this.props;
        return (
            <React.Fragment>
                <TextInput id="name" label={t('personalAccounts.modal.form.name')}
                           value={folder.name} error={nameError}
                           onChange={e => handleChange('name', e.target.value)}/>

                <div className={styles.serverError}>{serverError}</div>
            </React.Fragment>
        );
    }

}

export default withTranslation()(FolderForm);

FolderForm.propTypes = {
    handleChange: PropTypes.func.isRequired,
    folder: PropTypes.object.isRequired,
    nameError: PropTypes.string,
    serverError: PropTypes.string
};