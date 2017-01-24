package com.k_int.yarm

import grails.plugin.springsecurity.annotation.Secured


class HomeController {

  def springSecurityService

  def index() { 

    def result=[:]

    log.debug("Home::Index");
    if (springSecurityService.isLoggedIn()){
      result.user_contexts = []
      result.user_contexts.add([
                                name:springSecurityService.currentUser.uriName,
                                type:'user',
                                label:springSecurityService.currentUser.displayName]);

      render(view:'loggedInIndex', model:result)
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
