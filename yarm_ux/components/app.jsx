var React = require('react');
var ReactDOM = require('react-dom');
var PropTypes = require('prop-types');
var yarm_api = require('../utils/api')

import SideBar from './sidebar.jsx';
import TopBar from './topbar.jsx';
var YarmWorkspace = require('./yarmWorkspace.jsx');

export default class App extends React.Component {

  componentDidMount() {
    console.log("App.componentDidMount()");
    yarm_api.fetchUserProfile(); //.then( function(user_profile) {
      //console.log("User Profile...");
    //})
  }

  render() {
    var nav_components = [{label:'one'},{label:'two'},{label:'three'},{label:'four'}];
    var registeredComponents = {
      'yarmDashboard': YarmWorkspace
    };

    // This sets up ActiveComponent as a dynamic thing which can be switched around
    var ActiveComponent = registeredComponents['yarmDashboard']

    return (
      <div className="container">
        <div>Hello World!</div>
        <div>More text</div>
        <TopBar />
        <SideBar nav_components={nav_components} />
        <ActiveComponent />
      </div>
    )
  }
}
