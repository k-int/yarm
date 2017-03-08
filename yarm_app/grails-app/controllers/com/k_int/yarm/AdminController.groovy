package com.k_int.yarm

import grails.plugin.springsecurity.annotation.Secured
import grails.converters.JSON


class AdminController {

  def springSecurityService
  def genericOIDService
  def globalSourceSyncService

  @Secured(['ROLE_ADMIN', 'IS_AUTHENTICATED_FULLY'])
  def index() {
    def result=[:]
    result
  }

  @Secured(['ROLE_ADMIN', 'IS_AUTHENTICATED_FULLY'])
  def sync() {
    log.debug("sync(${params})")
    globalSourceSyncService.triggerSync();
    redirect(controller:'home',action:'index');
  }

}
