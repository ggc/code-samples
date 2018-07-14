import React from 'react';
import { StyleSheet, Text, View } from 'react-native';
import Footer from './pages/Footer';
// import { applyMiddleware } from "redux";
// import { Provider, connect } from "react-redux";
// import { createStore } from 'redux';

// import reducer from "./reducers/reducer";
// import getStore from "./pages/state/reducer";
// import 'es6-symbol/implement';
// const store = createStore(reducer);
// const store = getStore();
function reducer(state, action) {
    return state;
}
// var store = createStore(reducer);

export default class App extends React.Component {
    render() {
        const date = new Date();
        return (
                <View style={styles.container}>
                    <Text style={styles.lightBlue}>Header</Text>
                    <Text>{date.toString()}</Text>
                    <Text>Open up App.js to start working on your app!</Text>
                    <Text>Live reload ON!.</Text>
                    <Text>Shake (twice...) your phone to open the developer menu.</Text>
                    <Footer />
                </View>
        );
    }
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#fff',
        alignItems: 'center',
        justifyContent: 'center',
    },
    lightBlue: {
        backgroundColor: '#335599',
        width: 400,
        paddingTop: 48
    }
});
