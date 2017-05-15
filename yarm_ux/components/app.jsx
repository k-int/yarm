var React = require('react');
var ReactDOM = require('react-dom');
var PropTypes = require('prop-types');
var ReactRouter = require('react-router-dom');
var yarm_api = require('../utils/api')

var Router = ReactRouter.BrowserRouter;
var Route = ReactRouter.Route;
var Switch = ReactRouter.Switch;

import SideBar from './sidebar.jsx';
import TopBar from './topbar.jsx';
var YarmWorkspace = require('./yarmWorkspace.jsx');
var Search = require('./search.jsx');

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
      <Router>
        <div className="container">
          <div>Hello World!</div>
          <div>More text</div>
          <TopBar />
          <SideBar nav_components={nav_components} />
          <Switch>
            <Route exact path='/' component={YarmWorkspace} />
            <Route path='/search' component={Search} />
            <Route render={function() {
              return ( <p>Not Found</p> )
            }}/>
          </Switch>
        </div>
      </Router>
    )
  }
}
