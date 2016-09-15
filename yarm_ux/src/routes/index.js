// We only need to import the modules necessary for initial render
import CoreLayout from '../layouts/CoreLayout/CoreLayout'
import WelcomeLayout from '../layouts/WelcomeLayout/WelcomeLayout'
import Home from './Home'
import Welcome from './Welcome'
import CounterRoute from './Counter'

/*  Note: Instead of using JSX, we recommend using react-router
    PlainRoute objects to build route definitions.   */

/* See https://github.com/reactjs/react-router/blob/master/examples/auth-with-shared-root/config/routes.js for example of
   auth with shared root / landing page which changes when user logged in */

/* This was a massive help so far: https://github.com/davezuko/react-redux-starter-kit/issues/906 */

function requireAuth (store, replace) {
  const token = localStorage.getItem('@USER')
  if (!token) replace('/')
}


export const createRoutes = (store) => ({
  path        : '/',
  childRoutes : [
    {
      component   : WelcomeLayout,
      indexRoute  : Welcome
    }
  ]

/*
  childRoutes: [
    {
      component: CoreLayout,
      onEnter: requireAuth,
      indexRoute: Home,
      childRoutes: [
        CounterRoute(store)
        // ProfileRoute,
        // EtcRoute
      ]
    },
    // non-authed routes
    {
      component: WelcomeLayout,
      indexRoute  : Home,
      // childRoutes: [
        // AuthRoute // has for example /login /reset
      // ]
    }
  ]
*/
})

/*  Note: childRoutes can be chunked or otherwise loaded programmatically
    using getChildRoutes with the following signature:

    getChildRoutes (location, cb) {
      require.ensure([], (require) => {
        cb(null, [
          // Remove imports!
          require('./Counter').default(store)
        ])
      })
    }

    However, this is not necessary for code-splitting! It simply provides
    an API for async route definitions. Your code splitting should occur
    inside the route `getComponent` function, since it is only invoked
    when the route exists and matches.
*/

export default createRoutes
