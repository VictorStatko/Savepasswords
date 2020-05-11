import React from 'react';
import {withTranslation} from "react-i18next";
import * as PropTypes from "prop-types";
import {Modal} from "react-bootstrap";
import {Button, DeclineButton, PrimaryButton} from "components/default/buttons/Button/Button";
import Icon from "components/default/icons";
import styles from "./FolderModal.module.scss";
import {isEmpty} from "utils/stringUtils";
import {isStringMaxLengthValid} from "utils/validationUtils";
import FolderForm from "../FolderForm/FolderForm";
import {setStateAsync} from "utils/stateUtils";
import {toast} from "react-toastify";
import {connect} from "react-redux";
import {personalAccountFoldersOperations} from "ducks/personalAccountFolders";
import {compose} from "redux";
import {isObjectModified} from "utils/objectUtils";

const MAX_LENGTH_NAME = 255;

class FolderModal extends React.Component {
    state = {
        loading: false,
        folder: this.props.folder ? {...this.props.folder} : {}
    };

    handleChange = (input, value) => {
        this.setState({
            folder: {
                ...this.state.folder, [input]: value
            },
            [input + 'Error']: null,
            serverError: null
        });
    };

    onSubmit = async (e) => {
        e.preventDefault();

        if (!this.validate()) {
            return;
        }

        if (this.props.folder) {
            await this.onUpdate();
        } else {
            await this.onCreate();
        }
    };

    onCreate = async () => {
        const {t} = this.props;
        const {folder} = this.state;

        await setStateAsync(this, {loading: true});

        try {
            const createdFolder = await this.props.createFolder({...folder}, true);
            toast.success(t('personalAccountFolders.creationSuccess'));
            this.props.close();
            this.props.selectFolder(createdFolder.uuid);
        } catch (error) {
            await setStateAsync(this, {loading: false});

            if (error && error.message && error.message === 'showOnForm') {
                this.setState({
                    serverError: error.messageTranslation
                });
            } else {
                console.error(error);
            }
        }
    };

    onUpdate = async () => {
        const {t} = this.props;
        const {folder} = this.state;

        if (!isObjectModified(folder, this.props.folder)) {
            toast.success(t('personalAccountFolders.updateSuccess'));
            this.props.close();
        }

        await setStateAsync(this, {loading: true});

        try {
            await this.props.updateFolder({...folder});
            toast.success(t('personalAccountFolders.updateSuccess'));
            this.props.close();
        } catch (error) {
            await setStateAsync(this, {loading: false});

            if (error && error.message && error.message === 'showOnForm') {
                this.setState({
                    serverError: error.messageTranslation
                });
            }
        }
    };

    validate = () => {
        return this.validateName();
    };

    validateName = () => {
        const {t} = this.props;
        const {name} = this.state.folder;

        let valid = true;

        if (isEmpty(name)) {
            this.setState({
                nameError: t('global.validation.notEmpty')
            });
            valid = false;
        } else if (!isStringMaxLengthValid(name, MAX_LENGTH_NAME)) {
            this.setState({
                nameError: t('global.validation.maxLength', {maxLength: MAX_LENGTH_NAME})
            });
            valid = false;
        }

        return valid;
    };

    render() {
        const {t, close} = this.props;
        const {folder, nameError, serverError, loading} = this.state;

        return (
            <Modal show centered size="lg" backdrop='static'>
                <form onSubmit={this.onSubmit}>
                    <Modal.Header>
                        <Modal.Title>
                            {this.props.folder ? t('personalAccountFolders.modal.header.edit') : t('personalAccountFolders.modal.header.new')}
                        </Modal.Title>
                        <Button content={<Icon name='close' styles={styles.modalIcon}/>} onClick={close}/>
                    </Modal.Header>
                    <Modal.Body>
                        <FolderForm folder={folder} nameError={nameError} serverError={serverError}
                                    handleChange={this.handleChange}/>
                    </Modal.Body>
                    <Modal.Footer>
                        <PrimaryButton type="submit" disabled={loading} content={t('global.save')} loading={loading}/>
                        <DeclineButton content={t('global.cancel')} onClick={close}/>
                    </Modal.Footer>
                </form>
            </Modal>
        );
    }

}

const withConnect = connect(null, {
    createFolder: personalAccountFoldersOperations.createFolder,
    updateFolder: personalAccountFoldersOperations.updateFolder,
    selectFolder: personalAccountFoldersOperations.selectFolder
});

export default compose(withTranslation(), withConnect)(FolderModal);

FolderModal.propTypes = {
    close: PropTypes.func.isRequired,
    folder: PropTypes.object,
};
