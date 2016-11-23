package com.k_int.yarm

public class IdentifierNamespace {
  
  String ns

  static mapping = {
    id column:'idns_id'
    ns column:'idns_ns'
  }


  static constraints = {
             ns (nullable:false, blank:false, maxSize:2048)
  }

}
