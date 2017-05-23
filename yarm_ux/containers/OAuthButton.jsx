import React, { PropTypes, Component }  from 'react';
import { connect }                      from 'react-redux';

import classnames                       from 'classnames';

import { oAuthSignIn }                  from 'redux-oauth';

import omit                             from 'lodash/omit';

@connect(mapStateToProps)
export default class OAuthSignInButton extends Component {
  static propTypes = {
    provider:     PropTypes.string.isRequired,
    label:        PropTypes.string,
    children:     PropTypes.node,
    icon:         PropTypes.node,
    dispatch:     PropTypes.func,
    disabled:     PropTypes.bool,
    loading:      PropTypes.bool,
    className:    PropTypes.string
  };

  static defaultProps = {
    children: <span>OAuth Sign In</span>,
    icon:     null
  };

  handleClick = () => {
    const { provider, dispatch } = this.props;

    dispatch(oAuthSignIn({ provider }));
  };

  render() {
    const { disabled, loading, icon, className } = this.props;

    const restProps = omit(this.props, ['disabled', 'loading', 'icon', 'className', 'dispatch', 'provider']);

    const style = {};

    return (
      <button
        className = {classnames(className, 'oauth-sign-in-submit')}
        onClick   = {this.handleClick}
        {...restProps}
      >login d:{disabled}</button>
    );
  }
}

function mapStateToProps({ auth }, ownProps) {
  const disabled  = auth.getIn(['user', 'isSignedIn']);
  const loading   = auth.getIn(['oAuthSignIn', ownProps.provider, 'loading']);

  return { disabled, loading };
}