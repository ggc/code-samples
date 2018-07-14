import React from 'react';
import { StyleSheet, Text, View } from 'react-native';
import Footer from './pages/Footer';
import LoginPage from './pages/login/login-page';

export default class App extends React.Component {
    render() {
        const date = new Date();
        return (
            <View style={styles.container}>
                <LoginPage />
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
