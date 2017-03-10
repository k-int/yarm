package com.k_int.yarm

import groovy.util.logging.Log4j

@Log4j
public class Identifier {
  
  // N.B. namespace controlls the domain - ISSN - not eISSN/pISSN - the subtype goes in identifierSubtype
  IdentifierNamespace namespace
  String value
  String normalisedValue
  String identifierSubtype

  static mapping = {
    id column:'id_id'
    value column:'id_value'
    normalisedValue column:'norm_id_value', index:'id_value_idx'
    namespace column:'id_namespace', index:'id_value_idx'
    identifierSubtype column:'id_subtype'
  }


  static constraints = {
            namespace (nullable:false)
                value (nullable:false, blank:false, maxSize:2048)
      normalisedValue (nullable:true, blank:false, maxSize:2048)
    identifierSubtype (nullable:true, blank:false, maxSize:16)
  }

  static def lookupOrCreateCanonicalIdentifier(ns, value) {
    log.debug("lookupOrCreateCanonicalIdentifier(${ns},${value})");
    def namespace = null;
    def namespaces = IdentifierNamespace.findAllByNs(ns.toLowerCase())
    switch ( namespaces.size() ) {
      case 0:
        namespace = new IdentifierNamespace(ns:ns.toLowerCase()).save(failOnError:true);
        break;
      case 1:
        namespace = namespaces[0]
        break;
      default:
        throw new RuntimeException("Multiple Namespaces with value ${ns}");
        break;
    }
    def identifier = Identifier.findByNamespaceAndNormalisedValue(namespace,normalizeIdentifier(value)) ?:
                                    new Identifier(namespace:namespace, value:value).save(failOnError:true, flush:true)
    identifier
  }

  public static normalizeIdentifier(String id) {
    return id.toLowerCase().trim().replaceAll("\\W", "")
  }

  def beforeInsert() {
    log.debug("Normalising ${value}");
    this.normalisedValue = normalizeIdentifier(this.value);
  }


}
