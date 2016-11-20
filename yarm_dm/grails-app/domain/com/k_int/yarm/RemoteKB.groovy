package com.k_int.yarm

public class RemoteKB {

  String identifier
  String name
  String type
  Date haveUpTo
  String uri
  String listPrefix
  String fullPrefix
  String principal
  String credentials
  Long rectype
  Boolean active

  static mapping = {
                   id column:'rkb_id'
              version column:'rkb_version'
           identifier column:'rkb_identifier'
                 name column:'rkb_name'
             haveUpTo column:'rkb_have_up_to'
                  uri column:'rkb_uri'
           fullPrefix column:'rkb_full_prefix'
           listPrefix column:'rkb_list_prefix'
                 type column:'rkb_type'
            principal column:'rkb_principal'
          credentials column:'rkb_creds'
              rectype column:'rkb_rectype'
               active column:'rkb_active'
  }

  static constraints = {
     identifier(nullable:true, blank:false)
           name(nullable:true, blank:false)
       haveUpTo(nullable:true, blank:false)
            uri(nullable:true, blank:false)
           type(nullable:true, blank:false)
     fullPrefix(nullable:true, blank:false)
     listPrefix(nullable:true, blank:false)
      principal(nullable:true, blank:false)
    credentials(nullable:true, blank:false)
         active(nullable:true, blank:false)
  }


}
