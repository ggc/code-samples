import React from 'react';
import ReactDOM from 'react-dom';
import { Provider } from 'react-redux';
import { createStore } from 'redux';

import changeText from './actions/index.js';
import reducer from './reducers/index.js';
import store from './stores/index.js';

import ChangingHeader from './containers/changingHeader.js';
import Footer from './components/Footer.jsx';

import { Button, Badge } from 'reactstrap';
import 'bootstrap/dist/css/bootstrap.css';
// Add sass here and webpack loader
require('./public/stylesheets/bootstrap.css');

class ReactReduxMessyExample extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            header: 'Nothin here',
            input: 'Empty'
        }
        this.handleClick = this.handleClick.bind(this);
        this.handleInput = this.handleInput.bind(this);
    }

    handleClick() {
        store.dispatch(changeText(this.state.input));
        console.log('handleClick', store.getState());
    }

    handleInput(text) {
        this.setState((prevState, props) => ({ input: text }));
    }

    render() {
        return (
            <Provider store={store}>
                <div>
                    <h1>{this.state.header}</h1>
                    <ChangingHeader reduxText='Get your shit together' />
                    <Button color="primary" onClick={this.handleClick}>Save!</Button>{' '}
                    <Footer onTextWritten={this.handleInput} />
                </div>
            </Provider>
        )
    }
}
 

const phrase = ['...and ', 'theeeeere ', 'we go~!'];
ReactDOM.render(<ReactReduxMessyExample phrase={phrase} />, document.getElementById('root'));