var React = require('react');
var ReactDOM = require('react-dom');
var PropTypes = require('prop-types');

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
        <h2>{this.state.selectedComponent}</h2>
        <ul>
          {this.props.nav_components.map(function(comp){
              return <li
                onClick={this.updateSelectedComponent.bind(null, comp.label)}
                key={comp.label}> {comp.label} </li>
          }, this)}
        </ul>
      </div>
    )
  }
}

SideBar.propTypes={
  nav_components: PropTypes.arrayOf(PropTypes.shape({
    label: PropTypes.string.isRequired,
  })).isRequired,
};
