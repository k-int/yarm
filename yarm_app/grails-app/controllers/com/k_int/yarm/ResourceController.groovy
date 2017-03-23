package com.k_int.yarm

import grails.plugin.springsecurity.annotation.Secured
import grails.converters.JSON


class ResourceController {

  def springSecurityService
  def genericOIDService
  def yarmConfigService

  @Secured(['ROLE_USER', 'IS_AUTHENTICATED_FULLY'])
  def index() {
    return processResource()
  }

  @Secured(['ROLE_USER', 'IS_AUTHENTICATED_FULLY'])
  def processEmbeddedResource() {
    log.debug("processEmbeddedResource(${params})");
    processResource()
    redirect(url: request.getHeader('referer'))
  }

 

  private def processResource() {

    log.debug("ResourceController::index ${params}");

    def user = springSecurityService.currentUser
    def result=[:]

    log.debug("ResourceController::Index");
    if (springSecurityService.isLoggedIn()){
      result.yrt = genericOIDService.resolveOID2(params.cls,params.id);
      if ( request.method=='GET' ) {
        log.debug("Processing GET");
      }
      else {
        log.debug("Processing Post - Binding data ${params.yrt}");
        bindData(result.yrt, params.yrt);
        result.yrt.save(flush:true, failOnError:true);
      }
    }

    log.debug("Returning..");
    result
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
