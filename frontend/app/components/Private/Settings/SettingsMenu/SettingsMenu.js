import React from 'react';
import {connect} from "react-redux";
import {compose} from "redux";
import {withTranslation} from "react-i18next";
import {withRouter} from "react-router-dom";
import styles from "./SettingsMenu.module.scss";
import {SETTING_TYPES} from "utils/appConstants";
import {settingsOperations} from "ducks/settings";

class SettingsMenu extends React.Component {

    render() {
        const {t, selectedSettings, selectSetting} = this.props;

        const listItems = SETTING_TYPES.map((settingType, i) =>
            <div className={styles.itemContainer} key={i} onClick={() => selectSetting(settingType)}>
                <div className={styles.item}>
                    <span
                        className={selectedSettings === settingType ? styles.dot : `${styles.dot} ${styles.dotNotActive}`}/>
                    <h3>{t('settings.menu.item.' + settingType)}</h3>
                </div>
            </div>
        );

        return (
            <div className={styles.container}>
                {listItems}
            </div>
        );
    }

}

const mapStateToProps = (state) => {
    return {
        selectedSettings: state.settings.selectedSettings
    }
};

const withConnect = connect(mapStateToProps, {
    selectSetting: settingsOperations.selectSetting
});

export default compose(withTranslation(), withRouter, withConnect)(SettingsMenu);