package com.k_int.yarm

public class Identifier {
  
  IdentifierNamespace namespace
  String value
  String identifierSubtype

  static mapping = {
    id column:'id_id'
    namespace column:'id_namespace', index:'id_value_idx'
    value column:'id_value', index:'id_value_idx'
    identifierSubtype column:'id_subtype'
  }


  static constraints = {
            namespace (nullable:false, blank:false)
                value (nullable:false, blank:false, maxSize:2048)
    identifierSubtype (nullable:true, blank:false, maxSize:16)
  }

}
