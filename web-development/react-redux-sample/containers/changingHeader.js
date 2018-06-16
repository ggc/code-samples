import { connect } from 'react-redux';
import Header from '../components/Header.jsx';
import ChangeText from '../actions/index.js';
import store from '../stores/index.js';

const mapStateToProps = (state, ownProps) => {
    return {
        text: state.text
    }
};

const mapDispatchToProps = dispatch =>{
    return {
        onClick: () => {
            dispatch(ChangeText('Text from Redux'))
        }
    }    
};

const changeLink = connect(
    mapStateToProps,
    mapDispatchToProps
)(Header);

export default changeLink;