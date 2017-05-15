var React = require('react');
var ReactDOM = require('react-dom');
var PropTypes = require('prop-types');
var NavLink = require('react-router-dom').NavLink;

export default class SideBar extends React.Component {

  constructor (props) {
    super(props);
    this.state={
      selectedComponent : null,
    };
    this.updateSelectedComponent=this.updateSelectedComponent.bind(this);
  }

  updateSelectedComponent(comp){
    this.setState(function(){
      return {
        selectedComponent:comp
      }
    });
  }

  render() {

    return (
      <div>
        <h1>Sidebar v2</h1>
        <ul>
          <li><NavLink exact activeClassName='active' to='/'>Home</NavLink></li>
          <li><NavLink activeClassName='active' to='/search'>Search Users</NavLink></li>

          {this.props.nav_components.map((comp) => {
              return <li
                style={comp.label === this.state.selectedComponent ? { color:'#cc2222' } : null }
                onClick={this.updateSelectedComponent.bind(null, comp.label)}
                key={comp.label}> {comp.label} </li>
          })}
        </ul>
        <h2>{this.state.selectedComponent}</h2>
      </div>
    )
  }
}

SideBar.propTypes={
  nav_components: PropTypes.arrayOf(PropTypes.shape({
    label: PropTypes.string.isRequired,
  })).isRequired,
};

module.exports = SideBar;
