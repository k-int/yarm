var React = require('react');
var ReactDOM = require('react-dom');
var ReactRouter = require('react-router-dom');
var Link = ReactRouter.Link;

import * as yarm_utils from '../utils/api'

export default class Search extends React.Component {

  constructor(props) {
    super(props);
    this.state = {};

    // This binding is necessary to make `this` work in the callback
    this.ping = this.ping.bind(this);
  }

  ping() {
    console.log("Search::ping");
    yarm_utils.ping();
  }

  render() {
    return (
      <div>
        <h1>Search Component</h1>
        <Link to="/">Home</Link>
        <button onClick={this.ping}>Ping</button>
      </div>
    )
  }
}

module.exports = Search;
