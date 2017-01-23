package com.k_int.yarm

import grails.plugin.springsecurity.annotation.Secured


class HomeController {

  def springSecurityService

  def index() { 
    
    log.debug("Home::Index");
    if (springSecurityService.isLoggedIn()){
      render(view:'loggedInIndex')
    }
    else {
      render(view:'index')
    }
  }

  @Secured(['ROLE_USER', 'IS_AUTHENTICATED_FULLY'])
  def login() {
    log.debug("HomeController::login");
    redirect action:'index'
  }

}
