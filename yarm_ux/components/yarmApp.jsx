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
import YarmWorkspace from './yarmWorkspace.jsx';

import { connect }                      from 'react-redux';

@connect(mapStateToProps)
export default class YarmApp extends React.Component {

  constructor(props) {
    super(props);
  }

  static propTypes = {
    signedIn: PropTypes.bool,
    provider: PropTypes.string,
    name:     PropTypes.string,
    uid:      PropTypes.string
  };

  componentDidMount() {
    console.log("App.componentDidMount()");
  }

  // https://stackoverflow.com/questions/38279555/auth-based-redirecting-with-react-router
  render() {
    const { signedIn, name, uid, provider } = this.props;
    console.log("%s %s %s %o",name,uid,provider,signedIn);


    const requireAuth = (nextState, replace)=>{
      if (!signedIn) {
        console.log("Not signed in - redirect");
        replace({
          pathname: '/',
        })
      }
      else {
        console.log("User is signed in - no redirect needed");
      }
    };

    return (
      <Router>
        <Switch>
          <Route exact path='/' component={PublicHome} />
          <Route path='/home' component={YarmWorkspace} onEnter={requireAuth}/>
          <Route render={function() {
            return ( <p>Not Found</p> )
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

