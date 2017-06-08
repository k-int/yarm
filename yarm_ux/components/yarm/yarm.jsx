var React = require('react');
var ReactDOM = require('react-dom');

import { ScrollView, Box, Page, VBox, Flex } from 'react-layout-components';
import { connect }                      from 'react-redux';

var ReactRouter = require('react-router-dom');

var Link = ReactRouter.Link;

@connect(mapStateToProps)
export default class Yarm extends React.Component {
  render() {
    return (
      <Page>
        <Box column fit>
          <Box>
            YARM
            <Link to="/UniBorcetshire/erm/agreements/search">Search Agreements</Link>
          </Box>
          <Box>
            Logged in Dashboard {name}
          </Box>
        </Box>
      </Page>

    )
  }
}

module.exports =   Yarm

function mapStateToProps(state) {
  const signedIn = state.auth.getIn(['user', 'isSignedIn']) || false;

  if (signedIn) {
    console.log("Dashboard.. load %o",state.auth);
    const name      = state.auth.getIn(['user', 'attributes', 'name']);
    const provider  = state.auth.getIn(['user', 'attributes', 'provider']);
    const uid       = state.auth.getIn(['user', 'attributes', 'uid']);

    return { signedIn, name, provider, uid };
  }

  return { signedIn };
}

