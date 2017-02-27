package com.k_int.yarm

import grails.plugin.springsecurity.annotation.Secured

class DBSearchController {

  def springSecurityService
  def genericOIDService

  @Secured(['ROLE_USER', 'IS_AUTHENTICATED_FULLY'])
  def index() {

    log.debug("DBSearchController::index ${params} ${params.srch_cfg}");

    def user = springSecurityService.currentUser
    def result=[:]

    log.debug("Home::Index");
    if (springSecurityService.isLoggedIn()){

      log.debug("Build Query");

      com.k_int.grails.tools.query.HQLBuilder.build(grailsApplication,
                                                    params.srch_cfg,
                                                    params,
                                                    result,
                                                    genericOIDService,
                                                    'rows')
    }

    log.debug("Returning..");
    result;
  }

}
