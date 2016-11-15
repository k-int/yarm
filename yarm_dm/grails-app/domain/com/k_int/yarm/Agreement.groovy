package com.k_int.yarm

public class Agreement {
  
  String name
  String shortcode

  static mapping = {
    tablePerHierarchy false
    id column:'a_id'
    version column:'a_version'
    name column:'a_name', type:'text'
    shortcode column:'a_shortcode', index:'a_shortcode_idx'
  }


  static constraints = {
             name (nullable:true, blank:false, maxSize:2048)
        shortcode (nullable:true, blank:false, maxSize:128)
  }

}
