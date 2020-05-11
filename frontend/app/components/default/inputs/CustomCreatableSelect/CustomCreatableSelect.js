import React, {Component} from "react";
import CreatableSelect from 'react-select/creatable';
import styles from './CustomCreatableSelect.module.scss';
import {Spinner} from "components/default/spinner";
import * as PropTypes from "prop-types";
import {isEmpty, isNotEmpty} from "utils/stringUtils";
import {setStateAsync} from "utils/stateUtils";

class CustomCreatableSelect extends Component {
     customStyles = {
        control: (base, state) => ({
            ...base,
            height: '45px',
            minHeight: '45px',
            borderRadius: '8px',
            cursor: 'text',
            border: '0',
            boxShadow: isNotEmpty(this.props.error) ? 'inset 0 0 0 1px rgb(255, 56, 34)' : state.isFocused && !state.isDisabled ? 'inset 0 0 0 1px rgb(8, 106, 255)' : 'inset 0 0 0 1px rgba(0, 0, 0, 0.25)',
            marginTop: '0.2rem !important',
            backgroundColor: state.isDisabled ? 'rgb(247, 247, 247)' : 'inherit'
        }),

        menu: (base) => ({
            ...base,
            margin: '0'
        }),

         dropdownIndicator: (base, state) => ({
             ...base,
             cursor: 'pointer',
             display: state.isDisabled ? 'none' : 'flex'
         }),

         clearIndicator: (base, state) => ({
             ...base,
             cursor: 'pointer'
         }),

        input: (base) => ({
            ...base,
            width: '0',
            border: '0'
        }),
        indicatorSeparator: () => ({
            display: 'none',
        }),
        valueContainer: (base) => ({
            ...base,
            height: '45px',
            minHeight: '45px',
            padding: '0.6rem 1.2rem;'
        }),

        placeholder: (base) => ({
            ...base,
            visibility: 'hidden'
        }),

        singleValue: (base) => ({
            ...base,
            color: 'black'
        }),
    };

    state = {
        isLoading: false
    };

    handleCreate = async (inputValue) => {
        await setStateAsync(this, {isLoading: true});
        try {
           const newOption = await this.props.onCreate(inputValue);
            this.handleChange(newOption);
        } catch (e) {
            console.error(e);
        }finally {
            await setStateAsync(this, {isLoading: false});
        }
    };

    handleChange = (newOption) => {
        this.props.onChange(newOption);
    };

    render() {
        const {isLoading} = this.state;
        const {options, value, readOnly, error} = this.props;

        return (
            <div className={styles.selectContainer}>
                <span className={styles.labelText}>Folder</span>
                <CreatableSelect styles={this.customStyles}
                                 isClearable
                                 isDisabled={readOnly || isLoading}
                                 isLoading={isLoading}
                                 onChange={this.handleChange}
                                 onCreateOption={this.handleCreate}
                                 options={options}
                                 value={value}
                                 components={{LoadingIndicator: () => <Spinner className={styles.loadingSpinner}/>}}/>
                {isEmpty(error) ? null : <div className={styles.errorText}>{error}</div>}
            </div>

        );
    };
}

export default CustomCreatableSelect;

CustomCreatableSelect.propTypes = {
    onCreate: PropTypes.func.isRequired,
    onChange: PropTypes.func.isRequired,
    options: PropTypes.array.isRequired,
    value: PropTypes.object,
    readOnly: PropTypes.bool,
    error: PropTypes.string
};

CustomCreatableSelect.defaultProps = {
    readOnly: false
};