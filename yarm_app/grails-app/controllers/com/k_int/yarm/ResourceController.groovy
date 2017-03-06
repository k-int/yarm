package com.k_int.yarm

import grails.plugin.springsecurity.annotation.Secured
import grails.converters.JSON


class ResourceController {

  def springSecurityService
  def genericOIDService
  def yarmConfigService

  @Secured(['ROLE_USER', 'IS_AUTHENTICATED_FULLY'])
  def index() {

    log.debug("ResourceController::index ${params} ${params.srch_cfg}");

    def user = springSecurityService.currentUser
    def result=[:]

    log.debug("ResourceController::Index");
    if (springSecurityService.isLoggedIn()){
    }

    log.debug("Returning..");
    result;
  }

  @Secured(['ROLE_USER', 'IS_AUTHENTICATED_FULLY'])
  def edit() {
    def user = springSecurityService.currentUser
    def result=[:]
    respond result, formats:['json'], view : "test1"
  }

}
