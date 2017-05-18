var React = require('react');
var ReactDOM = require('react-dom');
var PropTypes = require('prop-types');
var NavLink = require('react-router-dom').NavLink;

export default class Login extends React.Component {

  constructor (props) {
    super(props);
    this.state={
    };
    // this.updateSelectedComponent=this.updateSelectedComponent.bind(this);
  }

  // updateSelectedComponent(comp){
  //   this.setState(function(){
  //     return {
  //       selectedComponent:comp
  //     }
  //   });
  // }

  render() {

    return (
      <div>
      </div>
    )
  }
}

Login.propTypes={
  // nav_components: PropTypes.arrayOf(PropTypes.shape({
  //   label: PropTypes.string.isRequired,
  // })).isRequired,
};

module.exports = Login;
