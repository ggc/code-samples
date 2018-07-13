import React from 'react';
import ReactDOM from 'react-native';
import Posts from './components/Posts.jsx';
import Footer from './components/Footer.jsx';
import { Button,Badge } from 'reactstrap';
import 'bootstrap/dist/css/bootstrap.css';
require('./public/stylesheets/bootstrap.css');

class App extends React.Component {

    constructor(props) {
        super(props);
        // Class methods are not bound by default!!!
        this.handleClick = this.handleClick.bind(this);
        this.handleInput = this.handleInput.bind(this);
        this.state = {
            toggle: "OFF",
            header: "Heading"
        };
    }

    componentWillMount() {
        console.log('will mount');
    }
    componentDidMount() {
        console.log('did mount');
    }
    componentWillReceiveProps(nextProps) {
        console.log('will receive props');
    }
    shouldComponentUpdate(nextProps, nextState) {
        console.log('should update?');
    }
    componentWillUpdate(nextProps, nextState) {
        console.log('will update');
    }
    componentDidUpdate(prevProps, prevState) {
        console.log('did update');
    }
    componentWillUnmount() {
        console.log('will unmount');
    }

    // This handler pass a new prop to Component (and it renders it)
    handleClick() {
        console.log('Clicked and '+this.state.toggle+'!');
        if(this.state.toggle == "OFF"){
            this.setState((prevState, props) => ({ toggle: "ON" }));
        }else{
            this.setState((prevState, props) => ({ toggle: "OFF" }));
        }
    }

    // This handler is called by its child (Footer on onTextWritten)
    handleInput(text) {
        this.setState((prevState, props) => ({ header: text }));
    }

    render() {
        // Keys help React to know where the changes came from
        const phrase = this.props.phrase;
        const listWords = phrase.map( (word,index) => 
            <h1 key={index}>
                {word}
            </h1>
        );
        return (
            <div>
                <Posts />

                <div>{listWords}</div>
                <h1>{this.state.header} <Badge>New</Badge></h1>
                <Button color="primary" onClick={this.handleClick} >primary</Button>{' '}
                
                <Footer options={this.state.toggle} onClick={this.handleClick} onTextWritten={this.handleInput}/>
            </div>
        )
    }
}
 

const phrase = ['...and ', 'theeeeere ', 'we go~!'];

ReactDOM.render(<App phrase={phrase} />, document.getElementById('root'));