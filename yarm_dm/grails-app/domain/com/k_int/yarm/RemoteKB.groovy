package com.k_int.yarm

public class RemoteKB {
  
  String name
  String baseUrl
  String profile

  static mapping = {
    id column:'rkb_id'
    version column:'rkb_version'
    name column:'rkb_name', type:'text'
    baseUrl column:'rkb_base_url', type:'text'
    profile column:'rkb_profile', type:'text'
  }


  static constraints = {
             name (nullable:true, blank:false, maxSize:2048)
          baseUrl (nullable:true, blank:false, maxSize:2048)
          profile (nullable:true, blank:false, maxSize:2048)
  }

}
