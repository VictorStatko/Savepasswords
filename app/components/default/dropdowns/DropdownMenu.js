import React, {Component} from 'react';
import * as PropTypes from "prop-types";
import styles from './DropdownMenu.module.scss';
import Icon from "components/default/icons";

class DropdownMenu extends Component {
    state = {
        showMenu: false,
    };

    showMenu = (event) => {
        event.preventDefault();

        this.setState({showMenu: true}, () => {
            document.addEventListener('click', this.closeMenu);
        });
    };

    closeMenu = () => {
        this.setState({showMenu: false}, () => {
            document.removeEventListener('click', this.closeMenu);
        });
    };

    render() {
        const {options, handleChange} = this.props;
        let content;

        if (this.state.showMenu) {
            content =
                <div className={styles.dropdownContent}>
                    {options.map((option, i) => {
                        return (<span key={i} onClick={() => handleChange(option.key)}>{option.name}</span>)
                    })}
                </div>;
        } else {
            content = null;
        }
        return (
            <div className={styles.dropdown}>
                <Icon name='menu' styles={styles.toggleButton} onClick={this.showMenu}/>
                {content}
            </div>
        );
    }
}

DropdownMenu.propTypes = {
    options: PropTypes.array.isRequired,
    handleChange: PropTypes.func.isRequired
};

export default DropdownMenu;