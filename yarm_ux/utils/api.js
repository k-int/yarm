// var axios = require('axios');

import { fetch, parseResponse } from 'redux-oauth';

export function fetchUserProfile() {
  console.log("yarm_api.fetchUserProfile()")
  return { hello : 'hello' };
}

export function ping() {
  console.log("yarm_api::Ping...");
  // Optionally the request above could also be done as
  // axios.get('http://localhost:8080/auth/ping', {
  fetch('http://localhost:8080/auth/ping', {
    // credentials: 'same-origin',  // 'include'
    credentials: 'include',
    params: {
      ID: 12345
    }
  })
  .then(({ payload }) => console.log(payload))
  .catch(function (error) {
    console.log(error);
  });

    return "OK";
  }
