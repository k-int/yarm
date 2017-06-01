var axios = require('axios');

module.exports = {
  fetchUserProfile: function() {
    console.log("yarm_api.fetchUserProfile()")
    return { hello : 'hello' };
  },

  ping: function() {
    console.log("Ping...");
    return "OK";
  }
}
