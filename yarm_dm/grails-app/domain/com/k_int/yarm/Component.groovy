package com.k_int.yarm

public abstract class Component {

  transient def titleNormalisationService
  
  String name
  String normname
  String shortcode
  String discriminator
  // The component hash is a hash of all the significant properties of a component.
  // It is used to see if a new descriptive record is different to the one we curretly have
  // The hash should include the primary property and all variants so we are not distracted
  // by repeat variant values
  String componentHash
  Set hashes

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
    id column:'c_id'
    version column:'c_version'
    name column:'c_name', type:'text'
    discriminator column:'c_discriminator'
    componentHash column:'c_component_hash'
    normname column:'c_normname', type:'text', index:'c_normname_idx'
    shortcode column:'c_shortcode', index:'c_shortcode'
  }


  static constraints = {
             name (nullable:true, blank:false, maxSize:2048)
         normname (nullable:true, blank:false, maxSize:2048)
        shortcode (nullable:true, blank:false, maxSize:128)
    discriminator (nullable:true, blank:false, maxSize:128)
    componentHash (nullable:true, blank:false, maxSize:128)
  }

  def addHash(session, type, value) {
    def candidate_hash = titleNormalisationService.generateComponentHash(titleNormalisationService.normalise(value))
    def type_rdv = RefdataCategory.lookupOrCreate(session,'ComponentHashType',type)
    def ch = new ComponentHash(owner:this,hashType:type_rdv,hashValue:candidate_hash)
    session.save(ch)
  }

}
