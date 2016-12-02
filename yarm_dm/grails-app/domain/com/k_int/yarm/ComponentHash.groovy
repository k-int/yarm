package com.k_int.yarm

public class ComponentHash {
  
      Component owner
   RefdataValue hashType
         String hashValue

  static mapping = {
    tablePerHierarchy false
           id column:'ch_id'
      version column:'ch_version'
     hashType column:'ch_hash_type_fk'
    hashValue column:'ch_hash_value'
  }

  static constraints = {
             owner (nullable:false, blank:false, maxSize:256)
          hashType (nullable:false, blank:false, maxSize:256)
         hashValue (nullable:false, blank:false, maxSize:256, unique: 'owner')
  }

}
