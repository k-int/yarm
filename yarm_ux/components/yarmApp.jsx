var React = require('react');
var ReactDOM = require('react-dom');
var PropTypes = require('prop-types');
var ReactRouter = require('react-router-dom');
var yarm_api = require('../utils/api')

var Router = ReactRouter.BrowserRouter;
var Route = ReactRouter.Route;
var Switch = ReactRouter.Switch;

import { ScrollView, Box, Page, VBox, Flex } from 'react-layout-components';

import Homepage from './Homepage.jsx';
import Yarm from './yarm/yarm.jsx';
import ResourceSharing from './resourceSharing/resourceSharing.jsx';
import APCManager from './apcManager/apcManager.jsx';
import Search from './search.jsx';

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

  // https://stackoverflow.com/questions/36273584/redirecting-user-based-on-state-in-react -- Nice use of onboarding towards end
  // https://stackoverflow.com/questions/38279555/auth-based-redirecting-with-react-router
  render() {
    const { signedIn, name, uid, provider } = this.props;
    console.log("yarmApp name:%s uid:%s provider:%s signedIn:%o",name,uid,provider,signedIn);


    const requireAuth = (nextState, replace)=>{

      console.log("requireAuth...");

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
          <Route exact path='/' component={Homepage} />
          <Route exact path='/search' component={Search} />
          <Route path='/:context/erm' component={Yarm} onEnter={requireAuth}/>
          <Route path='/:context/resourceSharing' component={ResourceSharing} onEnter={requireAuth}/>
          <Route path='/:context/apc' component={APCManager} onEnter={requireAuth}/>
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

