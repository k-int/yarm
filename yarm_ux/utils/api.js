// var axios = require('axios');

import { fetch as rofetch, parseResponse } from 'redux-oauth';
import store from '../app/store';

export default {
	fetch : function (url, conf) {
		var s = store;
		var fn =  rofetch(url, conf);
		var promise = fn(s.dispatch, s.getState);
		return promise;
	},
	
	fetchUserProfile: function () {
	  console.log("yarm_api.fetchUserProfile()")
	  return { hello : 'hello' };
	},
	
	ping: function () {
	  console.log("yarm_api::Ping...");
	  // Optionally the request above could also be done as
	  // axios.get('http://localhost:8080/auth/ping', {
	  var fr = this.fetch('http://localhost:8080/auth/ping', {
	    // credentials: 'same-origin',  // 'include'
	    credentials: 'include',
	    params: {
	      ID: 12345
	    }
	  });

	  fr.then(( payload ) => console.log(payload));

	  fr.catch(function (error) {
	    console.log(error);
	  });

	  return "OK";
	},
	
	ping2: function () {
	  console.log("ping2");
	  // Ripped off from https://github.com/yury-dymov/redux-oauth-client-demo/blob/master/src/redux/actions.js
	  return fetch('https://redux-oauth-backend.herokuapp.com/test/test')
	      .then(parseResponse)
	      .then(({ payload }) => console.log("%o",payload))
	      .catch(errors => console.log("Error",errors));
	}
};
