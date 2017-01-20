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

}
