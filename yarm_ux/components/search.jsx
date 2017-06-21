var ReactDOM = require('react-dom');
var ReactRouter = require('react-router-dom');
var Link = ReactRouter.Link;
import { getState } from 'redux';


import React, { Component, PropTypes }  from 'react';


import yarm_utils from '../utils/api'

import { connect }                      from 'react-redux';

@connect(mapStateToProps)
export default class Search extends React.Component {

  constructor(props) {
    super(props);
    this.state = {};

    // This binding is necessary to make `this` work in the callback
    this.ping = this.ping.bind(this);
  }


  ping() {

    // var st = getState()
    console.log("Search::ping - calling ping");

    // Ripped off from https://github.com/yury-dymov/redux-oauth-client-demo/blob/master/src/redux/actions.js
    yarm_utils.ping(this.props.apiUrl);
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

// Grab what we need from state and stuff in props
function mapStateToProps(state) {
  // state.auth.config.backend
  return {
    apiUrl : state.auth.getIn(['config', 'backend', 'apiUrl'])
  };
}

module.exports = Search;
