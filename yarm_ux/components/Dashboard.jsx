var React = require('react');
var ReactDOM = require('react-dom');
var PropTypes = require('prop-types');

import { ScrollView, Box, Page, VBox, Flex } from 'react-layout-components';
import { connect }                      from 'react-redux';

@connect(mapStateToProps)
export default class Dashboard extends React.Component {

  constructor (props) {
    super(props);
    this.state={
    };
  }

  render() {

    return (
      <Page>
        <Box column fit>
          <Box>
            Dash Nav
          </Box>
          <Box>
            Logged in Dashboard
          </Box>
        </Box>
      </Page>
    )
  }
}

Dashboard.propTypes={
};

module.exports = Dashboard;

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
