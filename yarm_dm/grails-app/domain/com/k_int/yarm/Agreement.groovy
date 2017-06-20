package com.k_int.yarm

import implicitauth.Tenant

public class Agreement {
  
  Tenant owner
  String name
  String shortcode


  static mapping = {
    tablePerHierarchy false
    id column:'a_id'
    version column:'a_version'
    owner column:'a_owner', index:'a_owner_fk'
    name column:'a_name', type:'text'
    shortcode column:'a_shortcode', index:'a_shortcode_idx'
  }


  static constraints = {
            owner (nullable:false, blank:false)
             name (nullable:true,  blank:false, maxSize:2048)
        shortcode (nullable:true,  blank:false, maxSize:128)
  }

}
