import React from 'react';
import { StyleSheet, TextInput, View, Button } from 'react-native';

export default class LoginPage extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            form: {
                username: '',
                password: ''
            }
        };
    }
    onLogin(ev) {
        console.log('Login in...', ev);
    }

    render() {
        return (
            <View>
                <TextInput
                    style={styles.textInput}
                    placeholder="Username"
                    onChangeText={(text) => this.setState({
                        form: {
                            username: text
                        }
                    })} />
                <TextInput
                    style={styles.textInput}
                    placeholder="Password"
                    onChangeText={(text) => this.setState({
                        form: {
                            password: text
                        }
                    })} />
                <Button
                    onPress={this.onLogin}
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
