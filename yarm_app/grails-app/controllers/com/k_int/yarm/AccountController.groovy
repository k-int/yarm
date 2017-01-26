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

      if ( request.method == 'POST' ) {
        // Redirect to new org home
        redirect(controller:'home',action:'index')
      }

      render view:'newOrganisation'
    }
    else {
    }

    result
  }

  @Secured(['ROLE_USER', 'IS_AUTHENTICATED_FULLY'])
  def validateProposedOrg() {
    def result=[:]

    // Reserved names
    def reserved_names = grailsApplication.config.reserved_names

    log.debug("reserved_names: ${reserved_names}");

    if ( params.name?.trim().length() < 3 ) {
      result.isOk = 1
      result.message="Sorry, names must be at least three characters"
    }
    else if ( reserved_names.contains(params.name?.toLowerCase()) ) {
      result.isOk = 1
      result.message="Sorry, ${params.name} is a reserved name";
    }
    else {
      result.isOk = Org.executeQuery('select count(o) from Org as o where o.displayName = :o',[o:params.name])[0]
      result.candiateOrgs = Org.executeQuery('select o.id, o.displayName from Org as o where textSearch(o.displayName,:oname) = true',[oname:params.name])
      if ( result.candiateOrgs.size() > 0 ) {
        result.message="${params.name} Seems to be OK as a new organisation name, but please check in the list below for similar names"
      }
      else {
        result.message="${params.name} Seems to be OK as a new organisation name"
      }
    }
    render result as JSON
  }
}
