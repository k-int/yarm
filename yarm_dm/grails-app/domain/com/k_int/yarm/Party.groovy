package com.k_int.yarm

/**
 *   Base class for User and Org - So either can be associated with actions and roles
 */ 
class Party {

  String uriName
  String displayName

  static mapping = {
    tablePerHierarchy false
    id column:'p_id'
    version column:'p_version'
    uriName column:'p_uriname'
    displayName column:'p_displayname'
  }

  static constraints = {
        uriName (nullable:false, blank:false, maxSize:64)
    displayName (nullable:false, blank:false, maxSize:64)
  }

}
