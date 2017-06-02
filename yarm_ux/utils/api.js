var axios = require('axios');

module.exports = {
  fetchUserProfile: function() {
    console.log("yarm_api.fetchUserProfile()")
    return { hello : 'hello' };
  },

  ping: function() {
    console.log("yarm_api::Ping...");
    // Optionally the request above could also be done as
    axios.get('http://localhost:8080/auth/ping', {
      params: {
        ID: 12345
      }
    })
    .then(function (response) {
      console.log(response);
    })
    .catch(function (error) {
      console.log(error);
    });

    return "OK";
  }
}
