import React from 'react';
import ReactDOM from 'react-dom';

class Footer extends React.Component {
    constructor(props) {
        super(props);
        this.handleChange = this.handleChange.bind(this);
    }

    // When input changes, handleChange fires and
    // the onTextWritten handler my father gave me
    // fires with my text. Too much callback-like 
    handleChange(e) {
        this.props.onTextWritten(e.target.value);
    }

    render() {
        return (
            <div>
                <p> {this.props.options}</p>
                <input onChange={this.handleChange} />
            </div>
            )
    }
}

module.exports = Footer;