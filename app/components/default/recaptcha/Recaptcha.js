import React, {Component} from "react";
import Reaptcha from 'reaptcha';
import * as PropTypes from "prop-types";

class Recaptcha extends Component {

    render() {
        const {classRef, onVerify, onLoad, onRender} = this.props;

        return (
            <Reaptcha
                ref={classRef}
                sitekey={process.env.REACT_APP_RECAPTCHA_CLIENT_TOKEN}
                onVerify={onVerify}
                onLoad={onLoad}
                onRender={onRender}
                explicit
            />
        );
    }
}

export default Recaptcha;

Recaptcha.propTypes = {
    classRef: PropTypes.oneOfType([
        PropTypes.func,
        PropTypes.shape({current: PropTypes.instanceOf(Element)})
    ]).isRequired,
    onVerify: PropTypes.func.isRequired,
    onLoad: PropTypes.func.isRequired,
    onRender: PropTypes.func.isRequired
};
