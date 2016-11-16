package com.k_int.yarm

class RefdataCategory {
  
  String description
  String label
  Set values

  static mapping = {
    values sort:'value', order:'asc', cascade: "all-delete-orphan"
  }

  static hasMany = [
    values:RefdataValue
  ]

  static mappedBy = [
    values:'owner'
  ]

  static constraints = {
    label(blank:true, nullable:true)
  }
}
