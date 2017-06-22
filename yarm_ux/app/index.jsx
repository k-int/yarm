import React                                                      from 'react';
import ReactDOM                                                   from 'react-dom';
import PropTypes                                                  from 'prop-types';
import store                                                      from './store';
import { Provider }                                               from 'react-redux';
import { initialize }                                             from 'redux-oauth';
import YarmApp                                                    from '../components/yarmApp.jsx';

require('./index.css');


console.log("hello %o %s %s",location,location.host,location.origin);

const reduxOauthConfig = {
  backend: {
    // apiUrl:                'http://localhost',
    // apiUrl defaults to location.origin. We are relying upon webpack proxy config in webpack.config.js to set up
    // the appropriate proxies. To isolate a specific service (Say, to run the declared auth and yarm war files, but run
    // some new service via grails run-app) use webpack.config.js to proxy the new service on a different port.
    apiUrl:                location.origin+'/yarm/ping',
    tokenValidationPath:   '/auth/oauth/validateToken',
    signOutPath:  null,
    authProviderPaths: {
      google: '/auth/oauth/google',
      github: '/auth/oauth/github'
    }
  },
  cookies: document.cookie,
  currentLocation: document.URL
};

store.dispatch(initialize(reduxOauthConfig)).then(
  () => {
    ReactDOM.render(
      <Provider store={store}>
        <YarmApp />
      </Provider>,
      document.getElementById('app')
    );
  }
);
