import { createStore, applyMiddleware, combineReducers, compose } from 'redux';
import { authStateReducer }                  from 'redux-oauth';
import thunk                                                      from 'redux-thunk';

const store = createStore(
  combineReducers({
    auth: authStateReducer
  }),
  {},
  compose(
    applyMiddleware(thunk)
  )
);

export default store;