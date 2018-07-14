import React from 'react';
import { StyleSheet, Text, View } from 'react-native';

export default class Footer extends React.Component {
    render() {
        const date = new Date();
        return (
            <View style={styles.container}>
                <Text>Im the footer</Text>
            </View>
        );
    }
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#00a1ff',
        alignItems: 'center',
        justifyContent: 'center',
        height: 100,
        width: 5000
    },
});
