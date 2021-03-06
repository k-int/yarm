package com.k_int.yarm

public class Package extends Component {

  String identifier
  String sortName
  String impId
  String vendorURL
  String cancellationAllowances
  RefdataValue packageType
  RefdataValue packageStatus
  RefdataValue packageListStatus
  RefdataValue breakable
  RefdataValue consistent
  RefdataValue fixed
  RefdataValue isPublic
  RefdataValue packageScope
  Platform nominalPlatform
  Date startDate
  Date endDate
  Date dateCreated
  Date lastUpdated

  static hasMany = [titles: Grpp,
                    orgs: OrgRole,
                    ids: IdentifierOccurrence ]

  static mappedBy = [titles: 'pkg',
                     orgs: 'pkg',
                     ids: 'owner' ]


  static mapping = {
                      id column:'pkg_id'
                 version column:'pkg_version'
              identifier column:'pkg_identifier'
                sortName column:'pkg_sort_name'
             packageType column:'pkg_type_rv_fk'
           packageStatus column:'pkg_status_rv_fk'
       packageListStatus column:'pkg_list_status_rv_fk'
               breakable column:'pkg_breakable_rv_fk'
              consistent column:'pkg_consistent_rv_fk'
                   fixed column:'pkg_fixed_rv_fk'
         nominalPlatform column:'pkg_nominal_platform_fk'
               startDate column:'pkg_start_date'
                 endDate column:'pkg_end_date'
                isPublic column:'pkg_is_public'
            packageScope column:'pkg_scope_rv_fk'
               vendorURL column:'pkg_vendor_url'
  cancellationAllowances column:'pkg_cancellation_allowances', type:'text'
                 forumId column:'pkg_forum_id'
  }

 static constraints = {
                identifier(nullable:false, blank:false);
                  sortName(nullable:true, blank:false);
               packageType(nullable:true);
             packageStatus(nullable:true);
         packageListStatus(nullable:true);
                 breakable(nullable:true);
                consistent(nullable:true);
                     fixed(nullable:true);
           nominalPlatform(nullable:true);
                 startDate(nullable:true, blank:false);
                   endDate(nullable:true, blank:false);
                  isPublic(nullable:true, blank:false);
              packageScope(nullable:true, blank:false);
                 vendorURL(nullable:true, blank:false);
    cancellationAllowances(nullable:true, blank:false);

  }
  
  public static List refdataFind(grails.web.servlet.mvc.GrailsParameterMap params) {
    def result = []
    def qr = Org.executeQuery('select o.id, o.name from Package as o where o.name like :f',[f:"%${params.q?:''}%"],params)
    qr.each { r ->
      result.add([id:r[0], text:r[1]]);
    }
    result
  }

}
