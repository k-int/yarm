package com.k_int.yarm

public class IdentifierOccurrence {
  
  Component owner
  Identifier identifier

  static mapping = {
    id column:'io_id'
    owner column:'io_owner'
    identifier column:'io_id'
  }


  static constraints = {
         owner (nullable:false, blank:false)
    identifier (nullable:false, blank:false)
  }

}
