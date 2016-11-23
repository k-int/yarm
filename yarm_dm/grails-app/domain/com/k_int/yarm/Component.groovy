package com.k_int.yarm

public abstract class Component {
  
  String name
  String normname
  String shortcode
  Set hashes

  static hasMany = [
    hashes:ComponentHash,
    identifiers: IdentifierOccurrence
  ]

  static mappedBy = [
    hashes:'owner'
    identifiers: 'owner'
  ]

  static mapping = {
    tablePerHierarchy false
    id column:'c_id'
    version column:'c_version'
    name column:'c_name', type:'text'
    normname column:'c_normname', type:'text', index:'c_normname_idx'
    shortcode column:'c_shortcode', index:'c_shortcode'
  }


  static constraints = {
             name (nullable:true, blank:false, maxSize:2048)
         normname (nullable:true, blank:false, maxSize:2048)
        shortcode (nullable:true, blank:false, maxSize:128)
  }

  /**
   *  Use rules particular to a resource type to either look up or create a DB record
   *  based on the provided description.
   */
  def public static upsert(resource_description) {
    throw new RuntimeException("Only concrete classes may upsert in this model");
  }


  /**
   * Use properties from the resource_description to attempt to look up a record
   * by value.
   */
  def public static lookupByValue(resource_description) {
  }

  /**
   * The only hash method we know about at the component level is a hash of the normalised DC title
   */
  public Set getHashMethods() {
    return [
      [ 'dc_title' ]
    ]
  }

}
