package com.k_int.yarm

public class IdentifierOccurrence {
  
  Component owner
  Identifier identifier

  // Status should be checked -- can be 'CORRECT' or 'INCORRECT' - INCORRECT denotes a known instance
  // of a title being misidentified using a specific identifier.
  RefdataValue status

  // Where status is INCORRECT, use can be used to specify what the correct title should be
  Identifier correctedAs

  static mapping = {
             id column:'io_id'
          owner column:'io_owner'
     identifier column:'io_identifier'
         status column:'io_status_fk'
    correctedAs column:'io_corrected_as_fk'
  }


  static constraints = {
          owner (nullable:false, blank:false)
     identifier (nullable:false, blank:false)
         status (nullable:true, blank:false)
    correctedAs (nullable:true, blank:false)
  }

}
