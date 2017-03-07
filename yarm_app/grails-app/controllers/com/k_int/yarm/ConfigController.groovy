package com.k_int.yarm

import grails.plugin.springsecurity.annotation.Secured
import grails.converters.JSON


class ConfigController {

  def springSecurityService

  @Secured(['ROLE_USER', 'IS_AUTHENTICATED_FULLY'])
  def index() {
    log.debug("ConfigController::index params:${params}");
    def user = springSecurityService.currentUser
    def result=[:]
    if (springSecurityService.isLoggedIn()){
    }
    result
  }

  @Secured(['ROLE_USER', 'IS_AUTHENTICATED_FULLY'])
  def jsonSchema() {
    render(view:"jsonSchema/${params.id}.json")
  }

  @Secured(['ROLE_USER', 'IS_AUTHENTICATED_FULLY'])
  def layout() {
    render(view:"layout/${params.id}.json")
  }
}
