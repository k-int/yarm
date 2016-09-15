// We only need to import the modules necessary for initial render
import CoreLayout from '../layouts/CoreLayout/CoreLayout'
import WelcomeLayout from '../layouts/WelcomeLayout/WelcomeLayout'
import Home from './Home'
import WelcomeRoute from './Welcome'
import CounterRoute from './Counter'

/*  Note: Instead of using JSX, we recommend using react-router
    PlainRoute objects to build route definitions.   */

/* See https://github.com/reactjs/react-router/blob/master/examples/auth-with-shared-root/config/routes.js for example of
   auth with shared root / landing page which changes when user logged in */

/* This was a massive help so far: https://github.com/davezuko/react-redux-starter-kit/issues/906 i
   https://medium.com/@peterpme/react-router-authentication-onenter-requireauth-plainroute-a-follow-up-to-a-github-issue-71ea3e7d59c9#.2uvm13rb2
*/

function requireAuth (nextState, replace) {
  const token = localStorage.getItem('@USER')
  if (!token) replace('/welcome')
}


// http://ricostacruz.com/cheatsheets/react-router.html
//
export const createRoutes = (store) => ({
  path        : '/',
  childRoutes: [
    {
      path : 'home',
      component: CoreLayout,
      indexRoute: Home,
      // onEnter: requireAuth,
      childRoutes: [
        CounterRoute(store)
        // ProfileRoute,
        // EtcRoute
      ]
    },
    {
      path: ':tenant',
      component: CoreLayout,
      indexRoute: Home,
      // onEnter: requireAuth,
      childRoutes: [
        CounterRoute(store)
        // ProfileRoute,
        // EtcRoute
      ]
    },
    // The default / route uses the welcome layout
    {
      component: WelcomeLayout,
      indexRoute: WelcomeRoute,
    }
  ]
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
