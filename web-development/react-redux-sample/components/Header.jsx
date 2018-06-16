import React from 'react';
import { Button } from 'reactstrap';
import store from '../stores/index.js';

const Header = ({ text, onClick }) => (
  <div>
    <h1>
      {text}
    </h1>
    <Button onClick={onClick} > {store.getState().text} </Button>
  </div>
)


export default Header