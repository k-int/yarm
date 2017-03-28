package com.k_int.yarm

public class AgreementItem {
  
  Agreement owner
  Component linkedContent

  static mapping = {
    tablePerHierarchy false
    id column:'ai_id'
    version column:'a_version'
    owner column:'ai_owner'
  }


  static constraints = {
             owner (nullable:false, blank:false)
  }

}
