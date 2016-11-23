package com.k_int.yarm

public class Grpp extends Component {
  
  GlobalResource global_resource
  Platform plat
  Package pkg

  static mapping = {
  }


  static hasMany = [
    coverageStatements:CoverageStatement
  ]

  static mappedBy = [
    coverageStatements:'owner'
  ]

  static constraints = {
    global_resource(nullable:false, blank:false);
    plat(nullable:false, blank:false);
    pkg(nullable:false, blank:false);
  }

}
