package com.k_int.yarm

class GenericOIDService {

  def grailsApplication
  def classCache = [:]

  def resolveOID2(oid) {
    def oid_components = oid.split(':');
    resolveOID2(oid_components[0],oid_components[1]);
  }

  def resolveOID2(class_name, id) {
    def result = null;

    def clazz = classCache[class_name]

    if ( clazz == null ) {
      def domain_class=null;
      domain_class = grailsApplication.getArtefact('Domain',class_name)
      if ( domain_class ) {
        clazz = domain_class.getClazz()
        classCache[class_name] = clazz
      }
    }

    if ( clazz ) {
      if ( id=='__new__' ) {
        result = clazz.newInstance()
      }
      else {
        result = clazz.get(id)
      }
    }
    else {
      log.error("resolve OID failed to identify a domain class. Input was ${class_name}, ${id}");
    }
    result
  }

}
