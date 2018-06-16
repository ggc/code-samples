import { createStore } from 'redux';
import blog from '../reducers';

const store = createStore(
    blog
);

module.exports = store;