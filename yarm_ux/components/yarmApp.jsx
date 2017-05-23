var React = require('react');
var ReactDOM = require('react-dom');
var PropTypes = require('prop-types');
var ReactRouter = require('react-router-dom');
var yarm_api = require('../utils/api')

var Router = ReactRouter.BrowserRouter;
var Route = ReactRouter.Route;
var Switch = ReactRouter.Switch;

import { ScrollView, Box, Page, VBox, Flex } from 'react-layout-components';

import PublicHome from './PublicHome.jsx';

export default class YarmApp extends React.Component {

  componentDidMount() {
    console.log("App.componentDidMount()");
    yarm_api.fetchUserProfile(); //.then( function(user_profile) {
      //console.log("User Profile...");
    //})
  }

  render() {
    return (
      <Router>
        <Switch>
          <Route exact path='/' component={PublicHome} />
          <Route path='/wibble' component={PublicHome} />
          <Route render={function() {
            return ( <p><h3>Not Found</h3></p> )
          }}/>
        </Switch>
      </Router>
    )
  }
}
