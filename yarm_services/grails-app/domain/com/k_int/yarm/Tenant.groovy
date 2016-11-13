package com.k_int.yarm

class Tenant {

  String uriname
  Date dateCreated
  Date lastUpdated

  static constraints = {
    uriname nullable:false, blank:false, unique:true
  }
}
