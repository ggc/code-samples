// Action Creators
let nextTodoId = 0;
const addTodo = (task) => {
    return {
        type: 'ADD_TODO',
        id: nextTodoId++,
        task
    };
};
let nextAuthorId = 0;
const addAuthor = (name, role) => {
    return {
        type: 'ADD_AUTHOR',
        id: nextAuthorId++,
        name,
        role,
    };
};