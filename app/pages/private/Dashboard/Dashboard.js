import React from 'react';
import PrivatePage from "containers/PrivatePage";

class Dashboard extends React.Component {

    render() {
        return (
            <div>
                <PrivatePage component={<div></div>}/>
            </div>
        );
    }

}

export default Dashboard;