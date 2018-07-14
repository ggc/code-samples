import React from 'react';
import { StyleSheet, TextInput, View, Text } from 'react-native';

export default class HomePage extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
        };
    }

    render() {
        return (
            <View>
                <Text>
                    Welcome!
                </Text>
            </View>
        );
    }
}

const styles = StyleSheet.create({
});
