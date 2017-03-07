package com.k_int.yarm

import grails.plugin.springsecurity.annotation.Secured
import grails.converters.JSON


class ResourceController {

  def springSecurityService
  def genericOIDService
  def yarmConfigService

  @Secured(['ROLE_USER', 'IS_AUTHENTICATED_FULLY'])
  def index() {

    log.debug("ResourceController::index ${params}");

    def user = springSecurityService.currentUser
    def result=[:]

    log.debug("ResourceController::Index");
    if (springSecurityService.isLoggedIn()){
    }

    log.debug("Returning..");
    render(view:params.gsp, model:result)
  }

  @Secured(['ROLE_USER', 'IS_AUTHENTICATED_FULLY'])
  def edit() {
    log.debug("edit(${params})")
    def user = springSecurityService.currentUser
    def result=[:]
    result.subject = genericOIDService.resolveOID2(params.cls,params.id);
    respond result, formats:['json'], view : params.gsonTemplate
  }

}
