// Reducers
const todos = (currentState = [], action) => {
    switch(action.type){
        case 'ADD_TODO':
            const nextState = [
                ...currentState,
                {
                    id: action.id,
                    task: action.task,
                    completed: false
                }
            ];
            return nextState;
            break;
        default:
            return currentState;
    }
};
const authors = (currentState = [], action) => {
    switch(action.type) {
        case 'ADD_AUTHOR':
            const nextState = [
                ...currentState,
                {
                    id: action.id,
                    name: action.name,
                    role: action.role
                }
            ];
            return nextState;
            break;
        default:
            return currentState;
    }
};
const todoApp = (currentState = {}, action) => {
    return {
        todos: todos(currentState.todos, action),
        authors: authors(currentState.authors, action),
    }
};