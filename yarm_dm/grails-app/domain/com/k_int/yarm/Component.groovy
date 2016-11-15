package com.k_int.yarm

public class Component {
  
  String name
  String normname
  String shortcode
  Set hashes

  static hasMany = [
    hashes:ComponentHash
  ]

  static mappedBy = [
    hashes:'owner'
  ]

  static mapping = {
    tablePerHierarchy false
    id column:'c_id'
    version column:'c_version'
    name column:'c_name', type:'text'
    normname column:'c_normname', type:'text', index:'c_normname_idx'
    shortcode column:'c_shortcode', index:'c_shortcode'
  }


  static constraints = {
             name (nullable:true, blank:false, maxSize:2048)
         normname (nullable:true, blank:false, maxSize:2048)
        shortcode (nullable:true, blank:false, maxSize:128)
  }

}
