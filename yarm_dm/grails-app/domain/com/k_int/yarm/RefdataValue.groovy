package com.k_int.yarm

import groovy.util.logging.Log4j

@Log4j
class RefdataValue {
  
  String value
  String icon
  String sortKey
  RefdataValue useInstead
  RefdataCategory owner

  static belongsTo = [
    owner:RefdataCategory
  ]

  static mapping = {
    owner cascade: "merge, save-update, lock, refresh, evict, replicate"
  }

  static constraints = {
    owner         (nullable:false)
    value         (nullable:false,  blank:true)
    sortKey       (nullable:true,  blank:true)
    icon          (nullable:true,  blank:true)
    useInstead    (nullable:true)
  }

  @Override
  public String toString() {
    return "${getValue()}"
  }

}
