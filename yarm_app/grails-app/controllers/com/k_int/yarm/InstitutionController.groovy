package com.k_int.yarm

import grails.plugin.springsecurity.annotation.Secured

class InstitutionController {

  @Secured(['ROLE_USER', 'IS_AUTHENTICATED_FULLY'])
  def index() { 
    log.debug("Institution::Index ${params}");
    def result=[:]
    result.tenant = Tenant.findByUriName(params.institution_shortcode)
    if ( result.tenant ) {
    }
    else {
      log.error("Unable to locate tenant ${params.id}");
    }
    result
  }

}
