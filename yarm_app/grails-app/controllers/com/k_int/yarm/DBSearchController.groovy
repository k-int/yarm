package com.k_int.yarm

import grails.plugin.springsecurity.annotation.Secured
import grails.converters.JSON


class DBSearchController {

  def springSecurityService
  def genericOIDService
  def yarmConfigService

  @Secured(['ROLE_USER', 'IS_AUTHENTICATED_FULLY'])
  def index() {

    log.debug("DBSearchController::index ${params} ${params.srch_cfg}");

    def user = springSecurityService.currentUser
    def result=[:]

    log.debug("Home::Index");
    if (springSecurityService.isLoggedIn()){
      log.debug("Get query config");
      // never go directly to config - we want to allow user customisation at some point
      // so always use the service
      result.qryconfig = yarmConfigService.getQueryConfig(params.srch_cfg);
    }

    log.debug("Returning..");
    result;
  }

  /**
   *  @params willl be used to control pagination etc
   */
  @Secured(['ROLE_USER', 'IS_AUTHENTICATED_FULLY'])
  def getSearchResult() {
    def result=[:]

    log.debug("Home::Index");
    if (springSecurityService.isLoggedIn()){
      def qryconfig = yarmConfigService.getQueryConfig(params.srch_cfg);

      def qry_result = com.k_int.grails.tools.query.HQLBuilder.build(grailsApplication,
                                                                     qryconfig,
                                                                     params,
                                                                     genericOIDService)


      if ( qry_result ) {
        result.reccount = qry_result.reccount
        result.recset = qry_result.recset
      }
    }
    render result as JSON
  }
 

}
