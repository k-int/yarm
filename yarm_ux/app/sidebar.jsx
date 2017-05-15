var React = require('react');
var ReactDOM = require('react-dom');

export default class SideBar extends React.Component {
  render() {
    var nav_components = ['one','two','three','four'];
    return (
      <div>
        <h1>Sidebar</h1>
        <ul>
          {nav_components.map(function(comp){
              return <li> {comp} </li>
          })}
        </ul>
      </div>
    )
  }
}
