var React = require('react');
var ReactDOM = require('react-dom');
var ReactRouter = require('react-router-dom');
var Link = ReactRouter.Link;


export default class Search extends React.Component {
  render() {
    return (
      <div>
        <h1>Search Component</h1>
        <Link to="/">Home</Link>
      </div>
    )
  }
}

module.exports =   Search;
