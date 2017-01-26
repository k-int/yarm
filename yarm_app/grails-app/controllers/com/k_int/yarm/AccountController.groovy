package com.k_int.yarm

import grails.plugin.springsecurity.annotation.Secured
import grails.converters.JSON

class AccountController {

  @Secured(['ROLE_USER', 'IS_AUTHENTICATED_FULLY'])
  def index() { 
    log.debug("Account::Index");
  }

  @Secured(['ROLE_USER', 'IS_AUTHENTICATED_FULLY'])
  def organisations() { 
    log.debug("AccountController::organisations ${params}");
    def result=[:]

    if ( params.id == 'new' ) {
      log.debug("newOrg");
      render view:'newOrganisation'
    }
    else {
    }

    result
  }

  @Secured(['ROLE_USER', 'IS_AUTHENTICATED_FULLY'])
  def validateProposedOrg() {
    def result=[:]

    result.isOk = Org.executeQuery('select count(o) from Org as o where o.displayName = :o',[o:params.name])
    result.candiateOrgs = Org.executeQuery('select o.id, o.displayName from Org as o where textSearch(o.displayName,:oname) = true',[oname:params.name])

    render result as JSON
  }
}
