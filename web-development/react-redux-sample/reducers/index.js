const text = (currentState = 'Emptiness', action) => {
    switch(action.type){
        case 'CHANGE_TEXT':
            return action.text;
            break;
        default:
            return currentState;
    }
};

const blog = (currentState, action) => {
    return {
        text: text(currentState, action),
    }
};

module.exports = blog;