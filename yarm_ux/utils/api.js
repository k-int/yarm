// var axios = require('axios');

import { fetch as rofetch, parseResponse } from 'redux-oauth';

export function fetchUserProfile() {
  console.log("yarm_api.fetchUserProfile()")
  return { hello : 'hello' };
}

export function ping() {
  console.log("yarm_api::Ping...");
  // Optionally the request above could also be done as
  // axios.get('http://localhost:8080/auth/ping', {
  var fr = rofetch('http://localhost:8080/auth/ping', {
    // credentials: 'same-origin',  // 'include'
    credentials: 'include',
    params: {
      ID: 12345
    }
  });

  fr.then(({ payload }) => console.log(payload));

  fr.catch(function (error) {
    console.log(error);
  });

  return "OK";
}

export function ping2() {
  console.log("ping2");
  // Ripped off from https://github.com/yury-dymov/redux-oauth-client-demo/blob/master/src/redux/actions.js
  return dispatch => {
    return dispatch(fetch('https://redux-oauth-backend.herokuapp.com/test/test'))
      .then(parseResponse)
      .then(({ payload }) => console.log("%o",payload))
      .catch(errors => dispatch(apiError(errors)));
  };
}
