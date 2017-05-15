var React = require('react');
var ReactDOM = require('react-dom');
var PropTypes = require('prop-types');

require('./index.css');

import App from '../components/app.jsx';

ReactDOM.render(
  <App />,
  document.getElementById('app')
);
