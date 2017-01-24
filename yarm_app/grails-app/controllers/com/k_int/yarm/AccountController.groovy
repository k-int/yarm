package com.k_int.yarm

import grails.plugin.springsecurity.annotation.Secured

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

}
