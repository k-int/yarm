package yarm_app

class Tenant {

  String uriname

  static constraints = {
    uriname nullable:false, blank:false, unique:true
  }
}
