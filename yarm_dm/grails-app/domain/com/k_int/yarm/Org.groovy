package com.k_int.yarm

/**
 *   Base class for User and Org - So either can be associated with actions and roles
 */ 
class Org extends Party {

  Boolean isConsortium

  static mapping = {
    isConsortium column:'o_is_consortium'
  }

  static constraints = {
    isConsortium (nullable:true, blank:false)
  }


  public static List refdataFind(grails.web.servlet.mvc.GrailsParameterMap params) {
    def result = []
    def qr = Org.executeQuery('select o.id, o.displayName from Org as o where o.displayName like :f',[f:"%${params.q?:''}%"],params)
    qr.each { r ->
      result.add([id:r[0], text:r[1]]);
    }
    result
  }
}
