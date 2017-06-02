var React = require('react');
var ReactDOM = require('react-dom');
var ReactRouter = require('react-router-dom');
var Link = ReactRouter.Link;

var yarm_api = require('../utils/api')

export default class Search extends React.Component {

  constructor(props) {
    super(props);
    this.state = {};

    // This binding is necessary to make `this` work in the callback
    this.ping = this.ping.bind(this);
  }

  ping() {
    console.log("Search::ping");
    yarm_api.ping();
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
