package com.k_int.yarm

public class LocalResource {
  
  String name
  String shortcode
  Agreement agreement

  static mapping = {
    tablePerHierarchy false
    id column:'lr_id'
    version column:'lr_version'
    name column:'lr_name', type:'text'
  }


  static constraints = {
             name (nullable:true, blank:false, maxSize:2048)
        agreement (nullable:true, blank:false, maxSize:2048)
  }

}
