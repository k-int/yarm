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

import { connect }                      from 'react-redux';

@connect(mapStateToProps)
export default class YarmApp extends React.Component {

  static propTypes = {
    signedIn: PropTypes.bool,
    provider: PropTypes.string,
    name:     PropTypes.string,
    uid:      PropTypes.string
  };

  componentDidMount() {
    console.log("App.componentDidMount()");
  }

  render() {
    const { signedIn, name, uid, provider } = this.props;
    console.log("%s %s %s",name,uid,provider);

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


function mapStateToProps(state) {
    const signedIn = state.auth.getIn(['user', 'isSignedIn']) || false;

    if (signedIn) {
      const name      = state.auth.getIn(['user', 'attributes', 'name']);
      const provider  = state.auth.getIn(['user', 'attributes', 'provider']);
      const uid       = state.auth.getIn(['user', 'attributes', 'uid']);

      return { signedIn, name, provider, uid };
    }

    return { signedIn };
}

