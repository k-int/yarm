var React = require('react');
var ReactDOM = require('react-dom');
var PropTypes = require('prop-types');

export default class SideBar extends React.Component {
  render() {

    return (
      <div>
        <h1>Sidebar</h1>
        <ul>
          {this.props.nav_components.map(function(comp){
              return <li key={comp}> {comp} </li>
          })}
        </ul>
      </div>
    )
  }
}

SideBar.propTypes={
  nav_components: PropTypes.array.isRequired,
};
