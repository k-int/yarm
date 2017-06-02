var ReactDOM = require('react-dom');
var ReactRouter = require('react-router-dom');
var Link = ReactRouter.Link;

import React, { Component, PropTypes }  from 'react';
import { connect }                      from 'react-redux';


import * as yarm_utils from '../utils/api'

@connect(mapStateToProps)
export default class Search extends React.Component {

  constructor(props) {
    super(props);
    this.state = {};

    // This binding is necessary to make `this` work in the callback
    this.ping = this.ping.bind(this);
  }


  ping() {
    console.log("Search::ping - calling ping2");
    this.props.dispatch(yarm_utils.ping2());
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
