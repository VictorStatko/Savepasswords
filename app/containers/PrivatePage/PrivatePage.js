import React from 'react';
import Header from "components/Private/Header";

class PrivatePage extends React.Component {

    render() {
        const {component} = this.props;
        return (
            <div>
                <Header/>
                {component}
            </div>
        );
    }

}

export default PrivatePage;


