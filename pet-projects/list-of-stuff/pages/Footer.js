import React from 'react';
import { FlatList, View, Text, TextInput } from 'react-native';
// import { listRepos } from '../reducers/reducer';
// import { connect } from 'react-redux';

class Footer extends React.Component {
    // constructor(props) {
    //     super(props);
    //     console.log('[LOG] constructor props', props);
    //     this.handleChange = this.handleChange.bind(this);
    // }
    // componentDidMount() {
    //     console.log('[LOG] this at Footer', this);
    //     // this.props.listRepos('relferreira');
    // }
    
    // renderItem = ({ item }) => (
    //     <View>
    //         <Text>{item.name}</Text>
    //     </View>
    // );
    render() {
        console.log('[LOG] this', this);
        return (
            <View>
                <Text>I'm a footer</Text>
            </View>
        );
    }
    // When input changes, handleChange fires and
    // the onTextWritten handler my father gave me
    // fires with my text. Too much callback-like 
    handleChange(e) {
        console.log('[LOG] text changed', e);
        // this.props.onTextWritten(e.target.value);
    }
    
    // render() {
    //     return (
    //         <View>
    //             <Text> {this.props.options}</Text>
    //             <TextInput onChangeText={() => console.log('Text chenaged!')} />
    //         </View>
    //         )
    // }
}

module.exports = Footer;