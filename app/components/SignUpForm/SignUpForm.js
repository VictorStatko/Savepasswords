import React, {Component} from 'react';
import FormUserDetails from "./FormUserDetails";
import FormPassword from "./FormPassword";

class SignUpForm extends Component {
    state = {
        step: 1,
        name: "",
        email: "",
        password: ""
    };

    nextStep = () => {
        const {step} = this.state;
        this.setState({
            step: step + 1
        });
    };

    handleChange = (input, value) => {
        this.setState({[input]: value});
    };

    onSubmit = (e) => {
        e.preventDefault();
        console.log(this.state.name + "-" + this.state.email + "-" + this.state.password);
    };

    renderMultiStepForm = (step) => {
        switch (step) {
            case 1:
                return (
                    <FormUserDetails
                        nextStep={this.nextStep}
                        handleChange={this.handleChange}
                        name={this.state.name}
                        email={this.state.email}
                    />
                );
            case 2:
                return (
                    <FormPassword
                        handleChange={(this.handleChange)}
                        password={this.state.password}
                    />
                );
            default:
                return null;
        }
    };


    render() {
        const {step} = this.state;
        return (
            <form onSubmit={this.onSubmit}>
                {this.renderMultiStepForm(step)}
            </form>
        )
    }
}

SignUpForm.propTypes = {};

export default SignUpForm;
