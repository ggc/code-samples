import React, { Component } from 'react';
import { Platform, StyleSheet, Text, View } from 'react-native';
import LoginPage from './pages/login/login-page';
import HomePage from './pages/home/home-page';
import { createStackNavigator } from 'react-navigation';

const instructions = Platform.select({
    ios: 'Press Cmd+R to reload,\n' + 'Cmd+D or shake for dev menu',
    android:
        'Double tap R on your keyboard to reload,\n' +
        'Shake or press menu button for dev menu',
});

export default createStackNavigator(
    {
        Login: LoginPage,
        Home: HomePage
    },
    {
        initialRouteName: 'Login'
    });

// type Props = {};
// export default class App extends Component<Props> {
//     render() {
//         return (
//             <View style={styles.container}>
//                 <LoginPage />
//                 <Text style={styles.instructions}>{instructions}</Text>
//             </View>
//         );
//     }
// }

// const styles = StyleSheet.create({
//     container: {
//         flex: 1,
//         justifyContent: 'center',
//         alignItems: 'center',
//         backgroundColor: '#F5FCFF',
//     },
//     welcome: {
//         fontSize: 20,
//         textAlign: 'center',
//         margin: 10,
//     },
//     instructions: {
//         marginTop: 8,
//         textAlign: 'center',
//         color: '#333333',
//         marginBottom: 5,
//     },
// });
