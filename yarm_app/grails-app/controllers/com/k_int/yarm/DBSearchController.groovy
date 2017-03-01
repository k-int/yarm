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
    log.debug("DBSearchController::getSearchResult(${params})");
    def result=[:]

    if (springSecurityService.isLoggedIn()){
      def qryconfig = yarmConfigService.getQueryConfig(params.srch_cfg);

      def qry_result = com.k_int.grails.tools.query.HQLBuilder.build(grailsApplication,
                                                                     qryconfig,
                                                                     params,
                                                                     genericOIDService)


      if ( qry_result ) {
        result.reccount = qry_result.reccount
        result.iTotalRecords = qry_result.reccount
        result.recset = toMap(qryconfig,qry_result.recset)
      }
    }
    render result as JSON
  }

 
  def private toMap(cfg, result) {
    def list_of_maps = []
    result.each { row ->
      def row_as_map = [:]
      row_as_map.__id=row[0]
      row_as_map.__cls=row[1]
      int ctr = 2;

      cfg.qbeConfig.selectList.each { select_list_entry_defn ->
        row_as_map[select_list_entry_defn.name] = row[ctr++]
      }
      list_of_maps.add(row_as_map);
    }
    list_of_maps;
  }

  def private enhanceQueryResult(cfg, result) {
    // result.each { row ->
    //   log.debug("Enhance row ${row.class.name} ${row}");
    //   row.add([href:createLink(action:"index"), text:"this is a link"])
    //   row.add([href:createLink(action:"index"), text:"this is a link"])
    // }
    result;
  }
}
