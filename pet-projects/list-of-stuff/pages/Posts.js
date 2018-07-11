import React from 'react';
import ReactDOM from 'react-dom';

class Posts extends React.Component {
    constructor(props) {
        super(props);
        this.state = {post: "Kittens"};
    }

    // After component render
    // Saves timer id to clear
    componentDidMount() {
        this.timerID = setInterval( 
            () => this.tick(),
            1000
        );
    }
    // After component unmount
    // Clear that ID
    componentWillUnmount() {
        clearInterval(this.timerID);
    }

    tick() {
        let s = new Date();
        this.setState((prevState, props) => ({
            post: "Time at "+s.getSeconds()
        }));
    }
    render() {
        return (
            <div>
                <h1> Smash {this.state.post} </h1>
            </div>
            )
    }
}

module.exports = Posts;