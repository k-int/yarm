var React = require('react');
var ReactDOM = require('react-dom');
var PropTypes = require('prop-types');

var ReactRouter = require('react-router-dom');

var Link = ReactRouter.Link;


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

    const { signedIn, name, uid, provider } = this.props;

    return (
      <Page>
        <Box column fit>
          <Box>
            Dash Nav
            <Link to="/search">Search</Link>
          </Box>
          <Box>
            Logged in Dashboard {name}
          </Box>
        </Box>
      </Page>
    )
  }
}

Dashboard.propTypes={
  signedIn: PropTypes.bool,
  provider: PropTypes.string,
  name:     PropTypes.string,
  uid:      PropTypes.string
};

module.exports = Dashboard;

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
