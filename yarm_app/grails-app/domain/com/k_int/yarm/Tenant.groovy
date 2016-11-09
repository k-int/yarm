package com.k_int.yarm

class Tenant {

  String uriname

  static constraints = {
    uriname nullable:false, blank:false, unique:true
  }
}
