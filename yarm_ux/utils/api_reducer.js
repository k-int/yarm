import Immutable                              from 'immutable';
import { createReducer }                      from 'redux-immutablejs';

import { YARM_API_PING_OK }     from './api';
import { SIGN_OUT }                           from 'redux-oauth';

const initialState = Immutable.fromJS({
  loading:  false,
  loaded:   false,
  time:     null,
  errors:   null
});

export default createReducer(initialState, {
  [YARM_API_PING_OK]: (state) => state.merge({ loading: true, loaded: false, time: null, errors: null }),
  [SIGN_OUT]: () => initialState
});
