import React                                                      from 'react';
import ReactDOM                                                   from 'react-dom';
import PropTypes                                                  from 'prop-types';
import { initialize, authStateReducer }                           from 'redux-oauth';
import { createStore, applyMiddleware, combineReducers, compose } from 'redux';
import thunk                                                      from 'redux-thunk';
import { Provider }                                               from 'react-redux';

import YarmApp                                                    from '../components/yarmApp.jsx';

require('./index.css');

const store = createStore(
  combineReducers({
    auth: authStateReducer
  }),
  {},
  compose(
    applyMiddleware(thunk)
  )
);

const reduxOauthConfig = {
  backend: {
    // apiUrl:       'https://redux-oauth-backend.herokuapp.com',
    apiUrl:       'http://localhost:8080',
    signOutPath:  null,
    authProviderPaths: {
      google: '/auth/google',
      github: '/auth/github'
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
