package com.k_int.yarm

import grails.plugin.springsecurity.annotation.Secured

class DBSearchController {

  def springSecurityService

  @Secured(['ROLE_USER', 'IS_AUTHENTICATED_FULLY'])
  def index() {

    log.debug("DBSearchController::index");

    def user = springSecurityService.currentUser
    def result=[:]

    log.debug("Home::Index");
    if (springSecurityService.isLoggedIn()){
    }

    result;
  }

}
