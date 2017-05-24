var React = require('react');
var ReactDOM = require('react-dom');
var PropTypes = require('prop-types');
var ReactRouter = require('react-router-dom');
var yarm_api = require('../utils/api')

var Router = ReactRouter.BrowserRouter;
var Route = ReactRouter.Route;
var Switch = ReactRouter.Switch;

import { ScrollView, Box, Page, VBox, Flex } from 'react-layout-components';

import SideBar from './sidebar.jsx';
import TopBar from './topbar.jsx';
import YarmWorkspace from './yarmWorkspace.jsx';
import Search from './search.jsx';
import Login from './login.jsx';

import OAuthButton                      from '../containers/OAuthButton.jsx';

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
      <Page>
        <Box fit column>
          <Box>
            <TopBar />
          </Box>
          <Router>
            <Box flex="1">
              <Box flex="0 0 200px">
                <SideBar nav_components={nav_components} />
            LButton1:: <OAuthButton provider='github'>GitHub</OAuthButton>
            LButton2:: <OAuthButton provider='google'>Google</OAuthButton>
              </Box>
              <ScrollView flex="1" flexGrow="1">
                <Switch>
                  <Route exact path='/' component={YarmWorkspace} />
                  <Route path='/login' component={Login} />
                  <Route path='/search' component={Search} />
                  <Route render={function() {
                    return ( <p>Not Found</p> )
                  }}/>
                </Switch>
              </ScrollView>
            </Box>
          </Router>
        </Box>
      </Page>
    )
  }
}
