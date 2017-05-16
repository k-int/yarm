var React = require('react');
var ReactDOM = require('react-dom');
var PropTypes = require('prop-types');
var ReactRouter = require('react-router-dom');
var yarm_api = require('../utils/api')

var Router = ReactRouter.BrowserRouter;
var Route = ReactRouter.Route;
var Switch = ReactRouter.Switch;

import { Box, Page, VBox, Flex } from 'react-layout-components';

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
      <Page className="debugLayout">
        <Box fit column className="debugLayout">
          <Box className="debugLayout">
            <TopBar />
          </Box>
          <Router>
            <Box className="debugLayout">
              <Box flex="0 0 200px" className="debugLayout">
                <SideBar nav_components={nav_components} />
              </Box>
              <Box flex="1" flexGrow="1" className="debugLayout">
                <Switch>
                  <Route exact path='/' component={YarmWorkspace} />
                  <Route path='/search' component={Search} />
                  <Route render={function() {
                    return ( <p>Not Found</p> )
                  }}/>
                </Switch>
              </Box>
            </Box>
          </Router>
        </Box>
      </Page>
    )
  }
}
