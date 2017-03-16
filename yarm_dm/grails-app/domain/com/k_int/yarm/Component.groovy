package com.k_int.yarm

import groovy.util.logging.Log4j
import com.k_int.grails.tools.utils.GOKbTextUtils


@Log4j
public abstract class Component {

  transient def titleNormalisationService
  
  String name
  String normname
  String shortcode
  String discr
  // The component hash is a hash of all the significant properties of a component.
  // It is used to see if a new descriptive record is different to the one we curretly have
  // The hash should include the primary property and all variants so we are not distracted
  // by repeat variant values
  String componentHash
  Set hashes

  RefdataValue status

  static hasMany = [
    hashes:ComponentHash,
    identifiers: IdentifierOccurrence
  ]

  static mappedBy = [
    hashes:'owner',
    identifiers: 'owner'
  ]

  static mapping = {
    tablePerHierarchy false
    discr column:'c_discrim'
    id column:'c_id'
    version column:'c_version'
    name column:'c_name', type:'text'
    componentHash column:'c_component_hash', index:'c_componenthash_idx'
    normname column:'c_normname', type:'text', index:'c_normname_idx'
    shortcode column:'c_shortcode', index:'c_shortcode'
    status column:'c_status_rdv_fk'
  }


  static constraints = {
            discr (nullable:true, blank:false, maxSize:128)
             name (nullable:true, blank:false, maxSize:2048)
         normname (nullable:true, blank:false, maxSize:2048)
        shortcode (nullable:true, blank:false, maxSize:128)
    componentHash (nullable:true, blank:false, maxSize:128)
           status (nullable:true, blank:false)
  }

  def addHash(type, value) {
    if ( ( value ) && ( value.length() > 0 ) ) {
      def candidate_hash = titleNormalisationService.generateComponentHash(titleNormalisationService.normalise(value))
      def type_rdv = RefdataCategory.lookupOrCreate(session,'ComponentHashType',type)
      def ch = new ComponentHash(owner:this,hashType:type_rdv,hashValue:candidate_hash)
      ch.save(flush:true, failOnError:true);
    }
  }

  public static lookupOrCreate(idlist,name) {
    throw new RuntimeException("lookupOrCreate ${idlist} ${name} not implemented");
  }

  def addIdentifier(namespace, value) {
  }

  public static def lookupOrCreateByName(clazz, name) {

    log.debug("lookupOrCreateByName(${clazz.name},${name})");

    def result = null;
    def name_hash = GOKbTextUtils.simpleComponentHash(name)

    def lookup_result = clazz.executeQuery("select c from ${clazz.name} as c where c.normname = :normname",[normname:name_hash])

    switch(lookup_result.size() ) {
      case 0:
        result = clazz.newInstance()
        result.name = name
        result.save(flush:true, failOnError:true);
        break;
      case 1:
        result = lookup_result.get(0);
        break;
      default:
        throw new RuntimeException("unable to locate unique ${clazz.name} for name ${name}");
        break;
    }
    result;
  }

}
