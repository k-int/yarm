package com.k_int.yarm

import grails.plugin.springsecurity.annotation.Secured

import implicitauth.PartyRelationship


class PingController {

  def springSecurityService

  @Secured(['ROLE_USER', 'IS_AUTHENTICATED_FULLY'])
  def index() { 
    log.debug("PingController::index");
    def user = springSecurityService.currentUser
    def result=[:]
  }

}
