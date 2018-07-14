import React from 'react';
import { StyleSheet, TextInput, View, Button, Alert, Text } from 'react-native';

export default class LoginPage extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            username: '',
            password: ''
        };
        this._onLogin = this._onLogin.bind(this);
    }

    _onLogin() {
        Alert.alert('Successful logged in', 'Now get the fuck out of here!');
        console.log('[LOG] this in _onLogin', this);
        this.props.navigation.navigate('Home');
    }

    render() {
        return (
            <View>
                <TextInput
                    style={styles.textInput}
                    placeholder="Username"
                    onChangeText={(text) => {
                        console.log('[LOG] this inline', this);
                        return this.setState({
                            username: text
                        })
                    }} />
                <TextInput
                    style={styles.textInput}
                    placeholder="Password"
                    onChangeText={(text) => this.setState({
                        password: text
                    })} />
                <Button
                    onPress={this._onLogin}
                    title="Login"
                    color="#61f298" />
            </View>
        );
    }
}


const styles = StyleSheet.create({
    textInput: {
        width: 200,
        height: 50
    },
});
