import React from 'react'
import Header from '../../components/Header'
import classes from './WelcomeLayout.scss'
import '../../styles/core.scss'

export const WelcomeLayout = ({ children }) => (
  <div className='container text-center'>
    This is the welcome page - no auth user here
  </div>
)

WelcomeLayout.propTypes = {
  children : React.PropTypes.element.isRequired
}

export default WelcomeLayout
