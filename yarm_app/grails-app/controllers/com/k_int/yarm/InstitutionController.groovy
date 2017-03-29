package com.k_int.yarm

import grails.plugin.springsecurity.annotation.Secured

class InstitutionController {

  private static String INST_TITLES_QRY = '''from Grpp as grpp where exists 
  ( select p from Package as p 
               join AgreementItem as ai with ( ai.linkedContent.id = p.id ) 
               join AgreementSignatory as asig with ( asig.agreement.id = ai.owner.id and asig.signatory = :sig and asig.activeYN = :yes )
    where p.id = grpp.pkg.id )
'''


  // Alternate version
  // Select all agreement signatory rows and titles related
  private static String INST_TITLES_QRY_2 = '''from AgreementSignatory as asig
join AgreementItem as ai with ( ai.owner.id = asig.agreement.id ),
Grpp as grpp where exists ( select p from Package as p where ai.linkedContent.id = p.id and grpp.pkg = p)
and asig.signatory = :sig  and asig.activeYN = :yes
'''

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
    log.debug("Institution::agreements ${params}");
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

  @Secured(['ROLE_USER', 'IS_AUTHENTICATED_FULLY'])
  def agreement() {
    log.debug("Institution::agreement ${params}");
    def result=[:]
    result.tenant = Tenant.findByUriName(params.institution_shortcode)
    if ( result.tenant ) {
      result.yrt = Agreement.get(params.id);
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

  @Secured(['ROLE_USER', 'IS_AUTHENTICATED_FULLY'])
  def addToAgreement() {
    log.debug("addToAgreement() ${params} - ${request.instituion}");
    if ( params.id ) {
      def agreement = Agreement.get(params.id)

      if ( params.pkg ) {
        log.debug("Add package ${params.pkg}");
        def pkg = Package.get(params.pkg);
        def existingItem = AgreementItem.findByOwnerAndLinkedContent(agreement, pkg) ?:new AgreementItem(owner:agreement, linkedContent:pkg).save(flush:true, failOnError:true);
      }
    }
    redirect(url: request.getHeader('referer'))
  }

  @Secured(['ROLE_USER', 'IS_AUTHENTICATED_FULLY'])
  def selectAgreement() {
    log.debug("selectAgreement ${params} ${request.institution}");
    def agreement = Agreement.get(params.agreement);

    if ( ( agreement != null ) && ( request.institution != null ) ) {

      def asig = AgreementSignatory.findBySignatoryAndAgreement(request.institution, agreement)

      if ( asig == null ) {
        log.debug("New agreement signatory ${request.instituion}");
        asig = new AgreementSignatory(
                                      signatory:request.institution, 
                                      agreement:agreement,
                                      status:RefdataCategory.lookupOrCreate('ASStatus','Selected'),
                                      activeYN:RefdataCategory.lookupOrCreate('YN','Yes')).save(flush:true, failOnError:true);

      }
      else {
        log.debug("Already a signatory");
      }
    }
    redirect(url: request.getHeader('referer'))
  }

  @Secured(['ROLE_USER', 'IS_AUTHENTICATED_FULLY'])
  def updateAgreementStatus() {
    log.debug("updateAgreementStatus ${params} ${request.institution}");
    def asig = AgreementSignatory.get(params.asig);
    if ( ( asig != null ) && ( request.institution != null ) ) {
      if ( params.status=='Yes' ) {
        log.debug("Set yes");
        asig.activeYN = RefdataCategory.lookupOrCreate('YN','Yes')
      }
      else {
        log.debug("Set no");
        asig.activeYN = RefdataCategory.lookupOrCreate('YN','No')
      }
      asig.save(flush:true, failOnError:true)
    }
    else {
      log.debug("No asig");
    }

    redirect(url: request.getHeader('referer'))
  }

  @Secured(['ROLE_USER', 'IS_AUTHENTICATED_FULLY'])
  def titles() {
    log.debug("Institution::Titles ${params}");
    def result = [:]

    def qry_params = [:]
    qry_params << params

    if ( qry_params.max == null ) {
      qry_params.max = 10;
    }

    // Find all titles the current institution has a live agreement to access
    def bindvars = [sig:request.institution, yes:RefdataCategory.lookupOrCreate('YN','Yes')]
    result.titleCount = Grpp.executeQuery('select count(grpp) '+INST_TITLES_QRY,bindvars)[0];
    result.titles = Grpp.executeQuery('select grpp '+INST_TITLES_QRY+"  order by grpp.global_resource.name",bindvars,qry_params);

    result
  }
}
