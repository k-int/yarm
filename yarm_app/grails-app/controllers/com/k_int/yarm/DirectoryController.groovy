package com.k_int.yarm

import grails.plugin.springsecurity.annotation.Secured

class DirectoryController {

  def index() { 
    log.debug("Directory::Index");
    def result=[:]
    result
  }

  @Secured(['ROLE_USER', 'IS_AUTHENTICATED_FULLY'])
  def user() { 
    log.debug("Directory::User ${params}");
    def result=[:]
    result
  }

  @Secured(['ROLE_USER', 'IS_AUTHENTICATED_FULLY'])
  def org() { 
    log.debug("Directory::Org ${params}");
    def result=[:]
    result
  }

  @Secured(['ROLE_USER', 'IS_AUTHENTICATED_FULLY'])
  def orgDash() { 
    log.debug("Directory::OrgDash ${params}");
    def result=[:]
    result.tenant = Tenant.findByUriName(params.id)
    if ( result.tenant ) {
    }
    else {
      log.error("Unable to locate tenant ${params.id}");
    }
    result
  }
}
