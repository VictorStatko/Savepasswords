import React from 'react';
import {compose} from "redux";
import {withTranslation} from "react-i18next";
import dateFormat from "dateformat";
import styles from "./ActivityItem.module.scss";

class ActivityItem extends React.Component {

    render() {
        const {t, item} = this.props;
        return (
            <tr>
                <td>{dateFormat(new Date(item.createdAt), "dddd, HH:MM")}
                    {item.current ? <span className={styles.current}> ({t('settings.security.current')})</span> : null}
                </td>
                <td>{item.systemInfo}</td>
                <td>{item.ipAddress} - {item.geoInfo}</td>
            </tr>
        );
    }

}

export default compose(withTranslation())(ActivityItem);