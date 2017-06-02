var ReactDOM = require('react-dom');
var ReactRouter = require('react-router-dom');
var Link = ReactRouter.Link;

import React, { Component, PropTypes }  from 'react';


import yarm_utils from '../utils/api'

export default class Search extends React.Component {

  constructor(props) {
    super(props);
    this.state = {};

    // This binding is necessary to make `this` work in the callback
    this.ping = this.ping.bind(this);
  }


  ping() {
    console.log("Search::ping - calling ping");

    // Ripped off from https://github.com/yury-dymov/redux-oauth-client-demo/blob/master/src/redux/actions.js
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

function mapStateToProps(state) {
  return {};
}

module.exports = Search;
