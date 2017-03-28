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

  @Secured(['ROLE_USER', 'IS_AUTHENTICATED_FULLY'])
  def agreements() { 
    log.debug("Institution::Index ${params}");
    def result=[:]
    result.tenant = Tenant.findByUriName(params.institution_shortcode)
    if ( result.tenant ) {
      def qry_builder = [ where_count:0, sw:new StringWriter(), qpmap:[inst:result.tenant] ]
      qry_builder.sw.write('select agg, asig from Agreement as agg left outer join AgreementSignatory as asig with ( asig.agreement.id = agg.id and asig.signatory = :inst )');

      addOwnerClause(qry_builder, result.tenant)

      def query = qry_builder.sw.toString()
      log.debug("Find agreements: ${query} ${qry_builder.qpmap}");
      result.agreements = Agreement.executeQuery(query, qry_builder.qpmap)
    }
    else {
      log.error("Unable to locate tenant ${params.id}");
    }
    result
  }

  private addOwnerClause(info, tenant) {
    if ( info.where_count == 0 ) {
      info.sw.write(' where');
    }
    else {
      info.sw.write(' and');
    }

    info.sw.write(' agg.owner = :inst')
  }

  def addToAgreement() {
    log.debug("addToAgreement() ${params}");
  }
}
