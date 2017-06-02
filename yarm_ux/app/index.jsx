import React                                                      from 'react';
import ReactDOM                                                   from 'react-dom';
import PropTypes                                                  from 'prop-types';
import store                                                      from './store';
import { Provider }                                               from 'react-redux';
import { initialize }                  from 'redux-oauth';

import YarmApp                                                    from '../components/yarmApp.jsx';

require('./index.css');


const reduxOauthConfig = {
    backend: {
      // apiUrl:       'https://redux-oauth-backend.herokuapp.com',
      apiUrl:                'http://localhost:8080',
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
